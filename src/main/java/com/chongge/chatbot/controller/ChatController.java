package com.chongge.chatbot.controller;

import com.chongge.chatbot.dto.ApiResponse;
import com.chongge.chatbot.dto.ChatRequest;
import com.chongge.chatbot.service.RagChatService;
import com.chongge.chatbot.util.ApiResponseUtil;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * 聊天控制器
 * 提供AI聊天相关的REST API接口
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    // private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final RagChatService ragChatService;

    /**
     * 构造函数注入ChatService
     */
    public ChatController(RagChatService chatService) {
        this.ragChatService = chatService;
    }

    /**
     * 发送消息给AI
     *
     * @param request 包含消息内容和可选的会话ID的请求对象
     * @return 包含AI回复的响应对象
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamMessage(@RequestBody ChatRequest request, @Param("sessionId") String sessionId) {
        System.out.println(Thread.currentThread().threadId() + "---".repeat(10));
        return ragChatService.chat(request.getMessage(), sessionId);
    }

    // /**
    // * 获取指定会话的聊天历史
    // *
    // * @param sessionId 会话ID
    // * @return 该会话的所有聊天消息
    // */
    // @GetMapping("/history/{sessionId}")
    // public ResponseEntity<ApiResponse<List<ChatMessage>>>
    // getChatHistory(@PathVariable String sessionId) {
    // logger.info("Fetching chat history for session: {}", sessionId);

    // try {
    // List<ChatMessage> history = chatService.getChatHistory(sessionId);
    // return ResponseEntity.ok(ApiResponseUtil.success(history, "获取成功"));

    // } catch (Exception e) {
    // logger.error("Error fetching chat history", e);
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(ApiResponseUtil.error(500, "获取失败：" + e.getMessage()));
    // }
    // }

    // /**
    // * 清除指定会话的聊天记录
    // *
    // * @param sessionId 会话ID
    // * @return 操作结果
    // */
    // @DeleteMapping("/history/{sessionId}")
    // public ResponseEntity<ApiResponse<Void>> clearChatHistory(@PathVariable
    // String sessionId) {
    // logger.info("Clearing chat history for session: {}", sessionId);

    // try {
    // chatService.clearChatHistory(sessionId);
    // return ResponseEntity.ok(ApiResponseUtil.success(null, "清除成功"));

    // } catch (Exception e) {
    // logger.error("Error clearing chat history", e);
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(ApiResponseUtil.error(500, "清除失败：" + e.getMessage()));
    // }
    // }

    // /**
    // * 获取所有聊天消息（管理接口）
    // *
    // * @return 所有聊天消息列表
    // */
    // @GetMapping("/messages")
    // public ResponseEntity<ApiResponse<List<ChatMessage>>> getAllMessages() {
    // logger.info("Fetching all messages");

    // try {
    // List<ChatMessage> messages = chatService.getAllMessages();
    // return ResponseEntity.ok(ApiResponseUtil.success(messages, "获取成功"));

    // } catch (Exception e) {
    // logger.error("Error fetching all messages", e);
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    // .body(ApiResponseUtil.error(500, "获取失败：" + e.getMessage()));
    // }
    // }

    /**
     * 健康检查端点
     *
     * @return 服务健康状态
     */
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponseUtil.success("OK", "服务运行中"));
    }
}
