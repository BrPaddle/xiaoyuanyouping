package com.hmdp;

import com.hmdp.service.KnowledgeBaseService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hmdp.mapper")
@SpringBootApplication
public class HmDianPingApplication implements CommandLineRunner {

    @Autowired
    private KnowledgeBaseService knowledgeBaseService;

    public static void main(String[] args) {
        SpringApplication.run(HmDianPingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 初始化知识库
        knowledgeBaseService.initKnowledgeBase();
    }

}
