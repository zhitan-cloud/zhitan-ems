package com.zhitan.basicdata.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.SysEnerclass;
import com.zhitan.basicdata.mapper.SysEnerclassMapper;
import com.zhitan.basicdata.services.ISysEnerclassService;
import com.zhitan.common.utils.SecurityUtils;
import com.zhitan.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 能源品种设置Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-10
 */
@Service
public class SysEnerclassServiceImpl implements ISysEnerclassService
{
    @Autowired
    private SysEnerclassMapper sysEnerclassMapper;

    /**
     * 查询能源品种设置
     *
     * @param enerclassid 能源品种设置ID
     * @return 能源品种设置
     */
    @Override
    public SysEnerclass selectSysEnerclassById(Integer enerclassid)
    {
        return sysEnerclassMapper.selectSysEnerclassById(enerclassid);
    }

    /**
     * 查询能源品种设置列表
     *
     * @param sysEnerclass 能源品种设置
     * @return 能源品种设置
     */
    @Override
    public List<SysEnerclass> selectSysEnerclassList(SysEnerclass sysEnerclass)
    {
        return sysEnerclassMapper.selectSysEnerclassList(sysEnerclass);
    }

    /**
     * 新增能源品种设置
     *
     * @param sysEnerclass 能源品种设置
     * @return 结果
     */
    @Override
    public int insertSysEnerclass(SysEnerclass sysEnerclass) {
        //获取当前登录人
        String nowMan = SecurityUtils.getUsername();
        sysEnerclass.setModMan(nowMan);
        sysEnerclass.setOprMan(nowMan);
        sysEnerclass.setCreateBy(nowMan);
        sysEnerclass.setUpdateBy(nowMan);
        return sysEnerclassMapper.insertSysEnerclass(sysEnerclass);
    }

    /**
     * 修改能源品种设置
     *
     * @param sysEnerclass 能源品种设置
     * @return 结果
     */
    @Override
    public int updateSysEnerclass(SysEnerclass sysEnerclass) {
        String nowMan = SecurityUtils.getUsername();
        sysEnerclass.setModMan(nowMan);
        sysEnerclass.setUpdateBy(nowMan);
        return sysEnerclassMapper.updateSysEnerclass(sysEnerclass);
    }

    /**
     * 批量删除能源品种设置
     *
     * @param enerclassids 需要删除的能源品种设置ID
     * @return 结果
     */
    @Override
    public int deleteSysEnerclassByIds(Integer[] enerclassids)
    {
        return sysEnerclassMapper.deleteSysEnerclassByIds(enerclassids);
    }

    /**
     * 删除能源品种设置信息
     *
     * @param enerclassid 能源品种设置ID
     * @return 结果
     */
    @Override
    public int deleteSysEnerclassById(Integer enerclassid)
    {
        return sysEnerclassMapper.deleteSysEnerclassById(enerclassid);
    }
    /**
     * 查询一样的能源名称有几个 能源名称唯一校验
     */
    @Override
    public int selectSameEnergyNameNum(String enerclassname) {
        return sysEnerclassMapper.selectSameEnergyNameNum(enerclassname);
    }
    /**
     * 修改的时候查询一样能源名称的id
     */
    @Override
    public Integer selectIdByName(String enerclassname) {
        return sysEnerclassMapper.selectIdByName(enerclassname);
    }

    @Override
    public Page<SysEnerclass> selectSysEnerclassPage(SysEnerclass sysEnerclass, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<SysEnerclass> queryWrapper = new LambdaQueryWrapper<SysEnerclass>();
        queryWrapper.like(StringUtils.isNotEmpty(sysEnerclass.getEnerclassname()),SysEnerclass::getEnerclassname,sysEnerclass.getEnerclassname());
        queryWrapper.orderByDesc(SysEnerclass::getCreateTime);
        return sysEnerclassMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
    }
}
