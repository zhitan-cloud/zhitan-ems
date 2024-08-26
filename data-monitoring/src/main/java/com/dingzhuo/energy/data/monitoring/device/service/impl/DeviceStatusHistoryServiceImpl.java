package com.dingzhuo.energy.data.monitoring.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.data.monitoring.device.mapper.DeviceStatusHistoryMapper;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceStatusHistory;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceStatusHistoryService;

/**
 * 设备启停历史监测Service业务层处理
 *
 * @author sys
 * @date 2020-03-25
 */
@Service
public class DeviceStatusHistoryServiceImpl implements IDeviceStatusHistoryService {

  @Autowired
  private DeviceStatusHistoryMapper deviceStatusHistoryMapper;

  /**
   * 查询设备启停历史监测
   *
   * @param id 设备启停历史监测ID
   * @return 设备启停历史监测
   */
  @Override
  public DeviceStatusHistory selectDeviceStatusHistoryById(String id) {
    return deviceStatusHistoryMapper.selectDeviceStatusHistoryById(id);
  }

  /**
   * 查询设备启停历史监测列表
   *
   * @param deviceStatusHistory 设备启停历史监测
   * @return 设备启停历史监测
   */
  @Override
  public List<DeviceStatusHistory> selectDeviceStatusHistoryList(
      DeviceStatusHistory deviceStatusHistory) {
    return deviceStatusHistoryMapper.selectDeviceStatusHistoryList(deviceStatusHistory);
  }

  /**
   * 新增设备启停历史监测
   *
   * @param deviceStatusHistory 设备启停历史监测
   * @return 结果
   */
  @Override
  public int insertDeviceStatusHistory(DeviceStatusHistory deviceStatusHistory) {
    return deviceStatusHistoryMapper.insertDeviceStatusHistory(deviceStatusHistory);
  }

  /**
   * 修改设备启停历史监测
   *
   * @param deviceStatusHistory 设备启停历史监测
   * @return 结果
   */
  @Override
  public int updateDeviceStatusHistory(DeviceStatusHistory deviceStatusHistory) {
    return deviceStatusHistoryMapper.updateDeviceStatusHistory(deviceStatusHistory);
  }

  /**
   * 批量删除设备启停历史监测
   *
   * @param ids 需要删除的设备启停历史监测ID
   * @return 结果
   */
  @Override
  public int deleteDeviceStatusHistoryByIds(String[] ids) {
    return deviceStatusHistoryMapper.deleteDeviceStatusHistoryByIds(ids);
  }

  /**
   * 删除设备启停历史监测信息
   *
   * @param id 设备启停历史监测ID
   * @return 结果
   */
  @Override
  public int deleteDeviceStatusHistoryById(String id) {
    return deviceStatusHistoryMapper.deleteDeviceStatusHistoryById(id);
  }

  /**
   * 查询设备启停历史监测列表
   *
   * @param deviceStatusHistory 设备启停历史监测
   * @return 设备启停历史监测
   */
  @Override
  public List<DeviceStatusHistory> selectDeviceStatusHistoryListNew(
      DeviceStatusHistory deviceStatusHistory) {
    return deviceStatusHistoryMapper.selectDeviceStatusHistoryListNew(deviceStatusHistory);
  }

  @Override
  public void saveHistoryStatus(DeviceStatusHistory history) {
    deviceStatusHistoryMapper.saveHistoryStatus(history);
  }
}
