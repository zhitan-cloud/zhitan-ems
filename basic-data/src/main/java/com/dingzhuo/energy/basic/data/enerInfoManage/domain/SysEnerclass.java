package com.dingzhuo.energy.basic.data.enerInfoManage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 能源品种设置对象 sys_enerclass
 * 
 * @author ruoyi
 * @date 2020-02-10
 */
public class SysEnerclass extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 能源类别ID */
    private Integer enerclassid;

    /** 能源类别名称 */
    @Excel(name = "能源类别名称")
    private String enerclassname;

    /** 操作人 */
    @Excel(name = "操作人")
    private String oprMan;

    /** 操作时间 */
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date oprTime;

    /** 更新人 */
    @Excel(name = "更新人")
    private String modMan;

    /** 更新时间 */
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date modTime;

    /** 备注 */
    @Excel(name = "备注")
    private String note;

    public void setEnerclassid(Integer enerclassid) 
    {
        this.enerclassid = enerclassid;
    }

    public Integer getEnerclassid() 
    {
        return enerclassid;
    }
    public void setEnerclassname(String enerclassname) 
    {
        this.enerclassname = enerclassname;
    }

    public String getEnerclassname() 
    {
        return enerclassname;
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
            .append("enerclassid", getEnerclassid())
            .append("enerclassname", getEnerclassname())
            .append("oprMan", getOprMan())
            .append("oprTime", getOprTime())
            .append("modMan", getModMan())
            .append("modTime", getModTime())
            .append("note", getNote())
            .toString();
    }
}
