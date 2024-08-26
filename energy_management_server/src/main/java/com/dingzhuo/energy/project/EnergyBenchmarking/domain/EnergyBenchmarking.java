package com.dingzhuo.energy.project.EnergyBenchmarking.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * energy_benchmarking对象 energy_benchmarking
 * 
 * @author sys
 * @date 2020-11-18
 */
public class EnergyBenchmarking extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 单位名称 */
    @Excel(name = "单位名称")
    private String name;

    /** 指标名称 */
    @Excel(name = "指标名称")
    private String codeId;

    /** 指标单位 */
    @Excel(name = "指标单位")
    private String unit;

    /** 标杆范围 */
    @Excel(name = "标杆范围")
    private String range;

    /** 标杆类型 */
    @Excel(name = "标杆类型")
    private String type;

    /** 标杆值 */
    @Excel(name = "标杆值")
    private String value;
    /** 标杆值 */
    private String modelNode;
    /** 有效期 */
    @Excel(name = "有效期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date termValidity;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setCodeId(String codeId) 
    {
        this.codeId = codeId;
    }

    public String getCodeId() 
    {
        return codeId;
    }
    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }
    public void setRange(String range) 
    {
        this.range = range;
    }

    public String getRange() 
    {
        return range;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }
    public void setTermValidity(Date termValidity) 
    {
        this.termValidity = termValidity;
    }

    public Date getTermValidity() 
    {
        return termValidity;
    }

    public String getModelNode() {
        return modelNode;
    }

    public void setModelNode(String modelNode) {
        this.modelNode = modelNode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("codeId", getCodeId())
            .append("unit", getUnit())
            .append("range", getRange())
            .append("type", getType())
            .append("value", getValue())
            .append("termValidity", getTermValidity())
            .append("modelNode", getModelNode())
            .toString();
    }
}
