package com.chongge.chatbot.dto;

import com.fasterxml.jackson.annotation.JsonInclude; 

/**
 * 流式事件DTO - 使用Record
 * 用于SSE流式传输的事件对象
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record StreamEvent(
    String type,        // 事件类型: start, content, complete, error, heartbeat
    Integer code,       // 状态码: 200成功, 400参数错误, 500服务器错误
    String message,     // 消息
    Object data,        // 数据
    String sessionId,   // 会话ID
    Long timestamp,     // 时间戳
    Integer totalLength, // 总长度
    Integer currentLength, // 当前长度
    Long duration       // 持续时间
) {
    
    // 紧凑构造函数，可以添加验证逻辑
    public StreamEvent {
        if (timestamp == null) {
            timestamp = System.currentTimeMillis();
        }
    }

    // 静态工厂方法 - 创建开始事件
    public static StreamEvent start(String sessionId, Integer totalLength) {
        return new StreamEvent(
            "start",
            200,
            "开始流式传输",
            null,
            sessionId,
            System.currentTimeMillis(),
            totalLength,
            0,
            null
        );
    }

    // 静态工厂方法 - 创建内容事件
    public static StreamEvent content(Object data) {
        return new StreamEvent(
            "content",
            200,
            null,
            data,
            null,
            System.currentTimeMillis(),
            null,
            null,
            null
        );
    }

    // 静态工厂方法 - 创建完成事件
    public static StreamEvent complete(String sessionId) {
        return new StreamEvent(
            "complete",
            200,
            "流式传输完成",
            null,
            sessionId,
            System.currentTimeMillis(),
            null,
            null,
            null
        );
    }

    // 静态工厂方法 - 创建错误事件
    public static StreamEvent error(Integer code, String message) {
        return new StreamEvent(
            "error",
            code != null ? code : 500,
            message,
            null,
            null,
            System.currentTimeMillis(),
            null,
            null,
            null
        );
    }

    // 静态工厂方法 - 创建心跳事件
    public static StreamEvent heartbeat() {
        return new StreamEvent(
            "heartbeat",
            null,
            null,
            null,
            null,
            System.currentTimeMillis(),
            null,
            null,
            null
        );
    }

    // 便捷方法：检查事件类型
    public boolean isStart() {
        return "start".equals(type);
    }

    public boolean isContent() {
        return "content".equals(type);
    }

    public boolean isComplete() {
        return "complete".equals(type);
    }

    public boolean isError() {
        return "error".equals(type);
    }

    public boolean isHeartbeat() {
        return "heartbeat".equals(type);
    }

    // 获取字符串数据（如果是内容事件）
    public String getDataAsString() {
        return data != null ? data.toString() : null;
    }

    @Override
    public String toString() {
        return String.format(
            "StreamEvent[type=%s, code=%d, message=%s, sessionId=%s]",
            type, code, message, sessionId
        );
    }
}