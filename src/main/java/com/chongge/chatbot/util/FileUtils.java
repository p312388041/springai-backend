package com.chongge.chatbot.util;

import com.github.houbb.word.checker.util.WordCheckerHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
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
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class FileUtils {

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

    public static void move(Path sourcePath, String targetPath) {
        try {
            Files.move(sourcePath, Path.of(targetPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
                    // TODO Auto-generated catch block
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
        Files.newOutputStream(Path.of(folder, fileName), StandardOpenOption.CREATE, StandardOpenOption.WRITE).write(content.getBytes(StandardCharsets.UTF_8));
    }
}
