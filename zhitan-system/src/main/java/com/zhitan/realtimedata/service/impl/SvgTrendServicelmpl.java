package com.zhitan.realtimedata.service.impl;

import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.realtimedata.mapper.SvgTrendMapper;
import com.zhitan.realtimedata.service.ISvgTrendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SvgTrendServicelmpl implements ISvgTrendService {

    @Autowired
    private SvgTrendMapper svgTrendMapper;
    /**
     * 查询指标信息
     *
     * @param energyIndex 指标信息ID
     * @return 指标信息
     */
    @Override
    public List<EnergyIndex> selectSvgList(EnergyIndex energyIndex) {
        return svgTrendMapper.selectSvgTrendList(energyIndex);
    }
}
