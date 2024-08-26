package com.dingzhuo.energy.project.basicSetup.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
/**
 * parameters对象 sys_standard_parameters
 *
 * @author ruoyi
 * @date 2020-02-12
 */
public class SysStandardParameters extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** 标准参数类别名称 */
    @Excel(name = "标准参数类别名称")
    private String name;

    /** 设计内容 */
    @Excel(name = "设计内容")
    private String configInfo;

    /** 标准参数类别编码 */
    @Excel(name = "标准参数类别编码")
    private String code;

    /** 数据展示类型，1-实时数据，2-阶段数据 */
    @Excel(name = "数据展示类型，1-实时数据，2-阶段数据")
    private String dataType;

    /** 是否显示报警 */
    @Excel(name = "是否显示报警")
    private String showAlarm;

    @Excel(name = "标准参数类别Id")
    private String categoryId;

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
    public void setConfigInfo(String configInfo)
    {
        this.configInfo = configInfo;
    }

    public String getConfigInfo()
    {
        return configInfo;
    }
    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }

    public String getDataType()
    {
        return dataType;
    }
    public void setShowAlarm(String showAlarm)
    {
        this.showAlarm = showAlarm;
    }

    public String getShowAlarm()
    {
        return showAlarm;
    }

    public String getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(String categoryId)
    {
        this.categoryId = categoryId;
    }
    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("configInfo", getConfigInfo())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .append("code", getCode())
                .append("dataType", getDataType())
                .append("showAlarm", getShowAlarm())
                .append("categoryId",  getCategoryId())
                .toString();
    }
}
