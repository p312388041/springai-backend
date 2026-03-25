package com.chongge.chatbot.service;

import com.chongge.chatbot.repository.ChatMessageRepository;
import io.modelcontextprotocol.client.McpSyncClient;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

/**
 * 聊天服务层
 * 处理与AI模型的交互逻辑和数据库存储
 */
@Service
public class RagChatService {

    private static final Logger logger = LoggerFactory.getLogger(RagChatService.class);

    private final ChatClient chatClient;

    /**
     * 构造函数注入依赖
     */
    public RagChatService(ChatClient chatClient, ChatMessageRepository chatMessageRepository, List<McpSyncClient> mcpSyncClients, VectorStore vectorStore) {
        this.chatClient = chatClient;
    }

    /**
     * 发送消息给AI并保存聊天记录
     *
     * @param userMessage 用户输入的消息
     * @param sessionId   会话ID，如果为null则生成新的
     * @return 聊天响应对象
     */
    public Flux<String> chat(String userMessage, String sessionId) {
        logger.info("Receiving message: {}", userMessage);
        // 调用AI模型获取回复
        return chatClient
            .prompt()
            .user(userMessage)
            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
            .stream()
            .content();
    }
}
