package com.zhitan.knowledgeBase.domain.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author DYL
 **/
@Data
@ApiModel(value = "知识库详情")
public class KnowledgeBaseDetailVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;
    /**
     * 能源类型(0:电;1:水;2:天然气;3:蒸汽)
     */
    @ApiModelProperty(value = "能源类型(0:电;1:水;2:天然气;3:蒸汽)")
    private Integer type;
    /**
     * 能源类型描述
     */
    @ApiModelProperty(value = "能源类型描述")
    private String typeDesc;
    /**
     * 内容
     */
    @ApiModelProperty(value = "内容")
    private String content;
    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<String> url;
}
