package com.hmdp.controller;

import com.hmdp.service.KnowledgeBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/knowledge")
public class KnowledgeBaseController {
    
    @Autowired
    private KnowledgeBaseService knowledgeBaseService;
    
    @PostMapping("/init")
    public void initKnowledgeBase() {
        knowledgeBaseService.initKnowledgeBase();
    }
    
    @PostMapping("/shop")
    public void addShopKnowledge(
            @RequestParam Long shopId,
            @RequestParam String shopInfo) {
        knowledgeBaseService.addShopKnowledge(shopId, shopInfo);
    }
    
    @PostMapping("/product")
    public void addProductKnowledge(
            @RequestParam Long productId,
            @RequestParam String productInfo) {
        knowledgeBaseService.addProductKnowledge(productId, productInfo);
    }
    
    @PostMapping("/review")
    public void addReviewKnowledge(
            @RequestParam Long reviewId,
            @RequestParam String reviewContent) {
        knowledgeBaseService.addReviewKnowledge(reviewId, reviewContent);
    }
} 