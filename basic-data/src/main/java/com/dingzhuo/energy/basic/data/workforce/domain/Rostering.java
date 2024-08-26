package com.dingzhuo.energy.basic.data.workforce.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 排班表查询对象 rostering
 * 
 * @author liuli
 * @date 2020-05-13
 */
public class Rostering extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private String id;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 轮值方案id */
    @Excel(name = "轮值方案id")
    private String schemeId;
    /** 轮值方案name */
    @Excel(name = "轮值方案name")
    private String schemeName;

    /** 班次id */
    @Excel(name = "班次id")
    private String shiftId;

    /** 班次 */
    @Excel(name = "班次")
    private String shiftName;

    /** 值次id */
    @Excel(name = "值次id")
    private String dutyId;

    /** 值次 */
    @Excel(name = "值次")
    private String dutyName;

    /** 描述 */
    @Excel(name = "描述")
    private String description;

    /** 组织结构Id */
    @Excel(name = "组织结构Id")
    private Long departMemberId;

    /** 组织结构Name */
    @Excel(name = "组织结构Name")
    private String deptName;

    /** 开始时间 */
    @Excel(name = "开始时间",dateFormat = "yyyy-MM-dd")
    private Date beginTimes;

    /** 结束时间 */
    @Excel(name = "结束时间",dateFormat = "yyyy-MM-dd")
    private Date endTimes;

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

    public void setSchemeId(String schemeId) 
    {
        this.schemeId = schemeId;
    }
    public String getSchemeId()
    {
        return schemeId;
    }

    public void setShiftId(String shiftId) 
    {
        this.shiftId = shiftId;
    }
    public String getShiftId() 
    {
        return shiftId;
    }

    public void setShiftName(String shiftName) 
    {
        this.shiftName = shiftName;
    }
    public String getShiftName() 
    {
        return shiftName;
    }

    public void setDutyId(String dutyId) 
    {
        this.dutyId = dutyId;
    }
    public String getDutyId() 
    {
        return dutyId;
    }

    public void setDutyName(String dutyName) 
    {
        this.dutyName = dutyName;
    }
    public String getDutyName() 
    {
        return dutyName;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }
    public String getDescription() 
    {
        return description;
    }

    public void setDepartMemberId(Long departMemberId)
    {
        this.departMemberId = departMemberId;
    }
    public Long getDepartMemberId() {
        return departMemberId;
    }

    public Date getBeginTimes() { return beginTimes; }
    public void setBeginTimes(Date beginTimes)
    {
        this.beginTimes = beginTimes;
    }

    public Date getEndTimes()
    {
        return endTimes;
    }
    public void setEndTimes(Date endTimes)
    {
        this.endTimes = endTimes;
    }

    public void setSchemeName(String schemeName) { this.schemeName = schemeName; }
    public String getSchemeName() { return schemeName; }

    public void setDeptName(String deptName) { this.deptName = deptName; }
    public String getDeptName() { return deptName; }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("schemeId", getSchemeId())
            .append("shiftId", getShiftId())
            .append("shiftName", getShiftName())
            .append("dutyId", getDutyId())
            .append("dutyName", getDutyName())
            .append("description", getDescription())
            .append("departMemberId",getDepartMemberId())
            .append("beginTimes",getBeginTimes())
            .append("endTimes",getEndTimes())
            .append("schemeName",getSchemeName())
            .append("deptName",getDeptName())
            .toString();
    }
}
