package com.dingzhuo.energy.basic.data.workforce.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 排班管理对象 rostering_duty
 * 
 * @author liuli
 * @date 2020-05-12
 */
public class RosteringDuty extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 编号 */
    @Excel(name = "编号")
    private String code;

    /** 值次名称 */
    @Excel(name = "值次名称")
    private String name;

    /** 组织结构id */
    @Excel(name = "组织结构id")
    private Long deptId;
    private String deptName;

    /** 顺序号 */
    @Excel(name = "顺序号")
    private String orderNo;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    public void setId(String id) 
    {
        this.id = id;
    }
    public String getId() 
    {
        return id;
    }

    public void setCode(String code) 
    {
        this.code = code;
    }
    public String getCode() 
    {
        return code;
    }

    public void setName(String name) 
    {
        this.name = name;
    }
    public String getName() 
    {
        return name;
    }

    public void setDeptId(Long deptId)
    {
        this.deptId = deptId;
    }
    public Long getDeptId()
    {
        return deptId;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }
    public String getDeptName()
    {
        return deptName;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("orderNo", getOrderNo())
            .append("description", getDescription())
            .toString();
    }
}
