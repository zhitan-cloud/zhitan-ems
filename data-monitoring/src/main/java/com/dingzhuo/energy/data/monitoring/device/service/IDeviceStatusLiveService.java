package com.dingzhuo.energy.data.monitoring.device.service;

import com.dingzhuo.energy.data.monitoring.device.domain.DeviceStatusLive;
import java.util.List;

/**
 * 设备启停实时监测Service接口
 * 
 * @author sys
 * @date 2020-03-23
 */
public interface IDeviceStatusLiveService 
{
    /**
     * 查询设备启停实时监测
     * 
     * @param id 设备启停实时监测ID
     * @return 设备启停实时监测
     */
    DeviceStatusLive selectDeviceStatusLiveById(String id);

    /**
     * 查询设备启停实时监测列表
     * 
     * @param deviceStatusLive 设备启停实时监测
     * @return 设备启停实时监测集合
     */
    List<DeviceStatusLive> selectDeviceStatusLiveList(DeviceStatusLive deviceStatusLive);

    /**
     * 新增设备启停实时监测
     * 
     * @param deviceStatusLive 设备启停实时监测
     * @return 结果
     */
    int insertDeviceStatusLive(DeviceStatusLive deviceStatusLive);

    /**
     * 修改设备启停实时监测
     * 
     * @param deviceStatusLive 设备启停实时监测
     * @return 结果
     */
    int updateDeviceStatusLive(DeviceStatusLive deviceStatusLive);

    /**
     * 批量删除设备启停实时监测
     * 
     * @param ids 需要删除的设备启停实时监测ID
     * @return 结果
     */
    int deleteDeviceStatusLiveByIds(String[] ids);

    /**
     * 删除设备启停实时监测信息
     * 
     * @param id 设备启停实时监测ID
     * @return 结果
     */
    int deleteDeviceStatusLiveById(String id);

    DeviceStatusLive getDeviceStatus(String deviceId, String stateId);
}
