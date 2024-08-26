package com.dingzhuo.energy.project.plannedOutput.mapper;

import com.dingzhuo.energy.project.plannedOutput.domain.energyMonitoring;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author sys
 * @date 2020-12-17
 */
public interface energyMonitoringMapper
{

    /**
     * 查询列表
     */
    public List<energyMonitoring> selectEnergyMonitoringList(@Param("indexIds") List<Integer> indexIds, @Param("timeCode") String timeCode);

}
