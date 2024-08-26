package com.dingzhuo.energy.data.monitoring.device.mapper;

import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormulaParam;
import java.util.List;

/**
 * 设备状态参数Mapper接口
 * 
 * @author zhaow
 * @date 2020-03-20
 */
public interface DeviceFormulaParamMapper 
{
    /**
     * 查询设备状态参数
     * 
     * @param id 设备状态参数ID
     * @return 设备状态参数
     */
    public DeviceFormulaParam selectDeviceFormulaParamById(String id);

    /**
     * 查询设备状态参数列表
     * 
     * @param deviceFormulaParam 设备状态参数
     * @return 设备状态参数集合
     */
    public List<DeviceFormulaParam> selectDeviceFormulaParamList(DeviceFormulaParam deviceFormulaParam);

    /**
     * 新增设备状态参数
     * 
     * @param deviceFormulaParam 设备状态参数
     * @return 结果
     */
    public int insertDeviceFormulaParam(DeviceFormulaParam deviceFormulaParam);

    /**
     * 修改设备状态参数
     * 
     * @param deviceFormulaParam 设备状态参数
     * @return 结果
     */
    public int updateDeviceFormulaParam(DeviceFormulaParam deviceFormulaParam);

    /**
     * 删除设备状态参数
     * 
     * @param id 设备状态参数ID
     * @return 结果
     */
    public int deleteDeviceFormulaParamById(String id);

    /**
     * 批量删除设备状态参数
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteDeviceFormulaParamByIds(String[] ids);

    /**
     * 根据 设备公式表主键ID查询设备状态参数列表
     *
     * @param devFormulaId 设备公式表主键ID
     * @return 设备状态参数集合
     */
    public List<DeviceFormulaParam> getFormulaParam(String devFormulaId);

    /**
     * 保存设备配置的状态的公式参数 根据公式主键和参数集合
     * @param devFormulaId
     * @param deviceFormulaParams
     */
    void saveDeviceFormulaParam(String devFormulaId, List<DeviceFormulaParam> deviceFormulaParams);
}
