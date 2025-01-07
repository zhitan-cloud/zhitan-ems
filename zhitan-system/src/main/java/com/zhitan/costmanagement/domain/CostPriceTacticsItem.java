package com.zhitan.costmanagement.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zhitan.common.annotation.Excel;

import java.math.BigDecimal;

/**
 * 【请填写功能名称】对象 cost_price_tactics_item
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@TableName("cost_price_tactics_item")
@Data
public class CostPriceTacticsItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 范围类型字典值cost_range_type */
    @Excel(name = "范围类型字典值cost_range_type")
    private String rangeType;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String tacticsNumber;

    /** 关联策略id */
    @Excel(name = "关联策略id")
    private String tacticsId;

    /** 字典值spikes_and_valleys */
    @Excel(name = "字典值spikes_and_valleys")
    private String electricityType;

    /** 区间范围最小值 */
    @Excel(name = "区间范围最小值")
    private String slotsMin;

    /** 区间范围最大值 */
    @Excel(name = "区间范围最大值")
    private String slotsMax;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;


}
