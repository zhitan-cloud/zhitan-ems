package com.dingzhuo.energy.basic.data.energy.mapper;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyReport;
import java.util.List;

/**
 * 节能分析报告管理Mapper接口
 * 
 * @author zhaow
 * @date 2020-12-21
 */
public interface EnergyReportMapper 
{
    /**
     * 查询节能分析报告管理
     * 
     * @param id 节能分析报告管理ID
     * @return 节能分析报告管理
     */
    public EnergyReport selectEnergyReportById(String id);

    /**
     * 查询节能分析报告管理列表
     * 
     * @param energyReport 节能分析报告管理
     * @return 节能分析报告管理集合
     */
    public List<EnergyReport> selectEnergyReportList(EnergyReport energyReport);

    /**
     * 新增节能分析报告管理
     * 
     * @param energyReport 节能分析报告管理
     * @return 结果
     */
    public int insertEnergyReport(EnergyReport energyReport);

    /**
     * 修改节能分析报告管理
     * 
     * @param energyReport 节能分析报告管理
     * @return 结果
     */
    public int updateEnergyReport(EnergyReport energyReport);

    /**
     * 删除节能分析报告管理
     * 
     * @param id 节能分析报告管理ID
     * @return 结果
     */
    public int deleteEnergyReportById(String id);

    /**
     * 批量删除节能分析报告管理
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteEnergyReportByIds(String[] ids);
}
