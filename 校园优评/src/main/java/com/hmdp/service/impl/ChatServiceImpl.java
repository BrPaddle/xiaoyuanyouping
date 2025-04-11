package com.hmdp.service.impl;

import com.hmdp.entity.ChatMessage;
import com.hmdp.entity.VectorData;
import com.hmdp.service.ChatService;
import com.hmdp.service.VectorService;
import com.hmdp.utils.PromptTemplate;
import org.springframework.ai.tongyi.TongYiAiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired
    private TongYiAiClient aiClient;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private VectorService vectorService;
    
    private static final String CHAT_HISTORY_KEY = "chat:history:";
    private static final long HISTORY_EXPIRE_TIME = 24 * 60 * 60; // 24小时
    
    @Override
    public void streamChat(String message, SseEmitter emitter) {
        try {
            // 1. 向量检索获取相关知识
            List<VectorData> relevantDocs = vectorService.searchSimilar(message, null, 3);
            String context = relevantDocs.stream()
                .map(VectorData::getContent)
                .collect(Collectors.joining("\n"));
            
            // 2. 判断场景类型
            String scene = determineScene(message);
            
            // 3. 构建场景化提示词
            String prompt = PromptTemplate.buildScenePrompt(scene, context, message);
            
            // 4. 调用AI服务
            aiClient.streamChat(prompt, 
                response -> {
                    try {
                        emitter.send(response);
                    } catch (Exception e) {
                        emitter.completeWithError(e);
                    }
                },
                error -> {
                    // 发生错误时使用错误提示词
                    String errorPrompt = PromptTemplate.buildErrorPrompt(message);
                    aiClient.streamChat(errorPrompt, 
                        response -> {
                            try {
                                emitter.send(response);
                            } catch (Exception e) {
                                emitter.completeWithError(e);
                            }
                        },
                        e -> emitter.completeWithError(e),
                        () -> emitter.complete()
                    );
                },
                () -> emitter.complete()
            );
            
            // 5. 保存对话历史
            saveChatHistory(message);
        } catch (Exception e) {
            emitter.completeWithError(e);
        }
    }
    
    @Override
    public List<ChatMessage> getHistory(Long sessionId) {
        String key = CHAT_HISTORY_KEY + sessionId;
        return (List<ChatMessage>) redisTemplate.opsForValue().get(key);
    }
    
    private String determineScene(String message) {
        // 简单的场景判断逻辑
        if (message.contains("推荐") || message.contains("找") || message.contains("搜索")) {
            return "search";
        } else if (message.contains("评价") || message.contains("评论") || message.contains("怎么样")) {
            return "review";
        } else if (message.contains("商品") || message.contains("产品") || message.contains("菜品")) {
            return "product";
        }
        return "general";
    }
    
    private void saveChatHistory(String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user");
        chatMessage.setContent(message);
        chatMessage.setCreateTime(LocalDateTime.now());
        
        String key = CHAT_HISTORY_KEY + "default"; // 这里可以改为实际的sessionId
        redisTemplate.opsForList().rightPush(key, chatMessage);
        redisTemplate.expire(key, HISTORY_EXPIRE_TIME, TimeUnit.SECONDS);
    }
} 