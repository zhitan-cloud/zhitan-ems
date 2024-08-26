package com.dingzhuo.energy.project.energyStatistics.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.mapper.PeriodDataMapper;
import com.dingzhuo.energy.project.energyStatistics.domain.dataTimeSVG;
import com.dingzhuo.energy.project.energyStatistics.mapper.energyStatisticsMapper;
import com.dingzhuo.energy.project.energyStatistics.service.IEnergyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-07
 */
@Service
public class energyStatisticsServiceImpl implements IEnergyStatisticsService {
    @Autowired
    private energyStatisticsMapper energyStatisticsMapper;

    @Autowired
    private PeriodDataMapper PeriodDataMapper;

    @Override
    public List<DataItem> statisticByCode(List<String> indexCodes, TimeType timeType, Date beginTime, Date endTime) {
        if (indexCodes == null || indexCodes.isEmpty()) {
            return Collections.emptyList();
        }
        return PeriodDataMapper.statisticByCode(indexCodes, timeType, beginTime, endTime);
    }
    @Override
    public List<dataTimeSVG> reportFormsvg(dataTimeSVG dataItem) {
        String timeCode = TimeManager.getTimeCode(dataItem.getDataTime(), dataItem.getTimeType());
        dataItem.setBeginTime(TimeManager.getBeginTime(timeCode));
        dataItem.setEndTime(TimeManager.getEndTime(timeCode));
        return energyStatisticsMapper.reportFormsvg(dataItem);
    }
    @Override
    public List<dataTimeSVG> selectDataTimeList(dataTimeSVG dataItem) {
        String timeCode = TimeManager.getTimeCode(dataItem.getDataTime(), dataItem.getTimeType());
        dataItem.setBeginTime(TimeManager.getBeginTime(timeCode));
        dataItem.setEndTime(TimeManager.getEndTime(timeCode));
        return energyStatisticsMapper.reportFormsvg(dataItem);
    }
}