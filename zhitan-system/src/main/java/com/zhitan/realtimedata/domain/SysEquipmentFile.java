package com.zhitan.realtimedata.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 拓扑图对象 sys_equipmentfile
 *
 */
@Data
@TableName("sys_equipmentfile")
public class SysEquipmentFile {

  @TableId
  private String nodeId;

  @TableField(value = "filepath")
  private String filePath;

  @TableField(exist = false)
  private String svgType;

  @TableField(exist = false)
  private List<SysSvgInfo> infoList;
}
