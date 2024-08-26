package com.dingzhuo.energy.basic.data.energy.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 节能项目管理对象 energy_project
 *
 * @author sys
 * @date 2020-12-07
 */
@ApiModel(value = "节能项目")
public class EnergyProject extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @ApiModelProperty(value = "id")
    private String id;

    /** 项目名称 */
    @ApiModelProperty(value = "项目名称")
    @Excel(name = "项目名称")
    private String name;

    /** 节能计划 */
    @ApiModelProperty(value = "节能计划")
    @Excel(name = "节能计划")
    private String plan;

    /** 节能目标 */
    @ApiModelProperty(value = "节能目标")
    @Excel(name = "节能目标")
    private String target;

    /** 开始时间 */
    @ApiModelProperty(value = "开始时间")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @ApiModelProperty(value = "结束时间")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date enddTime;

    /** 负责人 */
    @ApiModelProperty(value = "负责人")
    @Excel(name = "负责人")
    private String people;

    /** 制定时检 */
    @ApiModelProperty(value = "制定时检")
    @Excel(name = "制定时检", width = 30, dateFormat = "yyyy-MM-dd")
    private Date setTime;

    /** 创建人 */
    @ApiModelProperty(value = "创建人")
    @Excel(name = "创建人")
    private String createOperator;

    /** 修改人 */
    @ApiModelProperty(value = "修改人")
    @Excel(name = "修改人")
    private String updateOperator;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setPlan(String plan)
    {
        this.plan = plan;
    }

    public String getPlan()
    {
        return plan;
    }
    public void setTarget(String target)
    {
        this.target = target;
    }

    public String getTarget()
    {
        return target;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEnddTime(Date enddTime)
    {
        this.enddTime = enddTime;
    }

    public Date getEnddTime()
    {
        return enddTime;
    }
    public void setPeople(String people)
    {
        this.people = people;
    }

    public String getPeople()
    {
        return people;
    }
    public void setSetTime(Date setTime)
    {
        this.setTime = setTime;
    }

    public Date getSetTime()
    {
        return setTime;
    }
    public void setCreateOperator(String createOperator)
    {
        this.createOperator = createOperator;
    }

    public String getCreateOperator()
    {
        return createOperator;
    }
    public void setUpdateOperator(String updateOperator)
    {
        this.updateOperator = updateOperator;
    }

    public String getUpdateOperator()
    {
        return updateOperator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("plan", getPlan())
            .append("target", getTarget())
            .append("startTime", getStartTime())
            .append("enddTime", getEnddTime())
            .append("people", getPeople())
            .append("setTime", getSetTime())
            .append("createOperator", getCreateOperator())
            .append("createTime", getCreateTime())
            .append("updateOperator", getUpdateOperator())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
