package com.dingzhuo.energy.project.benchmarking.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 对标Service接口
 * 
 * @author sys
 * @date 2020-12-22
 */
public interface IPhaseBenchmarkingService 
{

    /**
     * 查询对标列表
     * 
     * @param
     * @return 对标集合
     */
    public List<BenchmarkingManagement> selectPhaseBenchmarkingList(String indexId, Date beginTime,Date endTime, TimeType timeType);
    public List<BenchmarkingManagement> selectRealTimeListrealTime(List<String> indexIds, Date dateTime, TimeType timeType);

}
