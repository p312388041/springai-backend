package com.chongge.chatbot.util;

import com.chongge.chatbot.dto.ResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.houbb.word.checker.util.WordCheckerHelper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import javax.imageio.ImageIO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.ToTextContentHandler;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.ReactorClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import reactor.netty.http.client.HttpClient;

public class FileUtils {

    public static final String BASE_URL = "http://localhost:6060/";

    public static final String LAYOUT_PARSING = "layout-parsing";

    public static final String RESTRUCTURE_PAGES = "restructure-pages";

    public static final String LAYOUT_PARSING_URL = BASE_URL + LAYOUT_PARSING;

    public static final String RESTRUCTURE_PAGES_URL = BASE_URL + RESTRUCTURE_PAGES;
    private static final RestClient client = RestClient.builder().baseUrl(BASE_URL).requestFactory(new ReactorClientHttpRequestFactory(HttpClient.create().responseTimeout(Duration.ofSeconds(3600)))).build();

    public static void paddleOrcVlParse(String filePath, String folder) throws IOException, InterruptedException, ExecutionException {
        List<String> base64List = convertPdfToBase64(filePath);
        int pageSize = base64List.size();
        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        CountDownLatch latch = new CountDownLatch(pageSize);
        Instant start = Instant.now();
        System.out.println("任务开始");
        Semaphore fSemaphore = new Semaphore(2);
        for (int i = 0; i < pageSize; i++) {
            final int index = i;
            if (!new File(folder + "\\done\\" + index).exists()) {
                executorService.execute(() -> {
                    try {
                        fSemaphore.acquire();
                        System.out.println("已发送请求：" + index);
                        ResponseDTO response = client
                            .post()
                            .uri(LAYOUT_PARSING)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Map.of("file", base64List.get(index), "fileType", 0)) // 使用 index 而不是 i
                            .retrieve()
                            .body(ResponseDTO.class);

                        List<String> imageList = new ArrayList<>();
                        ensureDirectoryExists(folder + "\\" + index);
                        ensureDirectoryExists(folder + "\\done");
                        String resultNoBase64 = parseResult(response, imageList);
                        saveImage(imageList, folder + "\\" + index);
                        saveContentToFile(resultNoBase64, folder, index + ".txt");
                        new File(folder + "\\done\\" + index).createNewFile();
                        System.out.println(index + "  处理完成");
                    } catch (RuntimeException exception) {
                        exception.printStackTrace();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    } finally {
                        fSemaphore.release();
                        latch.countDown();
                    }
                });
            } else {
                System.out.println(index + "已处理，跳过");
            }
        }
        latch.await();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("耗时: " + duration.toMillis());
        System.out.println("任务完成，开始关闭");
        executorService.shutdown();
        System.out.println("已调用关闭");
    }

    //保存PDF文件为图片，每页一张图片
    public static void savePagesToImages(String filePath) throws IOException {
        PDDocument document = Loader.loadPDF(new File(filePath));
        PDFRenderer renderer = new PDFRenderer(document);

        for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
            // 渲染图片，dpi 越高越清晰（300 是高质量打印标准）
            BufferedImage image = renderer.renderImageWithDPI(pageIndex, 100);

            File outputFile = new File("D:\\output\\" + "page_" + (pageIndex + 1) + ".png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("已保存: " + outputFile.getName());
        }
        // 5. 关闭文档
        document.close();
    }

    public static void classifyPdfFiles(String directoryPath) throws IOException {
        Files.list(Path.of(directoryPath))
            .filter(Files::isRegularFile)
            .forEach(path -> {
                var reader = new TikaDocumentReader(new FileSystemResource(path));
                final String target = directoryPath + "\\OCR\\" + path.getFileName();
                final String errorTarget = directoryPath + "\\Error\\" + path.getFileName();
                final String pdfTarget = directoryPath + "\\PDF\\" + path.getFileName();
                Document document = null;
                try {
                    document = reader.read().getFirst();
                } catch (StackOverflowError e) {
                    move(path, errorTarget);
                    System.err.println("栈溢出，移动到错误目录: " + path.getFileName());
                }
                if (document != null && document.getText() != null && document.getText().length() < 10000) {
                    move(path, target);
                    System.out.println("移动成功: " + path.getFileName());
                } else {
                    move(path, pdfTarget);
                }
            });
    }

    private static void saveImage(List<String> imageList, String folder) {
        for (int i = 0; i < imageList.size(); i++) {
            String base64 = imageList.get(i);
            try {
                // 解码base64
                byte[] imageBytes = Base64.getDecoder().decode(base64);
                // 写入文件
                FileOutputStream fos = new FileOutputStream(folder + "\\" + i + ".png");
                fos.write(imageBytes);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void move(Path sourcePath, String targetPath) {
        try {
            Files.move(sourcePath, Path.of(targetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //把整个PDF文件，解析成txt
    public static void parseOcrPdfFiles(String filePath, String targetFolder) throws IOException, InterruptedException {
        List<File> fileList = splitPDF(filePath);
        int documentCount = fileList.size();
        int coreCount = Runtime.getRuntime().availableProcessors();
        System.out.println("设置线程数目：" + coreCount);
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);
        CountDownLatch latch = new CountDownLatch(documentCount);
        Instant start = Instant.now();
        for (int i = 0; i < documentCount; i++) {
            final int index = i;
            executorService.execute(() -> {
                File document = fileList.get(index);
                try {
                    parseOcrPdfSinglePage(document, targetFolder, index);
                } catch (IOException | SAXException | TikaException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("耗时: " + duration.toMillis());
        System.out.println("任务完成，开始关闭");
        executorService.shutdownNow();
        System.out.println("已调用关闭");
    }

    private static List<File> splitPDF(String filePath) throws IOException {
        Splitter splitter = new Splitter();
        splitter.setSplitAtPage(1);
        PDDocument document = Loader.loadPDF(new File(filePath));
        List<PDDocument> documentList = splitter.split(document);
        int count = documentList.size();
        List<File> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            results.add(File.createTempFile("file" + i, ".temp"));
        }
        for (int i = 0; i < documentList.size(); i++) {
            documentList.get(i).save(results.get(i));
        }
        return results;
    }

    //解析单页PDF
    private static void parseOcrPdfSinglePage(File file, String targetFolder, int index) throws IOException, SAXException, TikaException {
        AutoDetectParser parser = new AutoDetectParser();
        // BodyContentHandler handler = new BodyContentHandler(-1); // -1 表示不限制内容长度
        Writer writer = new StringWriter();
        // Writer writer = new FileWriter(file, StandardCharsets.UTF_8);
        // Writer writer = new OutputStreamWriter(FileOutputStream.nullOutputStream(), StandardCharsets.UTF_8);
        ContentHandler handler = new ToTextContentHandler(writer);
        Metadata metadata = new Metadata();

        PDFParserConfig config = new PDFParserConfig();
        config.setExtractInlineImages(true);
        config.setOcrStrategy(PDFParserConfig.OCR_STRATEGY.OCR_ONLY);
        config.setOcrDPI(500);

        TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
        ocrConfig.setLanguage("chi_sim+eng");

        ParseContext context = new ParseContext();
        context.set(PDFParserConfig.class, config);
        context.set(TesseractOCRConfig.class, ocrConfig);
        context.set(AutoDetectParser.class, parser);
        parser.parse(new FileInputStream(file), handler, metadata, context);
        String ocrText = handler.toString();
        String correctResult = WordCheckerHelper.correct(ocrText);
        saveContentToFile(correctResult, targetFolder, index + ".txt");
    }

    private static void saveContentToFile(String content, String folder, String fileName) throws IOException {
        Files.writeString(Path.of(folder, fileName), content, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    }

    /**
     * 将 PDF 转换为 Base64 编码的图片列表（每页一张）
     */
    private static List<String> convertPdfToBase64(String filePath) throws IOException {
        List<String> base64Images = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            Splitter splitter = new Splitter();
            splitter.setSplitAtPage(1);
            List<PDDocument> documentList = splitter.split(document);
            int count = documentList.size();
            for (int pageNum = 0; pageNum < count; pageNum++) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                documentList.get(pageNum).save(baos);
                String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
                base64Images.add(base64Image);
            }
        }

        return base64Images;
    }

    private static boolean ensureDirectoryExists(String dirPath) {
        File directory = new File(dirPath);
        if (directory.exists()) {
            return directory.isDirectory();
        }
        return directory.mkdirs();
    }

    private static String parseResult(ResponseDTO response, List<String> base64List) {
        if (response == null) {
            return null;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            // 转成Map处理
            Map<String, Object> responseMap = mapper.convertValue(response, new TypeReference<Map<String, Object>>() {});

            // 处理三个字段
            processImagesFields(responseMap, base64List);

            return mapper.writeValueAsString(responseMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void processImagesFields(Map<String, Object> map, List<String> base64List) {
        // 处理result.layoutParsingResults
        if (map.containsKey("result")) {
            Map<String, Object> resultMap = (Map<String, Object>) map.get("result");
            if (resultMap.containsKey("layoutParsingResults")) {
                List<Map<String, Object>> layoutList = (List<Map<String, Object>>) resultMap.get("layoutParsingResults");
                for (Map<String, Object> layoutMap : layoutList) {
                    // 处理markdown.images
                    if (layoutMap.containsKey("markdown")) {
                        Map<String, Object> markdownMap = (Map<String, Object>) layoutMap.get("markdown");
                        if (markdownMap.containsKey("images")) {
                            Map<String, String> imagesMap = (Map<String, String>) markdownMap.get("images");
                            for (Map.Entry<String, String> entry : imagesMap.entrySet()) {
                                String base64Str = entry.getValue();
                                if (base64Str != null && !base64Str.isEmpty()) {
                                    base64List.add(base64Str);
                                    entry.setValue("base64");
                                }
                            }
                        }
                    }

                    // 处理outputImages
                    if (layoutMap.containsKey("outputImages")) {
                        Map<String, String> outputImagesMap = (Map<String, String>) layoutMap.get("outputImages");
                        for (Map.Entry<String, String> entry : outputImagesMap.entrySet()) {
                            String base64Str = entry.getValue();
                            if (base64Str != null && !base64Str.isEmpty()) {
                                base64List.add(base64Str);
                                entry.setValue("base64");
                            }
                        }
                    }

                    // 处理inputImage
                    if (layoutMap.containsKey("inputImage")) {
                        String inputImage = (String) layoutMap.get("inputImage");
                        if (inputImage != null && !inputImage.isEmpty()) {
                            base64List.add(inputImage);
                            layoutMap.put("inputImage", "base64");
                        }
                    }
                }
            }
        }
    }
}
