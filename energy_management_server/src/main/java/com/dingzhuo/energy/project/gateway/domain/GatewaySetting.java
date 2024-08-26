package com.dingzhuo.energy.project.gateway.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 网关配置信息对象 gateway_setting
 *
 * @author zhw
 * @date 2022-04-15
 */
public class GatewaySetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** UUID主键 */
    private String id;

    /** 网关编号 */
    @Excel(name = "网关编号")
    private String gatewayNum;

    /** 厂区 */
    @Excel(name = "厂区")
    private String plantArea;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 心跳时间 */
    @Excel(name = "心跳时间", width = 30, dateFormat = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date hbtTime;

    /**
     * 网关状态 离线/在线
     */
    @Transient
    private String state;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setGatewayNum(String gatewayNum)
    {
        this.gatewayNum = gatewayNum;
    }

    public String getGatewayNum()
    {
        return gatewayNum;
    }
    public void setPlantArea(String plantArea)
    {
        this.plantArea = plantArea;
    }

    public String getPlantArea()
    {
        return plantArea;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setHbtTime(Date hbtTime)
    {
        this.hbtTime = hbtTime;
    }

    public Date getHbtTime()
    {
        return hbtTime;
    }

    public String getState() {
        return state;
    }


    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("gatewayNum", getGatewayNum())
            .append("plantArea", getPlantArea())
            .append("address", getAddress())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("hbtTime", getHbtTime())
            .toString();
    }
}
