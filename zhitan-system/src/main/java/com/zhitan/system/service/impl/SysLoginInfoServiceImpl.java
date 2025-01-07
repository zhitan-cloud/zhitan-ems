package com.zhitan.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.system.domain.SysLoginInfo;
import com.zhitan.system.mapper.SysLoginInfoMapper;
import com.zhitan.system.service.ISysLoginInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author zhitan
 */
@Service
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoMapper,SysLoginInfo> implements ISysLoginInfoService
{

    @Resource
    private SysLoginInfoMapper loginInfoMapper;

    /**
     * 新增系统登录日志
     *
     * @param loginInfo 访问日志对象
     */
    @Override
    public void insertLoginInfo(SysLoginInfo loginInfo)
    {
        loginInfoMapper.insertLoginInfo(loginInfo);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param loginInfo 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLoginInfo> selectLogininforList(SysLoginInfo loginInfo)
    {
        return loginInfoMapper.selectLoginInfoList(loginInfo);
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLoginInfoByIds(Long[] infoIds)
    {
        return loginInfoMapper.deleteLoginInfoByIds(infoIds);
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLoginInfo()
    {
        loginInfoMapper.cleanLoginInfo();
    }
    @Override
    public Page<SysLoginInfo> selectLogininforPage(SysLoginInfo logininfor, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<SysLoginInfo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(logininfor.getUserName()),SysLoginInfo::getUserName,logininfor.getUserName());
        queryWrapper.like(StringUtils.isNotEmpty(logininfor.getIpaddr()),SysLoginInfo::getIpaddr,logininfor.getIpaddr());
        queryWrapper.eq(StringUtils.isNotEmpty(logininfor.getStatus()),SysLoginInfo::getStatus,logininfor.getStatus());
        if(logininfor.getParams().containsKey("beginTime")) {
            queryWrapper.between(SysLoginInfo::getCreateTime,
                    DateUtils.parseDate(logininfor.getParams().get("beginTime")),
                    DateUtils.parseDate(logininfor.getParams().get("endTime")));
        }
        return loginInfoMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
    }
}
