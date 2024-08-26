package com.dingzhuo.energy.data.monitoring.device.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 设备启停历史监测对象 device_status_history
 * 
 * @author sys
 * @date 2020-03-25
 */
public class DeviceStatusHistory extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /**
     * 名称
     * */
    @Excel(name = "设备名称")
    private String indexName;

    /** 设备主键 */
//    @Excel(name = "设备主键")
    private String deviceId;

    /** 关联状态配置 */
//    @Excel(name = "关联状态配置")
    private String statusId;

    /** 设备状态名称 */
    @Excel(name = "设备状态")
    private String statusName;

    /** 设备状态码 */
//    @Excel(name = "设备状态码")
    private String statusCode;

    /** 状态开始时间 */
    @Excel(name = "开始时间", width = 30)
    private String devBeginTime;

    /** 状态结束时间 */
    @Excel(name = "结束时间", width = 30)
    private String devEndTime;

    /** 状态持续时间 */
    @Excel(name = "持续时间")
    private String duration;

    /**
     * 模型查询层级标志 B本级  ALL包含下级
     * */
    private String eierarchyFlag;
    /**
     * 指标类型 COLLECT采集点  STATISTIC指标
     * */
    private String indexType;




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

    public String getDevBeginTime() {
        return devBeginTime;
    }

    public void setDevBeginTime(String devBeginTime) {
        this.devBeginTime = devBeginTime;
    }

    public String getDevEndTime() {
        return devEndTime;
    }

    public void setDevEndTime(String devEndTime) {
        this.devEndTime = devEndTime;
    }

    public void setDuration(String duration)
    {
        this.duration = duration;
    }

    public String getDuration()
    {
        return duration;
    }

    public String getEierarchyFlag() {
        return eierarchyFlag;
    }

    public void setEierarchyFlag(String eierarchyFlag) {
        this.eierarchyFlag = eierarchyFlag;
    }

    public String getIndexType() {
        return indexType;
    }

    public void setIndexType(String indexType) {
        this.indexType = indexType;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
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
            .append("endTime", getEndTime())
            .append("duration", getDuration())
            .toString();
    }
}
