package com.dingzhuo.energy.project.basicSetup.mapper;

import com.dingzhuo.energy.project.basicSetup.domain.SysEquipmentfile;
import com.dingzhuo.energy.project.basicSetup.domain.SysSvgInfo;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 组态图Mapper接口
 *
 * @author sys
 * @date 2020-02-24
 */
public interface SysEquipmentfileMapper {

  void saveEquipmentFile(SysEquipmentfile sysEquipmentfile);

  void saveSettingInfo(@Param("nodeId") String nodeId, @Param("svgInfo") List<SysSvgInfo> svgInfo);

  SysEquipmentfile getConfigure(String nodeId);

  List<SysSvgInfo> getConfigureTag(String nodeId);
}
