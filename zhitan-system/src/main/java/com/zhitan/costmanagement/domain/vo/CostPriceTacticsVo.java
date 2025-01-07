package com.zhitan.costmanagement.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.zhitan.costmanagement.domain.CostPriceTacticsItem;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysItem;
import lombok.Data;

import java.util.List;

/**
 * 成本策略对象 vo
 *
 *
 */
@Data
public class CostPriceTacticsVo extends CostPriceTactics {
    List<CostPriceTacticsItem> itemList;

}
