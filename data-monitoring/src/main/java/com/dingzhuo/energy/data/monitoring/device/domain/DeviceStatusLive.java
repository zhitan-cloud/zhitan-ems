package com.dingzhuo.energy.data.monitoring.device.domain;

import com.dingzhuo.energy.data.model.domain.StateType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 设备启停实时监测对象 device_status_live
 * 
 * @author sys
 * @date 2020-03-23
 */
public class DeviceStatusLive extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 设备主键关联模型的节点ID */
    @Excel(name = "设备主键关联模型的节点ID")
    private String deviceId;

    /** 关联状态配置，关联state_type.id */
    @Excel(name = "关联状态配置，关联state_type.id")
    private String statusId;

    /** 设备状态名称 */
    @Excel(name = "设备状态名称")
    private String statusName;

    /** 设备状态码 */
    @Excel(name = "设备状态码")
    private String statusCode;

    /** 状态开始时间 */
    @Excel(name = "状态开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private String beginTime;
    /**
     * 模型中文名称
     */
    private String modelNodeName;
    /**
     * 状态类型
     */
    private StateType stateType;

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
    public void setStatusId(String statusId) 
    {
        this.statusId = statusId;
    }

    public String getStatusId() 
    {
        return statusId;
    }
    public void setStatusName(String statusName) 
    {
        this.statusName = statusName;
    }

    public String getStatusName() 
    {
        return statusName;
    }
    public void setStatusCode(String statusCode) 
    {
        this.statusCode = statusCode;
    }

    public String getStatusCode() 
    {
        return statusCode;
    }
    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getBeginTime()
    {
        return beginTime;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public String getModelNodeName() {
        return modelNodeName;
    }

    public void setModelNodeName(String modelNodeName) {
        this.modelNodeName = modelNodeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("deviceId", getDeviceId())
            .append("statusId", getStatusId())
            .append("statusName", getStatusName())
            .append("statusCode", getStatusCode())
            .append("beginTime", getBeginTime())
            .toString();
    }
}
