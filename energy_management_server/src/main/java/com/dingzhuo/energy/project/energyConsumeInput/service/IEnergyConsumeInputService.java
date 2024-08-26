package com.dingzhuo.energy.project.energyConsumeInput.service;


import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;
import com.dingzhuo.energy.project.energyConsumeInput.domain.EnergyConsumeInput;
import com.dingzhuo.energy.project.energyConsumeInput.domain.SaveEnergyConsumeInputEntity;

import java.util.Date;
import java.util.List;

/**
 * 能流分析Service接口
 *
 * @author zhaow
 * @date 2020-12-26
 */
public interface IEnergyConsumeInputService {
    /**
     * 查询  能耗手动录入 数据列表
     *
     * @return 能耗手动录入    功能集合
     */
    public List<EnergyConsumeInput> selectEnergyConsumeInputList(String nodeId, String timeCode, String timeType);

    /**
     * 根据日期和指标id删除数据
     *
     * @param indexIds 指标id列表
     * @param timeCode 月份值
     * @return
     */
    public int deleteEnergyConsumeInputByIds(List<String> indexIds, String timeCode);

    /**
     * 保存能耗手动输入数据
     *
     * @param models
     * @return
     */
    public int saveEnergyConsumeInputList(List<SaveEnergyConsumeInputEntity> models);

    /**
     * 重算相关指标
     * @param indexIds
     * @param timeType
     * @param authorization
     * @param dataTime
     * @return
     */
    public boolean reCalcIndexIdByIds(List<String> indexIds, String timeType, String authorization, Date dataTime);
}
