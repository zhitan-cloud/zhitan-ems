package com.dingzhuo.energy.basic.data.workforce.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 轮值方案对象 rostering_scheme
 * 
 * @author sys
 * @date 2020-05-12
 */
public class RosteringScheme extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 编码 */
    @Excel(name = "编码")
    private String code;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 组织结构Id */
    @Excel(name = "组织结构Id")
    private Long departMemberId;
    private String departMember;
    /** 轮值方案类型 */
    @Excel(name = "轮值方案类型")
    private String rosteringschemekind;

    /** 参照日期 */
    @Excel(name = "参照日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date referencedate;

    /** 是否自动排班 */
    @Excel(name = "是否自动排班")
    private String isautorostering;

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
    public void setRosteringschemekind(String rosteringschemekind) 
    {
        this.rosteringschemekind = rosteringschemekind;
    }

    public String getRosteringschemekind() 
    {
        return rosteringschemekind;
    }
    public void setReferencedate(Date referencedate) 
    {
        this.referencedate = referencedate;
    }

    public Date getReferencedate() 
    {
        return referencedate;
    }
    public void setIsautorostering(String isautorostering)
    {
        this.isautorostering = isautorostering;
    }

    public String getIsautorostering()
    {
        return isautorostering;
    }
    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public Long getDepartMemberId() {
        return departMemberId;
    }

    public void setDepartMemberId(Long departMemberId) {
        this.departMemberId = departMemberId;
    }

    public String getDepartMember() {
        return departMember;
    }

    public void setDepartMember(String departMember) {
        this.departMember = departMember;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("departMemberId", getDepartMemberId())
            .append("rosteringschemekind", getRosteringschemekind())
            .append("referencedate", getReferencedate())
            .append("isautorostering", getIsautorostering())
            .append("description", getDescription())
            .toString();
    }
}
