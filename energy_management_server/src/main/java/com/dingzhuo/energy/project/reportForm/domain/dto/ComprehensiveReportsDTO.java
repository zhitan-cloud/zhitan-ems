package com.dingzhuo.energy.project.reportForm.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 综合报表请求类
 *
 * @Author: Zhujw
 * @Date: 2023/3/16
 */
@Data
public class ComprehensiveReportsDTO {

    /**
     * 模型code
     */
    @NotBlank(message = "模型编码为空")
    private String modeCode;

    /**
     * 查询时间
     */
    @NotNull(message = "未找到查询时间")
    @DateTimeFormat(pattern = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    private Date time;

    /**
     * 能源类型
     */
    private String energyType;
}
