package com.hmdp.config;

import org.springframework.ai.tongyi.TongYiAiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    
    @Value("${tongyi.api.key}")
    private String apiKey;
    
    @Bean
    public TongYiAiClient tongYiAiClient() {
        return new TongYiAiClient(apiKey);
    }
} 