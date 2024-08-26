package com.dingzhuo.energy.project.plannedOutput.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.plannedOutput.domain.PlannedOutput;

import java.util.Date;
import java.util.List;

/**
 * plannedOutputService接口
 * 
 * @author sys
 * @date 2020-12-16
 */
public interface IPlannedOutputService {
    /**
     * 查询plannedOutput列表
     * 
     * @param
     * @return plannedOutput集合
     */
    public List<PlannedOutput> selectPlannedOutputList(List<Integer> indexIds, TimeType timeType,Date dataTime,String palnType);
    public List<PlannedOutput> selectPlanList(List<Integer> indexIds);
    /**
     * 新增plannedOutput
     *
     * @return 结果
     */
    //public int insertPlannedOutput(PlannedOutput plannedOutput);
    public void insertPlannedOutput(List<PlannedOutput> datas);
    /**
     * 修改plannedOutput
     * 
     * @param plannedOutput plannedOutput
     * @return 结果
     */
    public int updatePlannedOutput(PlannedOutput plannedOutput);

}
