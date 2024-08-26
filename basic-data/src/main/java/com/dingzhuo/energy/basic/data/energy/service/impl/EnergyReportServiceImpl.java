package com.dingzhuo.energy.basic.data.energy.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.energy.mapper.EnergyReportMapper;
import com.dingzhuo.energy.basic.data.energy.domain.EnergyReport;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyReportService;

/**
 * 节能分析报告管理Service业务层处理
 * 
 * @author zhaow
 * @date 2020-12-21
 */
@Service
public class EnergyReportServiceImpl implements IEnergyReportService 
{
    @Autowired
    private EnergyReportMapper energyReportMapper;

    /**
     * 查询节能分析报告管理
     * 
     * @param id 节能分析报告管理ID
     * @return 节能分析报告管理
     */
    @Override
    public EnergyReport selectEnergyReportById(String id)
    {
        return energyReportMapper.selectEnergyReportById(id);
    }

    /**
     * 查询节能分析报告管理列表
     * 
     * @param energyReport 节能分析报告管理
     * @return 节能分析报告管理
     */
    @Override
    public List<EnergyReport> selectEnergyReportList(EnergyReport energyReport)
    {
        return energyReportMapper.selectEnergyReportList(energyReport);
    }

    /**
     * 新增节能分析报告管理
     * 
     * @param energyReport 节能分析报告管理
     * @return 结果
     */
    @Override
    public int insertEnergyReport(EnergyReport energyReport)
    {
        energyReport.setCreateTime(DateUtils.getNowDate());
        return energyReportMapper.insertEnergyReport(energyReport);
    }

    /**
     * 修改节能分析报告管理
     * 
     * @param energyReport 节能分析报告管理
     * @return 结果
     */
    @Override
    public int updateEnergyReport(EnergyReport energyReport)
    {
        energyReport.setUpdateTime(DateUtils.getNowDate());
        return energyReportMapper.updateEnergyReport(energyReport);
    }

    /**
     * 批量删除节能分析报告管理
     * 
     * @param ids 需要删除的节能分析报告管理ID
     * @return 结果
     */
    @Override
    public int deleteEnergyReportByIds(String[] ids)
    {
        return energyReportMapper.deleteEnergyReportByIds(ids);
    }

    /**
     * 删除节能分析报告管理信息
     * 
     * @param id 节能分析报告管理ID
     * @return 结果
     */
    @Override
    public int deleteEnergyReportById(String id)
    {
        return energyReportMapper.deleteEnergyReportById(id);
    }
}
