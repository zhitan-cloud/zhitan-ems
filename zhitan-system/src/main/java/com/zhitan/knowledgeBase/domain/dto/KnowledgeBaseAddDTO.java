package com.zhitan.knowledgeBase.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author DYL
 **/
@Data
@ApiModel(value = "知识库新增参数")
public class KnowledgeBaseAddDTO {

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题", required = true)
    @NotBlank(message = "请填写标题")
    private String title;

    /**
     * 能源类型
     */
    @ApiModelProperty(value = "能源类型", required = true)
    @NotNull(message = "请选择能源类型")
    private Integer type;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    @NotBlank(message = "请填写内容")
    private String content;

    /**
     * 附件
     */
    @ApiModelProperty(value = "附件")
    private List<String> url;
}
