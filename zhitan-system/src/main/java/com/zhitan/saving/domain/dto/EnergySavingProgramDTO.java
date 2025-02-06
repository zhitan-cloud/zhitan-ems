package com.zhitan.saving.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * @author Geoffrey
 * @date 2025/01/13
 */
@Data
@ApiModel(value = "节能项目DTO")
public class EnergySavingProgramDTO {
    private Long id;


    /**
     * 节能计划
     */
    private String plan;


    /**
     * 完成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "完成时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date completionTime;
    /**
     * 项目组长
     */
    @Excel(name = "项目组长")
    private String liablePerson;


    /**
     * 实施计划
     */
    private String implementationPlan;
    /**
     * 当前工作
     */
    private String currentWork;
    /**
     * 节约量
     */
    private String savingAmount;
    /**
     * 备注
     */
    private String remark;


}
