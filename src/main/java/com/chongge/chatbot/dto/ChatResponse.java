package com.chongge.chatbot.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 聊天消息响应 DTO
 */
public class ChatResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 回复内容
     */
    private String reply;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 消息ID
     */
    private Long messageId;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    public ChatResponse() {
    }

    public ChatResponse(String reply) {
        this.reply = reply;
        this.createdAt = LocalDateTime.now();
    }

    public ChatResponse(String reply, String sessionId, Long messageId) {
        this.reply = reply;
        this.sessionId = sessionId;
        this.messageId = messageId;
        this.createdAt = LocalDateTime.now();
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ChatResponse{" +
                "reply='" + reply + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", messageId=" + messageId +
                ", createdAt=" + createdAt +
                '}';
    }
}
