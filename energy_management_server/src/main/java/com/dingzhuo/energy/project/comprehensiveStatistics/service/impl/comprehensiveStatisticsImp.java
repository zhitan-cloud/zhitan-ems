package com.dingzhuo.energy.project.comprehensiveStatistics.service.impl;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.comprehensiveStatistics;
import com.dingzhuo.energy.project.comprehensiveStatistics.mapper.comprehensiveStatisticsMapper;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.IComprehensiveStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-07
 */
@Service
public  class comprehensiveStatisticsImp implements IComprehensiveStatisticsService{
    @Autowired
    private comprehensiveStatisticsMapper statisticsMapperpper;
    @Override
    public List<FacilityArchives> getFacilityArchives() {
        return statisticsMapperpper.getFacilityArchives();
    }

    @Override
    public List<comprehensiveStatistics> getDatasByList(List<String> indexIds, Date beginTime, Date endTime,TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            return statisticsMapperpper.getDatasByList(indexIds, beginTime,endTime,timeType);
        }
        return Collections.emptyList();
    }
    @Override
    public List<comprehensiveStatistics> getDatasByIndex(List<String> indexIds, Date dataTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return statisticsMapperpper.getDatasByIndex(indexIds, timeCode);
        }
        return Collections.emptyList();
    }
    @Override
    public List<comprehensiveStatistics> getDatasIndex(List<String> indexIds, Date dataTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return statisticsMapperpper.getDatasIndex(indexIds, timeCode);
        }
        return Collections.emptyList();
    }
    @Override
    public List<comprehensiveStatistics> getEnergyByIndex(String indexType) {
            return statisticsMapperpper.getEnergyByIndex(indexType);

    }

}