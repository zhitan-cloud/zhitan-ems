package com.zhitan.peakvalley.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 【请填写功能名称】对象 electricity_price
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
@TableName("electricity_price")
public class ElectricityPrice extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 父级id */
    @Excel(name = "父级id")
    @NotEmpty(message = "父级id不能为空")
    private String parentId;

    /** 用电类型（尖、峰、平、谷、深谷） */
    @Excel(name = "用电类型", readConverterExp = "尖、峰、平、谷、深谷")
    @NotEmpty(message = "用电类型不能为空")
    private String type;

    /** 时段开始时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "时段开始时间", width = 30, dateFormat = "HH:mm:ss")
    @NotNull(message = "时段开始时间不能为空")
    private Date startTime;

    /** 时段结束时间 */
    @JsonFormat(pattern = "HH:mm:ss")
    @Excel(name = "时段结束时间", width = 30, dateFormat = "HH:mm:ss")
    @NotNull(message = "时段结束时间不能为空")
    private Date stopTime;
//    @Excel(name = "范围类型字典值cost_range_type")
//    private String rangeType;

    /** 电价 */
    @Excel(name = "电价")
    @NotEmpty(message = "电价不能为空")
    private BigDecimal effecticityPrice;

//    public String getRangeType() {
//        return rangeType;
//    }
//
//    public void setRangeType(String rangeType) {
//        this.rangeType = rangeType;
//    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
        
    public void setParentId(String parentId)
    {
        this.parentId = parentId;
    }

    public String getParentId()
    {
        return parentId;
    }
        
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
        
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
        
    public void setStopTime(Date stopTime)
    {
        this.stopTime = stopTime;
    }

    public Date getStopTime()
    {
        return stopTime;
    }
        
    public void setEffecticityPrice(BigDecimal effecticityPrice)
    {
        this.effecticityPrice = effecticityPrice;
    }

    public BigDecimal getEffecticityPrice()
    {
        return effecticityPrice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("type", getType())
            .append("startTime", getStartTime())
            .append("stopTime", getStopTime())
            .append("effecticityPrice", getEffecticityPrice())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
        .toString();
    }
}
