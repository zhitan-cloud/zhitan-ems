package com.zhitan.energyMonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.energyMonitor.domain.ElectricLoadEntity;
import com.zhitan.energyMonitor.domain.EnergyUnitToDevice;
import com.zhitan.energyMonitor.domain.vo.ListElectricLoadVO;
import com.zhitan.model.domain.EnergyIndex;

/**
 * @Description:
 * @Author: jeecg-boot
 * @Date: 2022-04-19
 * @Version: V1.0
 */
public interface IElectricLoadService extends IService<ElectricLoadEntity> {

    /**
     * 获取负荷分析数据
     *
     * @param timeType
     * @param timeCode
     * @param energyIndex
     * @return
     */
    ListElectricLoadVO list(String timeType, String timeCode, EnergyIndex energyIndex, EnergyUnitToDevice energyUnitToDevice);
}
