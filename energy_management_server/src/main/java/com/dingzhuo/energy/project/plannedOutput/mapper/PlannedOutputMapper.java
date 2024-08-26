package com.dingzhuo.energy.project.plannedOutput.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.plannedOutput.domain.PlannedOutput;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * plannedOutputMapper接口
 * 
 * @author sys
 * @date 2020-12-16
 */
public interface PlannedOutputMapper 
{
    /**
     * 查询plannedOutput列表
     * 
     * @param
     * @return plannedOutput集合
     */
    public List<PlannedOutput> selectPlannedOutputList (@Param("indexIds") List<Integer> indexIds,
                                                        @Param("timeCode") String timeCode, @Param("palnType") String palnType);
    public List<PlannedOutput> selectPlanList (@Param("indexIds") List<Integer> indexIds);
    /**
     * 新增plannedOutput
     * 
     * @param plannedOutput plannedOutput
     * @return 结果
     */
    public int insertPlannedOutput(PlannedOutput plannedOutput);

    /**
     * 修改plannedOutput
     * 
     * @param plannedOutput plannedOutput
     * @return 结果
     */
    public int updatePlannedOutput(PlannedOutput plannedOutput);
    void save(@Param("plannedOutput") PlannedOutput plannedOutput);
    void saveDataList(@Param("PlannedOutput") List<PlannedOutput> datas);

}
