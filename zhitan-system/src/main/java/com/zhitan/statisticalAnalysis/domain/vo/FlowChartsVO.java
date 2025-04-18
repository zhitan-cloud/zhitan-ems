package com.zhitan.statisticalAnalysis.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

/**
 * 能流图查询
 */
@Data
public class FlowChartsVO {

    // 总累积量
    @ApiModelProperty(value = "总累积量")
    private BigDecimal totalAccumulatedAmount;

    // 子节点累积量
    @ApiModelProperty(value = "子节点累积量")
    private BigDecimal childNodeAccumulatedAmount;

    // 差值
    @ApiModelProperty(value = "差值")
    private BigDecimal difference;

    // 能耗损失比例
    @ApiModelProperty(value = "能耗损失比例")
    private BigDecimal energyLossRatio;

    private List<FlowChartsItemVO> itemVOList;

    public BigDecimal getTotalAccumulatedAmount() {
        if (totalAccumulatedAmount == null){
            return BigDecimal.ZERO;
        }
        return totalAccumulatedAmount;
    }

    public BigDecimal getChildNodeAccumulatedAmount() {
        if (childNodeAccumulatedAmount == null){
            return BigDecimal.ZERO;
        }
        return childNodeAccumulatedAmount;
    }

    public BigDecimal getDifference() {
        return difference = totalAccumulatedAmount.subtract(childNodeAccumulatedAmount);
    }

    public BigDecimal getEnergyLossRatio() {
        if (BigDecimal.ZERO.compareTo(totalAccumulatedAmount) == 0
                || BigDecimal.ZERO.compareTo(difference) == 0) {
            return BigDecimal.ZERO;
        }
        // 先计算比例，再乘以 100 转换为百分数
        return energyLossRatio = difference.divide(totalAccumulatedAmount, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
    }

    public FlowChartsVO() {
        this.totalAccumulatedAmount = BigDecimal.ZERO;
        this.childNodeAccumulatedAmount = BigDecimal.ZERO;
        this.difference = BigDecimal.ZERO;
        this.energyLossRatio = BigDecimal.ZERO;
        this.itemVOList = Collections.emptyList();
    }
}
