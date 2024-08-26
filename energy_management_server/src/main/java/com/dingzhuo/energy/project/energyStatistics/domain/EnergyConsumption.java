package com.dingzhuo.energy.project.energyStatistics.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EnergyConsumption implements Serializable {
  private static final long serialVersionUID = -2777479013884125925L;
  private String lable;
  private String prop;
  private String maxValue;
  private String minValue;
  private String dataTime;
  private List<EnergyConsumption> childNodes =new ArrayList<>();


  public List<EnergyConsumption> getChildNodes() {
    return childNodes;
  }

  public void setChildNodes(List<EnergyConsumption> childNodes) {
    this.childNodes = childNodes;
  }

  public void addChildNode(EnergyConsumption childNode){
    this.childNodes.add(childNode);
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

}