package com.dingzhuo.energy.project.electricity.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.service.impl.PeriodDataServiceImpl;
import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
import com.dingzhuo.energy.project.electricity.domain.TimePeriodPrice;
import com.dingzhuo.energy.project.electricity.domain.electricityDataItem;
import com.dingzhuo.energy.project.electricity.domain.statistics;
import com.dingzhuo.energy.project.electricity.mapper.statisticsMapper;
import com.dingzhuo.energy.project.electricity.service.IstatisticsService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * electricityPriceService业务层处理
 *
 * @author sys
 * @date 2020-02-17
 */
@Service
public class statisticsServiceImpl implements IstatisticsService
{
    @Autowired
    private statisticsMapper statisticsMapper;
    @Override
    public List<EnergyIndex> getStatisticsList(statistics statistics){
        return statisticsMapper.getStatisticsList(statistics);
    }
    public List<TimePeriodPrice> getElectricityPriceList(statistics statistics){
        return  statisticsMapper.getElectricityPriceList(statistics);
    }

    @Override
    public List<electricityDataItem> getDatasByIndex(List<String> indexIds, Date beginTime,
                                                     Date endTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            return statisticsMapper.getDatasByIndex(indexIds,beginTime, endTime,timeType);
        }
        return Collections.emptyList();
    }
    @Override
    public List<electricityDataItem> getPeriodDatasByIndex(List<String> indexIds, Date beginTime,
                                                     Date endTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            return statisticsMapper.getPeriodDatasByIndex(indexIds, beginTime, endTime, timeType);
        }

        return Collections.emptyList();
    }
    private static class ReBuildTime {
        private Date endTime;
        private DateTime end;

        public ReBuildTime(Date endTime) {
            this.endTime = endTime;
        }
        public DateTime getEnd() {
            return end;
        }

        public statisticsServiceImpl.ReBuildTime invoke() {
            end = new DateTime(endTime);
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:00:00");
            formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
            end = formatter.parseDateTime(end.toString(formatter));
            return this;
        }
    }
}
