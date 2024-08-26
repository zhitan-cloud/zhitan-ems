package com.dingzhuo.energy.project.workingProcedure.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.dailyWorkingProcedure;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 日
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IdailyWorkingProcedure {
    public List<dailyWorkingProcedure> getdailyWorkingProcedureList(List<String> indexIds, List<dailyWorkingProcedure> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    public List<dailyWorkingProcedure> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
