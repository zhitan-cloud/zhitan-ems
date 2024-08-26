package com.dingzhuo.energy.basic.data.policy.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 政策法规维护对象 policy_regulations
 *
 * @author liuli
 * @date 2020-04-23
 */
@ApiModel(value = "政策法规")
public class PolicyRegulations extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 序号 */
    @ApiModelProperty(value = "序号")
    private String id;

    /** 标题 */
    @Excel(name = "标题")
    @ApiModelProperty(value = "标题")
    private String titleName;

    /** 内容 */
    @Excel(name = "内容")
    @ApiModelProperty(value = "内容")
    private String content;

    /** 分类 */
    @Excel(name = "分类")
    @ApiModelProperty(value = "分类")
    private String sort;

    /** 创建时间 */
    @Excel(name = "创建时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date creationTime;

    /** 操作人员 */
    @Excel(name = "操作人员")
    @ApiModelProperty(value = "操作人员")
    private String operator;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setTitleName(String titleName)
    {
        this.titleName = titleName;
    }

    public String getTitleName()
    {
        return titleName;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setSort(String sort)
    {
        this.sort = sort;
    }

    public String getSort()
    {
        return sort;
    }
    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    public Date getCreationTime()
    {
        return creationTime;
    }
    public void setOperator(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("titleName", getTitleName())
            .append("content", getContent())
            .append("sort", getSort())
            .append("creationTime", getCreationTime())
            .append("operator", getOperator())
            .toString();
    }
}
