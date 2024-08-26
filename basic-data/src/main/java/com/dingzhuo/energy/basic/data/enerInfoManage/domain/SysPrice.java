package com.dingzhuo.energy.basic.data.enerInfoManage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 单价设置对象 sys_price
 *
 * @author ruoyi
 * @date 2020-02-15
 */
public class SysPrice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 单价ID */
    private Integer priceid;

    /** ObjectID_对象ID：
　　能源ID、原料ID或产品ID
 */
    @Excel(name = "ObjectID_对象ID： 能源ID、原料ID或产品ID ")
    private Integer objectid;

    /** ObjectType_对象类型
　　1=能源(Energy表)；2=原料(Material表)；3=产品(Product表) */
    private Integer objecttype;

    /** 单价 */
    @Excel(name = "单价")
    private Double price;

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

    //能源名
    private String enername;

    public String getEnername() {
        return enername;
    }

    public void setEnername(String enername) {
        this.enername = enername;
    }

    public void setPriceid(Integer priceid)
    {
        this.priceid = priceid;
    }

    public Integer getPriceid()
    {
        return priceid;
    }
    public void setObjectid(Integer objectid)
    {
        this.objectid = objectid;
    }

    public Integer getObjectid()
    {
        return objectid;
    }
    public void setObjecttype(Integer objecttype)
    {
        this.objecttype = objecttype;
    }

    public Integer getObjecttype()
    {
        return objecttype;
    }
    public void setPrice(Double price)
    {
        this.price = price;
    }

    public Double getPrice()
    {
        return price;
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
            .append("priceid", getPriceid())
            .append("objectid", getObjectid())
            .append("objecttype", getObjecttype())
            .append("price", getPrice())
            .append("execdate", getExecdate())
            .append("oprMan", getOprMan())
            .append("oprTime", getOprTime())
            .append("modMan", getModMan())
            .append("modTime", getModTime())
            .append("note", getNote())
            .toString();
    }
}
