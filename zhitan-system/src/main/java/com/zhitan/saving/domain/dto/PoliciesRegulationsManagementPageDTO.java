package com.zhitan.saving.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author ZhiTan
 * @date 2025/01/13
 */
@Data
@ApiModel(value = "PoliciesRegulationsManagementPageDTO", description = "政策法规-列表查询Dto")
public class PoliciesRegulationsManagementPageDTO {

    /**
     * 类型
     */
    private String type;

    /**
     * 标题
     */
    private String title;
}
