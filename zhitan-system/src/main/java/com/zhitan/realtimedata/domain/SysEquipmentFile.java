package com.zhitan.realtimedata.domain;

import lombok.Data;

import java.util.List;

/**
 * 拓扑图对象 sys_equipmentfile
 *
 */
@Data
public class SysEquipmentFile {

  private String nodeId;
  private String filePath;
  private String svgType;
  private List<SysSvgInfo> infoList;
}
