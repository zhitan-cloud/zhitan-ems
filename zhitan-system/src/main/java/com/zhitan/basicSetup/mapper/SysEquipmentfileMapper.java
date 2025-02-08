package com.zhitan.basicSetup.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.realtimedata.domain.SysEquipmentFile;
import com.zhitan.realtimedata.domain.SysSvgInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组态图Mapper接口
 *
 * @author sys
 * @date 2020-02-24
 */
public interface SysEquipmentfileMapper extends BaseMapper<SysEquipmentFile> {

  void saveEquipmentFile(SysEquipmentFile sysEquipmentfile);

  void saveSettingInfo(@Param("nodeId") String nodeId, @Param("svgInfo") List<SysSvgInfo> svgInfo);

  SysEquipmentFile getConfigure(String nodeId);

  List<SysSvgInfo> getConfigureTag(String nodeId);
}
