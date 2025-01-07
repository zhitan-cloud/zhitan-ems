package com.zhitan.basicdata.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 【请填写功能名称】对象 sys_energy
 *
 * @author ZhiTan
 * @date 2024-10-15
 */
@TableName("sys_energy")
public class SysEnergy extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 能源名称 */
    @Excel(name = "能源名称")
    private String enername;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String muid;

    @TableField(exist = false)
    private String muidString;

    /** 能源类别ID */
    @Excel(name = "能源类别ID")
    private Integer enerclassid;

    /** 能源编号 */
    @Excel(name = "能源编号")
    private String enersno;

    /** 是否存储、0为是、1为否 */
    @Excel(name = "是否存储、0为是、1为否")
    private String isstorage;

    /** 能源类型 */
    @Excel(name = "能源类型")
    @TableField(exist = false)
    private String enerclassname;

    /** 是否储存（字符串） */
    @TableField(exist = false)
    private String isstorageString;

    /** 操作人 */
    @Excel(name = "操作人")
    private String oprMan;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date oprTime;

    /** 修改人 */
    @Excel(name = "修改人")
    private String modMan;

    /** 修改时间 */
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modTime;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** $column.columnComment */
    @Excel(name = "${comment}", readConverterExp = "$column.readConverterExp()")
    private Integer enerid;

    /** 等价折标系数 */
    @Excel(name = "等价折标系数")
    private BigDecimal coefficient;

    //折标系数note
    @TableField(exist = false)
    private String coefficientnote;

    /** 折标系数执行日期 */
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    @TableField(exist = false)
    private Date coefficientexecdate;

    /** 执行日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执行日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date execdate;

    /** 单价 */
    @Excel(name = "单价")
    private BigDecimal price;

    /** 排放因子 */
    @Excel(name = "排放因子")
    private BigDecimal emissionFactors;

        
    public void setEnername(String enername)
    {
        this.enername = enername;
    }

    public String getEnername()
    {
        return enername;
    }
        
    public void setMuid(String muid)
    {
        this.muid = muid;
    }

    public String getMuid()
    {
        return muid;
    }
        
    public void setEnerclassid(Integer enerclassid)
    {
        this.enerclassid = enerclassid;
    }

    public Integer getEnerclassid()
    {
        return enerclassid;
    }
        
    public void setEnersno(String enersno)
    {
        this.enersno = enersno;
    }

    public String getEnersno()
    {
        return enersno;
    }
        
    public void setIsstorage(String isstorage)
    {
        this.isstorage = isstorage;
    }

    public String getIsstorage()
    {
        return isstorage;
    }
        
    public void setOprMan(String oprMan)
    {
        this.oprMan = oprMan;
    }

    public String getOprMan()
    {
        return oprMan;
    }
        
    public void setOprTime(Date oprTime)
    {
        this.oprTime = oprTime;
    }

    public Date getOprTime()
    {
        return oprTime;
    }
        
    public void setModMan(String modMan)
    {
        this.modMan = modMan;
    }

    public String getModMan()
    {
        return modMan;
    }
        
    public void setModTime(Date modTime)
    {
        this.modTime = modTime;
    }

    public Date getModTime()
    {
        return modTime;
    }
        
    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }
        
    public void setEnerid(Integer enerid)
    {
        this.enerid = enerid;
    }

    public Integer getEnerid()
    {
        return enerid;
    }
        
    public void setCoefficient(BigDecimal coefficient)
    {
        this.coefficient = coefficient;
    }

    public BigDecimal getCoefficient()
    {
        return coefficient;
    }
        
    public void setExecdate(Date execdate)
    {
        this.execdate = execdate;
    }

    public Date getExecdate()
    {
        return execdate;
    }
        
    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public BigDecimal getPrice()
    {
        return price;
    }
        
    public void setEmissionFactors(BigDecimal emissionFactors)
    {
        this.emissionFactors = emissionFactors;
    }

    public BigDecimal getEmissionFactors()
    {
        return emissionFactors;
    }

    public String getIsstorageString() {
        return isstorageString;
    }

    public void setIsstorageString(String isstorageString) {
        this.isstorageString = isstorageString;
    }

    public String getEnerclassname() {
        return enerclassname;
    }

    public void setEnerclassname(String enerclassname) {
        this.enerclassname = enerclassname;
    }

    public String getCoefficientnote() {
        return coefficientnote;
    }

    public void setCoefficientnote(String coefficientnote) {
        this.coefficientnote = coefficientnote;
    }

    public Date getCoefficientexecdate() {
        return coefficientexecdate;
    }

    public void setCoefficientexecdate(Date coefficientexecdate) {
        this.coefficientexecdate = coefficientexecdate;
    }

    public String getMuidString() {
        return muidString;
    }

    public void setMuidString(String muidString) {
        this.muidString = muidString;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("enername", getEnername())
            .append("muid", getMuid())
            .append("enerclassid", getEnerclassid())
            .append("enersno", getEnersno())
            .append("isstorage", getIsstorage())
            .append("oprMan", getOprMan())
            .append("oprTime", getOprTime())
            .append("modMan", getModMan())
            .append("modTime", getModTime())
            .append("note", getNote())
            .append("enerid", getEnerid())
            .append("coefficient", getCoefficient())
            .append("execdate", getExecdate())
            .append("price", getPrice())
            .append("emissionFactors", getEmissionFactors())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
        .toString();
    }
}
