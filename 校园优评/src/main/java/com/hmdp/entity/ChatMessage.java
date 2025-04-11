package com.hmdp.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChatMessage {
    private Long id;
    private Long sessionId;
    private String role;  // user 或 assistant
    private String content;
    private LocalDateTime createTime;
} 