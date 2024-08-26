package com.dingzhuo.energy.data.model.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 系统状态维护对象 state_type
 * 
 * @author sys
 * @date 2020-03-18
 */
public class StateType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 状态主键id */
    @Excel(name = "状态主键id")
    private String stateId;

    /** 状态名称 */
    @Excel(name = "状态名称")
    private String stateName;

    /** 状态编码 */
    @Excel(name = "状态编码")
    private String stateCode;

    /** 色号 */
    @Excel(name = "色号")
    private String colorNumber;

    public void setStateId(String stateId) 
    {
        this.stateId = stateId;
    }

    public String getStateId() 
    {
        return stateId;
    }
    public void setStateName(String stateName) 
    {
        this.stateName = stateName;
    }

    public String getStateName() 
    {
        return stateName;
    }
    public void setStateCode(String stateCode) 
    {
        this.stateCode = stateCode;
    }

    public String getStateCode() 
    {
        return stateCode;
    }
    public void setColorNumber(String colorNumber) 
    {
        this.colorNumber = colorNumber;
    }

    public String getColorNumber() 
    {
        return colorNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("stateId", getStateId())
            .append("stateName", getStateName())
            .append("stateCode", getStateCode())
            .append("colorNumber", getColorNumber())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
