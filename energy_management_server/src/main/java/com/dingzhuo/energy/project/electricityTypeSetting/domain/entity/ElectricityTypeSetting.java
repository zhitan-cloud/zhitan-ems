package com.dingzhuo.energy.project.electricityTypeSetting.domain.entity;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 计费规则对象 rules
 *
 * @author ruoyi
 * @date 2024-06-14
 */
@Data
public class ElectricityTypeSetting extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private String id;

    /**
     * 规则名称
     */
    @Excel(name = "规则名称")
    private String name;

    /**
     * 生效时间
     */
    @Excel(name = "生效时间")
    private Date effectiveDate;

    /**
     * 尖电费价格
     */
    @Excel(name = "尖电费价格")
    private BigDecimal sharpFee;

    /**
     * 峰电费价格
     */
    @Excel(name = "峰电费价格")
    private BigDecimal peakFee;

    /**
     * 平电费价格
     */
    @Excel(name = "平电费价格")
    private BigDecimal flatFee;

    /**
     * 谷电费价格
     */
    @Excel(name = "谷电费价格")
    private BigDecimal valleyFee;

    /**
     * 组织架构id
     */
    @Excel(name = "组织架构id")
    private Long deptId;

    /**
     * '0'删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

}
