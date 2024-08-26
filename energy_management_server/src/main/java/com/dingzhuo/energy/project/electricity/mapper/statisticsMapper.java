package com.dingzhuo.energy.project.electricity.mapper;

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
 * electricityPriceMapper接口
 *
 * @author sys
 * @date 2020-02-18
 */
public interface statisticsMapper {
    List<EnergyIndex> getStatisticsList(statistics statistics);
    List<TimePeriodPrice> getElectricityPriceList(statistics statistics);
    public List<electricityDataItem> getDatasByIndex(@Param("indexIds")List<String> indexIds, @Param("beginTime") Date beginTime, @Param("endTime") Date endTime,@Param("timeType")TimeType timeType);

    List<electricityDataItem> getPeriodDatasByIndex(@Param("indexIds") List<String> indexIds,
                                                    @Param("beginTime") Date beginTime,
                                                    @Param("endTime") Date endTime,
                                                    @Param("timeType") TimeType timeType);
}