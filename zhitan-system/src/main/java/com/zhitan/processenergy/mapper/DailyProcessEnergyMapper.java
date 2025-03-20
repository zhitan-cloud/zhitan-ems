package com.zhitan.processenergy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.processenergy.domain.DailyProcessEnergy;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *工序能耗 日
 *
 * @author sys
 */
public interface DailyProcessEnergyMapper extends BaseMapper<DailyProcessEnergy> {

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
    List<DailyProcessEnergy> getDailyProcessEnergyList(@Param("indexIds") List<String> indexIds,
                                                              @Param("dataList") List<DailyProcessEnergy> dataList,
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
    List<DailyProcessEnergy> getListChart(@Param("indexId") String indexId,
                                          @Param("beginTime") Date beginTime,
                                          @Param("endTime") Date endTime,
                                          @Param("timeType") String timeType,
                                          @Param("indexStorageId")  String indexStorageId);
}
