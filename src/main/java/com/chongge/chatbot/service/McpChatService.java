package com.chongge.chatbot.service;
 
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;

import io.modelcontextprotocol.client.McpSyncClient;
import reactor.core.publisher.Flux;

public class McpChatService {

    private static final Logger logger = LoggerFactory.getLogger(RagChatService.class);

    private final ChatClient chatClient;
    private final List<McpSyncClient> mcpSyncClients; 

    /**
     * 构造函数注入依赖
     */
    public McpChatService(List<McpSyncClient> mcpSyncClients, ChatClient chatClient) {
        this.mcpSyncClients = mcpSyncClients; 
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
        return chatClient.prompt()
                .user(userMessage)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, sessionId))
                .toolCallbacks(SyncMcpToolCallbackProvider.builder().mcpClients(mcpSyncClients).build())
                .stream()
                .content();
    }
}
