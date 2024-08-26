package com.dingzhuo.energy.basic.data.energy.service;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyReportAnnex;
import java.util.List;

/**
 * 节能分析报告管理附件Service接口
 * 
 * @author zhaow
 * @date 2020-12-21
 */
public interface IEnergyReportAnnexService 
{
    /**
     * 查询节能分析报告管理附件
     * 
     * @param id 节能分析报告管理附件ID
     * @return 节能分析报告管理附件
     */
    public EnergyReportAnnex selectEnergyReportAnnexById(String id);

    /**
     * 查询节能分析报告管理附件列表
     * 
     * @param energyReportAnnex 节能分析报告管理附件
     * @return 节能分析报告管理附件集合
     */
    public List<EnergyReportAnnex> selectEnergyReportAnnexList(EnergyReportAnnex energyReportAnnex);

    /**
     * 新增节能分析报告管理附件
     * 
     * @param energyReportAnnex 节能分析报告管理附件
     * @return 结果
     */
    public int insertEnergyReportAnnex(EnergyReportAnnex energyReportAnnex);

    /**
     * 修改节能分析报告管理附件
     * 
     * @param energyReportAnnex 节能分析报告管理附件
     * @return 结果
     */
    public int updateEnergyReportAnnex(EnergyReportAnnex energyReportAnnex);

    /**
     * 批量删除节能分析报告管理附件
     * 
     * @param ids 需要删除的节能分析报告管理附件ID
     * @return 结果
     */
    public int deleteEnergyReportAnnexByIds(String[] ids);

    /**
     * 删除节能分析报告管理附件信息
     * 
     * @param id 节能分析报告管理附件ID
     * @return 结果
     */
    public int deleteEnergyReportAnnexById(String id);
}
