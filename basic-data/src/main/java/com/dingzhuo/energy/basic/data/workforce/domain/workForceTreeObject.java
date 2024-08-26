package com.dingzhuo.energy.basic.data.workforce.domain;

import com.dingzhuo.energy.project.system.domain.SysDept;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 *
 * @author ruoyi
 */
public class workForceTreeObject implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 节点ID
   */
  private Long id;

  /**
   * 节点名称
   */
  private String label;

  /**
   * 子节点
   */
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<workForceTreeObject> children;

  public workForceTreeObject() {

  }

  public workForceTreeObject(SysDept sysDept) {
    this.id = sysDept.getDeptId();
    this.label = sysDept.getDeptName();
    this.children = sysDept.getChildren().stream().map(workForceTreeObject::new).collect(Collectors.toList());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public List<workForceTreeObject> getChildren() {
    return children;
  }

  public void setChildren(List<workForceTreeObject> children) {
    this.children = children;
  }
}
