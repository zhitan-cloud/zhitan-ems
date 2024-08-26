package com.dingzhuo.energy.project.reportForm.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.reportForm.domain.annualReport;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.mapper.annualReportMapper;
import com.dingzhuo.energy.project.reportForm.mapper.reportFormMapper;
import com.dingzhuo.energy.project.reportForm.service.IannualReportService;
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
public class AnnualReportFormServiceImpl implements IannualReportService {
    @Autowired
    private annualReportMapper annualReportMapper;

    @Override
    public List<annualReport> getannualReportList(List<String> indexIds, List<annualReport> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId) {
        return annualReportMapper.getannualReportList(indexIds,dataList, beginTime, endTime, timeType,indexStorageId);
    }
}
