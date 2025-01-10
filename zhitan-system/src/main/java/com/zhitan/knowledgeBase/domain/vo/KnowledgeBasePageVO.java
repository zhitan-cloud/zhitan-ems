package com.zhitan.knowledgeBase.domain.vo;

import lombok.Data;

/**
 * @Author DYL
 **/
@Data
public class KnowledgeBasePageVO {

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
     * 能源类型描述
     */
    private String typeDesc;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建时间
     */
    private String createTime;
}
