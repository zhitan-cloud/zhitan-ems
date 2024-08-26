package com.dingzhuo.energy.project.reportForm.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.domain.monthlyReport;
import com.dingzhuo.energy.project.reportForm.mapper.monthlyReportMapper;
import com.dingzhuo.energy.project.reportForm.mapper.reportFormMapper;
import com.dingzhuo.energy.project.reportForm.service.ImonthlyReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * stagseDataEntryService业务层处理
 * 
 * @author sys
 * @date 2020-03-25
 */
@Service
public class monthlyReportServiceImpl implements ImonthlyReportService {
    @Autowired
    private monthlyReportMapper monthlyReportMapper;

    public List<monthlyReport> getMonthlyReporList(List<String> indexIds,List<monthlyReport> dataList, Date beginTime, Date endTime, TimeType timeType,String indexStorageId){
        return monthlyReportMapper.getmonthlyReportList(indexIds,dataList, beginTime, endTime, timeType,indexStorageId);
    }

}
