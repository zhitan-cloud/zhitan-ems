package com.dingzhuo.energy.basic.data.enerInfoManage.service.impl;

import java.util.List;

import com.dingzhuo.energy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.enerInfoManage.mapper.SysEnerclassMapper;
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnerclass;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysEnerclassService;

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
}
