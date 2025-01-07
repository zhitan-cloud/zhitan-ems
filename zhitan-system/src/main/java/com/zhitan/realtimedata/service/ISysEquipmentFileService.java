package com.zhitan.realtimedata.service;


import com.zhitan.realtimedata.domain.SysEquipmentFile;
import com.zhitan.realtimedata.domain.SysSvgInfo;

import java.util.List;

/**
 * 拓扑图Service接口
 */
public interface ISysEquipmentFileService {

  void saveEquipmentFile(SysEquipmentFile sysEquipmentfile);

  void saveSettingInfo(String nodeId, List<SysSvgInfo> svgInfo);

  /**
   * 获取拓扑图配置信息
   * @param nodeId 模型节点 id
   * @return
   */
  SysEquipmentFile getConfigure(String nodeId);

  /**
   * 获取拓扑图配置的点位号
   * @param nodeId 模型节点 id
   * @return
   */
  List<SysSvgInfo> getConfigureTag(String nodeId);
}
