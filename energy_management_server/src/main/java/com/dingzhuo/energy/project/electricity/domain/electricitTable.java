package com.dingzhuo.energy.project.electricity.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class electricitTable implements Serializable {
  private static final long serialVersionUID = -2777479013884125925L;
  private List<electricity> tablehead =new ArrayList<>();
  private List<Map> tabledataMap = new ArrayList<Map>();
  public List<Map> getTabledataMap() {
    return tabledataMap;
  }
  public void setTabledataMap(List<Map> tabledataMap) {
    this.tabledataMap = tabledataMap;
  }
  public List<electricity> getTablehead() {
    return tablehead;
  }
  public void setTablehead(List<electricity> tablehead) {
    this.tablehead = tablehead;
  }

}