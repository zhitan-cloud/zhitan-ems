package com.dingzhuo.energy.project.workingProcedure.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.monthlyWorkingProcedure;


import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 月
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface ImonthlyWorkingProcedure {
    public List<monthlyWorkingProcedure> getMonthlyWorkingProcedureList(List<String> indexIds, List<monthlyWorkingProcedure> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<monthlyWorkingProcedure> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
