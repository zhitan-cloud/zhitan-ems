package com.zhitan.productoutput.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zhitan.common.annotation.Excel;
import com.zhitan.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 产品产量对象 product_output
 *
 * @author ZhiTan
 * @date 2024-10-08
 */
@TableName("product_output")
public class ProductOutput extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** productOutputId */
    @Excel(name = "productOutputId")
    private String productOutputId;

    /** 用能单元id（model_node） */
    @Excel(name = "用能单元id")
    private String nodeId;

    /** 用能单元id（model_node） */
    @Excel(name = "用能单元名称")
    private String nodeName;

    /** 时间类型（年、月、日） */
    @Excel(name = "时间类型")
    private String timeType;

    /** 时间 */
    @Excel(name = "时间")
    private String dataTime;

    /** 名称 */
    @Excel(name = "名称")
    private String name;

    /** 产量 */
    @Excel(name = "产量")
    private BigDecimal number;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 1产量2仪表3指标 */
    @Excel(name = "1产量2仪表3指标")
    private String dataType;

    /** productOutputId */
    @Excel(name = "productType")
    private String productType;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductOutputId() {
        return productOutputId;
    }

    public void setProductOutputId(String productOutputId) {
        this.productOutputId = productOutputId;
    }

    public void setNodeId(String nodeId){
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }
    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public String getTimeType() {
        return timeType;
    }
    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getDataTime() {
        return dataTime;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getNumber() {
        return number;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("productOutputId", getProductOutputId())
            .append("nodeId", getNodeId())
            .append("nodeName", getNodeName())
            .append("timeType", getTimeType())
            .append("dataTime", getDataTime())
            .append("name", getName())
            .append("number", getNumber())
            .append("unit", getUnit())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("dataType", getDataType())
        .toString();
    }
}
