package com.hmdp.service;

import com.hmdp.entity.VectorData;
import java.util.List;

public interface KnowledgeBaseService {
    /**
     * 初始化知识库
     */
    void initKnowledgeBase();
    
    /**
     * 添加商家知识
     * @param shopId 商家ID
     * @param shopInfo 商家信息
     */
    void addShopKnowledge(Long shopId, String shopInfo);
    
    /**
     * 添加商品知识
     * @param productId 商品ID
     * @param productInfo 商品信息
     */
    void addProductKnowledge(Long productId, String productInfo);
    
    /**
     * 添加评论知识
     * @param reviewId 评论ID
     * @param reviewContent 评论内容
     */
    void addReviewKnowledge(Long reviewId, String reviewContent);
    
    /**
     * 批量添加知识
     * @param knowledgeList 知识列表
     */
    void batchAddKnowledge(List<VectorData> knowledgeList);
} 