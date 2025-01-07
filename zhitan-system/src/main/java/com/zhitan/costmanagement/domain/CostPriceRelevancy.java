package com.zhitan.costmanagement.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.zhitan.common.annotation.Excel;

/**
 * 单价关联对象 cost_price_relevancy
 *
 * @author ZhiTan
 * @date 2024-11-09
 */
@TableName("cost_price_relevancy")
@Data
public class CostPriceRelevancy extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 用能单位 */
    @Excel(name = "用能单位")
    private String nodeId;

    /** 单价策略id */
    @Excel(name = "单价策略id")
    private String tacticsId;

    /** 能源品种 */
    @Excel(name = "能源品种")
    private Integer energyType;

    /** 有效期开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effectiveBeginTime;

    /** 有效期结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "有效期结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date effectiveEndTime;


}
