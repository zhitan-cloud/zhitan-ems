package com.dingzhuo.energy.project.comprehensiveStatistics.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.monthlyComprehensive;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 月
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface ImonthlyComprehensive {
    public List<monthlyComprehensive> getMonthlyComprehensiveList(String nodeId, List<monthlyComprehensive> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<monthlyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
