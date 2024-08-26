package com.dingzhuo.energy.basic.data.enerInfoManage.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 产品对象 sys_product
 *
 * @author sys
 * @date 2020-02-19
 */
public class SysProduct extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 产品ID */
    private Integer productid;

    /** 产品名称 */
    @Excel(name = "产品名称")
    private String productname;

    /** 产品编号 */
    @Excel(name = "产品编号")
    private String productsno;

    /** 计量单位 */
    @Excel(name = "计量单位")
    private String muid;

    /** 父级产品ID */
    @Excel(name = "父级产品ID")
    private Integer superid;

    /** 是否为子级 */
    @Excel(name = "是否为子级")
    private String issub;

    /** 折算系数 */
    @Excel(name = "折算系数")
    private Double procofficient;

    /** 是否显示 */
    @Excel(name = "是否显示")
    private String isshow;

    /** 是否主要产品 */
    @Excel(name = "是否主要产品")
    private String iscpnyproduct;

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
    //价格
    private double price;

    private String superidname;

    public String getSuperidname() {
        return superidname;
    }

    public void setSuperidname(String superidname) {
        this.superidname = superidname;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public void setProductid(Integer productid)
    {
        this.productid = productid;
    }

    public Integer getProductid()
    {
        return productid;
    }
    public void setProductname(String productname)
    {
        this.productname = productname;
    }

    public String getProductname()
    {
        return productname;
    }
    public void setProductsno(String productsno)
    {
        this.productsno = productsno;
    }

    public String getProductsno()
    {
        return productsno;
    }
    public void setMuid(String muid)
    {
        this.muid = muid;
    }

    public String getMuid()
    {
        return muid;
    }
    public void setSuperid(Integer superid)
    {
        this.superid = superid;
    }

    public Integer getSuperid()
    {
        return superid;
    }
    public void setIssub(String issub)
    {
        this.issub = issub;
    }

    public String getIssub()
    {
        return issub;
    }
    public void setProcofficient(Double procofficient)
    {
        this.procofficient = procofficient;
    }

    public Double getProcofficient()
    {
        return procofficient;
    }
    public void setIsshow(String isshow)
    {
        this.isshow = isshow;
    }

    public String getIsshow()
    {
        return isshow;
    }
    public void setIscpnyproduct(String iscpnyproduct)
    {
        this.iscpnyproduct = iscpnyproduct;
    }

    public String getIscpnyproduct()
    {
        return iscpnyproduct;
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
            .append("productid", getProductid())
            .append("productname", getProductname())
            .append("productsno", getProductsno())
            .append("muid", getMuid())
            .append("superid", getSuperid())
            .append("issub", getIssub())
            .append("procofficient", getProcofficient())
            .append("isshow", getIsshow())
            .append("iscpnyproduct", getIscpnyproduct())
            .append("oprMan", getOprMan())
            .append("oprTime", getOprTime())
            .append("modMan", getModMan())
            .append("modTime", getModTime())
            .append("note", getNote())
            .toString();
    }
}
