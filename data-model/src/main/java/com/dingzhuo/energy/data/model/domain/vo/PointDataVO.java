package com.dingzhuo.energy.data.model.domain.vo;

/**
 * 模型点位id与名称
 *
 * @Author: Zhujw
 * @Date: 2023/3/6
 */
public class PointDataVO {

  /**
   * 点位id
   */
  private String indexId;

  /**
   * 点位名称
   */
  private String indexName;

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public String getIndexName() {
    return indexName;
  }

  public void setIndexName(String indexName) {
    this.indexName = indexName;
  }
}
