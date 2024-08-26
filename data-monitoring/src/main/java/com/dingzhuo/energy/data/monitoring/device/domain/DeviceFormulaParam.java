package com.dingzhuo.energy.data.monitoring.device.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 设备状态参数对象 device_formula_param
 * 
 * @author zhaow
 * @date 2020-03-20
 */
public class DeviceFormulaParam extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String id;

    /** device_formula表的主键Id */
    private String devFormulaId;

    /** 计算公式中参数名称 */
    private String paramName;

    /** 计算公式中参数值 */
    private String paramValue;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setDevFormulaId(String devFormulaId) 
    {
        this.devFormulaId = devFormulaId;
    }

    public String getDevFormulaId() 
    {
        return devFormulaId;
    }
    public void setParamName(String paramName) 
    {
        this.paramName = paramName;
    }

    public String getParamName() 
    {
        return paramName;
    }
    public void setParamValue(String paramValue) 
    {
        this.paramValue = paramValue;
    }

    public String getParamValue() 
    {
        return paramValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("devFormulaId", getDevFormulaId())
            .append("paramName", getParamName())
            .append("paramValue", getParamValue())
            .toString();
    }
}
