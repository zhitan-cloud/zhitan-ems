package com.dingzhuo.energy.project.electricity.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
import com.dingzhuo.energy.project.electricity.domain.TimePeriodPrice;
import com.dingzhuo.energy.project.electricity.domain.electricityDataItem;
import com.dingzhuo.energy.project.electricity.domain.statistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 峰平谷接口
 *
 * @author sys
 * @date 2020-02-18
 */
public interface IstatisticsService {
    List<EnergyIndex> getStatisticsList(statistics statistics);
    List<TimePeriodPrice> getElectricityPriceList(statistics statistics);
    List<electricityDataItem> getDatasByIndex(List<String> indexIds, Date beginTime,Date endTime,TimeType timeType);
    /**
     * 获取指标某一时刻的数据.
     *
     * @param indexIds  计算指标存储集合
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @param timeType  周期类型
     */
    List<electricityDataItem> getPeriodDatasByIndex(List<String> indexIds,
                                              Date beginTime,
                                              Date endTime,
                                              TimeType timeType);
}