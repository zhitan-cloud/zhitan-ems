package com.dingzhuo.energy.project.plannedOutput.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.plannedOutput.domain.energyMonitoring;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author sys
 * @date 2020-12-17
 */
public interface energyMonitoringService
{
    /**
     * 查询列表
     *
     * @param
     * @return
     */
    public List<energyMonitoring> selectEnergyMonitoringList(List<Integer> indexIds, TimeType timeType, Date dataTime);

}
