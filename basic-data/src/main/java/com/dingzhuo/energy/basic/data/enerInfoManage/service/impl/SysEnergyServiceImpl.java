package com.dingzhuo.energy.basic.data.enerInfoManage.service.impl;

import java.util.List;

import com.dingzhuo.energy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.enerInfoManage.mapper.SysEnergyMapper;
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnergy;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysEnergyService;

/**
 * energyService业务层处理
 *
 * @author ruoyi
 * @date 2020-02-12
 */
@Service
public class SysEnergyServiceImpl implements ISysEnergyService
{
    @Autowired
    private SysEnergyMapper sysEnergyMapper;

    /**
     * 查询energy
     *
     * @param
     * @return energy
     */
    @Override
    public SysEnergy selectSysEnergyById(Integer enerid)
    {
        return sysEnergyMapper.selectSysEnergyById(enerid);
    }

    /**
     * 查询energy列表
     *
     * @param sysEnergy energy
     * @return energy
     */
    @Override
    public List<SysEnergy> selectSysEnergyList(SysEnergy sysEnergy)
    {
        String s = "1";
        //放入  是否储存（字符串）
        List<SysEnergy> list = sysEnergyMapper.selectSysEnergyList(sysEnergy);
        for (SysEnergy energy : list) {
            if (energy.getIsstorage() == 0) {
                energy.setIsstorageString("是");
            }else {
                energy.setIsstorageString("否");
            }
        }
        return list;
    }

    /**
     * 新增energy
     *
     * @param sysEnergy energy
     * @return 结果
     */
    @Override
    public int insertSysEnergy(SysEnergy sysEnergy)
    {
        //获取当前登录人
        String nowMan = SecurityUtils.getUsername();
        sysEnergy.setModMan(nowMan);
        sysEnergy.setOprMan(nowMan);
        return sysEnergyMapper.insertSysEnergy(sysEnergy);
    }

    /**
     * 修改energy
     *
     * @param sysEnergy energy
     * @return 结果
     */
    @Override
    public int updateSysEnergy(SysEnergy sysEnergy)
    {
        String nowMan = SecurityUtils.getUsername();
        sysEnergy.setModMan(nowMan);
        return sysEnergyMapper.updateSysEnergy(sysEnergy);
    }

    /**
     * 批量删除energy
     *
     * @param
     * @return 结果
     */
    @Override
    public int deleteSysEnergyByIds(Integer[] enerids)
    {
        return sysEnergyMapper.deleteSysEnergyByIds(enerids);
    }

    /**
     * 删除energy信息
     *
     * @param
     * @return 结果
     */
    @Override
    public int deleteSysEnergyById(Integer enerid)
    {
        return sysEnergyMapper.deleteSysEnergyById(enerid);
    }
    /**
     * 查询能源类型下拉框
     *
     * @param
     * @return 结果
     */
    @Override
    public List getenerclassname() {
        return sysEnergyMapper.getenerclassname();
    }

    @Override
    public Integer getEnerClassid(String enerclassname) {
        return sysEnergyMapper.getEnerClassid(enerclassname);
    }

    /**
     * 查询一样的能源名称有几个 能源名称唯一校验
     */
    @Override
    public int selectSameEnergyNameNum(String enername) {
        return sysEnergyMapper.selectSameEnergyNameNum(enername);
    }
    /**
     * 修改的时候查询一样能源名称的id
     */
    @Override
    public Integer selectIdByName(String enername) {
        return sysEnergyMapper.selectIdByName(enername);
    }

    @Override
    public Integer getPriceCountByEnerid(SysEnergy sysEnergy) {
        return sysEnergyMapper.getPriceCountByEnerid(sysEnergy);
    }

    @Override
    public Integer insertEnergyPrice(SysEnergy sysEnergy) {
        //获取当前登录人
        String nowMan = SecurityUtils.getUsername();
        sysEnergy.setModMan(nowMan);
        sysEnergy.setOprMan(nowMan);
        return sysEnergyMapper.insertEnergyPrice(sysEnergy);
    }

    @Override
    public Integer updateEnergyPrice(SysEnergy sysEnergy) {
        String nowMan = SecurityUtils.getUsername();
        sysEnergy.setModMan(nowMan);
        return sysEnergyMapper.updateEnergyPrice(sysEnergy);
    }

    @Override
    public Integer getCoefficientCountByEnerid(Integer enerid) {
        return sysEnergyMapper.getCoefficientCountByEnerid(enerid);
    }

    @Override
    public Integer insertEnergyCoefficient(SysEnergy sysEnergy) {
        //获取当前登录人
        String nowMan = SecurityUtils.getUsername();
        sysEnergy.setModMan(nowMan);
        sysEnergy.setOprMan(nowMan);
        return sysEnergyMapper.insertEnergyCoefficient(sysEnergy);
    }

    @Override
    public Integer updateEnergyCoefficient(SysEnergy sysEnergy) {
        String nowMan = SecurityUtils.getUsername();
        sysEnergy.setModMan(nowMan);
        return sysEnergyMapper.updateEnergyCoefficient(sysEnergy);
    }
}
