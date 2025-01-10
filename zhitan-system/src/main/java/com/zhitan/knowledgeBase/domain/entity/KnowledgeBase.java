package com.zhitan.knowledgeBase.domain.entity;

import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * 知识库(KnowledgeBase)实体类
 *
 * @author makejava
 * @since 2025-01-10 14:57:10
 */
@Data
public class KnowledgeBase extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -68391633692977272L;
    /**
     * id
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 能源类型(0:电;1:水;2:天然气;3:蒸汽)
     */
    private Integer type;
    /**
     * 内容
     */
    private String content;
    /**
     * 删除标志
     */
    private Boolean delFlag;
}

