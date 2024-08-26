package com.dingzhuo.energy.project.reportForm.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.domain.reportForm;

import java.util.Date;
import java.util.List;

/**
 * 阶段数据录入接口
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IdailyReportService {
    public List<dailyReport> getDailyReportList(List<String> indexIds,List<dailyReport> dataList, Date beginTime,
                                                Date endTime, TimeType timeType,String indexStorageId);

    public List<dailyReport> listDailyReportList(String nodeId,List<dailyReport> dataList, Date beginTime,
                                                 Date endTime, TimeType timeType,String indexStorageId);
}
