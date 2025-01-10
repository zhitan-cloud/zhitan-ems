package com.zhitan.knowledgeBase.domain.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 知识库附件表(KnowledgeBaseFile)实体类
 *
 * @author makejava
 * @since 2025-01-10 14:57:12
 */
@Data
public class KnowledgeBaseFile implements Serializable {
    private static final long serialVersionUID = 998732434860554353L;
    /**
     * id
     */
    private Long id;
    /**
     * 知识库id
     */
    private Long knowledgeBaseId;
    /**
     * 文件地址
     */
    private String url;
}

