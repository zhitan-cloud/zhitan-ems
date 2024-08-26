package com.dingzhuo.energy.project.EnergyBenchmarking.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.EnergyBenchmarking.mapper.EnergyBenchmarkingMapper;
import com.dingzhuo.energy.project.EnergyBenchmarking.domain.EnergyBenchmarking;
import com.dingzhuo.energy.project.EnergyBenchmarking.service.IEnergyBenchmarkingService;

/**
 * energy_benchmarkingService业务层处理
 * 
 * @author sys
 * @date 2020-11-18
 */
@Service
public class EnergyBenchmarkingServiceImpl implements IEnergyBenchmarkingService 
{
    @Autowired
    private EnergyBenchmarkingMapper energyBenchmarkingMapper;

    /**
     * 查询energy_benchmarking
     * 
     * @param id energy_benchmarkingID
     * @return energy_benchmarking
     */
    @Override
    public EnergyBenchmarking selectEnergyBenchmarkingById(String id)
    {
        return energyBenchmarkingMapper.selectEnergyBenchmarkingById(id);
    }

    /**
     * 查询energy_benchmarking列表
     * 
     * @param energyBenchmarking energy_benchmarking
     * @return energy_benchmarking
     */
    @Override
    public List<EnergyBenchmarking> selectEnergyBenchmarkingList(EnergyBenchmarking energyBenchmarking)
    {
        return energyBenchmarkingMapper.selectEnergyBenchmarkingList(energyBenchmarking);
    }

    /**
     * 新增energy_benchmarking
     * 
     * @param energyBenchmarking energy_benchmarking
     * @return 结果
     */
    @Override
    public int insertEnergyBenchmarking(EnergyBenchmarking energyBenchmarking)
    {
        return energyBenchmarkingMapper.insertEnergyBenchmarking(energyBenchmarking);
    }

    /**
     * 修改energy_benchmarking
     * 
     * @param energyBenchmarking energy_benchmarking
     * @return 结果
     */
    @Override
    public int updateEnergyBenchmarking(EnergyBenchmarking energyBenchmarking)
    {
        return energyBenchmarkingMapper.updateEnergyBenchmarking(energyBenchmarking);
    }

    /**
     * 批量删除energy_benchmarking
     * 
     * @param ids 需要删除的energy_benchmarkingID
     * @return 结果
     */
    @Override
    public int deleteEnergyBenchmarkingByIds(String[] ids)
    {
        return energyBenchmarkingMapper.deleteEnergyBenchmarkingByIds(ids);
    }

    /**
     * 删除energy_benchmarking信息
     * 
     * @param id energy_benchmarkingID
     * @return 结果
     */
    @Override
    public int deleteEnergyBenchmarkingById(String id)
    {
        return energyBenchmarkingMapper.deleteEnergyBenchmarkingById(id);
    }
}
