package com.dingzhuo.energy.project.workingProcedure.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.yearWorkingProcedure;

import java.util.Date;
import java.util.List;

/**
 * 工序能耗统计 年
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IyearWorkingProcedure {
    public List<yearWorkingProcedure> getYearWorkingProcedureList(List<String> indexIds, List<yearWorkingProcedure> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<yearWorkingProcedure> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
