package com.zhitan.comprehensivestatistics.service;


import com.zhitan.comprehensivestatistics.domain.MonthlyComprehensive;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 月
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface ImonthlyComprehensive {
    /**
     * 全厂综合能耗统计 月
     *
     * @param nodeId
     * @param dataList
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<MonthlyComprehensive> getMonthlyComprehensiveList(String nodeId, List<MonthlyComprehensive> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId);

    /**
     * 全厂综合能耗统计 月
     *
     * @param indexId
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<MonthlyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId);
}
