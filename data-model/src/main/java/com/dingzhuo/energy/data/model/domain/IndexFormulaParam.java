package com.dingzhuo.energy.data.model.domain;

public class IndexFormulaParam {

  private String id;
  private String indexId;
  private String formulaId;
  private String paramName;
  private String paramValue;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public String getFormulaId() {
    return formulaId;
  }

  public void setFormulaId(String formulaId) {
    this.formulaId = formulaId;
  }

  public String getParamName() {
    return paramName;
  }

  public void setParamName(String paramName) {
    this.paramName = paramName;
  }

  public String getParamValue() {
    return paramValue;
  }

  public void setParamValue(String paramValue) {
    this.paramValue = paramValue;
  }
}
