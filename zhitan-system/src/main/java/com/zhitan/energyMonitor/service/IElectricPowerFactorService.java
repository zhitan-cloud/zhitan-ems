package com.zhitan.energyMonitor.service;

import com.zhitan.energyMonitor.domain.vo.ElectricPowerFactorVO;
import com.zhitan.model.domain.EnergyIndex;


/**
 * @Description:
 * @Author: jeecg-boot
 * @Date: 2022-04-19
 * @Version: V1.0
 */
public interface IElectricPowerFactorService {

    /**
     * 获取负荷分析数据
     */
    ElectricPowerFactorVO list(String timeCode, EnergyIndex energyIndex);
}
