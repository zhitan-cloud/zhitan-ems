package com.dingzhuo.energy.project.comprehensiveStatistics.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.monthlyComprehensive;
import com.dingzhuo.energy.project.comprehensiveStatistics.mapper.monthlyComprehensiveMapper;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.ImonthlyComprehensive;
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
public class monthlyComprehensiveServiceImpl implements ImonthlyComprehensive {
    @Autowired
    private monthlyComprehensiveMapper monthMapper;

    public List<monthlyComprehensive> getMonthlyComprehensiveList(String nodeId, List<monthlyComprehensive> dataList,
                                                                  Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (StringUtils.isNotEmpty(nodeId)) {
            return monthMapper.getMonthlyComprehensiveList(nodeId, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<monthlyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return monthMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
