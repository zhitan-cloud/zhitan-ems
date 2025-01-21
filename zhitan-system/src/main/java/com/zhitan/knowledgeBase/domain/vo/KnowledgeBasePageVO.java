package com.zhitan.knowledgeBase.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author DYL
 **/
@Data
public class KnowledgeBasePageVO {

    /**
     * id
     */
    @JsonSerialize(using = ToStringSerializer.class)
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
