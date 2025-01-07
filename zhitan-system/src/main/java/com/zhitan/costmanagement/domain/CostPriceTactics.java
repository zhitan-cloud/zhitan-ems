package com.zhitan.costmanagement.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zhitan.common.annotation.Excel;

/**
 * 成本策略对象 cost_price_tactics
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@TableName("cost_price_tactics")
@Data
public class CostPriceTactics extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 是否阶梯价格0否1是 */
    @Excel(name = "是否阶梯价格0否1是")
    private String type;

    /** 策略编码 */
    @Excel(name = "策略编码")
    private String tacticsNumber;

    /** 策略名称 */
    @Excel(name = "策略名称")
    private String tacticsName;

    /** 能源品种 */
    @Excel(name = "能源品种")
    private Integer energyType;


}
