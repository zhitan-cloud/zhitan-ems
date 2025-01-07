package com.zhitan.meter.mapper;


import com.zhitan.meter.domain.MeterConfig;
import com.zhitan.meter.domain.MeterImplements;
import com.zhitan.meter.domain.MeterParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @InterfaceName: MeterImplementMapper
 * @Author: Yarry
 * @CreateTime: 2024-09-20 16-23-18
 * @Description: TODO
 * @Version: 1.0
 * @Since: JDK1.8
 */
public interface MetersMapper {

    /**
     * 获取所有计量表数据
     * @param meterType
     * @return
     */
    List<MeterImplements> listMeterData(@Param("meterType") String meterType);

    /**
     * 获取点位配置数据
     * @param meterType
     * @param indexType
     * @return
     */
    List<MeterParam> listConfigurationData(@Param("meterType") String meterType, @Param("indexType") String indexType);

    /**
     * 获取点位配置数据
     * @param indexType
     * @return
     */
    List<MeterConfig> listConfigData(@Param("indexType") String indexType);
}
