package com.dingzhuo.energy.project.gateway.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.gateway.mapper.GatewaySettingMapper;
import com.dingzhuo.energy.project.gateway.domain.GatewaySetting;
import com.dingzhuo.energy.project.gateway.service.IGatewaySettingService;

/**
 * 网关配置信息Service业务层处理
 *
 * @author zhw
 * @date 2022-04-15
 */
@Service
public class GatewaySettingServiceImpl implements IGatewaySettingService
{
    @Autowired
    private GatewaySettingMapper gatewaySettingMapper;

    /**
     * 查询网关配置信息
     *
     * @param id 网关配置信息ID
     * @return 网关配置信息
     */
    @Override
    public GatewaySetting selectGatewaySettingById(String id)
    {
        return gatewaySettingMapper.selectGatewaySettingById(id);
    }

    /**
     * 查询网关配置信息列表
     *
     * @param gatewaySetting 网关配置信息
     * @return 网关配置信息
     */
    @Override
    public List<GatewaySetting> selectGatewaySettingList(GatewaySetting gatewaySetting)
    {
        return gatewaySettingMapper.selectGatewaySettingList(gatewaySetting);
    }

    /**
     * 新增网关配置信息
     *
     * @param gatewaySetting 网关配置信息
     * @return 结果
     */
    @Override
    public int insertGatewaySetting(GatewaySetting gatewaySetting)
    {
        gatewaySetting.setCreateTime(DateUtils.getNowDate());
        return gatewaySettingMapper.insertGatewaySetting(gatewaySetting);
    }

    /**
     * 修改网关配置信息
     *
     * @param gatewaySetting 网关配置信息
     * @return 结果
     */
    @Override
    public int updateGatewaySetting(GatewaySetting gatewaySetting)
    {
        gatewaySetting.setUpdateTime(DateUtils.getNowDate());
        return gatewaySettingMapper.updateGatewaySetting(gatewaySetting);
    }

    /**
     * 批量删除网关配置信息
     *
     * @param ids 需要删除的网关配置信息ID
     * @return 结果
     */
    @Override
    public int deleteGatewaySettingByIds(String[] ids)
    {
        return gatewaySettingMapper.deleteGatewaySettingByIds(ids);
    }

    /**
     * 删除网关配置信息信息
     *
     * @param id 网关配置信息ID
     * @return 结果
     */
    @Override
    public int deleteGatewaySettingById(String id)
    {
        return gatewaySettingMapper.deleteGatewaySettingById(id);
    }

    /**
     * 根据网关编号更新心跳时间
     * @param gatewaySetting
     * @return
     */
    @Override
    public int updateGatewaySettingByNum(GatewaySetting gatewaySetting){
        return gatewaySettingMapper.updateGatewaySettingByNum(gatewaySetting);
    }

    /**
     * 查询网关监控信息列表
     *
     * @return 网关配置信息
     */
    @Override
    public List<GatewaySetting> selectGatewaySettingMonitorList()
    {
        return gatewaySettingMapper.selectGatewaySettingMonitorList();
    }

    /**
     * 网关编号唯一性检查
     * @return
     */
    @Override
    public List<GatewaySetting> checkOne(GatewaySetting gatewaySetting){
        return gatewaySettingMapper.checkOne(gatewaySetting);
    }
}
