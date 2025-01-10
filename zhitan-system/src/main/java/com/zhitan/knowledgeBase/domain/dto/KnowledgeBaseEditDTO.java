package com.zhitan.knowledgeBase.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author DYL
 **/
@Data
@ApiModel(value = "知识库编辑参数")
public class KnowledgeBaseEditDTO extends KnowledgeBaseAddDTO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    @NotNull(message = "id不能为空")
    private Long id;
}
