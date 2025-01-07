package com.zhitan.spikesandvalleys.domain;

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
 * 【请填写功能名称】对象 spikes_and_valleys_item
 *
 * @author ZhiTan
 * @date 2024-10-29
 */
@TableName("spikes_and_valleys_item")
@Data
public class SpikesAndValleysItem extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 时段:尖峰平谷字典值1尖2峰3平4谷 */
    @Excel(name = "时段:尖峰平谷字典值1尖2峰3平4谷")
    private String time;

    /** 电费 */
    @Excel(name = "电费")
    private BigDecimal electrovalency;

    /** 开始时间 */
    @Excel(name = "开始时间")
    private String startTime;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private String endTime;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String schemeId;


}
