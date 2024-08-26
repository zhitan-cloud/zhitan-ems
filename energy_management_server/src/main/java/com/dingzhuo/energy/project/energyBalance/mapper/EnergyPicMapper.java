package com.dingzhuo.energy.project.energyBalance.mapper;


import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;

import java.util.List;

/**
 * 能流分析  Mapper接口
 * 
 * @author zhaow
 * @date 2020-12-26
 */
public interface EnergyPicMapper
{
    /**
     * 查询  能流分析  指标数据列表
     *
     * @param energyPic 工序单耗统计功能报表的指标数据
     * @return 能流分析    功能集合
     */
    public List<EnergyPic> selectEnergyPicList(EnergyPic energyPic);
}
