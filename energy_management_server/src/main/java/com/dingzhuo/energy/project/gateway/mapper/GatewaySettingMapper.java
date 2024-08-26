package com.dingzhuo.energy.project.gateway.mapper;

import com.dingzhuo.energy.project.gateway.domain.GatewaySetting;
import java.util.List;

/**
 * 网关配置信息Mapper接口
 *
 * @author zhw
 * @date 2022-04-15
 */
public interface GatewaySettingMapper
{
    /**
     * 查询网关配置信息
     *
     * @param id 网关配置信息ID
     * @return 网关配置信息
     */
    public GatewaySetting selectGatewaySettingById(String id);

    /**
     * 查询网关配置信息列表
     *
     * @param gatewaySetting 网关配置信息
     * @return 网关配置信息集合
     */
    public List<GatewaySetting> selectGatewaySettingList(GatewaySetting gatewaySetting);


    /**
     * 新增网关配置信息
     *
     * @param gatewaySetting 网关配置信息
     * @return 结果
     */
    public int insertGatewaySetting(GatewaySetting gatewaySetting);

    /**
     * 修改网关配置信息
     *
     * @param gatewaySetting 网关配置信息
     * @return 结果
     */
    public int updateGatewaySetting(GatewaySetting gatewaySetting);

    /**
     * 删除网关配置信息
     *
     * @param id 网关配置信息ID
     * @return 结果
     */
    public int deleteGatewaySettingById(String id);

    /**
     * 批量删除网关配置信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGatewaySettingByIds(String[] ids);

    /**
     * 根据网关编号更新心跳时间
     * @param gatewaySetting
     * @return
     */
    public int updateGatewaySettingByNum(GatewaySetting gatewaySetting);

    /**
     * 网关实时状态监控列表
     * @return
     */
    public List<GatewaySetting> selectGatewaySettingMonitorList();

    /**
     * 网关编号唯一性检查
     * @return
     */
    public List<GatewaySetting> checkOne(GatewaySetting gatewaySetting);
}
