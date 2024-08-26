package com.dingzhuo.energy.basic.data.energy.service;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyProjectAnnex;
import java.util.List;

/**
 * 节能项目管理附件Service接口
 * 
 * @author sys
 * @date 2020-12-08
 */
public interface IEnergyProjectAnnexService 
{
    /**
     * 查询节能项目管理附件
     * 
     * @param id 节能项目管理附件ID
     * @return 节能项目管理附件
     */
    public EnergyProjectAnnex selectEnergyProjectAnnexById(String id);

    /**
     * 查询节能项目管理附件列表
     * 
     * @param energyProjectAnnex 节能项目管理附件
     * @return 节能项目管理附件集合
     */
    public List<EnergyProjectAnnex> selectEnergyProjectAnnexList(EnergyProjectAnnex energyProjectAnnex);

    /**
     * 新增节能项目管理附件
     * 
     * @param energyProjectAnnex 节能项目管理附件
     * @return 结果
     */
    public int insertEnergyProjectAnnex(EnergyProjectAnnex energyProjectAnnex);

    /**
     * 修改节能项目管理附件
     * 
     * @param energyProjectAnnex 节能项目管理附件
     * @return 结果
     */
    public int updateEnergyProjectAnnex(EnergyProjectAnnex energyProjectAnnex);

    /**
     * 批量删除节能项目管理附件
     * 
     * @param ids 需要删除的节能项目管理附件ID
     * @return 结果
     */
    public int deleteEnergyProjectAnnexByIds(String[] ids);

    /**
     * 删除节能项目管理附件信息
     * 
     * @param id 节能项目管理附件ID
     * @return 结果
     */
    public int deleteEnergyProjectAnnexById(String id);
}
