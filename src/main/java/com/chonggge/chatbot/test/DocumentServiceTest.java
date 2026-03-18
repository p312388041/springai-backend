package com.chonggge.chatbot.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.chongge.chatbot.service.DocumentIngestionService;

@SpringBootApplication
public class DocumentServiceTest {
    
    @Autowired
    private static DocumentIngestionService documentIngestionService;

    public static void main(String[] args) {
                // Arrange
        StringBuilder longText = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longText.append("这是一个很长的测试文本段落，用于测试文本分割功能。");
        }
        String source = "long-text-source"; 
        // Act
        documentIngestionService.storePlainText(longText.toString(), source); 
    }
}
