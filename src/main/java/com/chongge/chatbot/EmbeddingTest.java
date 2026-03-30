package com.chongge.chatbot;

import com.chongge.chatbot.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class EmbeddingTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        // String directory = "D:\\Python电子书合集(6.27G)\\Error";
        // String filePath = "D:\\Python电子书合集(6.27G)\\Error\\Python库参考手册.pdf";
        // String filePath = "D:\\Python电子书合集(6.27G)\\Error\\aaa.pdf";
        String filePath = "D:\\Python电子书合集(6.27G)\\OCR\\跟老齐学Python 轻松入门.pdf";
        String directory = "D:\\output";
        FileUtils.parseOcrPdfFiles(filePath, directory);

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

    private static String cleanData(String data) {
        return data.replaceAll("\s", " ");
    }
}
