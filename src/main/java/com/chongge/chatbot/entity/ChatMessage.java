package com.chongge.chatbot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * 聊天记录实体类
 * JPA数据库映射类，存储聊天消息
 */
@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    /**
     * 消息ID，主键，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会话ID，用于区分不同的对话
     */
    @Column(nullable = false, length = 50)
    private String sessionId;

    /**
     * 用户输入的问题内容
     */
    @Column(nullable = false, columnDefinition = "TEXT")
    private String userMessage;

    /**
     * AI的回复内容
     */
    @Column(columnDefinition = "TEXT")
    private String aiReply;

    /**
     * 创建时间
     */
    @Column(nullable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Constructors
    public ChatMessage() {
    }

    public ChatMessage(String sessionId, String userMessage, String aiReply) {
        this.sessionId = sessionId;
        this.userMessage = userMessage;
        this.aiReply = aiReply;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getAiReply() {
        return aiReply;
    }

    public void setAiReply(String aiReply) {
        this.aiReply = aiReply;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", sessionId='" + sessionId + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", aiReply='" + aiReply + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
