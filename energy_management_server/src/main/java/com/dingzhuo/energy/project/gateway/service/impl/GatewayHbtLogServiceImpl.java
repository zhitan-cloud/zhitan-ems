package com.dingzhuo.energy.project.gateway.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.gateway.mapper.GatewayHbtLogMapper;
import com.dingzhuo.energy.project.gateway.domain.GatewayHbtLog;
import com.dingzhuo.energy.project.gateway.service.IGatewayHbtLogService;

/**
 * 网关心跳日志Service业务层处理
 * 
 * @author zhw
 * @date 2022-04-15
 */
@Service
public class GatewayHbtLogServiceImpl implements IGatewayHbtLogService 
{
    @Autowired
    private GatewayHbtLogMapper gatewayHbtLogMapper;

    /**
     * 查询网关心跳日志
     * 
     * @param id 网关心跳日志ID
     * @return 网关心跳日志
     */
    @Override
    public GatewayHbtLog selectGatewayHbtLogById(String id)
    {
        return gatewayHbtLogMapper.selectGatewayHbtLogById(id);
    }

    /**
     * 查询网关心跳日志列表
     * 
     * @param gatewayHbtLog 网关心跳日志
     * @return 网关心跳日志
     */
    @Override
    public List<GatewayHbtLog> selectGatewayHbtLogList(GatewayHbtLog gatewayHbtLog)
    {
        return gatewayHbtLogMapper.selectGatewayHbtLogList(gatewayHbtLog);
    }

    /**
     * 新增网关心跳日志
     * 
     * @param gatewayHbtLog 网关心跳日志
     * @return 结果
     */
    @Override
    public int insertGatewayHbtLog(GatewayHbtLog gatewayHbtLog)
    {
        return gatewayHbtLogMapper.insertGatewayHbtLog(gatewayHbtLog);
    }

    /**
     * 修改网关心跳日志
     * 
     * @param gatewayHbtLog 网关心跳日志
     * @return 结果
     */
    @Override
    public int updateGatewayHbtLog(GatewayHbtLog gatewayHbtLog)
    {
        return gatewayHbtLogMapper.updateGatewayHbtLog(gatewayHbtLog);
    }

    /**
     * 批量删除网关心跳日志
     * 
     * @param ids 需要删除的网关心跳日志ID
     * @return 结果
     */
    @Override
    public int deleteGatewayHbtLogByIds(String[] ids)
    {
        return gatewayHbtLogMapper.deleteGatewayHbtLogByIds(ids);
    }

    /**
     * 删除网关心跳日志信息
     * 
     * @param id 网关心跳日志ID
     * @return 结果
     */
    @Override
    public int deleteGatewayHbtLogById(String id)
    {
        return gatewayHbtLogMapper.deleteGatewayHbtLogById(id);
    }
}
