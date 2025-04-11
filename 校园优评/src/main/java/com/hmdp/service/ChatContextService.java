package com.hmdp.service;

import com.hmdp.entity.ChatContext;
import com.hmdp.entity.ChatMessage;
import java.util.List;

public interface ChatContextService {
    /**
     * 获取或创建对话上下文
     * @param sessionId 会话ID
     * @return 对话上下文
     */
    ChatContext getOrCreateContext(String sessionId);
    
    /**
     * 更新对话上下文
     * @param context 对话上下文
     */
    void updateContext(ChatContext context);
    
    /**
     * 添加消息到上下文
     * @param sessionId 会话ID
     * @param message 消息
     */
    void addMessage(String sessionId, ChatMessage message);
    
    /**
     * 获取最近的对话历史
     * @param sessionId 会话ID
     * @param maxMessages 最大消息数
     * @return 对话历史
     */
    List<ChatMessage> getRecentHistory(String sessionId, int maxMessages);
    
    /**
     * 更新上下文摘要
     * @param sessionId 会话ID
     * @param summary 摘要
     */
    void updateSummary(String sessionId, String summary);
    
    /**
     * 更新用户偏好
     * @param sessionId 会话ID
     * @param preference 用户偏好
     */
    void updatePreference(String sessionId, ChatContext.UserPreference preference);
    
    /**
     * 清理过期的上下文
     * @param maxAge 最大年龄（小时）
     */
    void cleanupExpiredContexts(int maxAge);
} 