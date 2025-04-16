package com.zhitan.comprehensivestatistics.service;


import com.zhitan.comprehensivestatistics.domain.YearComperhensive;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 年
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IyearComprehensive {
    /**
     * 获取综合指标分析（年）列表
     *
     * @param nodeId
     * @param dataList
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<YearComperhensive> getYearComprehensiveList(String nodeId, List<YearComperhensive> dataList,
                                                            Date beginTime, Date endTime, String timeType, String indexStorageId);

    /**
     * 获取综合指标分析（年）图表数据
     *
     * @param indexId
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<YearComperhensive> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId);
}
