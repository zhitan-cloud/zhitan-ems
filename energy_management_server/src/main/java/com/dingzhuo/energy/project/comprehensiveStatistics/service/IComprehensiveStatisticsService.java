package com.dingzhuo.energy.project.comprehensiveStatistics.service;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.comprehensiveStatistics;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2020-02-07
 */
public interface IComprehensiveStatisticsService {
    List<FacilityArchives> getFacilityArchives();
    List<comprehensiveStatistics> getDatasByList(List<String> indexIds,Date beginTime, Date endTime,TimeType timeType);
    List<comprehensiveStatistics> getDatasByIndex(List<String> indexIds,Date beginTime, TimeType timeType);
    List<comprehensiveStatistics>  getDatasIndex(List<String> indexIds,Date dataTime, TimeType timeType);
    List<comprehensiveStatistics> getEnergyByIndex(String indexType);
}