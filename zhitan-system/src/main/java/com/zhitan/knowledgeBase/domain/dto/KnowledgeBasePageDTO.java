package com.zhitan.knowledgeBase.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author DYL
 **/
@Data
@ApiModel(value = "知识库分页查询参数")
public class KnowledgeBasePageDTO {


    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private Integer pageNum;

    /**
     * 每页条数
     */
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 能源类型
     */
    @ApiModelProperty(value = "能源类型")
    private Integer type;
}
