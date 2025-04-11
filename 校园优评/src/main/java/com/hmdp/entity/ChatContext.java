package com.hmdp.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatContext {
    private String sessionId;
    private String currentScene;  // 当前场景
    private List<ChatMessage> history;  // 对话历史
    private String lastIntent;  // 上一次的意图
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private int tokenCount;  // token计数，用于控制上下文长度
    
    // 上下文摘要，用于快速理解对话主题
    private String summary;
    
    // 用户偏好信息
    private UserPreference preference;
    
    @Data
    public static class UserPreference {
        private String preferredCuisine;  // 偏好的菜系
        private String priceRange;  // 价格区间
        private String diningStyle;  // 用餐风格
        private List<String> dietaryRestrictions;  // 饮食限制
    }
} 