package com.dingzhuo.energy.project.comprehensiveStatistics.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.dailyComprehensive;
import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 日
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IdailyComprehensive {
    public List<dailyComprehensive> getdailyComprehensiveList(String nodeId, List<dailyComprehensive> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<dailyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
