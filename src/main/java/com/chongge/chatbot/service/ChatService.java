package com.chongge.chatbot.service;

import com.chongge.chatbot.dto.ChatResponse;
import com.chongge.chatbot.entity.ChatMessage;
import com.chongge.chatbot.repository.ChatMessageRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.UUID;

/**
 * 聊天服务层
 * 处理与AI模型的交互逻辑和数据库存储
 */
@Service
public class ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);

    private final ChatClient chatClient;
    private final ChatMessageRepository chatMessageRepository;

    /**
     * 构造函数注入依赖
     */
    public ChatService(ChatClient chatClient, ChatMessageRepository chatMessageRepository) {
        this.chatClient = chatClient;
        this.chatMessageRepository = chatMessageRepository;
    }

    /**
     * 发送消息给AI并保存聊天记录
     *
     * @param userMessage 用户输入的消息
     * @param sessionId   会话ID，如果为null则生成新的
     * @return 聊天响应对象
     */
    public ChatResponse chat(String userMessage, String sessionId) {
        logger.info("Receiving message: {}", userMessage);

        // 生成或使用提供的会话ID
        if (sessionId == null || sessionId.trim().isEmpty()) {
            sessionId = UUID.randomUUID().toString();
            logger.info("Generated new session ID: {}", sessionId);
        }

        final String finalSessionId = sessionId; // 用于lambda表达式中的有效final变量
        try {
            // 调用AI模型获取回复
            String aiReply = chatClient.prompt()
                    .user(userMessage)
                    .advisors(a->a.param(ChatMemory.CONVERSATION_ID, finalSessionId)) // 添加会话ID参数
                    .call()
                    .content();

            logger.info("AI reply: {}", aiReply);

            // 保存聊天记录到数据库
            ChatMessage chatMessage = new ChatMessage(sessionId, userMessage, aiReply);
            ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

            // long messageId = new Random().nextLong(); // 模拟生成消息ID
            long messageId = savedMessage.getId(); // 使用数据库生成的消息ID

            // logger.info("Chat message saved with ID: {}", savedMessage.getId());

            // 构建响应对象
            return new ChatResponse(aiReply, sessionId, messageId);

        } catch (Exception e) {
            logger.error("Error processing chat message", e);
            throw new RuntimeException("AI处理消息失败: " + e.getMessage());
        }
    }

    /**
     * 获取指定会话的所有聊天记录
     *
     * @param sessionId 会话ID
     * @return 聊天消息列表
     */
    public List<ChatMessage> getChatHistory(String sessionId) {
        logger.info("Fetching chat history for session: {}", sessionId);
        return chatMessageRepository.findBySessionId(sessionId);
    }

    /**
     * 清除指定会话的聊天记录
     *
     * @param sessionId 会话ID
     */
    public void clearChatHistory(String sessionId) {
        logger.info("Clearing chat history for session: {}", sessionId);
        chatMessageRepository.deleteBySessionId(sessionId);
    }

    /**
     * 获取所有聊天消息（仅用于管理功能）
     *
     * @return 所有聊天消息列表
     */
    public List<ChatMessage> getAllMessages() {
        logger.debug("Fetching all messages");
        return chatMessageRepository.findAll();
    }
}
