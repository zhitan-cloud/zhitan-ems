package com.dingzhuo.energy.project.reportForm.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.reportForm.domain.annualReport;

import java.util.Date;
import java.util.List;

/**
 * 阶段数据录入接口
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IannualReportService {
    public List<annualReport> getannualReportList(List<String> indexIds, List<annualReport> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
