package com.zhitan.energyMonitor.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.energyMonitor.domain.EnergyUnitToDevice;
import com.zhitan.energyMonitor.domain.vo.EnergyCalculateCalcTV;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description: 用能单元关联的平台模板中仪表的界面逻辑关系 不含有 采集、计算信息
 * @Author: jeecg-boot
 * @Date: 2022-01-26
 * @Version: V1.0
 */
public interface EnergyUnitToDeviceMapper extends BaseMapper<EnergyUnitToDevice> {

    /**
     * 根据计算点位主键和用能单元主键 查询 使用的 采集点及仪表信息
     *
     * @param calculateIndexId
     * @param energyUnitId
     * @return
     */
    public List<EnergyCalculateCalcTV> getCalculateDevices(@Param("calculateIndexId") String calculateIndexId,
                                                           @Param("energyUnitId") String energyUnitId,
                                                           @Param("tenantId") String tenantId);

    /**
     * 中科-根据租户id集合查询计量器具id
     *
     * @param unitIds
     * @return
     */
    @InterceptorIgnore(tenantLine = "true")
    List<String> listDeviceByTenantIds(@Param("unitIds") List<String> unitIds);
}
