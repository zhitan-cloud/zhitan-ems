package com.dingzhuo.energy.basic.data.enerInfoManage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * energy对象 sys_energy
 *
 * @author ruoyi
 * @date 2020-02-12
 */
public class SysEnergy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

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

    /** 能源ID */
    private Integer enerid;

    /** 能源名称 */
    @Excel(name = "能源名称")
    private String enername;

    /** 计量单位ID */
    private Integer muid;

    /** 能源类别ID */
    private Integer enerclassid;

    /** 能源编号 */
    @Excel(name = "能源编号")
    private String enersno;

    /** 是否存储 */
    @Excel(name = "是否存储")
    private Integer isstorage;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    /** 能源类型 */
    @Excel(name = "能源类型")
    private String enerclassname;

    /** 是否储存（字符串） */
    private String isstorageString;

    /** 单价*/
    private double price;

    /** 执行日期 */
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date execdate;

    /** 价格备注 */
    private String pricenote;

    private Integer muidString;

    public String getMuidName() {
        return muidName;
    }

    public void setMuidName(String muidName) {
        this.muidName = muidName;
    }

    private String muidName;

    //等价折标系数
    private double coefficient;

    //当量折标系数
    private double coefficient2;

    //折标系数note
    private String coefficientnote;

    /** 折标系数执行日期 */
    @Excel(name = "修改时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date coefficientexecdate;

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient2() {
        return coefficient2;
    }

    public void setCoefficient2(double coefficient2) {
        this.coefficient2 = coefficient2;
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

    public Integer getMuidString() {
        return muidString;
    }

    public void setMuidString(Integer muidString) {
        this.muidString = muidString;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getExecdate() {
        return execdate;
    }

    public void setExecdate(Date execdate) {
        this.execdate = execdate;
    }

    public String getPricenote() {
        return pricenote;
    }

    public void setPricenote(String pricenote) {
        this.pricenote = pricenote;
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
    public void setEnerid(Integer enerid)
    {
        this.enerid = enerid;
    }

    public Integer getEnerid()
    {
        return enerid;
    }
    public void setEnername(String enername)
    {
        this.enername = enername;
    }

    public String getEnername()
    {
        return enername;
    }
    public void setMuid(Integer muid)
    {
        this.muid = muid;
    }

    public Integer getMuid()
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
    public void setIsstorage(Integer isstorage)
    {
        this.isstorage = isstorage;
    }

    public Integer getIsstorage()
    {
        return isstorage;
    }
    public void setNote(String note)
    {
        this.note = note;
    }

    public String getNote()
    {
        return note;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("oprMan", getOprMan())
            .append("oprTime", getOprTime())
            .append("modMan", getModMan())
            .append("modTime", getModTime())
            .append("enerid", getEnerid())
            .append("enername", getEnername())
            .append("muid", getMuid())
            .append("enerclassid", getEnerclassid())
            .append("enersno", getEnersno())
            .append("isstorage", getIsstorage())
            .append("note", getNote())
            .toString();
    }
}
