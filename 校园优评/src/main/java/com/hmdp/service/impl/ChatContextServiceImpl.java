package com.hmdp.service.impl;

import com.hmdp.entity.ChatContext;
import com.hmdp.entity.ChatMessage;
import com.hmdp.service.ChatContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class ChatContextServiceImpl implements ChatContextService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String CONTEXT_KEY_PREFIX = "chat:context:";
    private static final int DEFAULT_EXPIRE_HOURS = 24;
    private static final int MAX_HISTORY_MESSAGES = 10;
    
    @Override
    public ChatContext getOrCreateContext(String sessionId) {
        String key = CONTEXT_KEY_PREFIX + sessionId;
        ChatContext context = (ChatContext) redisTemplate.opsForValue().get(key);
        
        if (context == null) {
            context = new ChatContext();
            context.setSessionId(sessionId);
            context.setCreateTime(LocalDateTime.now());
            context.setUpdateTime(LocalDateTime.now());
            context.setHistory(new ArrayList<>());
            context.setPreference(new ChatContext.UserPreference());
            
            redisTemplate.opsForValue().set(key, context, DEFAULT_EXPIRE_HOURS, TimeUnit.HOURS);
        }
        
        return context;
    }
    
    @Override
    public void updateContext(ChatContext context) {
        String key = CONTEXT_KEY_PREFIX + context.getSessionId();
        context.setUpdateTime(LocalDateTime.now());
        redisTemplate.opsForValue().set(key, context, DEFAULT_EXPIRE_HOURS, TimeUnit.HOURS);
    }
    
    @Override
    public void addMessage(String sessionId, ChatMessage message) {
        ChatContext context = getOrCreateContext(sessionId);
        List<ChatMessage> history = context.getHistory();
        
        // 控制历史消息数量
        if (history.size() >= MAX_HISTORY_MESSAGES) {
            history = history.subList(history.size() - MAX_HISTORY_MESSAGES + 1, history.size());
        }
        
        history.add(message);
        context.setHistory(history);
        updateContext(context);
    }
    
    @Override
    public List<ChatMessage> getRecentHistory(String sessionId, int maxMessages) {
        ChatContext context = getOrCreateContext(sessionId);
        List<ChatMessage> history = context.getHistory();
        
        if (history.size() <= maxMessages) {
            return history;
        }
        
        return history.subList(history.size() - maxMessages, history.size());
    }
    
    @Override
    public void updateSummary(String sessionId, String summary) {
        ChatContext context = getOrCreateContext(sessionId);
        context.setSummary(summary);
        updateContext(context);
    }
    
    @Override
    public void updatePreference(String sessionId, ChatContext.UserPreference preference) {
        ChatContext context = getOrCreateContext(sessionId);
        context.setPreference(preference);
        updateContext(context);
    }
    
    @Override
    @Scheduled(fixedRate = 3600000) // 每小时执行一次
    public void cleanupExpiredContexts(int maxAge) {
        // 这里可以实现清理过期上下文的逻辑
        // 由于Redis已经设置了过期时间，这里可以暂时不实现
    }
    
    /**
     * 生成新的会话ID
     * @return 会话ID
     */
    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }
} 