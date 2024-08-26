package com.dingzhuo.energy.project.comprehensiveStatistics.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.dailyComprehensive;
import com.dingzhuo.energy.project.comprehensiveStatistics.mapper.dailyComprehensiveMapper;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.IdailyComprehensive;
import org.apache.commons.lang3.StringUtils;
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
public class dailyComprehensiveServiceImpl implements IdailyComprehensive {
    @Autowired
    private dailyComprehensiveMapper dailyMapper;

    public List<dailyComprehensive> getdailyComprehensiveList(String nodeId, List<dailyComprehensive> dataList,
                                                              Date beginTime, Date endTime, TimeType timeType, String indexStorageId){

        if (StringUtils.isNotEmpty(nodeId)) {
            return dailyMapper.getdailyComprehensiveList(nodeId, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<dailyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return dailyMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
