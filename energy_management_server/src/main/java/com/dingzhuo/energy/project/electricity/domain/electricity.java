package com.dingzhuo.energy.project.electricity.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import com.dingzhuo.energy.project.energyStatistics.domain.EnergyConsumption;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * electricityPrice对象 electricity_price
 *
 * @author sys
 * @date 2020-02-18
 */
public class electricity implements Serializable {
    private static final long serialVersionUID = -2777479013884125925L;
    private String lable;
    private String prop;
    private String maxValue;
    private String minValue;
    private String dataTime;
    private  String value;
    private  String price;
    private String valueProp;
    private String priceProp;
    private String name;
    private List<electricity> childNodes =new ArrayList<>();

    public List<electricity> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<electricity> childNodes) {
        this.childNodes = childNodes;
    }

    public void addChildNode(electricity childNode){
        this.childNodes.add(childNode);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getValueProp() {
        return valueProp;
    }

    public void setValueProp(String valueProp) {
        this.valueProp = valueProp;
    }

    public String getPriceProp() {
        return priceProp;
    }

    public void setPriceProp(String priceProp) {
        this.priceProp = priceProp;
    }
}