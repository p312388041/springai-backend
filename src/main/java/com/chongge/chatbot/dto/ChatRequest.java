package com.chongge.chatbot.dto;

import java.io.Serializable;

/**
 * 聊天消息请求 DTO
 */
public class ChatRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户输入的消息内容
     */
    private String message;

    /**
     * 会话ID（可选，用于分组对话）
     */
    private String sessionId;

    public ChatRequest() {
    }

    public ChatRequest(String message) {
        this.message = message;
    }

    public ChatRequest(String message, String sessionId) {
        this.message = message;
        this.sessionId = sessionId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public String toString() {
        return "ChatRequest{" +
                "message='" + message + '\'' +
                ", sessionId='" + sessionId + '\'' +
                '}';
    }
}
