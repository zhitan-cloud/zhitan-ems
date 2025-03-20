package com.zhitan.comprehensivestatistics.service.impl;

import com.zhitan.comprehensivestatistics.domain.DailyComprehensive;
import com.zhitan.comprehensivestatistics.mapper.DailyComprehensiveMapper;
import com.zhitan.comprehensivestatistics.service.IDailyComprehensiveService;
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
public class DailyComprehensiveServiceImpl implements IDailyComprehensiveService {
    @Autowired
    private DailyComprehensiveMapper dailyMapper;

    public List<DailyComprehensive> getDailyComprehensiveList(String nodeId, List<DailyComprehensive> dataList,
                                                              Date beginTime, Date endTime, String timeType, String indexStorageId){

        if (StringUtils.isNotEmpty(nodeId)) {
            return dailyMapper.getDailyComprehensiveList(nodeId, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<DailyComprehensive> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return dailyMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
