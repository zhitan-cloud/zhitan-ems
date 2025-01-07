package com.zhitan.comprehensivestatistics.service;


import com.zhitan.common.enums.TimeType;
import com.zhitan.comprehensivestatistics.domain.DailyComprehensive;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 日
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IDailyComprehensiveService {
    public List<DailyComprehensive> getDailyComprehensiveList(String nodeId, List<DailyComprehensive> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<DailyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
