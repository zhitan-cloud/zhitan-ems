package com.zhitan.peakvalley.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 尖峰平谷电价时间段对象 electricity_price_date
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
@TableName("electricity_price_date")
public class ElectricityPriceDate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date beginDate;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endDate;

    private String remark;


//    /** 是否阶梯价格0否1是 */
//    @Excel(name = "是否阶梯价格0否1是")
//    private String type;
//
//    /** 策略编码 */
//    @Excel(name = "策略编码")
//    private String tacticsNumber;
//
//    /** 策略名称 */
//    @Excel(name = "策略名称")
//    private String tacticsName;
//
//    /** 能源品种 */
//    @Excel(name = "能源品种")
//    private Integer energyType;

//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public void setTacticsNumber(String tacticsNumber) {
//        this.tacticsNumber = tacticsNumber;
//    }
//
//    public void setTacticsName(String tacticsName) {
//        this.tacticsName = tacticsName;
//    }
//
//    public void setEnergyType(Integer energyType) {
//        this.energyType = energyType;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public String getTacticsNumber() {
//        return tacticsNumber;
//    }
//
//    public String getTacticsName() {
//        return tacticsName;
//    }
//
//    public Integer getEnergyType() {
//        return energyType;
//    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return id;
    }
    public void setBeginDate(Date beginDate)
    {
        this.beginDate = beginDate;
    }

    public Date getBeginDate()
    {
        return beginDate;
    }
    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("beginDate", getBeginDate())
            .append("endDate", getEndDate())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
        .toString();
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
