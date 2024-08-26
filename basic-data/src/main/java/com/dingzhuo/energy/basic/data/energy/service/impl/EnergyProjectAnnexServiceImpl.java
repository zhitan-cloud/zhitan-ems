package com.dingzhuo.energy.basic.data.energy.service.impl;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyProjectAnnex;
import com.dingzhuo.energy.basic.data.energy.mapper.EnergyProjectAnnexMapper;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyProjectAnnexService;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 节能项目管理附件Service业务层处理
 * 
 * @author sys
 * @date 2020-12-08
 */
@Service
public class EnergyProjectAnnexServiceImpl implements IEnergyProjectAnnexService 
{
    @Autowired
    private EnergyProjectAnnexMapper energyProjectAnnexMapper;

    /**
     * 查询节能项目管理附件
     * 
     * @param id 节能项目管理附件ID
     * @return 节能项目管理附件
     */
    @Override
    public EnergyProjectAnnex selectEnergyProjectAnnexById(String id)
    {
        return energyProjectAnnexMapper.selectEnergyProjectAnnexById(id);
    }

    /**
     * 查询节能项目管理附件列表
     * 
     * @param energyProjectAnnex 节能项目管理附件
     * @return 节能项目管理附件
     */
    @Override
    public List<EnergyProjectAnnex> selectEnergyProjectAnnexList(EnergyProjectAnnex energyProjectAnnex)
    {
        return energyProjectAnnexMapper.selectEnergyProjectAnnexList(energyProjectAnnex);
    }

    /**
     * 新增节能项目管理附件
     * 
     * @param energyProjectAnnex 节能项目管理附件
     * @return 结果
     */
    @Override
    public int insertEnergyProjectAnnex(EnergyProjectAnnex energyProjectAnnex)
    {
        energyProjectAnnex.setCreateTime(DateUtils.getNowDate());
        return energyProjectAnnexMapper.insertEnergyProjectAnnex(energyProjectAnnex);
    }

    /**
     * 修改节能项目管理附件
     * 
     * @param energyProjectAnnex 节能项目管理附件
     * @return 结果
     */
    @Override
    public int updateEnergyProjectAnnex(EnergyProjectAnnex energyProjectAnnex)
    {
        energyProjectAnnex.setUpdateTime(DateUtils.getNowDate());
        return energyProjectAnnexMapper.updateEnergyProjectAnnex(energyProjectAnnex);
    }

    /**
     * 批量删除节能项目管理附件
     * 
     * @param ids 需要删除的节能项目管理附件ID
     * @return 结果
     */
    @Override
    public int deleteEnergyProjectAnnexByIds(String[] ids)
    {
        return energyProjectAnnexMapper.deleteEnergyProjectAnnexByIds(ids);
    }

    /**
     * 删除节能项目管理附件信息
     * 
     * @param id 节能项目管理附件ID
     * @return 结果
     */
    @Override
    public int deleteEnergyProjectAnnexById(String id)
    {
        return energyProjectAnnexMapper.deleteEnergyProjectAnnexById(id);
    }
}
