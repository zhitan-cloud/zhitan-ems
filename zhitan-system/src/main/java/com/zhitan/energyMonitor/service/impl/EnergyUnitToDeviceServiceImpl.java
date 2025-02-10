package com.zhitan.energyMonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.energyMonitor.domain.EnergyUnitToDevice;
import com.zhitan.energyMonitor.domain.vo.ListElectricityMeterVO;
import com.zhitan.energyMonitor.domain.vo.UnitToDeviceRelationVO;
import com.zhitan.energyMonitor.mapper.EnergyUnitToDeviceMapper;
import com.zhitan.energyMonitor.service.IEnergyUnitToDeviceService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 用能单元关联的平台模板中仪表的界面逻辑关系 不含有 采集、计算信息
 * @Author: jeecg-boot
 * @Date: 2022-01-26
 * @Version: V1.0
 */
@Service
public class EnergyUnitToDeviceServiceImpl extends ServiceImpl<EnergyUnitToDeviceMapper, EnergyUnitToDevice>
        implements IEnergyUnitToDeviceService {


//    @Autowired
//    private IMeasuringInstrumentsService meterService;


    /**
     * 根据用能单元id获取租户下的其下的仪表列表
     *
     * @param unitId
     * @return
     */
    @Override
    public List<EnergyUnitToDevice> queryTenantUnitDeviceByUnitId(String unitId) {

        QueryWrapper<EnergyUnitToDevice> queryWrapper = new QueryWrapper<EnergyUnitToDevice>();
//                .eq(TableColumnConst.TABLE_COLUMN_ENERGY_UNIT_ID, unitId);
        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 根据用能单元ID获取租户下所有有效的实体表集合
     *
     * @param unitId
     * @return
     */
    @Override
    public List<EnergyUnitToDevice> queryTenantEntityMeterByUnitId(String unitId) {

        List<EnergyUnitToDevice> unitToDeviceList = baseMapper.selectList(Wrappers.<EnergyUnitToDevice>lambdaQuery()
                .eq(EnergyUnitToDevice::getEnergyUnitId, unitId)
        );
//        if (CollectionUtils.isNotEmpty(unitToDeviceList)) {
//            List<String> meterIds = unitToDeviceList.stream().map(EnergyUnitToDevice::getId).collect(Collectors.toList());
//            // 过滤出实体表
//            Integer applianceType = EquipmentInformationEnum.MEASURING_INSTRUMENT_TYPE.ENTITY.getValue();
//            List<Meter> meterList = meterService.list(Wrappers.<Meter>lambdaQuery()
//                    .select(Meter::getId).in(Meter::getId, meterIds)
//                    .eq(Meter::getApplianceType, applianceType)
//            );
//            List<String> newMeterIds = meterList.stream().map(Meter::getId).collect(Collectors.toList());
//            unitToDeviceList = unitToDeviceList.stream().filter(li -> newMeterIds.contains(li.getId())).collect(Collectors.toList());
//        }
        return unitToDeviceList;
    }

    /**
     * 根据用能单元id获取租户下有效的仪表id集合
     *
     * @param unitId 用能单元id
     * @return 仪表id集合
     */
    @Override
//    @Cacheable(value = CacheConstant.ENERGY_UNIT_DEVICE_ID_CACHE, key = "#unitId", unless = "#result == null")
    public List<String> getEnergyUnitDeviceIdByUnitId(String unitId) {

        if (StringUtils.isEmpty(unitId)) {
            return Collections.emptyList();
        }
        List<EnergyUnitToDevice> energyUnitToDevices = baseMapper.selectList(Wrappers.<EnergyUnitToDevice>lambdaQuery()
                .select(EnergyUnitToDevice::getId).eq(EnergyUnitToDevice::getEnergyUnitId, unitId)
        );
        if (CollectionUtils.isEmpty(energyUnitToDevices)) {
            return Collections.emptyList();
        }
        return energyUnitToDevices.stream().map(EnergyUnitToDevice::getId).collect(Collectors.toList());
    }

    /**
     * 根据用能单元id集合获取租户下有效的仪表id集合
     *
     * @param unitIds 用能单元id
     */
    @Override
    public List<String> getEnergyUnitDeviceIdByUnitIds(List<String> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return Collections.emptyList();
        }
        List<String> deviceIdList = new ArrayList<>();
        for (String unitId : unitIds) {
            List<String> unitDeviceIdByUnitId = this.getEnergyUnitDeviceIdByUnitId(unitId);
            if (CollectionUtils.isNotEmpty(unitDeviceIdByUnitId)) {
                deviceIdList.addAll(unitDeviceIdByUnitId);
            }
        }
        return deviceIdList;
    }

    /**
     * 根据用能单元id集合+能源类型获取租户下有效的仪表id集合
     *
     * @param unitIds 用能单元id集合
     * @return
     */
    @Override
    public List<String> getDeviceIdByUnitIds(List<String> unitIds) {
        if (CollectionUtils.isEmpty(unitIds)) {
            return Collections.emptyList();
        }

        return baseMapper.listDeviceByTenantIds(unitIds);
    }

    /**
     * 更新能单元id获取租户下有效的仪表id集合
     *
     * @param unitId 用能单元id
     * @return 仪表id集合
     */
    @Override
//    @CachePut(value = CacheConstant.ENERGY_UNIT_DEVICE_ID_CACHE, key = "#unitId", unless = "#result == null")
    public List<String> updateEnergyUnitDeviceIdByUnitId(String unitId) {

        if (StringUtils.isEmpty(unitId)) {
            return Collections.emptyList();
        }
        List<EnergyUnitToDevice> energyUnitToDevices = baseMapper.selectList(Wrappers.<EnergyUnitToDevice>lambdaQuery()
                .select(EnergyUnitToDevice::getId).eq(EnergyUnitToDevice::getEnergyUnitId, unitId)
        );
        if (CollectionUtils.isEmpty(energyUnitToDevices)) {
            return Collections.emptyList();
        }
        return energyUnitToDevices.stream().map(EnergyUnitToDevice::getId).collect(Collectors.toList());
    }

    /**
     * 删除能单元id获取租户下有效的仪表id集合
     *
     * @param unitId 用能单元id
     */
    @Override
//    @CacheEvict(value = CacheConstant.ENERGY_UNIT_DEVICE_ID_CACHE, key = "#unitId")
    public void deleteEnergyUnitDeviceIdByUnitId(String unitId) {
    }

    @Override
    public List<ListElectricityMeterVO> listElectricityMeter(String unitId) {

        LambdaQueryWrapper<EnergyUnitToDevice> queryWrapper = new LambdaQueryWrapper<EnergyUnitToDevice>()
                .eq(EnergyUnitToDevice::getEnergyUnitId, unitId);
//                .eq(EnergyUnitToDevice::getDeviceType, EnergyTypeConst.ELECTRICITY);
        List<EnergyUnitToDevice> models = baseMapper.selectList(queryWrapper);

        List<ListElectricityMeterVO> resultList = new ArrayList<>();
        for (EnergyUnitToDevice model : models) {
            ListElectricityMeterVO temp = new ListElectricityMeterVO();
            temp.setCode(model.getId());
            temp.setLabel(model.getName());
            resultList.add(temp);
        }
        return resultList;
    }

    @Override
    public List<EnergyUnitToDevice> listAllMeter() {
        QueryWrapper<EnergyUnitToDevice> queryWrapper = new QueryWrapper<EnergyUnitToDevice>();
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public EnergyUnitToDevice getEnergyUnitToDeviceById(String unitId, String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        LambdaQueryWrapper<EnergyUnitToDevice> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(EnergyUnitToDevice::getEnergyUnitId, unitId);
        lambdaQueryWrapper.eq(EnergyUnitToDevice::getId, id);
        List<EnergyUnitToDevice> energyUnitToDevices = baseMapper.selectList(lambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(energyUnitToDevices)) {
            return energyUnitToDevices.get(CommonConst.DIGIT_0);
        }
        return null;
    }

    /**
     * 根据计量器具id集合查询与用能的关系
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<UnitToDeviceRelationVO> listDeviceByUnitIds(List<String> unitIds) {
        List<UnitToDeviceRelationVO> relationList = new ArrayList<>();
        if (CollectionUtils.isEmpty(unitIds)) {
            return relationList;
        }
        List<EnergyUnitToDevice> unitToDeviceList = baseMapper.selectList(Wrappers.<EnergyUnitToDevice>lambdaQuery()
                .select(EnergyUnitToDevice::getId, EnergyUnitToDevice::getEnergyUnitId, EnergyUnitToDevice::getName)
                .in(EnergyUnitToDevice::getEnergyUnitId, unitIds)
        );
        if (CollectionUtils.isNotEmpty(unitToDeviceList)) {
            unitToDeviceList.forEach(li -> {
                UnitToDeviceRelationVO vo = new UnitToDeviceRelationVO();
                vo.setDeviceId(li.getId());
                vo.setUnitId(li.getEnergyUnitId());
                vo.setDeviceName(li.getName());
                relationList.add(vo);
            });
        }
        return relationList;
    }
}
