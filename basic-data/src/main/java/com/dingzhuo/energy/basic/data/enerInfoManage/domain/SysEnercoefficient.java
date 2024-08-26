package com.dingzhuo.energy.basic.data.enerInfoManage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 能源折标系数对象 sys_enercoefficient
 *
 * @author sys
 * @date 2020-02-18
 */
public class SysEnercoefficient extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 折标系数ID */
    private Integer ecid;

    /** 能源ID */
    @Excel(name = "能源ID")
    private Integer enerid;

    /** 等价折标系数 */
    @Excel(name = "等价折标系数")
    private double coefficient;

    /** 当量折标系数 */
    @Excel(name = "当量折标系数")
    private double coefficient2;

    /** 执行日期 */
    @Excel(name = "执行日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date execdate;

    /** 操作人 */
    private String oprMan;

    /** 操作时间 */
    private Date oprTime;

    /** 更新人 */
    private String modMan;

    /** 更新时间 */
    private Date modTime;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    //  能源名称
    private String enername;

    public String getEnername() {
        return enername;
    }

    public void setEnername(String enername) {
        this.enername = enername;
    }

    public void setEcid(Integer ecid)
    {
        this.ecid = ecid;
    }

    public Integer getEcid()
    {
        return ecid;
    }
    public void setEnerid(Integer enerid)
    {
        this.enerid = enerid;
    }

    public Integer getEnerid()
    {
        return enerid;
    }
    public void setCoefficient(double coefficient)
    {
        this.coefficient = coefficient;
    }

    public double getCoefficient()
    {
        return coefficient;
    }
    public void setCoefficient2(double coefficient2)
    {
        this.coefficient2 = coefficient2;
    }

    public double getCoefficient2()
    {
        return coefficient2;
    }
    public void setExecdate(Date execdate)
    {
        this.execdate = execdate;
    }

    public Date getExecdate()
    {
        return execdate;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("ecid", getEcid())
            .append("enerid", getEnerid())
            .append("coefficient", getCoefficient())
            .append("coefficient2", getCoefficient2())
            .append("execdate", getExecdate())
            .append("oprMan", getOprMan())
            .append("oprTime", getOprTime())
            .append("modMan", getModMan())
            .append("modTime", getModTime())
            .append("note", getNote())
            .toString();
    }
}
