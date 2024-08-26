package com.dingzhuo.energy.basic.data.energy.service;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyProject;
import java.util.List;

/**
 * 节能项目管理Service接口
 * 
 * @author sys
 * @date 2020-12-07
 */
public interface IEnergyProjectService 
{
    /**
     * 查询节能项目管理
     * 
     * @param id 节能项目管理ID
     * @return 节能项目管理
     */
    public EnergyProject selectEnergyProjectById(String id);

    /**
     * 查询节能项目管理列表
     * 
     * @param energyProject 节能项目管理
     * @return 节能项目管理集合
     */
    public List<EnergyProject> selectEnergyProjectList(EnergyProject energyProject);

    /**
     * 新增节能项目管理
     * 
     * @param energyProject 节能项目管理
     * @return 结果
     */
    public int insertEnergyProject(EnergyProject energyProject);

    /**
     * 修改节能项目管理
     * 
     * @param energyProject 节能项目管理
     * @return 结果
     */
    public int updateEnergyProject(EnergyProject energyProject);

    /**
     * 批量删除节能项目管理
     * 
     * @param ids 需要删除的节能项目管理ID
     * @return 结果
     */
    public int deleteEnergyProjectByIds(String[] ids);

    /**
     * 删除节能项目管理信息
     * 
     * @param id 节能项目管理ID
     * @return 结果
     */
    public int deleteEnergyProjectById(String id);
}
