package com.dingzhuo.energy.project.basicSetup.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * category对象 sys_standard_category
 * 
 * @author ruoyi
 * @date 2020-02-12
 */
public class SysStandardCategory extends BaseEntity
{
    private static final long serialVersionUID = 1L;
    private String tableName;
    private String tableRow;
    private String colName;
    private String tableColumn;
    private String name;
    private String item;
    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableRow() {
        return tableRow;
    }

    public void setTableRow(String tableRow) {
        this.tableRow = tableRow;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(String tableColumn) {
        this.tableColumn = tableColumn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    /* *//** $column.columnComment *//*
    private String id;

    *//** 标准参数类别名称 *//*
    @Excel(name = "标准参数类别名称")
    private String categoryName;

    *//** 标准参数类别编码 *//*
    @Excel(name = "标准参数类别编码")
    private String categoryCode;

    *//** 数据展示类型，1-实时数据，2-阶段数据 *//*
    @Excel(name = "数据展示类型，1-实时数据，2-阶段数据")
    private String dataType;

    *//** 是否显示报警 *//*
    @Excel(name = "是否显示报警")
    private String showAlarm;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setCategoryName(String categoryName) 
    {
        this.categoryName = categoryName;
    }

    public String getCategoryName() 
    {
        return categoryName;
    }
    public void setCategoryCode(String categoryCode) 
    {
        this.categoryCode = categoryCode;
    }

    public String getCategoryCode() 
    {
        return categoryCode;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("categoryName", getCategoryName())
            .append("categoryCode", getCategoryCode())
            .append("dataType", getDataType())
            .append("showAlarm", getShowAlarm())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }*/
}
