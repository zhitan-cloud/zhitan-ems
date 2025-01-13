package com.zhitan.saving.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;


/**
 * 节能项目管理
 *
 * @author Geoffrey
 * @date 2025/01/13
 */
@Data
@ApiModel(value = "EnergySavingProgram", description = "节能项目管理实体")
public class EnergySavingProgram extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;


    /**
     * 总体计划
     */
    @Excel(name = "总体计划")
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


    /**
     * 是否删除
     */
    @Excel(name = "是否删除")
    private Long del;


}
