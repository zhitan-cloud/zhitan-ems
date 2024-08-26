package com.dingzhuo.energy.project.benchmarking.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 对标Mapper接口
 * 
 * @author sys
 * @date 2020-12-22
 */
public interface PhaseBenchmarkingMapper 
{
    /**
     * 查询对标列表
     * 
     * @param
     * @return 对标集合
     */
    public List<BenchmarkingManagement> selectPhaseBenchmarkingList(@Param("indexId") String indexIds,@Param("beginTime") Date beginTime,
                                                                    @Param("endTime") Date endTime,@Param("timeType") TimeType timeType);
    public List<BenchmarkingManagement> selectRealTimeListrealTime(@Param("indexIds") List<String> indexIds, @Param("timeCode") String timeCode);

}
