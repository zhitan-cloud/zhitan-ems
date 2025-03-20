package com.zhitan.comprehensivestatistics.mapper;

import com.zhitan.comprehensivestatistics.domain.MonthlyComprehensive;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 月
 *
 * @author sys
 * @date 2020-03-25
 */
public interface MonthlyComprehensiveMapper {
    /**
     * 获取综合指标分析（月）列表
     *
     * @param nodeId
     * @param dataList
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    public List<MonthlyComprehensive> getMonthlyComprehensiveList(@Param("nodeId") String nodeId,
                                                                  @Param("dataList") List<MonthlyComprehensive> dataList,
                                                                  @Param("beginTime") Date beginTime,
                                                                  @Param("endTime") Date endTime,
                                                                  @Param("timeType") String timeType,
                                                                  @Param("indexStorageId") String indexStorageId);

    /**
     * 获取综合指标分析图表（月）数据
     *
     * @param indexId
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<MonthlyComprehensive> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") String timeType,
                                            @Param("indexStorageId") String indexStorageId);

}
