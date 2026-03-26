package com.chongge.chatbot;

import com.chongge.chatbot.util.FileUtils;
import java.io.IOException;
import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class EmbeddingTest {

    public static void main(String[] args) throws IOException {
        String filePath = "D:\\Python电子书合集(6.27G)\\python网络爬虫从入门到实践.pdf";
        String directoryPath = "D:\\Python电子书合集(6.27G)";
        // var reader = new TikaDocumentReader(new FileSystemResource(filePath));

        // 2. 配置 PDF 读取器（每页作为一个 Document）
        // TikaDocumentReader pdfReader = new TikaDocumentReader(new FileSystemResource(filePath));

        // Document document = pdfReader.read().getFirst();
        // var metadata = document.getMetadata();
        // String data = cleanData(document.getText());

        FileUtils.classifyPdfFiles(directoryPath);
        // 2. 配置 PDF 读取器（每页作为一个 Document）

        // 3. 读取文档：得到 List<Document>，此时每个 Document 的内容是一页的文本
        // System.out.println("读取到 " + documents.size() + " 页原始文档");

        // 4. 文本分割：将长页切分成更小的块（块大小按 token 计算）[citation:8]
        //    默认分割器参数：chunkSize=800, minChunkSizeChars=350, minChunkLengthToEmbed=5, maxNumChunks=10000
        // List<Document> splitDocuments = tokenTextSplitter.apply(documents);
        // System.out.println("分割后得到 " + splitDocuments.size() + " 个文本块");

        // // 5. 存入向量库（这一步会自动调用 EmbeddingModel 生成向量，并插入 PostgreSQL）
        // vectorStore.add(splitDocuments);
        // System.out.println("已存入向量库");
    }

    private static String cleanData(String data) {
        return data.replaceAll("\s", " ");
    }
}
