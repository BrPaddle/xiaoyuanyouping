package com.hmdp.service.impl;

import com.hmdp.entity.VectorData;
import com.hmdp.service.KnowledgeBaseService;
import com.hmdp.service.VectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {
    
    @Autowired
    private VectorService vectorService;
    
    @Override
    public void initKnowledgeBase() {
        // 1. 初始化商家知识
        initShopKnowledge();
        
        // 2. 初始化商品知识
        initProductKnowledge();
        
        // 3. 初始化评论知识
        initReviewKnowledge();
    }
    
    @Override
    public void addShopKnowledge(Long shopId, String shopInfo) {
        VectorData vectorData = new VectorData();
        vectorData.setId("shop_" + shopId);
        vectorData.setContent(shopInfo);
        vectorData.setType("shop");
        vectorData.setMetadata("{\"shopId\":" + shopId + "}");
        
        vectorService.addVector(vectorData);
    }
    
    @Override
    public void addProductKnowledge(Long productId, String productInfo) {
        VectorData vectorData = new VectorData();
        vectorData.setId("product_" + productId);
        vectorData.setContent(productInfo);
        vectorData.setType("product");
        vectorData.setMetadata("{\"productId\":" + productId + "}");
        
        vectorService.addVector(vectorData);
    }
    
    @Override
    public void addReviewKnowledge(Long reviewId, String reviewContent) {
        VectorData vectorData = new VectorData();
        vectorData.setId("review_" + reviewId);
        vectorData.setContent(reviewContent);
        vectorData.setType("review");
        vectorData.setMetadata("{\"reviewId\":" + reviewId + "}");
        
        vectorService.addVector(vectorData);
    }
    
    @Override
    public void batchAddKnowledge(List<VectorData> knowledgeList) {
        vectorService.batchAddVectors(knowledgeList);
    }
    
    private void initShopKnowledge() {
        // 这里可以从数据库加载商家信息
        List<VectorData> shopKnowledge = new ArrayList<>();
        
        // 示例数据
        shopKnowledge.add(createShopVector(1L, "海底捞火锅", 
            "海底捞是一家知名的火锅连锁店，以优质的服务和新鲜的食材著称。特色菜品包括：毛肚、虾滑、牛肉片等。"));
        
        shopKnowledge.add(createShopVector(2L, "星巴克咖啡", 
            "星巴克是全球知名的咖啡连锁店，提供各种咖啡饮品和轻食。环境优雅，适合商务洽谈和休闲。"));
        
        vectorService.batchAddVectors(shopKnowledge);
    }
    
    private void initProductKnowledge() {
        // 这里可以从数据库加载商品信息
        List<VectorData> productKnowledge = new ArrayList<>();
        
        // 示例数据
        productKnowledge.add(createProductVector(1L, "毛肚", 
            "海底捞特色菜品，选用优质牛肚，口感鲜嫩，是火锅必点菜品。"));
        
        productKnowledge.add(createProductVector(2L, "美式咖啡", 
            "星巴克经典咖啡，选用阿拉比卡咖啡豆，口感醇厚，提神醒脑。"));
        
        vectorService.batchAddVectors(productKnowledge);
    }
    
    private void initReviewKnowledge() {
        // 这里可以从数据库加载评论信息
        List<VectorData> reviewKnowledge = new ArrayList<>();
        
        // 示例数据
        reviewKnowledge.add(createReviewVector(1L, 
            "海底捞的服务真的很好，服务员态度热情，食材新鲜，特别是毛肚非常好吃！"));
        
        reviewKnowledge.add(createReviewVector(2L, 
            "星巴克的环境很舒适，咖啡味道不错，适合和朋友聊天或者工作。"));
        
        vectorService.batchAddVectors(reviewKnowledge);
    }
    
    private VectorData createShopVector(Long shopId, String name, String description) {
        VectorData vectorData = new VectorData();
        vectorData.setId("shop_" + shopId);
        vectorData.setContent(name + "：" + description);
        vectorData.setType("shop");
        vectorData.setMetadata("{\"shopId\":" + shopId + "}");
        return vectorData;
    }
    
    private VectorData createProductVector(Long productId, String name, String description) {
        VectorData vectorData = new VectorData();
        vectorData.setId("product_" + productId);
        vectorData.setContent(name + "：" + description);
        vectorData.setType("product");
        vectorData.setMetadata("{\"productId\":" + productId + "}");
        return vectorData;
    }
    
    private VectorData createReviewVector(Long reviewId, String content) {
        VectorData vectorData = new VectorData();
        vectorData.setId("review_" + reviewId);
        vectorData.setContent(content);
        vectorData.setType("review");
        vectorData.setMetadata("{\"reviewId\":" + reviewId + "}");
        return vectorData;
    }
} 