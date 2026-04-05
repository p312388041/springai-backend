package com.chongge.chatbot.service;

import java.net.MalformedURLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 单元测试类：DocumentIngestionService
 */
@SpringBootTest
class DocumentIngestionServiceTest {

    @Autowired
    private DocumentIngestionService documentIngestionService;

    @BeforeEach
    void setUp() {
        // documentIngestionService = new DocumentIngestionService();
        // // 通过反射设置私有字段，因为@Autowired依赖需要注入
        // try {
        //     var vectorStoreField = DocumentIngestionService.class.getDeclaredField("vectorStore");
        //     vectorStoreField.setAccessible(true);
        //     vectorStoreField.set(documentIngestionService, vectorStore);
        //     var textSplitterField = DocumentIngestionService.class.getDeclaredField("textSplitter");
        //     textSplitterField.setAccessible(true);
        //     textSplitterField.set(documentIngestionService, textSplitter);
        // } catch (Exception e) {
        //     throw new RuntimeException("Failed to inject dependencies via reflection", e);
        // }
    }

    @Test
    void testLoadPdfAndStore_success() throws MalformedURLException {
        // Arrange
        String filePath = "C:\\aa.pdf";

        documentIngestionService.loadPdfAndStore(filePath);
        System.out.println("PDF加载测试跳过，需要实际PDF文件");
    }

    @Test
    void testStorePlainText_success() {
        // Arrange
        String text = "这是一段测试文本内容，用于验证向量存储功能。";
        String source = "test-source";

        // when(textSplitter.apply(anyList())).thenReturn(mockSplitDocuments);

        // Act
        documentIngestionService.storePlainText(text, source);

        // Assert
        // verify(textSplitter).apply(anyList());
        // verify(vectorStore).add(mockSplitDocuments);
    }

    @Test
    void testStorePlainText_emptyText() {
        // Arrange
        String text = "";
        String source = "empty-source";

        // Document mockDocument = new Document(text, Map.of("source", source));
        // List<Document> mockSplitDocuments = Arrays.asList(mockDocument);

        // when(textSplitter.apply(anyList())).thenReturn(mockSplitDocuments);

        // Act
        documentIngestionService.storePlainText(text, source);

        // Assert
        // verify(textSplitter).apply(anyList());
        // verify(vectorStore).add(mockSplitDocuments);
    }

    @Test
    void testStorePlainText_longText() {
        // Arrange
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            longText.append("这是一个很长的测试文本段落，用于测试文本分割功能。");
        }
        String source = "long-text-source";
        // Act
        documentIngestionService.storePlainText(longText.toString(), source);
    }
}
