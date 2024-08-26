package com.dingzhuo.energy.basic.data.energy.service.impl;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyProject;
import com.dingzhuo.energy.basic.data.energy.mapper.EnergyProjectMapper;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyProjectService;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 节能项目管理Service业务层处理
 * 
 * @author sys
 * @date 2020-12-07
 */
@Service
public class EnergyProjectServiceImpl implements IEnergyProjectService 
{
    @Autowired
    private EnergyProjectMapper energyProjectMapper;

    /**
     * 查询节能项目管理
     * 
     * @param id 节能项目管理ID
     * @return 节能项目管理
     */
    @Override
    public EnergyProject selectEnergyProjectById(String id)
    {
        return energyProjectMapper.selectEnergyProjectById(id);
    }

    /**
     * 查询节能项目管理列表
     * 
     * @param energyProject 节能项目管理
     * @return 节能项目管理
     */
    @Override
    public List<EnergyProject> selectEnergyProjectList(EnergyProject energyProject)
    {
        return energyProjectMapper.selectEnergyProjectList(energyProject);
    }

    /**
     * 新增节能项目管理
     * 
     * @param energyProject 节能项目管理
     * @return 结果
     */
    @Override
    public int insertEnergyProject(EnergyProject energyProject)
    {
        energyProject.setCreateTime(DateUtils.getNowDate());
        return energyProjectMapper.insertEnergyProject(energyProject);
    }

    /**
     * 修改节能项目管理
     * 
     * @param energyProject 节能项目管理
     * @return 结果
     */
    @Override
    public int updateEnergyProject(EnergyProject energyProject)
    {
        energyProject.setUpdateTime(DateUtils.getNowDate());
        energyProject.setUpdateOperator(SecurityUtils.getUsername());
        return energyProjectMapper.updateEnergyProject(energyProject);
    }

    /**
     * 批量删除节能项目管理
     * 
     * @param ids 需要删除的节能项目管理ID
     * @return 结果
     */
    @Override
    public int deleteEnergyProjectByIds(String[] ids)
    {
        return energyProjectMapper.deleteEnergyProjectByIds(ids);
    }

    /**
     * 删除节能项目管理信息
     * 
     * @param id 节能项目管理ID
     * @return 结果
     */
    @Override
    public int deleteEnergyProjectById(String id)
    {
        return energyProjectMapper.deleteEnergyProjectById(id);
    }
}
