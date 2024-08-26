package com.dingzhuo.energy.data.model.domain;

public class EnergyIndexQuery {

  private String nodeId;
  private String codeOrName;
  private String indexCategory;
  private IndexType indexType;

  public EnergyIndexQuery() {
  }

  public EnergyIndexQuery(String nodeId, String codeOrName, String indexCategory,
      IndexType energyType) {
    this.nodeId = nodeId;
    this.codeOrName = codeOrName;
    this.indexCategory = indexCategory;
    this.indexType = energyType;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getCodeOrName() {
    return codeOrName;
  }

  public void setCodeOrName(String codeOrName) {
    this.codeOrName = codeOrName;
  }

  public String getIndexCategory() {
    return indexCategory;
  }

  public void setIndexCategory(String indexCategory) {
    this.indexCategory = indexCategory;
  }

  public IndexType getIndexType() {
    return indexType;
  }

  public void setIndexType(IndexType indexType) {
    this.indexType = indexType;
  }
}
