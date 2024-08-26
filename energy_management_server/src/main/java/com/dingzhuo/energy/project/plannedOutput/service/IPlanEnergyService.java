package com.dingzhuo.energy.project.plannedOutput.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.plannedOutput.domain.PlanEnergy;
import com.dingzhuo.energy.project.plannedOutput.domain.PlannedOutput;

import java.util.Date;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 * 
 * @author sys
 * @date 2020-12-17
 */
public interface IPlanEnergyService 
{
    /**
     * 查询列表
     * 
     * @param
     * @return
     */
    public List<PlanEnergy> selectPlanEnergyList(List<Integer> indexIds,TimeType timeType, Date dataTime);
    public List<PlanEnergy> PlanEnergyList(List<Integer> indexIds, TimeType timeType, Date dataTime,String type);
    /**
     * 新增修改
     */
    public void insertPlanEnergy(List<PlanEnergy> datas);
}
