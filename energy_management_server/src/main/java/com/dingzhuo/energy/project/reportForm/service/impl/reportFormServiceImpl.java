package com.dingzhuo.energy.project.reportForm.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.domain.reportForm;
import com.dingzhuo.energy.project.reportForm.mapper.reportFormMapper;
import com.dingzhuo.energy.project.reportForm.service.IreportFormService;
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
public class reportFormServiceImpl implements IreportFormService {
    @Autowired
    private reportFormMapper reportFormMapper;
    public List<reportForm> selectReportFormList(String code){
        return reportFormMapper.selectReportFormList(code);
    }
    public List<reportForm> selectReportFormListp(String code){
        return reportFormMapper.selectReportFormListp(code);
    }
    public List<reportForm> selectList(String timeDate,String code,String timeYear,TimeType timeType){
        return reportFormMapper.selectList(timeDate,code,timeYear,timeType);
    }
    /*public List<DataItem> getDailyReportList(List<String> indexIds, Date beginTime, Date endTime, TimeType timeType){
        if (indexIds != null && !indexIds.isEmpty()) {
            return reportFormMapper.getDailyReportList(indexIds, beginTime, endTime, timeType);
        }
        return Collections.emptyList();
        return reportFormMapper.getDailyReportList(*//*indexIds, beginTime, endTime, timeType*//*);
    }*/
    public List<dailyReport> getDailyReportList(List<String> indexIds,List<dailyReport> dataList, Date beginTime, Date endTime, TimeType timeType,String indexStorageId){
        return reportFormMapper.getDailyReportList(indexIds,dataList, beginTime, endTime, timeType,indexStorageId);
    }
}
