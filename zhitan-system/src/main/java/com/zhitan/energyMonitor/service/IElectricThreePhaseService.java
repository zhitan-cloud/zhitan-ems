package com.zhitan.energyMonitor.service;

import com.zhitan.energyMonitor.domain.vo.ElectricThreePhaseVO;
import com.zhitan.model.domain.EnergyIndex;

import java.util.List;

/**
 * @Description:
 * @Author: jeecg-boot
 * @Date: 2022-04-19
 * @Version: V1.0
 */
public interface IElectricThreePhaseService {

    /**
     * 获取三相不平衡数据
     */
    ElectricThreePhaseVO list(String timeType, String timeCode, List<EnergyIndex> energyIndexList, String requestType, String meterId);
}
