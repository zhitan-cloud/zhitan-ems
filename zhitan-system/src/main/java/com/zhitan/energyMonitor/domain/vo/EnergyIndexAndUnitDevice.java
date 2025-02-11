package com.zhitan.energyMonitor.domain.vo;

import lombok.Data;

/**
 * 采集点用能单元电表
 *
 * @author fanxinfu
 */
@Data
public class EnergyIndexAndUnitDevice {

  private String indexId;
  private String indexName;
  private String energyType;
  private String energyName;
  private String energyUnitName;
  private String energyUnitDeviceName;

}
