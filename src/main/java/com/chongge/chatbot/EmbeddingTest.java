package com.chongge.chatbot;

import com.chongge.chatbot.util.FileUtils;
// import com.chongge.chatbot.dto.ResponseDTO.DataInfo;
// import com.chongge.chatbot.dto.ResponseDTO.LayoutParsingResult;
// import com.chongge.chatbot.dto.ResponseDTO.Markdown;
// import com.chongge.chatbot.dto.ResponseDTO.Result;
// import com.chongge.chatbot.util.HttpClientUtils;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

// import java.time.Duration;
// import java.time.Instant;
// import java.util.List;

public class EmbeddingTest {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // String url = "https://paddle-model-ecology.bj.bcebos.com/paddlex/imgs/demo_image/paddleocr_vl_demo.png";
        // Instant start = Instant.now();
        // var responseDTO = HttpClientUtils.parse(url);
        // Instant end = Instant.now();
        // System.out.println(Duration.between(start, end).toMillis());
        // System.out.println(responseDTO);
        // System.out.println("error code: " + responseDTO.errorCode());
        // System.out.println("error message: " + responseDTO.errorMsg());
        // System.out.println("log id: " + responseDTO.logId());
        // Result result = responseDTO.result();
        // DataInfo dataInfo = result.dataInfo();
        // System.out.println("height: " + dataInfo.height());
        // System.out.println("width: " + dataInfo.width());
        // List<LayoutParsingResult> layoutParsingResults = result.layoutParsingResults();
        // for (LayoutParsingResult r : layoutParsingResults) {
        //     Markdown markdown = r.markdown();
        //     System.out.println(markdown.text());
        // }
        // String directory = "D:\\Python电子书合集(6.27G)\\Error";
        // String filePath = "D:\\Python电子书合集(6.27G)\\Error\\Python库参考手册.pdf";
        // String filePath = "D:\\Python电子书合集(6.27G)\\Error\\aaa.pdf";
        String filePath = "D:\\Python电子书合集(6.27G)\\OCR\\跟老齐学Python 轻松入门.pdf";
        // String directory = "D:\\output";
        String folder = "D:\\paddle\\output";
        FileUtils.paddleOrcVlParse(filePath, folder);
        // FileUtils.parseOcrPdfFiles(filePath, directory);
        // FileUtils.savePagesToImages(filePath);
        // String directoryPath = "D:\\Python电子书合集(6.27G)";
        // var reader = new TikaDocumentReader(new FileSystemResource(filePath));
        // 2. 配置 PDF 读取器（每页作为一个 Document）
        // Files.list(Path.of(directory))
        //     .filter(Files::isRegularFile)
        //     .forEach(file -> {
        //         System.out.println(file.getFileName());
        //         var pdfReader = new TikaDocumentReader(new FileSystemResource(file));
        //         Document document = pdfReader.read().getFirst();
        //         // var metadata = document.getMetadata();
        //         String data = cleanData(document.getText());
        //         final String pdfTarget = directory + "\\complete\\" + file.getFileName();
        //         FileUtils.move(file, pdfTarget);
        //     });
        // 3. 读取文档：得到 List<Document>，此时每个 Document 的内容是一页的文本
        // System.out.println("读取到 " + documents.size() + " 页原始文档");
        // 4. 文本分割：将长页切分成更小的块（块大小按 token 计算）[citation:8]
        //    默认分割器参数：chunkSize=800, minChunkSizeChars=350, minChunkLengthToEmbed=5, maxNumChunks=10000
        // List<Document> splitDocuments = tokenTextSplitter.apply(documents);
        // System.out.println("分割后得到 " + splitDocuments.size() + " 个文本块");
        // // 5. 存入向量库（这一步会自动调用 EmbeddingModel 生成向量，并插入 PostgreSQL）
        // vectorStore.add(splitDocuments);
        // System.out.println("已存入向量库");
        // FileUtils.classifyPdfFiles(directoryPath);
        // 2. 配置 PDF 读取器（每页作为一个 Document）
    }
}
