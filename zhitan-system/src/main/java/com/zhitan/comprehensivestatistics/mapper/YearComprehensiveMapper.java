package com.zhitan.comprehensivestatistics.mapper;

import com.zhitan.comprehensivestatistics.domain.YearComperhensive;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 全厂综合能耗统计 年
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface YearComprehensiveMapper {
    /**
     * 获取年数据
     *
     * @param nodeId
     * @param dataList
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    public List<YearComperhensive> getYearComprehensiveList(@Param("nodeId") String nodeId,
                                                            @Param("dataList") List<YearComperhensive> dataList,
                                                            @Param("beginTime") Date beginTime,
                                                            @Param("endTime") Date endTime,
                                                            @Param("timeType") String timeType,
                                                            @Param("indexStorageId") String indexStorageId);

    /**
     * 获取年数据-chart
     *
     * @param indexId
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<YearComperhensive> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") String timeType,
                                            @Param("indexStorageId") String indexStorageId);

}
