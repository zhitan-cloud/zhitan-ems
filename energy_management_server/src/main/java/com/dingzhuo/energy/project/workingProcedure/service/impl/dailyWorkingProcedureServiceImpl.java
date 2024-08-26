package com.dingzhuo.energy.project.workingProcedure.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.dailyWorkingProcedure;
import com.dingzhuo.energy.project.workingProcedure.mapper.dailyWorkingProcedureMapper;
import com.dingzhuo.energy.project.workingProcedure.service.IdailyWorkingProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 业务层处理
 * 
 * @author sys
 * @date 2020-03-25
 */
@Service
public class dailyWorkingProcedureServiceImpl implements IdailyWorkingProcedure {
    @Autowired
    private dailyWorkingProcedureMapper dailyMapper;

    public List<dailyWorkingProcedure> getdailyWorkingProcedureList(List<String> indexIds, List<dailyWorkingProcedure> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return dailyMapper.getdailyWorkingProcedureList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<dailyWorkingProcedure> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return dailyMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
