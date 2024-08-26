package com.dingzhuo.energy.project.basicSetup.service;

import com.dingzhuo.energy.project.basicSetup.domain.SysEquipmentfile;
import com.dingzhuo.energy.project.basicSetup.domain.SysSvgInfo;

import java.util.List;

/**
 * 组态图Service接口
 *
 * @author sys
 * @date 2020-02-24
 */
public interface ISysEquipmentfileService {

  void saveEquipmentFile(SysEquipmentfile sysEquipmentfile);

  void saveSettingInfo(String nodeId, List<SysSvgInfo> svgInfo);

  /**
   * 获取组态图配置信息
   * @param nodeId 模型节点 id
   * @return
   */
  SysEquipmentfile getConfigure(String nodeId);

  /**
   * 获取组态图配置的点位号
   * @param nodeId 模型节点 id
   * @return
   */
  List<SysSvgInfo> getConfigureTag(String nodeId);
}
