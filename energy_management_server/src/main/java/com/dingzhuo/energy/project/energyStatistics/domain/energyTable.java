package com.dingzhuo.energy.project.energyStatistics.domain;

import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.domain.StatisticResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class energyTable implements Serializable {
  private static final long serialVersionUID = -2777479013884125925L;
  private List<EnergyConsumption> tablehead =new ArrayList<>();
  private List<Map> tabledataMap = new ArrayList<Map>();
  private List<dataTimeSVG> tabledata = new ArrayList<dataTimeSVG>();
  private List<DataItem> listXFL=new ArrayList<DataItem>();//能源消费成本 消费量
  private List<DataItem> listZBL=new ArrayList<DataItem>(); //能源消费成本 折标量
  private List<DataItem> listXFCB=new ArrayList<DataItem>(); //能源消费成本 消费成本
  private List<Map> resultListZBL=new ArrayList<>(); //能源消费成本 折标对比
  private List<Map> resultListXFCB=new ArrayList<>(); //能源消费成本 消费成本对比
  private List<Map> resultZBL = new ArrayList<Map>();
  private List<Map> resultXFCB = new ArrayList<Map>();
  public List<Map> getTabledataMap() {
    return tabledataMap;
  }
  public void setTabledataMap(List<Map> tabledataMap) {
    this.tabledataMap = tabledataMap;
  }
  public List<EnergyConsumption> getTablehead() {
    return tablehead;
  }
  public void setTablehead(List<EnergyConsumption> tablehead) {
    this.tablehead = tablehead;
  }

  public List<dataTimeSVG> getTabledata() {
    return tabledata;
  }

  public void setTabledata(List<dataTimeSVG> tabledata) {
    this.tabledata = tabledata;
  }

  public List<DataItem> getListXFL() {
    return listXFL;
  }

  public void setListXFL(List<DataItem> listXFL) {
    this.listXFL = listXFL;
  }

  public List<DataItem> getListZBL() {
    return listZBL;
  }

  public void setListZBL(List<DataItem> listZBL) {
    this.listZBL = listZBL;
  }

  public List<DataItem> getListXFCB() {
    return listXFCB;
  }

  public void setListXFCB(List<DataItem> listXFCB) {
    this.listXFCB = listXFCB;
  }

  public List<Map> getResultListZBL() {
    return resultListZBL;
  }

  public void setResultListZBL(List<Map> resultListZBL) {
    this.resultListZBL = resultListZBL;
  }

  public List<Map> getResultListXFCB() {
    return resultListXFCB;
  }

  public void setResultListXFCB(List<Map> resultListXFCB) {
    this.resultListXFCB = resultListXFCB;
  }

  public List<Map> getResultZBL() {
    return resultZBL;
  }

  public void setResultZBL(List<Map> resultZBL) {
    this.resultZBL = resultZBL;
  }

  public List<Map> getResultXFCB() {
    return resultXFCB;
  }

  public void setResultXFCB(List<Map> resultXFCB) {
    this.resultXFCB = resultXFCB;
  }
}