package com.dingzhuo.energy.data.model.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.xmlbeans.impl.common.ValidatorListener;

/**
 * 计算函数对象 calc_function
 *
 * @author fanxinfu
 * @date 2020-03-10
 */
@ApiModel(value = "计算函数")
public class CalcFunction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @ApiModelProperty(value = "主键")
    private String id;

    /** 函数名 */
    @Excel(name = "函数名")
    @ApiModelProperty(value = "函数名")
    private String funcName;

    /** 函数文本 */
    @Excel(name = "函数文本")
    @ApiModelProperty(value = "函数文本")
    private String funcText;

    /** 介绍 */
    @Excel(name = "介绍")
    @ApiModelProperty(value = "介绍")
    private String info;

    public void setInfo(String info)
    {
        this.info = info;
    }

    public String getInfo()
    {
        return info;
    }
    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setFuncName(String funcName)
    {
        this.funcName = funcName;
    }

    public String getFuncName()
    {
        return funcName;
    }
    public void setFuncText(String funcText)
    {
        this.funcText = funcText;
    }

    public String getFuncText()
    {
        return funcText;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("info", getInfo())
            .append("id", getId())
            .append("funcName", getFuncName())
            .append("funcText", getFuncText())
            .toString();
    }
}
