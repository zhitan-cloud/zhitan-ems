package com.zhitan.comprehensivestatistics.service;


import com.zhitan.basicdata.domain.FacilityArchives;
import com.zhitan.common.enums.TimeType;
import com.zhitan.comprehensivestatistics.domain.ComprehensiveStatistics;

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
    List<ComprehensiveStatistics> getDatasByList(List<String> indexIds, Date beginTime, Date endTime, TimeType timeType);
    List<ComprehensiveStatistics> getDatasByIndex(List<String> indexIds,Date beginTime, TimeType timeType);
    List<ComprehensiveStatistics>  getDatasIndex(List<String> indexIds,Date dataTime, TimeType timeType);
    List<ComprehensiveStatistics> getEnergyByIndex(String indexType);
}