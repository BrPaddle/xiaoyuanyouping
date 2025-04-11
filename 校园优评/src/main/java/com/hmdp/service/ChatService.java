package com.hmdp.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.List;

public interface ChatService {
    /**
     * 流式对话
     * @param message 用户消息
     * @param emitter SSE发射器
     */
    void streamChat(String message, SseEmitter emitter);
    
    /**
     * 获取对话历史
     * @param sessionId 会话ID
     * @return 历史消息列表
     */
    List<ChatMessage> getHistory(Long sessionId);
} 