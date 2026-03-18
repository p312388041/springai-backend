package com.chongge.chatbot.dto;

/**
 * 流式错误DTO
 */
public class StreamError {
    private String type;
    private int code;
    private String message;
    private String details;
    private long timestamp;

    public StreamError() {
        this.type = "error";
        this.timestamp = System.currentTimeMillis();
    }

    public StreamError(int code, String message) {
        this.type = "error";
        this.code = code;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public StreamError(int code, String message, String details) {
        this.type = "error";
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = System.currentTimeMillis();
    }

    // Getter 和 Setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "StreamError{" +
                "type='" + type + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", details='" + details + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}