package com.chongge.chatbot.config;

import javax.sql.DataSource;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chongge.chatbot.advisor.StreamAdvisor;

@Configuration
public class ChatMemoryConfig {

    /**
     * 配置基于 JDBC 的聊天记忆存储器，专门用于 PostgreSQL。
     * 新版 API 会自动从 DataSource 推断数据库方言 (PostgreSQLDialect)，无需手动指定。
     */
    @Bean
    public ChatMemory chatMemory(DataSource dataSource) {
        // 1. 创建 JDBC 存储器。直接传入 DataSource，内部会自动创建 JdbcTemplate 并推断方言 [citation:8]
        ChatMemoryRepository repository = JdbcChatMemoryRepository.builder()
                .dataSource(dataSource) // 简洁的构建方式
                .build();

        // 2. 创建带有滑动窗口策略的记忆管理器。这里设置最多记住10条历史消息，防止 Token 浪费 [citation:2][citation:3]
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(repository)
                .maxMessages(10)
                .build();
    }

    /**
     * 配置 ChatClient，并默认应用 MessageChatMemoryAdvisor。
     * 这样每次调用都会自动处理历史消息的存取。
     */
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, StreamAdvisor advisor,VectorStore vectorStore) { 
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor(),advisor, retrievalAugmentationAdvisor) // 添加日志和检索增强顾问
                .build();
    }
}