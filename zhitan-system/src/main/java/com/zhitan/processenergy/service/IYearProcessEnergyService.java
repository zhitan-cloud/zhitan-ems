package com.zhitan.processenergy.service;

import com.zhitan.common.utils.TypeTime;
import com.zhitan.processenergy.domain.YearProcessEnergy;
import com.zhitan.realtimedata.domain.dto.DataItemQueryDTO;

import java.util.Date;
import java.util.List;

/**
 *工序能耗 日
 *
 * @author sys
 * @date 2021-01-11
 */
public interface IYearProcessEnergyService {

    List<YearProcessEnergy> getYearProcessEnergy(List<String> indexIds, List<TypeTime> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId);

    /**
     *
     * @param queryDto
     * @return
     */
    List<YearProcessEnergy> getListChart(DataItemQueryDTO queryDto);
}
