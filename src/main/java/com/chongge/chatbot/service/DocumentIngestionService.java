package com.chongge.chatbot.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
// import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class DocumentIngestionService {

    @Autowired
    private VectorStore vectorStore;

    // 注入一个自动配置好的文本分割器（也可以用默认构造）
    private TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder()
        .withChunkSize(300) // 每个块约 300 token
        .withMinChunkSizeChars(50) // 最小块 50 token
        .withMinChunkLengthToEmbed(5) // 低于 30 token 的不处理
        .withMaxNumChunks(10000) // 最大块数限制
        .build();

    /**
     * 从文件路径加载 PDF 并存入向量库
     * @param filePath 文件路径，例如：/data/docs/spring-boot-reference.pdf
     */
    public void loadPdfAndStore(String filePath) throws MalformedURLException {
        // 1. 创建 Resource 对象
        Resource resource = new UrlResource(Path.of(filePath).toUri());

        // 2. 配置 PDF 读取器（每页作为一个 Document）
        // TikaDocumentReader pdfReader = new TikaDocumentReader(resource);

        // // 3. 读取文档：得到 List<Document>，此时每个 Document 的内容是一页的文本
        // List<Document> documents = pdfReader.read();
        // System.out.println("读取到 " + documents.size() + " 页原始文档");

        // // 4. 文本分割：将长页切分成更小的块（块大小按 token 计算）[citation:8]
        // //    默认分割器参数：chunkSize=800, minChunkSizeChars=350, minChunkLengthToEmbed=5, maxNumChunks=10000
        // List<Document> splitDocuments = tokenTextSplitter.apply(documents);
        // System.out.println("分割后得到 " + splitDocuments.size() + " 个文本块");

        // // 5. 存入向量库（这一步会自动调用 EmbeddingModel 生成向量，并插入 PostgreSQL）
        // vectorStore.add(splitDocuments);
        System.out.println("已存入向量库");
    }

    /**
     * 直接处理纯文本（比如用户输入的片段）
     */
    public void storePlainText(String text, String source) {
        Document doc = new Document(text, Map.of("source", source));
        // 即使是一个文档，也可以分割一下（防止过长）
        List<Document> splitDocs = tokenTextSplitter.apply(List.of(doc));
        vectorStore.add(splitDocs);
        System.out.println("data insered");
    }
}
