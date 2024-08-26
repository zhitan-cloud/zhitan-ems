package com.dingzhuo.energy.project.workingProcedure.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.monthlyWorkingProcedure;
import com.dingzhuo.energy.project.workingProcedure.mapper.monthlyWorkingProcedureMapper;
import com.dingzhuo.energy.project.workingProcedure.service.ImonthlyWorkingProcedure;
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
public class monthlyWorkingProcedureServiceImpl implements ImonthlyWorkingProcedure {
    @Autowired
    private monthlyWorkingProcedureMapper monthMapper;

    public List<monthlyWorkingProcedure> getMonthlyWorkingProcedureList(List<String> indexIds, List<monthlyWorkingProcedure> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return monthMapper.getMonthlyWorkingProcedureList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<monthlyWorkingProcedure> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return monthMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
