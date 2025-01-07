package com.zhitan.energyIndicators.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.core.domain.BaseEntity;
import lombok.Data;
import com.zhitan.common.annotation.Excel;

import java.math.BigDecimal;

/**
 * 能源指标对象 energy_indicators
 *
 * @author ZhiTan
 * @date 2024-10-25
 */
@TableName("energy_indicators")
@Data
public class EnergyIndicators extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 用能单元id（model_node） */
    @Excel(name = "用能单元id", readConverterExp = "model_node")
    private String nodeId;

    /** 时间类型（年、月、日） */
    @Excel(name = "时间类型", readConverterExp = "年、月、日")
    private String timeType;

    /** 时间 */
    @Excel(name = "时间")
    private String dataTime;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 产量 */
    @Excel(name = "产量")
    private BigDecimal number;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 能源类型，字典项energy_type */
    @Excel(name = "能源类型，字典项energy_type")
    private String energyType;

    /** 指标类型，字典项indicators_type */
    @Excel(name = "指标类型，字典项indicators_type")
    private String indicatorsType;

    /** 主键 */
    @Excel(name = "主键")
    private String energyIndicatorsId;

    /** 节点名称 */
    @Excel(name = "节点名称")
    private String nodeName;

//
//    public void setNodeId(String nodeId)
//    {
//        this.nodeId = nodeId;
//    }
//
//    public String getNodeId()
//    {
//        return nodeId;
//    }
//
//    public void setTimeType(String timeType)
//    {
//        this.timeType = timeType;
//    }
//
//    public String getTimeType()
//    {
//        return timeType;
//    }
//
//    public void setDataTime(String dataTime)
//    {
//        this.dataTime = dataTime;
//    }
//
//    public String getDataTime()
//    {
//        return dataTime;
//    }
//
//    public void setName(String name)
//    {
//        this.name = name;
//    }
//
//    public String getName()
//    {
//        return name;
//    }
//
//    public void setNumber(String number)
//    {
//        this.number = number;
//    }
//
//    public String getNumber()
//    {
//        return number;
//    }
//
//    public void setUnit(String unit)
//    {
//        this.unit = unit;
//    }
//
//    public String getUnit()
//    {
//        return unit;
//    }
//
//    public void setDelFlag(String delFlag)
//    {
//        this.delFlag = delFlag;
//    }
//
//    public String getDelFlag()
//    {
//        return delFlag;
//    }
//
//    public void setEnergyType(String energyType)
//    {
//        this.energyType = energyType;
//    }
//
//    public String getEnergyType()
//    {
//        return energyType;
//    }
//
//    public void setIndicatorsType(String indicatorsType)
//    {
//        this.indicatorsType = indicatorsType;
//    }
//
//    public String getIndicatorsType()
//    {
//        return indicatorsType;
//    }
//
//    public void setEnergyIndicatorsId(String energyIndicatorsId)
//    {
//        this.energyIndicatorsId = energyIndicatorsId;
//    }
//
//    public String getEnergyIndicatorsId()
//    {
//        return energyIndicatorsId;
//    }
//
//    public void setNodeName(String nodeName)
//    {
//        this.nodeName = nodeName;
//    }
//
//    public String getNodeName()
//    {
//        return nodeName;
//    }
//
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
//            .append("nodeId", getNodeId())
//            .append("timeType", getTimeType())
//            .append("dataTime", getDataTime())
//            .append("name", getName())
//            .append("number", getNumber())
//            .append("unit", getUnit())
//            .append("delFlag", getDelFlag())
//            .append("createBy", getCreateBy())
//            .append("createTime", getCreateTime())
//            .append("updateBy", getUpdateBy())
//            .append("updateTime", getUpdateTime())
//            .append("remark", getRemark())
//            .append("energyType", getEnergyType())
//            .append("indicatorsType", getIndicatorsType())
//            .append("energyIndicatorsId", getEnergyIndicatorsId())
//            .append("nodeName", getNodeName())
//        .toString();
//    }
}
