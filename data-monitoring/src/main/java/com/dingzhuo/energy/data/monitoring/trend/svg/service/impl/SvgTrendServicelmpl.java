package com.dingzhuo.energy.data.monitoring.trend.svg.service.impl;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.monitoring.trend.svg.mapper.SvgTrendMapper;
import com.dingzhuo.energy.data.monitoring.trend.svg.service.ISvgTrendService;
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
