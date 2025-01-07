package com.zhitan.meter.services;

import com.zhitan.meter.domain.MeterConfig;
import com.zhitan.meter.domain.MeterImplements;
import com.zhitan.meter.domain.MeterParam;

import java.util.List;

/**
 * @InterfaceName: IMeterImplementService
 * @Author: Yarry
 * @CreateTime: 2024-09-20 16-21-54
 * @Description: TODO
 * @Version: 1.0
 * @Since: JDK1.8
 */
public interface IMetersService {

    /**
     * 获取所有计量表数据
     * @return
     */
    List<MeterImplements> listMeterData(String meterType);

    /**
     * 获取点位配置数据
     * @param meterType
     * @param indexType
     * @return
     */
    List<MeterParam> listConfigurationData(String meterType, String indexType);

    /**
     * 获取点位code配置数据
     *
     * @param indexType
     * @return
     */
    List<MeterConfig> listConfigData(String indexType);
}
