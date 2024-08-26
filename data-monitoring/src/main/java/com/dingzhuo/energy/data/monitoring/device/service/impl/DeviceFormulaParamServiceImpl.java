package com.dingzhuo.energy.data.monitoring.device.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.data.monitoring.device.mapper.DeviceFormulaParamMapper;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormulaParam;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceFormulaParamService;

/**
 * 设备状态参数Service业务层处理
 * 
 * @author zhaow
 * @date 2020-03-20
 */
@Service
public class DeviceFormulaParamServiceImpl implements IDeviceFormulaParamService 
{
    @Autowired
    private DeviceFormulaParamMapper deviceFormulaParamMapper;

    /**
     * 查询设备状态参数
     * 
     * @param id 设备状态参数ID
     * @return 设备状态参数
     */
    @Override
    public DeviceFormulaParam selectDeviceFormulaParamById(String id)
    {
        return deviceFormulaParamMapper.selectDeviceFormulaParamById(id);
    }

    /**
     * 查询设备状态参数列表
     * 
     * @param deviceFormulaParam 设备状态参数
     * @return 设备状态参数
     */
    @Override
    public List<DeviceFormulaParam> selectDeviceFormulaParamList(DeviceFormulaParam deviceFormulaParam)
    {
        return deviceFormulaParamMapper.selectDeviceFormulaParamList(deviceFormulaParam);
    }

    /**
     * 新增设备状态参数
     * 
     * @param deviceFormulaParam 设备状态参数
     * @return 结果
     */
    @Override
    public int insertDeviceFormulaParam(DeviceFormulaParam deviceFormulaParam)
    {
        return deviceFormulaParamMapper.insertDeviceFormulaParam(deviceFormulaParam);
    }

    /**
     * 修改设备状态参数
     * 
     * @param deviceFormulaParam 设备状态参数
     * @return 结果
     */
    @Override
    public int updateDeviceFormulaParam(DeviceFormulaParam deviceFormulaParam)
    {
        return deviceFormulaParamMapper.updateDeviceFormulaParam(deviceFormulaParam);
    }

    /**
     * 批量删除设备状态参数
     * 
     * @param ids 需要删除的设备状态参数ID
     * @return 结果
     */
    @Override
    public int deleteDeviceFormulaParamByIds(String[] ids)
    {
        return deviceFormulaParamMapper.deleteDeviceFormulaParamByIds(ids);
    }

    /**
     * 删除设备状态参数信息
     * 
     * @param id 设备状态参数ID
     * @return 结果
     */
    @Override
    public int deleteDeviceFormulaParamById(String id)
    {
        return deviceFormulaParamMapper.deleteDeviceFormulaParamById(id);
    }
}
