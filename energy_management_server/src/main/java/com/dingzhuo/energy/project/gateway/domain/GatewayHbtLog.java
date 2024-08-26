package com.dingzhuo.energy.project.gateway.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 网关心跳日志对象 gateway_hbt_log
 * 
 * @author zhw
 * @date 2022-04-15
 */
public class GatewayHbtLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 网关编号 */
    @Excel(name = "网关编号")
    private String gatewayNo;

    /** 心跳时间 */
    @Excel(name = "心跳时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hbtTime;

    /** 心跳包 */
    @Excel(name = "心跳包")
    private String content;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setGatewayNo(String gatewayNo) 
    {
        this.gatewayNo = gatewayNo;
    }

    public String getGatewayNo() 
    {
        return gatewayNo;
    }
    public void setHbtTime(Date hbtTime) 
    {
        this.hbtTime = hbtTime;
    }

    public Date getHbtTime() 
    {
        return hbtTime;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("gatewayNo", getGatewayNo())
            .append("hbtTime", getHbtTime())
            .append("content", getContent())
            .toString();
    }
}
