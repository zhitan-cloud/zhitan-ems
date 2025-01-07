package com.zhitan.gatewaysetting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.energyIndicators.domain.EnergyIndicators;
import com.zhitan.gatewaysetting.domain.GatewaySetting;
import com.zhitan.gatewaysetting.mapper.GatewaySettingMapper;
import com.zhitan.gatewaysetting.service.IGatewaySettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 网关配置信息Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-10-30
 */
@Service
public class GatewaySettingServiceImpl extends ServiceImpl<GatewaySettingMapper, GatewaySetting> implements IGatewaySettingService {
    @Autowired
    private GatewaySettingMapper gatewaySettingMapper;

    /**
     * 查询网关配置信息
     *
     * @param id 网关配置信息主键
     * @return 网关配置信息
     */
    @Override
    public GatewaySetting selectGatewaySettingById(String id) {
        return gatewaySettingMapper.selectGatewaySettingById(id);
    }

    /**
     * 查询网关配置信息列表
     *
     * @param gatewaySetting 网关配置信息
     * @return 网关配置信息
     */
    @Override
    public List<GatewaySetting> selectGatewaySettingList(GatewaySetting gatewaySetting) {
        return gatewaySettingMapper.selectGatewaySettingList(gatewaySetting);
    }

    /**
     * 新增网关配置信息
     *
     * @param gatewaySetting 网关配置信息
     * @return 结果
     */
    @Override
    public int insertGatewaySetting(GatewaySetting gatewaySetting) {
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
    public int updateGatewaySetting(GatewaySetting gatewaySetting) {
        gatewaySetting.setUpdateTime(DateUtils.getNowDate());
        return gatewaySettingMapper.updateGatewaySetting(gatewaySetting);
    }

    /**
     * 批量删除网关配置信息
     *
     * @param ids 需要删除的网关配置信息主键
     * @return 结果
     */
    @Override
    public int deleteGatewaySettingByIds(String[] ids) {
        return gatewaySettingMapper.deleteGatewaySettingByIds(ids);
    }

    /**
     * 删除网关配置信息信息
     *
     * @param id 网关配置信息主键
     * @return 结果
     */
    @Override
    public int deleteGatewaySettingById(String id) {
        return gatewaySettingMapper.deleteGatewaySettingById(id);
    }

    /**
     * 校验网关编码是否唯一
     * @param gatewaySetting
     * @return
     */
    @Override
    public int checkOne(GatewaySetting gatewaySetting) {
        return gatewaySettingMapper.checkOne(gatewaySetting);
    }

    /**
     *
     * @param gatewaySetting 过滤参数
     * @return 结果
     */
    @Override
    public GatewaySetting ptNum(GatewaySetting gatewaySetting) {
        return gatewaySettingMapper.ptNum(gatewaySetting);
    }

    /**
     * 查询网关配置信息列表分页
     *
     * @param gatewaySetting 网关配置信息
     * @return 网关配置信息
     */
    @Override
    public Page<GatewaySetting> selectGatewaySettingPage(GatewaySetting gatewaySetting,Long pageNum, Long pageSize) {
        LambdaQueryWrapper<GatewaySetting> queryWrapper = new LambdaQueryWrapper<GatewaySetting>();
        queryWrapper.orderByDesc(GatewaySetting::getCreateTime);
        return gatewaySettingMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
    }
}
