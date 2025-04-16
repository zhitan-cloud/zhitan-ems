package com.zhitan.comprehensivestatistics.mapper;

import com.zhitan.comprehensivestatistics.domain.DailyComprehensive;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *全厂综合能耗统计 日
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface DailyComprehensiveMapper {
    List<DailyComprehensive> getDailyComprehensiveList(@Param("nodeId") String nodeId,
                                                              @Param("dataList") List<DailyComprehensive> dataList,
                                                              @Param("beginTime") Date beginTime,
                                                              @Param("endTime") Date endTime,
                                                              @Param("timeType") String timeType,
                                                              @Param("indexStorageId") String indexStorageId);

    List<DailyComprehensive> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") String timeType,
                                            @Param("indexStorageId")  String indexStorageId);

}
