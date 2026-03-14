package com.chongge.chatbot.repository;

import com.chongge.chatbot.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * 聊天消息数据库访问接口
 * 继承 JpaRepository 获得基本的CRUD操作能力
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * 根据会话ID查询所有消息
     *
     * @param sessionId 会话ID
     * @return 该会话的所有消息列表
     */
    List<ChatMessage> findBySessionId(String sessionId);

    /**
     * 根据会话ID删除所有消息
     *
     * @param sessionId 会话ID
     */
    void deleteBySessionId(String sessionId);
}
