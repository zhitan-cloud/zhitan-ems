package com.zhitan.comprehensivestatistics.service.impl;

import com.zhitan.basicdata.domain.FacilityArchives;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.time.TimeManager;
import com.zhitan.comprehensivestatistics.domain.ComprehensiveStatistics;
import com.zhitan.comprehensivestatistics.mapper.ComprehensiveStatisticsMapper;
import com.zhitan.comprehensivestatistics.service.IComprehensiveStatisticsService;
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
public  class ComprehensiveStatisticsImpl implements IComprehensiveStatisticsService {
    @Autowired
    private ComprehensiveStatisticsMapper statisticsMapperpper;
    @Override
    public List<FacilityArchives> getFacilityArchives() {
        return statisticsMapperpper.getFacilityArchives();
    }

    @Override
    public List<ComprehensiveStatistics> getDatasByList(List<String> indexIds, Date beginTime, Date endTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            return statisticsMapperpper.getDatasByList(indexIds, beginTime,endTime,timeType);
        }
        return Collections.emptyList();
    }
    @Override
    public List<ComprehensiveStatistics> getDatasByIndex(List<String> indexIds, Date dataTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return statisticsMapperpper.getDatasByIndex(indexIds, timeCode);
        }
        return Collections.emptyList();
    }
    @Override
    public List<ComprehensiveStatistics> getDatasIndex(List<String> indexIds, Date dataTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return statisticsMapperpper.getDatasIndex(indexIds, timeCode);
        }
        return Collections.emptyList();
    }
    @Override
    public List<ComprehensiveStatistics> getEnergyByIndex(String indexType) {
            return statisticsMapperpper.getEnergyByIndex(indexType);
    }

}