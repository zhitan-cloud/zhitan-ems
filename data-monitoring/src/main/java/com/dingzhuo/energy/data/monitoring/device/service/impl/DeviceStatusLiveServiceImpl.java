package com.dingzhuo.energy.data.monitoring.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.data.monitoring.device.mapper.DeviceStatusLiveMapper;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceStatusLive;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceStatusLiveService;

/**
 * 设备启停实时监测Service业务层处理
 *
 * @author sys
 * @date 2020-03-23
 */
@Service
public class DeviceStatusLiveServiceImpl implements IDeviceStatusLiveService {

  @Autowired
  private DeviceStatusLiveMapper deviceStatusLiveMapper;

  /**
   * 查询设备启停实时监测
   *
   * @param id 设备启停实时监测ID
   * @return 设备启停实时监测
   */
  @Override
  public DeviceStatusLive selectDeviceStatusLiveById(String id) {
    return deviceStatusLiveMapper.selectDeviceStatusLiveById(id);
  }

  /**
   * 查询设备启停实时监测列表
   *
   * @param deviceStatusLive 设备启停实时监测
   * @return 设备启停实时监测
   */
  @Override
  public List<DeviceStatusLive> selectDeviceStatusLiveList(DeviceStatusLive deviceStatusLive) {
    return deviceStatusLiveMapper.selectDeviceStatusLiveList(deviceStatusLive);
  }

  /**
   * 新增设备启停实时监测
   *
   * @param deviceStatusLive 设备启停实时监测
   * @return 结果
   */
  @Override
  public int insertDeviceStatusLive(DeviceStatusLive deviceStatusLive) {
    return deviceStatusLiveMapper.insertDeviceStatusLive(deviceStatusLive);
  }

  /**
   * 修改设备启停实时监测
   *
   * @param deviceStatusLive 设备启停实时监测
   * @return 结果
   */
  @Override
  public int updateDeviceStatusLive(DeviceStatusLive deviceStatusLive) {
    return deviceStatusLiveMapper.updateDeviceStatusLive(deviceStatusLive);
  }

  /**
   * 批量删除设备启停实时监测
   *
   * @param ids 需要删除的设备启停实时监测ID
   * @return 结果
   */
  @Override
  public int deleteDeviceStatusLiveByIds(String[] ids) {
    return deviceStatusLiveMapper.deleteDeviceStatusLiveByIds(ids);
  }

  /**
   * 删除设备启停实时监测信息
   *
   * @param id 设备启停实时监测ID
   * @return 结果
   */
  @Override
  public int deleteDeviceStatusLiveById(String id) {
    return deviceStatusLiveMapper.deleteDeviceStatusLiveById(id);
  }

  @Override
  public DeviceStatusLive getDeviceStatus(String deviceId, String stateId) {
    return deviceStatusLiveMapper.getDeviceStatus(deviceId, stateId);
  }
}
