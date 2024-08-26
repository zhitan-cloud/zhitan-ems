package com.dingzhuo.energy.basic.data.enerInfoManage.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.enerInfoManage.mapper.SysEnercoefficientMapper;
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnercoefficient;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysEnercoefficientService;

/**
 * 能源折标系数Service业务层处理
 * 
 * @author sys
 * @date 2020-02-18
 */
@Service
public class SysEnercoefficientServiceImpl implements ISysEnercoefficientService 
{
    @Autowired
    private SysEnercoefficientMapper sysEnercoefficientMapper;

    /**
     * 查询能源折标系数
     * 
     * @param ecid 能源折标系数ID
     * @return 能源折标系数
     */
    @Override
    public SysEnercoefficient selectSysEnercoefficientById(Integer ecid)
    {
        return sysEnercoefficientMapper.selectSysEnercoefficientById(ecid);
    }

    /**
     * 查询能源折标系数列表
     * 
     * @param sysEnercoefficient 能源折标系数
     * @return 能源折标系数
     */
    @Override
    public List<SysEnercoefficient> selectSysEnercoefficientList(SysEnercoefficient sysEnercoefficient)
    {
        return sysEnercoefficientMapper.selectSysEnercoefficientList(sysEnercoefficient);
    }

    /**
     * 新增能源折标系数
     * 
     * @param sysEnercoefficient 能源折标系数
     * @return 结果
     */
    @Override
    public int insertSysEnercoefficient(SysEnercoefficient sysEnercoefficient)
    {
        return sysEnercoefficientMapper.insertSysEnercoefficient(sysEnercoefficient);
    }

    /**
     * 修改能源折标系数
     * 
     * @param sysEnercoefficient 能源折标系数
     * @return 结果
     */
    @Override
    public int updateSysEnercoefficient(SysEnercoefficient sysEnercoefficient)
    {
        return sysEnercoefficientMapper.updateSysEnercoefficient(sysEnercoefficient);
    }

    /**
     * 批量删除能源折标系数
     * 
     * @param ecids 需要删除的能源折标系数ID
     * @return 结果
     */
    @Override
    public int deleteSysEnercoefficientByIds(Integer[] ecids)
    {
        return sysEnercoefficientMapper.deleteSysEnercoefficientByIds(ecids);
    }

    /**
     * 删除能源折标系数信息
     * 
     * @param ecid 能源折标系数ID
     * @return 结果
     */
    @Override
    public int deleteSysEnercoefficientById(Integer ecid)
    {
        return sysEnercoefficientMapper.deleteSysEnercoefficientById(ecid);
    }
}
