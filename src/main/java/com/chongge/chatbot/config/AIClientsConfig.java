package com.chongge.chatbot.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.chongge.chatbot.advisor.StreamAdvisor;

@Configuration
public class AIClientsConfig {
    /**
     * 配置 ChatClient，并默认应用 MessageChatMemoryAdvisor。
     * 这样每次调用都会自动处理历史消息的存取。
     */
    @Bean
    public ChatClient ragChatClient(ChatClient.Builder builder, StreamAdvisor advisor, VectorStore vectorStore) {
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder().similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor(), advisor, retrievalAugmentationAdvisor) // 添加日志和检索增强顾问
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, StreamAdvisor advisor, VectorStore vectorStore) {
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(VectorStoreDocumentRetriever.builder().similarityThreshold(0.50)
                        .vectorStore(vectorStore)
                        .build())
                .build();

        return builder
                .defaultAdvisors(new SimpleLoggerAdvisor(), advisor, retrievalAugmentationAdvisor) // 添加日志和检索增强顾问
                .build();
    }

}
