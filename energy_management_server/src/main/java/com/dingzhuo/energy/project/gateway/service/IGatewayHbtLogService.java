package com.dingzhuo.energy.project.gateway.service;

import com.dingzhuo.energy.project.gateway.domain.GatewayHbtLog;
import java.util.List;

/**
 * 网关心跳日志Service接口
 * 
 * @author zhw
 * @date 2022-04-15
 */
public interface IGatewayHbtLogService 
{
    /**
     * 查询网关心跳日志
     * 
     * @param id 网关心跳日志ID
     * @return 网关心跳日志
     */
    public GatewayHbtLog selectGatewayHbtLogById(String id);

    /**
     * 查询网关心跳日志列表
     * 
     * @param gatewayHbtLog 网关心跳日志
     * @return 网关心跳日志集合
     */
    public List<GatewayHbtLog> selectGatewayHbtLogList(GatewayHbtLog gatewayHbtLog);

    /**
     * 新增网关心跳日志
     * 
     * @param gatewayHbtLog 网关心跳日志
     * @return 结果
     */
    public int insertGatewayHbtLog(GatewayHbtLog gatewayHbtLog);

    /**
     * 修改网关心跳日志
     * 
     * @param gatewayHbtLog 网关心跳日志
     * @return 结果
     */
    public int updateGatewayHbtLog(GatewayHbtLog gatewayHbtLog);

    /**
     * 批量删除网关心跳日志
     * 
     * @param ids 需要删除的网关心跳日志ID
     * @return 结果
     */
    public int deleteGatewayHbtLogByIds(String[] ids);

    /**
     * 删除网关心跳日志信息
     * 
     * @param id 网关心跳日志ID
     * @return 结果
     */
    public int deleteGatewayHbtLogById(String id);
}
