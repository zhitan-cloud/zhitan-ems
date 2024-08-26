package com.dingzhuo.energy.data.monitoring.device.domain;

import com.dingzhuo.energy.data.model.domain.StateType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备计算公式对象 device_formula
 * 
 * @author zhaow
 * @date 2020-03-20
 */
public class DeviceFormula extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private String id;

    /** 设备模型树的nodeid */
    private String deviceId;

    /** 设备状态表的id */
    private String stateId;

    /** 设备状态设置的计算公式 */
    private String formulaText;

    /** 引擎用计算文本 */
    private String calcText;
    /**
     * 是否启用
     */
    private String isEnable;

    /**
     * 设备指定的系统状态信息
     */
    private StateType stateType;
    /**
     * 设备状态计算公式下的 参数集合对象
     */
    private List<DeviceFormulaParam> deviceFormulaParams = new ArrayList<>();

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setDeviceId(String deviceId) 
    {
        this.deviceId = deviceId;
    }

    public String getDeviceId() 
    {
        return deviceId;
    }
    public void setStateId(String stateId) 
    {
        this.stateId = stateId;
    }

    public String getStateId() 
    {
        return stateId;
    }
    public void setFormulaText(String formulaText) 
    {
        this.formulaText = formulaText;
    }

    public String getFormulaText() 
    {
        return formulaText;
    }
    public void setCalcText(String calcText) 
    {
        this.calcText = calcText;
    }

    public String getCalcText() 
    {
        return calcText;
    }

    public List<DeviceFormulaParam> getDeviceFormulaParams() {
        return deviceFormulaParams;
    }

    public void setDeviceFormulaParams(List<DeviceFormulaParam> deviceFormulaParams) {
        this.deviceFormulaParams = deviceFormulaParams;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("stateId", getStateId())
            .append("formulaText", getFormulaText())
            .append("calcText", getCalcText())
            .toString();
    }
}
