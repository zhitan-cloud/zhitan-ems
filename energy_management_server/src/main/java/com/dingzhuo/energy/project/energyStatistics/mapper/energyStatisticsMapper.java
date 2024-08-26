package com.dingzhuo.energy.project.energyStatistics.mapper;

import com.dingzhuo.energy.project.energyStatistics.domain.dataTimeSVG;

import java.util.List;

public interface energyStatisticsMapper {
  List<dataTimeSVG> reportFormsvg(dataTimeSVG dataItem);
  List<dataTimeSVG> selectDataTimelist(dataTimeSVG dataItem);
}
