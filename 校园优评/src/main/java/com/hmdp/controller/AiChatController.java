package com.hmdp.controller;

import com.hmdp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/ai")
public class AiChatController {
    
    @Autowired
    private ChatService chatService;
    
    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter chat(@RequestParam String message) {
        SseEmitter emitter = new SseEmitter(30 * 1000L); // 30秒超时
        chatService.streamChat(message, emitter);
        return emitter;
    }
    
    @GetMapping("/history")
    public List<ChatMessage> getHistory(@RequestParam Long sessionId) {
        return chatService.getHistory(sessionId);
    }
} 