package com.zhitan.comprehensivestatistics.service.impl;

import com.zhitan.comprehensivestatistics.domain.MonthlyComprehensive;
import com.zhitan.comprehensivestatistics.mapper.MonthlyComprehensiveMapper;
import com.zhitan.comprehensivestatistics.service.ImonthlyComprehensive;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class MonthlyComprehensiveServiceImpl implements ImonthlyComprehensive {
    @Resource
    private MonthlyComprehensiveMapper monthMapper;

    public List<MonthlyComprehensive> getMonthlyComprehensiveList(String nodeId, List<MonthlyComprehensive> dataList,
                                                                  Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (StringUtils.isNotEmpty(nodeId)) {
            return monthMapper.getMonthlyComprehensiveList(nodeId, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<MonthlyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return monthMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
