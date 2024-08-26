package com.dingzhuo.energy.project.reportForm.domain;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * 焦化工序综合报表对象 consolidated_statements
 *
 * @author sys
 * @date 2021-01-15
 */
public class consolidatedStatements extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private String id;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trRljmZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trRljmSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trRljmZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trDianZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trDianSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trDianZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trShuiZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trShuiSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trShuiZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trZqZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trZqSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trZqZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccJtZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccJtSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccJtZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccJyZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccJySwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccJyZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccCbZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccCbSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccCbZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccMqZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccMqSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccMqZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccDianZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccDianSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccDianZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccZqZb;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccZqSwl;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccZqZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}")
    private TimeType timeType;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String timeCode;
    @Excel(name = "${comment}")
    private String dataTime;
    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String trZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String ccZbzm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String jhbm;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private String zhnh;
    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setTrRljmZb(String trRljmZb)
    {
        this.trRljmZb = trRljmZb;
    }

    public String getTrRljmZb()
    {
        return trRljmZb;
    }
    public void setTrRljmSwl(String trRljmSwl)
    {
        this.trRljmSwl = trRljmSwl;
    }

    public String getTrRljmSwl()
    {
        return trRljmSwl;
    }
    public void setTrRljmZbzm(String trRljmZbzm)
    {
        this.trRljmZbzm = trRljmZbzm;
    }

    public String getTrRljmZbzm()
    {
        return trRljmZbzm;
    }
    public void setTrDianZb(String trDianZb)
    {
        this.trDianZb = trDianZb;
    }

    public String getTrDianZb()
    {
        return trDianZb;
    }
    public void setTrDianSwl(String trDianSwl)
    {
        this.trDianSwl = trDianSwl;
    }

    public String getTrDianSwl()
    {
        return trDianSwl;
    }
    public void setTrDianZbzm(String trDianZbzm)
    {
        this.trDianZbzm = trDianZbzm;
    }

    public String getTrDianZbzm()
    {
        return trDianZbzm;
    }
    public void setTrShuiZb(String trShuiZb)
    {
        this.trShuiZb = trShuiZb;
    }

    public String getTrShuiZb()
    {
        return trShuiZb;
    }
    public void setTrShuiSwl(String trShuiSwl)
    {
        this.trShuiSwl = trShuiSwl;
    }

    public String getTrShuiSwl()
    {
        return trShuiSwl;
    }
    public void setTrShuiZbzm(String trShuiZbzm)
    {
        this.trShuiZbzm = trShuiZbzm;
    }

    public String getTrShuiZbzm()
    {
        return trShuiZbzm;
    }
    public void setTrZqZb(String trZqZb)
    {
        this.trZqZb = trZqZb;
    }

    public String getTrZqZb()
    {
        return trZqZb;
    }
    public void setTrZqSwl(String trZqSwl)
    {
        this.trZqSwl = trZqSwl;
    }

    public String getTrZqSwl()
    {
        return trZqSwl;
    }
    public void setTrZqZbzm(String trZqZbzm)
    {
        this.trZqZbzm = trZqZbzm;
    }

    public String getTrZqZbzm()
    {
        return trZqZbzm;
    }
    public void setCcJtZb(String ccJtZb)
    {
        this.ccJtZb = ccJtZb;
    }

    public String getCcJtZb()
    {
        return ccJtZb;
    }
    public void setCcJtSwl(String ccJtSwl)
    {
        this.ccJtSwl = ccJtSwl;
    }

    public String getCcJtSwl()
    {
        return ccJtSwl;
    }
    public void setCcJtZbzm(String ccJtZbzm)
    {
        this.ccJtZbzm = ccJtZbzm;
    }

    public String getCcJtZbzm()
    {
        return ccJtZbzm;
    }
    public void setCcJyZb(String ccJyZb)
    {
        this.ccJyZb = ccJyZb;
    }

    public String getCcJyZb()
    {
        return ccJyZb;
    }
    public void setCcJySwl(String ccJySwl)
    {
        this.ccJySwl = ccJySwl;
    }

    public String getCcJySwl()
    {
        return ccJySwl;
    }
    public void setCcJyZbzm(String ccJyZbzm)
    {
        this.ccJyZbzm = ccJyZbzm;
    }

    public String getCcJyZbzm()
    {
        return ccJyZbzm;
    }
    public void setCcCbZb(String ccCbZb)
    {
        this.ccCbZb = ccCbZb;
    }

    public String getCcCbZb()
    {
        return ccCbZb;
    }
    public void setCcCbSwl(String ccCbSwl)
    {
        this.ccCbSwl = ccCbSwl;
    }

    public String getCcCbSwl()
    {
        return ccCbSwl;
    }
    public void setCcCbZbzm(String ccCbZbzm)
    {
        this.ccCbZbzm = ccCbZbzm;
    }

    public String getCcCbZbzm()
    {
        return ccCbZbzm;
    }
    public void setCcMqZb(String ccMqZb)
    {
        this.ccMqZb = ccMqZb;
    }

    public String getCcMqZb()
    {
        return ccMqZb;
    }
    public void setCcMqSwl(String ccMqSwl)
    {
        this.ccMqSwl = ccMqSwl;
    }

    public String getCcMqSwl()
    {
        return ccMqSwl;
    }
    public void setCcMqZbzm(String ccMqZbzm)
    {
        this.ccMqZbzm = ccMqZbzm;
    }

    public String getCcMqZbzm()
    {
        return ccMqZbzm;
    }
    public void setCcDianZb(String ccDianZb)
    {
        this.ccDianZb = ccDianZb;
    }

    public String getCcDianZb()
    {
        return ccDianZb;
    }
    public void setCcDianSwl(String ccDianSwl)
    {
        this.ccDianSwl = ccDianSwl;
    }

    public String getCcDianSwl()
    {
        return ccDianSwl;
    }
    public void setCcDianZbzm(String ccDianZbzm)
    {
        this.ccDianZbzm = ccDianZbzm;
    }

    public String getCcDianZbzm()
    {
        return ccDianZbzm;
    }
    public void setCcZqZb(String ccZqZb)
    {
        this.ccZqZb = ccZqZb;
    }

    public String getCcZqZb()
    {
        return ccZqZb;
    }
    public void setCcZqSwl(String ccZqSwl)
    {
        this.ccZqSwl = ccZqSwl;
    }

    public String getCcZqSwl()
    {
        return ccZqSwl;
    }
    public void setCcZqZbzm(String ccZqZbzm)
    {
        this.ccZqZbzm = ccZqZbzm;
    }

    public String getCcZqZbzm()
    {
        return ccZqZbzm;
    }
    public void setTimeType(TimeType timeType)
    {
        this.timeType = timeType;
    }

    public TimeType getTimeType()
    {
        return timeType;
    }
    public void setTimeCode(String timeCode)
    {
        this.timeCode = timeCode;
    }

    public String getTimeCode()
    {
        return timeCode;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getTrZbzm() {
        return trZbzm;
    }

    public void setTrZbzm(String trZbzm) {
        this.trZbzm = trZbzm;
    }

    public String getCcZbzm() {
        return ccZbzm;
    }

    public void setCcZbzm(String ccZbzm) {
        this.ccZbzm = ccZbzm;
    }

    public String getJhbm() {
        return jhbm;
    }

    public void setJhbm(String jhbm) {
        this.jhbm = jhbm;
    }

    public String getZhnh() {
        return zhnh;
    }

    public void setZhnh(String zhnh) {
        this.zhnh = zhnh;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("trRljmZb", getTrRljmZb())
                .append("trRljmSwl", getTrRljmSwl())
                .append("trRljmZbzm", getTrRljmZbzm())
                .append("trDianZb", getTrDianZb())
                .append("trDianSwl", getTrDianSwl())
                .append("trDianZbzm", getTrDianZbzm())
                .append("trShuiZb", getTrShuiZb())
                .append("trShuiSwl", getTrShuiSwl())
                .append("trShuiZbzm", getTrShuiZbzm())
                .append("trZqZb", getTrZqZb())
                .append("trZqSwl", getTrZqSwl())
                .append("trZqZbzm", getTrZqZbzm())
                .append("ccJtZb", getCcJtZb())
                .append("ccJtSwl", getCcJtSwl())
                .append("ccJtZbzm", getCcJtZbzm())
                .append("ccJyZb", getCcJyZb())
                .append("ccJySwl", getCcJySwl())
                .append("ccJyZbzm", getCcJyZbzm())
                .append("ccCbZb", getCcCbZb())
                .append("ccCbSwl", getCcCbSwl())
                .append("ccCbZbzm", getCcCbZbzm())
                .append("ccMqZb", getCcMqZb())
                .append("ccMqSwl", getCcMqSwl())
                .append("ccMqZbzm", getCcMqZbzm())
                .append("ccDianZb", getCcDianZb())
                .append("ccDianSwl", getCcDianSwl())
                .append("ccDianZbzm", getCcDianZbzm())
                .append("ccZqZb", getCcZqZb())
                .append("ccZqSwl", getCcZqSwl())
                .append("ccZqZbzm", getCcZqZbzm())
                .append("timeType", getTimeType())
                .append("createTime", getCreateTime())
                .append("createBy", getCreateBy())
                .append("updateTime", getUpdateTime())
                .append("updateBy", getUpdateBy())
                .append("remark", getRemark())
                .append("timeCode", getTimeCode())
                .append("dataTime", getDataTime())
                .append("trZbzm", getTrZbzm())
                .append("ccZbzm", getCcZbzm())
                .append("jhbm", getJhbm())
                .append("zhnh", getZhnh())
                .toString();
    }
}