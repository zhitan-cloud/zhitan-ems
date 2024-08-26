package com.dingzhuo.energy.project.comprehensiveStatistics.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.yearComperhensive;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 年
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IyearComprehensive {
    public List<yearComperhensive> getYearComprehensiveList(String nodeId, List<yearComperhensive> dataList,
                                                            Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<yearComperhensive> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
