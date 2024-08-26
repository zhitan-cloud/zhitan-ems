package com.dingzhuo.energy.data.model.domain;

import java.util.ArrayList;
import java.util.List;

public class IndexFormula {

  private String id;
  private String formulaText;
  private String indexId;
  private List<IndexFormulaParam> indexFormulaParams = new ArrayList<>();

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFormulaText() {
    return formulaText;
  }

  public void setFormulaText(String formulaText) {
    this.formulaText = formulaText;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public List<IndexFormulaParam> getIndexFormulaParams() {
    return indexFormulaParams;
  }

  public void setIndexFormulaParams(
      List<IndexFormulaParam> indexFormulaParams) {
    this.indexFormulaParams = indexFormulaParams;
  }
}
