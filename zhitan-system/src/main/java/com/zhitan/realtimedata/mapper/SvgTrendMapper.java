package com.zhitan.realtimedata.mapper;


import com.zhitan.model.domain.EnergyIndex;

import java.util.List;

public interface SvgTrendMapper {
    /**
     * 实时检测 功能 的多 sheet页  展示 组态图  测点 报警信息
     *
     * @param energyIndex
     * @return
     */
    List<EnergyIndex> selectSvgTrendList(EnergyIndex energyIndex);
}
