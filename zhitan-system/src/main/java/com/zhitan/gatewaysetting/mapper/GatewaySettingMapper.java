package com.zhitan.gatewaysetting.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.gatewaysetting.domain.GatewaySetting;

import java.util.List;

/**
 * 网关配置信息Mapper接口
 *
 * @author ZhiTan
 * @date 2024-10-30
 */
public interface GatewaySettingMapper extends BaseMapper<GatewaySetting> {
    /**
     * 查询网关配置信息
     *
     * @param id 网关配置信息主键
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
     * @param id 网关配置信息主键
     * @return 结果
     */
    public int deleteGatewaySettingById(String id);

    /**
     * 批量删除网关配置信息
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGatewaySettingByIds(String[] ids);

    /**
     * 校验网关编码是否唯一
     *
     * @param gatewaySetting 校验参数
     * @return 结果
     */
    int checkOne(GatewaySetting gatewaySetting);

    /**
     * 统计计量器具数量和测点数量
     *
     * @param gatewaySetting 过滤参数
     * @return 结果
     */
    GatewaySetting ptNum(GatewaySetting gatewaySetting);

    int addNum(GatewaySetting gatewaySetting);

    int subNum(GatewaySetting gatewaySetting);
}
