package com.dingzhuo.energy.project.benchmarking.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.benchmarking.mapper.PhaseBenchmarkingMapper;
import com.dingzhuo.energy.project.benchmarking.service.IPhaseBenchmarkingService;

/**
 * 对标Service业务层处理
 * 
 * @author sys
 * @date 2020-12-22
 */
@Service
public class PhaseBenchmarkingServiceImpl implements IPhaseBenchmarkingService 
{
    @Autowired
    private PhaseBenchmarkingMapper phaseBenchmarkingMapper;

    /**
     * 查询对标列表
     * 
     * @param
     * @return 对标
     */
    @Override
    public List<BenchmarkingManagement> selectPhaseBenchmarkingList(String indexId,Date beginTime, Date endTime, TimeType timeType)
    {
        return phaseBenchmarkingMapper.selectPhaseBenchmarkingList(indexId,beginTime,endTime,timeType);
    }
    @Override
    public List<BenchmarkingManagement> selectRealTimeListrealTime(List<String> indexIds, Date dateTime, TimeType timeType)
    {
        if (indexIds.size() !=0) {
            String timeCode = TimeManager.getTimeCode(dateTime, timeType);
            return phaseBenchmarkingMapper.selectRealTimeListrealTime(indexIds,timeCode);
        }
        return Collections.emptyList();

    }
}
