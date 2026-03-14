package com.chongge.chatbot.util;

import com.chongge.chatbot.dto.ApiResponse;

/**
 * API 响应工具类
 * 用于构建统一的API响应格式
 */
public class ApiResponseUtil {

    /**
     * 成功响应
     *
     * @param data 返回的数据
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success(data);
    }

    /**
     * 成功响应（带消息）
     *
     * @param data    返回的数据
     * @param message 响应消息
     * @return 成功响应对象
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.success(data, message);
    }

    /**
     * 错误响应
     *
     * @param code    错误代码
     * @param message 错误消息
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return ApiResponse.error(code, message);
    }

    /**
     * 错误响应（默认500）
     *
     * @param message 错误消息
     * @return 错误响应对象
     */
    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.error(500, message);
    }
}
