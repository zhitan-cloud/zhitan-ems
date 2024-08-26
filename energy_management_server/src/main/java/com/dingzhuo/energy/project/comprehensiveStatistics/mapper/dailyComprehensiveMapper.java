package com.dingzhuo.energy.project.comprehensiveStatistics.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.dailyComprehensive;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *全厂综合能耗统计 日
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface dailyComprehensiveMapper {
    public List<dailyComprehensive> getdailyComprehensiveList(@Param("nodeId") String nodeId,
                                                              @Param("dataList") List<dailyComprehensive> dataList,
                                                              @Param("beginTime") Date beginTime,
                                                              @Param("endTime") Date endTime,
                                                              @Param("timeType") TimeType timeType,
                                                              @Param("indexStorageId") String indexStorageId);

    List<dailyComprehensive> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") TimeType timeType,
                                            @Param("indexStorageId")  String indexStorageId);

}
