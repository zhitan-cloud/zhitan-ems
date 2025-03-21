package com.zhitan.processenergy.service;

import com.zhitan.processenergy.domain.DailyProcessEnergy;

import java.util.Date;
import java.util.List;

/**
 *工序能耗 日
 *
 * @author sys
 * @date 2021-01-11
 */
public interface IDailyProcessEnergyService {

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
    List<DailyProcessEnergy> getDailyProcessEnergyList(List<String> indexIds, List<DailyProcessEnergy> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId);

    /**
     *
     * @param indexId
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexStorageId
     * @return
     */
    List<DailyProcessEnergy> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId);
}
