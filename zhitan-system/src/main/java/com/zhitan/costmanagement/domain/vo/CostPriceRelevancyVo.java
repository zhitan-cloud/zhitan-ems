package com.zhitan.costmanagement.domain.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import com.zhitan.costmanagement.domain.CostPriceRelevancy;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 单价关联对象 cost_price_relevancy
 *
 * @author ZhiTan
 * @date 2024-11-09
 */
@Data
public class CostPriceRelevancyVo extends CostPriceRelevancy {
    private BigDecimal price;
    private String electricityType;

}
