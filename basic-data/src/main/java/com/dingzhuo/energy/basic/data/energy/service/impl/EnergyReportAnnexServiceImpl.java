package com.dingzhuo.energy.basic.data.energy.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.energy.mapper.EnergyReportAnnexMapper;
import com.dingzhuo.energy.basic.data.energy.domain.EnergyReportAnnex;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyReportAnnexService;

/**
 * 节能分析报告管理附件Service业务层处理
 * 
 * @author zhaow
 * @date 2020-12-21
 */
@Service
public class EnergyReportAnnexServiceImpl implements IEnergyReportAnnexService 
{
    @Autowired
    private EnergyReportAnnexMapper energyReportAnnexMapper;

    /**
     * 查询节能分析报告管理附件
     * 
     * @param id 节能分析报告管理附件ID
     * @return 节能分析报告管理附件
     */
    @Override
    public EnergyReportAnnex selectEnergyReportAnnexById(String id)
    {
        return energyReportAnnexMapper.selectEnergyReportAnnexById(id);
    }

    /**
     * 查询节能分析报告管理附件列表
     * 
     * @param energyReportAnnex 节能分析报告管理附件
     * @return 节能分析报告管理附件
     */
    @Override
    public List<EnergyReportAnnex> selectEnergyReportAnnexList(EnergyReportAnnex energyReportAnnex)
    {
        return energyReportAnnexMapper.selectEnergyReportAnnexList(energyReportAnnex);
    }

    /**
     * 新增节能分析报告管理附件
     * 
     * @param energyReportAnnex 节能分析报告管理附件
     * @return 结果
     */
    @Override
    public int insertEnergyReportAnnex(EnergyReportAnnex energyReportAnnex)
    {
        energyReportAnnex.setCreateTime(DateUtils.getNowDate());
        return energyReportAnnexMapper.insertEnergyReportAnnex(energyReportAnnex);
    }

    /**
     * 修改节能分析报告管理附件
     * 
     * @param energyReportAnnex 节能分析报告管理附件
     * @return 结果
     */
    @Override
    public int updateEnergyReportAnnex(EnergyReportAnnex energyReportAnnex)
    {
        energyReportAnnex.setUpdateTime(DateUtils.getNowDate());
        return energyReportAnnexMapper.updateEnergyReportAnnex(energyReportAnnex);
    }

    /**
     * 批量删除节能分析报告管理附件
     * 
     * @param ids 需要删除的节能分析报告管理附件ID
     * @return 结果
     */
    @Override
    public int deleteEnergyReportAnnexByIds(String[] ids)
    {
        return energyReportAnnexMapper.deleteEnergyReportAnnexByIds(ids);
    }

    /**
     * 删除节能分析报告管理附件信息
     * 
     * @param id 节能分析报告管理附件ID
     * @return 结果
     */
    @Override
    public int deleteEnergyReportAnnexById(String id)
    {
        return energyReportAnnexMapper.deleteEnergyReportAnnexById(id);
    }
}
