package com.hmdp.entity;

import lombok.Data;
import java.util.List;

@Data
public class VectorData {
    private String id;
    private String content;
    private List<Float> vector;
    private String type;  // 数据类型：shop, product, review等
    private String metadata;  // 额外元数据，JSON格式
} 