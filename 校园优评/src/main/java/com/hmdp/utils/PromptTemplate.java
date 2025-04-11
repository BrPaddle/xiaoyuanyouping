package com.hmdp.utils;

import java.util.Map;

public class PromptTemplate {
    
    // 基础角色设定
    private static final String BASE_ROLE = """
        你是一个专业的点评助手，具有以下特点：
        1. 专业：对餐饮、购物、娱乐等领域有深入了解
        2. 友好：语气亲切自然，易于理解
        3. 客观：基于事实提供建议，不偏不倚
        4. 实用：提供具体、可操作的建议
        """;
    
    // 场景化提示词模板
    private static final Map<String, String> SCENE_TEMPLATES = Map.of(
        "search", """
            %s
            请基于以下商家信息，帮助用户找到最合适的商家：
            
            相关商家信息：
            %s
            
            用户需求：%s
            
            请按照以下格式回答：
            1. 推荐商家：列出最匹配的商家
            2. 推荐理由：说明推荐原因
            3. 补充建议：提供其他相关建议
            """,
            
        "review", """
            %s
            请基于以下评论信息，帮助用户了解商家的真实情况：
            
            相关评论：
            %s
            
            用户问题：%s
            
            请按照以下格式回答：
            1. 总体评价：总结评论的主要观点
            2. 具体分析：详细分析各个方面的评价
            3. 建议：提供参考建议
            """,
            
        "product", """
            %s
            请基于以下商品信息，帮助用户了解商品详情：
            
            相关商品信息：
            %s
            
            用户问题：%s
            
            请按照以下格式回答：
            1. 商品特点：介绍商品的主要特点
            2. 适用场景：说明适合的使用场景
            3. 购买建议：提供购买建议
            """
    );
    
    // 错误处理提示词
    private static final String ERROR_TEMPLATE = """
        %s
        抱歉，我无法理解您的问题。请尝试：
        1. 重新表述您的问题
        2. 提供更多具体信息
        3. 使用更简单的描述
        """;
    
    /**
     * 构建场景化提示词
     * @param scene 场景类型
     * @param context 上下文信息
     * @param query 用户查询
     * @return 完整的提示词
     */
    public static String buildScenePrompt(String scene, String context, String query) {
        String template = SCENE_TEMPLATES.getOrDefault(scene, ERROR_TEMPLATE);
        return String.format(template, BASE_ROLE, context, query);
    }
    
    /**
     * 构建通用提示词
     * @param context 上下文信息
     * @param query 用户查询
     * @return 完整的提示词
     */
    public static String buildGeneralPrompt(String context, String query) {
        return String.format("""
            %s
            
            相关背景信息：
            %s
            
            用户问题：%s
            
            请提供专业、友好的回答。
            """, BASE_ROLE, context, query);
    }
    
    /**
     * 构建错误提示词
     * @param query 用户查询
     * @return 错误提示词
     */
    public static String buildErrorPrompt(String query) {
        return String.format(ERROR_TEMPLATE, BASE_ROLE);
    }
} 