package com.zhitan.model.domain.vo;


import com.zhitan.common.core.domain.BaseEntity;

/**
 * 模型节点与点位关系对象
 *
 * @Author: Zhujw
 * @Date: 2023/3/2
 */
public class ModelNodeIndexInfor extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  private String nodeId;

  /**
   * 名称
   */
  private String name;

  /**
   * 点位id
   */
  private String indexId;
  
  /**
   * 能源类型
   */
  private String energyId;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getIndexId() {
    return indexId;
  }

  public void setIndexId(String indexId) {
    this.indexId = indexId;
  }

  public String getEnergyId() {
    return energyId;
  }

  public void setEnergyId(String energyId) {
    this.energyId = energyId;
  }
}
