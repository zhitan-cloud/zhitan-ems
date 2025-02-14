package com.zhitan.realtimedata.service;

import com.zhitan.model.domain.EnergyIndex;

import java.util.List;

public interface ISvgTrendService {

    /**
     * 实时检测 功能 的多 sheet页  展示趋势图
     * @param energyIndex
     * @return
     */
    List<EnergyIndex> selectSvgList(EnergyIndex energyIndex);
}
