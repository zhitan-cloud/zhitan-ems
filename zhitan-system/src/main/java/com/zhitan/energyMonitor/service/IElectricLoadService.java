package com.zhitan.energyMonitor.service;

import com.zhitan.energyMonitor.domain.vo.ListElectricLoadVO;
import com.zhitan.model.domain.EnergyIndex;

/**
 * @Description:
 * @Author: jeecg-boot
 * @Date: 2022-04-19
 * @Version: V1.0
 */
public interface IElectricLoadService {

    /**
     * 获取负荷分析数据
     */
    ListElectricLoadVO list(String timeType, String timeCode, EnergyIndex energyIndex);
}
