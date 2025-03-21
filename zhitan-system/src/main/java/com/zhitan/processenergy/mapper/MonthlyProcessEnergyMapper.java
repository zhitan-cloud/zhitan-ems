package com.zhitan.processenergy.mapper;

import com.zhitan.processenergy.domain.MonthlyProcessEnergy;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *工序能耗 日
 *
 * @author sys
 */
public interface MonthlyProcessEnergyMapper {

    /**
     *
     * @param indexIds
     * @param dataList
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<MonthlyProcessEnergy> getMonthlyProcessEnergy(@Param("indexIds") List<String> indexIds,
                                                               @Param("dataList") List<MonthlyProcessEnergy> dataList,
                                                               @Param("beginTime") Date beginTime,
                                                               @Param("endTime") Date endTime,
                                                               @Param("timeType") String timeType,
                                                               @Param("indexStorageId") String indexStorageId);

    /**
     *
     * @param indexId
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<MonthlyProcessEnergy> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") String timeType,
                                            @Param("indexStorageId") String indexStorageId);
}
