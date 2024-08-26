package com.dingzhuo.energy.project.basicSetup.domain;

import java.util.List;

/**
 * 组态图对象 sys_equipmentfile
 *
 * @author sys
 * @date 2020-02-24
 */
public class SysEquipmentfile {

  private String nodeId;
  private String filePath;
  private String svgType;
  private List<SysSvgInfo> infoList;

  public String getNodeId() {
    return nodeId;
  }

  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  public String getFilePath() {
    return filePath;
  }

  public void setFilePath(String filePath) {
    this.filePath = filePath;
  }

  public List<SysSvgInfo> getInfoList() {
    return infoList;
  }

  public void setInfoList(
      List<SysSvgInfo> infoList) {
    this.infoList = infoList;
  }

  public String getSvgType() {
    return svgType;
  }

  public void setSvgType(String svgType) {
    this.svgType = svgType;
  }
}
