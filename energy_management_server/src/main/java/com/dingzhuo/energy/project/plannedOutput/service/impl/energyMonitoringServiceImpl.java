package com.dingzhuo.energy.project.plannedOutput.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.plannedOutput.domain.energyMonitoring;
import com.dingzhuo.energy.project.plannedOutput.mapper.energyMonitoringMapper;
import com.dingzhuo.energy.project.plannedOutput.service.energyMonitoringService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service业务层处理
 * 
 * @author sys
 * @date 2020-12-17
 */
@Service
public class energyMonitoringServiceImpl implements energyMonitoringService
{
    private Logger logger = LogManager.getLogger(energyMonitoringServiceImpl.class);
    @Autowired
    private energyMonitoringMapper energyMonitoringMapper;

    /**
     * 查询列表
     */
    @Override
    public List<energyMonitoring> selectEnergyMonitoringList(List<Integer> indexIds, TimeType timeType, Date dataTime)
    {
        if (indexIds.size() !=0) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return energyMonitoringMapper.selectEnergyMonitoringList(indexIds,timeCode);
        }
        return Collections.emptyList();
    }
}
