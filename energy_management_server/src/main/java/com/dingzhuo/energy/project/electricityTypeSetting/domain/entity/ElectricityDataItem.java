package com.dingzhuo.energy.project.electricityTypeSetting.domain.entity;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 尖峰平谷数据表
 *
 * @author sys
 * @date 2024-08-27
 */
@Data
public class ElectricityDataItem {
    private static final long serialVersionUID = 1L;

    /**
     * 点位id
     */
    @Excel(name = "点位id")
    private String indexId;

    /**
     * 时间编号
     */
    @Excel(name = "时间编号")
    private String timeCode;

    /**
     * 时间类型
     */
    @Excel(name = "时间类型")
    private String timeType;

    /**
     * 用电类型峰、平、谷等
     */
    @Excel(name = "用电类型峰、平、谷等")
    private String electricityType;

    /**
     * 指标编码，冗余字段，可以用 code 查询
     */
    @Excel(name = "指标编码，冗余字段，可以用 code 查询")
    private String indexCode;

    /**
     * 数据时间
     */
    @Excel(name = "数据时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataTime;

    /**
     * 开始时间
     */
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 结束时间
     */
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 用电量
     */
    @Excel(name = "用电量")
    private BigDecimal electricity;

    /**
     * 电费
     */
    @Excel(name = "电费")
    private BigDecimal cost;

    /**
     * 单价
     */
    @Excel(name = "单价")
    private BigDecimal price;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;
}
