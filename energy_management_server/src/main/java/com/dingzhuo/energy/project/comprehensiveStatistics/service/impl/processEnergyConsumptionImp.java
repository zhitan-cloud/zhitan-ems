package com.dingzhuo.energy.project.comprehensiveStatistics.service.impl;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.comprehensive;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.comprehensiveStatistics;
import com.dingzhuo.energy.project.comprehensiveStatistics.mapper.comprehensiveStatisticsMapper;
import com.dingzhuo.energy.project.comprehensiveStatistics.mapper.processEnergyConsumptionMapper;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.IComprehensiveStatisticsService;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.processEnergyConsumptionService;
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
public  class processEnergyConsumptionImp implements processEnergyConsumptionService {
    @Autowired
    private processEnergyConsumptionMapper processEnergyConsumption;

}