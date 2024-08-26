package com.dingzhuo.energy.project.workingProcedure.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.yearWorkingProcedure;
import com.dingzhuo.energy.project.workingProcedure.mapper.yearWorkingProcedureMapper;
import com.dingzhuo.energy.project.workingProcedure.service.IyearWorkingProcedure;
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
public class yearWorkingProcedureServiceImpl implements IyearWorkingProcedure {
    @Autowired
    private yearWorkingProcedureMapper yearMapper;

    public List<yearWorkingProcedure> getYearWorkingProcedureList(List<String> indexIds, List<yearWorkingProcedure> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return yearMapper.getYearWorkingProcedureList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<yearWorkingProcedure> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return yearMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
