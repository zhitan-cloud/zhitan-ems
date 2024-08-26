package com.dingzhuo.energy.data.model.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * 模型节点对象 model_node
 *
 * @author fanxinfu
 * @date 2020-02-10
 */
public class ModelNode extends BaseEntity {
  private static final long serialVersionUID = 1L;

  /**
   * 主键
   */
  private String nodeId;

  /**
   * 编码
   */
  @Excel(name = "编码")
  private String code;

  /**
   * 名称
   */
  @Excel(name = "名称")
  private String name;

  /**
   * 父节点 id
   */
  private String parentId;

  /**
   * 地址
   */
  private String address;

  /**
   * 模型 id
   */
  private String modelCode;

  private String nodeCategory;

  private int orderNum;

  private List<ModelNode> children = new ArrayList<>();

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getNodeId() {
    return nodeId;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setParentId(String parentId) {
    this.parentId = parentId;
  }

  public String getParentId() {
    return parentId;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress() {
    return address;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
        .append("nodeId", getNodeId())
        .append("code", getCode())
        .append("name", getName())
        .append("parentId", getParentId())
        .append("address", getAddress())
        .append("modelCode", getModelCode())
        .append("orderNum", getOrderNum())
        .toString();
  }

  public List<ModelNode> getChildren() {
    return children;
  }

  public void setChildren(List<ModelNode> children) {
    this.children = children;
  }

  public String getModelCode() {
    return modelCode;
  }

  public void setModelCode(String modelCode) {
    this.modelCode = modelCode;
  }

  public int getOrderNum() {
    return orderNum;
  }

  public void setOrderNum(int orderNum) {
    this.orderNum = orderNum;
  }

  public String getNodeCategory() {
    return nodeCategory;
  }

  public void setNodeCategory(String nodeCategory) {
    this.nodeCategory = nodeCategory;
  }
}
