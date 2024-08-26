package com.dingzhuo.energy.project.energyBalance.service;


import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;

import java.util.List;

/**
 * 能流分析Service接口
 * 
 * @author zhaow
 * @date 2020-12-26
 */
public interface IEnergyPicService
{

    /**
     * 查询能流分析报表的指标数据列表
     *
     * @param energyPic 能流分析报表的指标数据
     * @return 能流分析集合
     */
    public List<EnergyPic> selectEnergyPicList(EnergyPic energyPic);
}
