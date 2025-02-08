package com.zhitan.basicSetup.service.impl;

import com.zhitan.basicSetup.mapper.SysEquipmentfileMapper;
import com.zhitan.basicSetup.service.ISysEquipmentfileService;
import com.zhitan.realtimedata.domain.SysEquipmentFile;
import com.zhitan.realtimedata.domain.SysSvgInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 组态图Service业务层处理
 *
 * @author sys
 * @date 2020-02-24
 */
@Service
public class SysEquipmentfileServiceImpl implements ISysEquipmentfileService {

  @Autowired
  private SysEquipmentfileMapper equipmentfileMapper;

  @Override
  public void saveEquipmentFile(SysEquipmentFile sysEquipmentfile) {
    equipmentfileMapper.saveEquipmentFile(sysEquipmentfile);
  }

  @Override
  public void saveSettingInfo(String nodeId, List<SysSvgInfo> svgInfo) {
    equipmentfileMapper.saveSettingInfo(nodeId, svgInfo);
  }

  @Override
  public SysEquipmentFile getConfigure(String nodeId) {
    SysEquipmentFile sysEquipmentfile = equipmentfileMapper.getConfigure(nodeId);
    List<SysSvgInfo> infos = getConfigureTag(nodeId);
    if (sysEquipmentfile != null) {
      sysEquipmentfile.setInfoList(infos);
    }

    return sysEquipmentfile;
  }

  @Override
  public List<SysSvgInfo> getConfigureTag(String nodeId) {
    return equipmentfileMapper.getConfigureTag(nodeId);
  }
}
