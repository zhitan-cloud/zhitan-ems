package com.dingzhuo.energy.project.EnergyBenchmarking.service;

import com.dingzhuo.energy.project.EnergyBenchmarking.domain.EnergyBenchmarking;
import java.util.List;

/**
 * energy_benchmarkingService接口
 * 
 * @author sys
 * @date 2020-11-18
 */
public interface IEnergyBenchmarkingService 
{
    /**
     * 查询energy_benchmarking
     * 
     * @param id energy_benchmarkingID
     * @return energy_benchmarking
     */
    public EnergyBenchmarking selectEnergyBenchmarkingById(String id);

    /**
     * 查询energy_benchmarking列表
     * 
     * @param energyBenchmarking energy_benchmarking
     * @return energy_benchmarking集合
     */
    public List<EnergyBenchmarking> selectEnergyBenchmarkingList(EnergyBenchmarking energyBenchmarking);

    /**
     * 新增energy_benchmarking
     * 
     * @param energyBenchmarking energy_benchmarking
     * @return 结果
     */
    public int insertEnergyBenchmarking(EnergyBenchmarking energyBenchmarking);

    /**
     * 修改energy_benchmarking
     * 
     * @param energyBenchmarking energy_benchmarking
     * @return 结果
     */
    public int updateEnergyBenchmarking(EnergyBenchmarking energyBenchmarking);

    /**
     * 批量删除energy_benchmarking
     * 
     * @param ids 需要删除的energy_benchmarkingID
     * @return 结果
     */
    public int deleteEnergyBenchmarkingByIds(String[] ids);

    /**
     * 删除energy_benchmarking信息
     * 
     * @param id energy_benchmarkingID
     * @return 结果
     */
    public int deleteEnergyBenchmarkingById(String id);
}
