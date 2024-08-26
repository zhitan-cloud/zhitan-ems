package com.dingzhuo.energy.project.plannedOutput.mapper;

import com.dingzhuo.energy.project.plannedOutput.domain.PlanEnergy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 【请填写功能名称】Mapper接口
 * 
 * @author sys
 * @date 2020-12-17
 */
public interface PlanEnergyMapper 
{

    /**
     * 查询【请填写功能名称】列表
     * 
     * @param
     * @return 【请填写功能名称】集合
     */
    public List<PlanEnergy> selectPlanEnergyList(@Param("indexIds") List<Integer> indexIds,@Param("timeCode") String timeCode);
    public List<PlanEnergy> PlanEnergyList(@Param("indexIds") List<Integer> indexIds,@Param("timeCode") String timeCode, @Param("type") String type);
    void save(@Param("planEnergy") PlanEnergy planEnergy);
}
