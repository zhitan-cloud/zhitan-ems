package com.zhitan.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.system.domain.SysOperLog;
import com.zhitan.system.mapper.SysOperLogMapper;
import com.zhitan.system.service.ISysOperLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author zhitan
 */
@Service
public class SysOperLogServiceImpl extends ServiceImpl<SysOperLogMapper,SysOperLog> implements ISysOperLogService
{
    @Resource
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insertOperlog(operLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperLog> selectOperLogList(SysOperLog operLog)
    {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
        return operLogMapper.deleteOperLogByIds(operIds);
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
        return operLogMapper.selectOperLogById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        operLogMapper.cleanOperLog();
    }

    @Override
    public Page<SysOperLog> selectOperLogPage(SysOperLog operLog, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<SysOperLog> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(operLog.getOperIp()), SysOperLog::getOperIp,operLog.getOperIp());
        queryWrapper.like(StringUtils.isNotEmpty(operLog.getTitle()),SysOperLog::getTitle,operLog.getTitle());
        queryWrapper.like(StringUtils.isNotEmpty(operLog.getOperName()),SysOperLog::getOperName,operLog.getOperName());
        queryWrapper.eq(null != operLog.getBusinessType(),SysOperLog::getBusinessType,operLog.getBusinessType());
        queryWrapper.eq(null != operLog.getStatus(), SysOperLog::getStatus,operLog.getStatus());
        if(operLog.getParams().containsKey("beginTime")) {
            queryWrapper.between(SysOperLog::getCreateTime,
                    DateUtils.parseDate(operLog.getParams().get("beginTime")),
                    DateUtils.parseDate(operLog.getParams().get("endTime")));
        }
        return operLogMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
    }
}
