package com.zhitan.processenergy.mapper;

import com.zhitan.common.utils.TypeTime;
import com.zhitan.processenergy.domain.YearProcessEnergy;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *工序能耗 日
 *
 * @author sys
 */
public interface YearProcessEnergyMapper {

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
    List<YearProcessEnergy> getYearProcessEnergy(@Param("indexIds") List<String> indexIds,
                                                           @Param("dataList") List<TypeTime> dataList,
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
     * @return
     */
    List<YearProcessEnergy> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") String timeType);
}
