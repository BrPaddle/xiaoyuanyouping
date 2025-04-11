package com.hmdp.service;

import com.hmdp.entity.VectorData;
import java.util.List;

public interface VectorService {
    /**
     * 添加向量数据
     * @param vectorData 向量数据
     */
    void addVector(VectorData vectorData);
    
    /**
     * 批量添加向量数据
     * @param vectorDataList 向量数据列表
     */
    void batchAddVectors(List<VectorData> vectorDataList);
    
    /**
     * 相似度搜索
     * @param query 查询文本
     * @param type 数据类型
     * @param topK 返回结果数量
     * @return 相似度最高的向量数据列表
     */
    List<VectorData> searchSimilar(String query, String type, int topK);
    
    /**
     * 删除向量数据
     * @param id 向量数据ID
     */
    void deleteVector(String id);
} 