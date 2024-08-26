package com.dingzhuo.energy.project.energyConsumeInput.mapper;


import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;
import com.dingzhuo.energy.project.energyConsumeInput.domain.EnergyConsumeInput;
import com.dingzhuo.energy.project.energyConsumeInput.domain.SaveEnergyConsumeInputEntity;

import java.util.List;

/**
 * 能流分析  Mapper接口
 *
 * @author yxw
 * @date 2020-12-26
 */
public interface EnergyConsumeInputMapper {

    /**
     * 查询  能耗手动录入 数据列表
     *
     * @param nodeId
     * @param timeCode
     * @param timeType
     * @return
     */
    List<EnergyConsumeInput> selectEnergyConsumeInputList(String nodeId, String timeCode, String timeType);

    /**
     * 根据日期和指标id删除数据
     *
     * @param indexIds 指标id列表
     * @param timeCode 月份值
     * @return
     */
    int deleteEnergyConsumeInputByIds(List<String> indexIds, String timeCode);

    /**
     * 保存能耗手动输入数据
     *
     * @param models
     * @return
     */
    int saveEnergyConsumeInputList(List<SaveEnergyConsumeInputEntity> models);

    /**
     * 查询要重算的indexId列表
     *
     * @param indexIds
     * @return
     */
    List<EnergyConsumeInput> queryReCalcIndexIdByIds(List<String> indexIds);
}
