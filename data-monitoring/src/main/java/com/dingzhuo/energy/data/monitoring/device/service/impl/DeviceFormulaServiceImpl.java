package com.dingzhuo.energy.data.monitoring.device.service.impl;

import java.util.ArrayList;
import java.util.List;
import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormulaParam;
import com.dingzhuo.energy.data.monitoring.device.mapper.DeviceFormulaParamMapper;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.data.monitoring.device.mapper.DeviceFormulaMapper;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceFormulaService;


/**
 * 设备计算公式Service业务层处理
 * 
 * @author zhaow
 * @date 2020-03-20
 */
@Service
public class DeviceFormulaServiceImpl implements IDeviceFormulaService 
{
    @Autowired
    private DeviceFormulaMapper deviceFormulaMapper;

    @Autowired
    private DeviceFormulaParamMapper deviceFormulaParamMapper;

    /**
     * 查询设备计算公式
     * 
     * @param id 设备计算公式ID
     * @return 设备计算公式
     */
    @Override
    public DeviceFormula selectDeviceFormulaById(String id)
    {
        return deviceFormulaMapper.selectDeviceFormulaById(id);
    }

    /**
     * 查询设备计算公式列表
     * 
     * @param deviceFormula 设备计算公式
     * @return 设备计算公式
     */
    @Override
    public List<DeviceFormula> selectDeviceFormulaList(DeviceFormula deviceFormula)
    {
        return deviceFormulaMapper.selectDeviceFormulaList(deviceFormula);
    }

    /**
     * 新增设备计算公式
     * 
     * @param deviceFormula 设备计算公式
     * @return 结果
     */
    @Override
    public int insertDeviceFormula(DeviceFormula deviceFormula)
    {
        return deviceFormulaMapper.insertDeviceFormula(deviceFormula);
    }

    /**
     * 修改设备计算公式
     * 
     * @param deviceFormula 设备计算公式
     * @return 结果
     */
    @Override
    public int updateDeviceFormula(DeviceFormula deviceFormula)
    {
        return deviceFormulaMapper.updateDeviceFormula(deviceFormula);
    }

    /**
     * 批量删除设备计算公式
     * 
     * @param ids 需要删除的设备计算公式ID
     * @return 结果
     */
    @Override
    public int deleteDeviceFormulaByIds(String[] ids)
    {
        return deviceFormulaMapper.deleteDeviceFormulaByIds(ids);
    }

    /**
     * 删除设备计算公式信息
     * 
     * @param id 设备计算公式ID
     * @return 结果
     */
    @Override
    public int deleteDeviceFormulaById(String id)
    {
        return deviceFormulaMapper.deleteDeviceFormulaById(id);
    }

    /**
     * 取根据 设备模型 节点ID  和  设备状态主键 ID  取  设备公式信息
     * @param nodeId
     * @param stateId
     * @return
     */
    @Override
    public DeviceFormula getDeviceFormula(String nodeId,String stateId) {
        //根据 模型节点ID 和 设备状态主键ID获取 设备公式信息
        DeviceFormula deviceFormula = deviceFormulaMapper.getDeviceFormula(nodeId,stateId);
        if (deviceFormula != null) {
            //根据设备 设备公式表主键ID获取 设备对应的参数集合
            List<DeviceFormulaParam> deviceFormulaParams = deviceFormulaParamMapper.getFormulaParam(deviceFormula.getId());
            if (!deviceFormulaParams.isEmpty()) {
                deviceFormula.setDeviceFormulaParams(deviceFormulaParams);
            }
        } else {
            deviceFormula = new DeviceFormula();
        }

        return deviceFormula;
    }

    /**
     * 设备状态 配置 公式保存
     * @param deviceFormula
     * @return
     */
    @Override
    public void saveDeviceFormula(DeviceFormula deviceFormula) {

        String calcText = deviceFormula.getFormulaText();
        List<String> paramNames = new ArrayList<>();
        for (DeviceFormulaParam param : deviceFormula.getDeviceFormulaParams()) {
            calcText = calcText.replace(param.getParamName(), String.format("'%s'", param.getParamValue()));
        }
            deviceFormula.setCalcText(calcText);
        //1、保存 状态的配置公式
        if (StringUtils.isEmpty(deviceFormula.getId())) {
            deviceFormula.setId(UUID.fastUUID().toString());
            deviceFormulaMapper.insertDeviceFormula(deviceFormula);
        } else {
            deviceFormulaMapper.updateDeviceFormula(deviceFormula);
        }

        //2、处理 公式对应的参数 并进行保存
        deviceFormula.getDeviceFormulaParams().forEach(param -> {
            param.setId(UUID.fastUUID().toString());
            param.setDevFormulaId(deviceFormula.getId());
        });
        deviceFormulaParamMapper
                .saveDeviceFormulaParam(deviceFormula.getId(), deviceFormula.getDeviceFormulaParams());
    }

    /**
     * 设备状态 公式 启用设置
     */
    @Override
    public int editDeviceFormulaIsEnable(String nodeId,String isEnable, String[] ids)
    {
        return deviceFormulaMapper.editDeviceFormulaIsEnable(nodeId,isEnable, ids);
    }

    @Override
    public List<DeviceFormula> getAllDeviceFormula() {
        return deviceFormulaMapper.getAllDeviceFormula();
    }
}
