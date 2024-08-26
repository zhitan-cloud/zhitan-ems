package com.dingzhuo.energy.data.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

/**
 * 报警限值类型维护对象 limit_type
 *
 * @author zw
 * @date 2020-03-07
 */
@ApiModel(value = "报警限值")
public class LimitType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    @ApiModelProperty(value = "主键id")
    private String id;

    /** $column.columnComment */
    @Excel(name = "限值类型名称")
    @ApiModelProperty(value = "限值类型名称")
    private String limitName;

    /** $column.columnComment */
    @Excel(name = "限值类型编码")
    @ApiModelProperty(value = "限值类型编码")
    private String limitCode;

    /** $column.columnComment */
    @Excel(name = "色号")
    @ApiModelProperty(value = "色号")
    private String colorNumber;

    /** $column.columnComment */
    @Excel(name = "比较运算符")
    @ApiModelProperty(value = "比较运算符")
    private String comparatorOperator;

    /** $column.columnComment */
    @Excel(name = "警戒类型")
    @ApiModelProperty(value = "警戒类型")
    private String alarmType;

    public String getComparatorOperator() {
        return comparatorOperator;
    }

    public void setComparatorOperator(String comparatorOperator) {
        this.comparatorOperator = comparatorOperator;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setLimitName(String limitName)
    {
        this.limitName = limitName;
    }

    public String getLimitName()
    {
        return limitName;
    }
    public void setLimitCode(String limitCode)
    {
        this.limitCode = limitCode;
    }

    public String getLimitCode()
    {
        return limitCode;
    }
    public void setColorNumber(String colorNumber)
    {
        this.colorNumber = colorNumber;
    }

    public String getColorNumber()
    {
        return colorNumber;
    }

    public void setAlarmType(String alarmType)
    {
        this.alarmType = alarmType;
    }

    public String getAlarmType()
    {
        return alarmType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("limitName", getLimitName())
            .append("limitCode", getLimitCode())
            .append("colorNumber", getColorNumber())
            .append("alarmType", getAlarmType())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .toString();
    }
}
