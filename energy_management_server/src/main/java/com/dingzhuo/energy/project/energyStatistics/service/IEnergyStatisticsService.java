package com.dingzhuo.energy.project.energyStatistics.service;


import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.energyStatistics.domain.dataTimeSVG;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2020-02-07
 */
public interface IEnergyStatisticsService {
    public List<DataItem> statisticByCode(List<String> indexCodes, TimeType timeType, Date beginTime, Date endTime);
    List<dataTimeSVG> reportFormsvg(dataTimeSVG dataItem);
    List<dataTimeSVG> selectDataTimeList(dataTimeSVG dataItem);
}