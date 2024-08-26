package com.dingzhuo.energy.data.model.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
public class TreeObject implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 节点ID
   */
  private String id;

  /**
   * 节点名称
   */
  private String label;

  /**
   * 子节点
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<TreeObject> children;

  public TreeObject() {

  }

  public TreeObject(ModelNode modelNode) {
    this.id = modelNode.getNodeId();
    this.label = modelNode.getName();
    this.children = modelNode.getChildren().stream().map(TreeObject::new).collect(Collectors.toList());
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<TreeObject> getChildren() {
    return children;
  }

  public void setChildren(List<TreeObject> children) {
    this.children = children;
  }
}
