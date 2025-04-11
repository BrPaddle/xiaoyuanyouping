package com.hmdp.service.impl;

import com.hmdp.entity.VectorData;
import com.hmdp.service.VectorService;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.search.Document;
import redis.clients.jedis.search.Query;
import redis.clients.jedis.search.Schema;
import redis.clients.jedis.search.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VectorServiceImpl implements VectorService {
    
    @Autowired
    private EmbeddingClient embeddingClient;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String VECTOR_INDEX = "vector_index";
    private static final int VECTOR_DIMENSION = 1536; // 通义千问向量维度
    
    @Override
    public void addVector(VectorData vectorData) {
        // 1. 获取文本向量
        List<Float> vector = embeddingClient.embed(vectorData.getContent());
        vectorData.setVector(vector);
        
        // 2. 存储到Redis
        try (Jedis jedis = new Jedis()) {
            // 创建索引（如果不存在）
            createIndexIfNotExists(jedis);
            
            // 存储向量数据
            Map<String, String> fields = Map.of(
                "content", vectorData.getContent(),
                "type", vectorData.getType(),
                "metadata", vectorData.getMetadata()
            );
            
            jedis.hset("vector:" + vectorData.getId(), fields);
            jedis.hset("vector:" + vectorData.getId(), "vector", 
                vector.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
    }
    
    @Override
    public void batchAddVectors(List<VectorData> vectorDataList) {
        vectorDataList.forEach(this::addVector);
    }
    
    @Override
    public List<VectorData> searchSimilar(String query, String type, int topK) {
        // 1. 获取查询向量
        List<Float> queryVector = embeddingClient.embed(query);
        
        // 2. 执行向量搜索
        try (Jedis jedis = new Jedis()) {
            String vectorStr = queryVector.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
            
            // 构建查询
            Query searchQuery = new Query("*=>[KNN $k @vector $vector AS score]")
                .addParam("k", topK)
                .addParam("vector", vectorStr)
                .dialect(2);
            
            if (type != null) {
                searchQuery.addFilter("@type:" + type);
            }
            
            // 执行搜索
            SearchResult result = jedis.ftSearch(VECTOR_INDEX, searchQuery);
            
            // 转换结果
            return result.getDocuments().stream()
                .map(this::documentToVectorData)
                .collect(Collectors.toList());
        }
    }
    
    @Override
    public void deleteVector(String id) {
        try (Jedis jedis = new Jedis()) {
            jedis.del("vector:" + id);
        }
    }
    
    private void createIndexIfNotExists(Jedis jedis) {
        if (!jedis.exists(VECTOR_INDEX)) {
            Schema schema = new Schema()
                .addTextField("content", 1.0)
                .addTextField("type", 1.0)
                .addTextField("metadata", 1.0)
                .addVectorField("vector", Schema.VectorField.VectorAlgo.HNSW,
                    Map.of("TYPE", "FLOAT32",
                          "DIM", VECTOR_DIMENSION,
                          "DISTANCE_METRIC", "COSINE"));
            
            jedis.ftCreate(VECTOR_INDEX, schema);
        }
    }
    
    private VectorData documentToVectorData(Document doc) {
        VectorData vectorData = new VectorData();
        vectorData.setId(doc.getId());
        vectorData.setContent(doc.getString("content"));
        vectorData.setType(doc.getString("type"));
        vectorData.setMetadata(doc.getString("metadata"));
        
        String vectorStr = doc.getString("vector");
        if (vectorStr != null) {
            List<Float> vector = new ArrayList<>();
            for (String v : vectorStr.split(",")) {
                vector.add(Float.parseFloat(v));
            }
            vectorData.setVector(vector);
        }
        
        return vectorData;
    }
} 