package com.zhitan.energyMonitor.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.energyMonitor.domain.EnergyUnitToDevice;
import com.zhitan.energyMonitor.domain.vo.ListElectricityMeterVO;
import com.zhitan.energyMonitor.domain.vo.UnitToDeviceRelationVO;

import java.util.List;

/**
 * @Description: 用能单元关联的平台模板中仪表的界面逻辑关系 不含有 采集、计算信息
 * @Author: jeecg-boot
 * @Date:   2022-01-26
 * @Version: V1.0
 */
public interface IEnergyUnitToDeviceService extends IService<EnergyUnitToDevice> {

    /**
     * 根据用能单元ID获取租户下有效的仪表集合
     * @param unitId
     * @return
     */
    List<EnergyUnitToDevice> queryTenantUnitDeviceByUnitId(String unitId);

    /**
     * 根据用能单元ID获取租户下所有有效的实体表集合
     * @param unitId
     * @return
     */
    List<EnergyUnitToDevice> queryTenantEntityMeterByUnitId(String unitId);

    /**
     * 根据用能单元id获取租户下有效的仪表id集合
     *
     * @param unitId 用能单元id
     * @return 仪表id集合
     */
    List<String> getEnergyUnitDeviceIdByUnitId(String unitId);

    /**
     * 根据用能单元id集合获取租户下有效的仪表id集合
     *
     * @param unitIds 用能单元id
     */
    List<String> getEnergyUnitDeviceIdByUnitIds(List<String> unitIds);

    /**
     * 根据用能单元id集合+能源类型获取租户下有效的仪表id集合
     *
     * @param unitIds    用能单元id集合
     * @return
     */
    List<String> getDeviceIdByUnitIds(List<String> unitIds);

    /**
     * 更新能单元id获取租户下有效的仪表id集合
     *
     * @param unitId 用能单元id
     * @return 仪表id集合
     */
    List<String> updateEnergyUnitDeviceIdByUnitId(String unitId);

    /**
     * 删除能单元id获取租户下有效的仪表id集合
     * @param unitId    用能单元id
     * @return  仪表id集合
     */
    void deleteEnergyUnitDeviceIdByUnitId(String unitId);

    /**
     * 根据用能单元ID获取租户下有效的电表集合
     * @param unitId
     * @return
     */
    List<ListElectricityMeterVO> listElectricityMeter(String unitId);

    /**
     * 获取租户下有效的表集合
     * @return
     */
    List<EnergyUnitToDevice> listAllMeter();

    /**
     * 根据计量器具id和用能单元id查询表关系
     *
     * @param unitId 用能单元id
     * @param id     id
     * @return EnergyUnitToDevice实体
     */
    EnergyUnitToDevice getEnergyUnitToDeviceById(String unitId, String id);

    /**
     * 根据计量器具id集合查询与用能的关系
     *
     * @param unitIds
     * @return
     */
    List<UnitToDeviceRelationVO> listDeviceByUnitIds(List<String> unitIds);
}
