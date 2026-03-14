package com.chongge.chatbot.controller;

import com.chongge.chatbot.dto.ApiResponse;
import com.chongge.chatbot.util.ApiResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 根路径控制器
 * 处理根路径请求
 */
@RestController
public class RootController {

    private static final Logger logger = LoggerFactory.getLogger(RootController.class);

    /**
     * 根路径欢迎信息
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> welcome() {
        logger.info("访问根路径");
        return ResponseEntity.ok(ApiResponseUtil.success("欢迎使用AI聊天API！请访问 /api/chat/send 发送消息。"));
    }
}