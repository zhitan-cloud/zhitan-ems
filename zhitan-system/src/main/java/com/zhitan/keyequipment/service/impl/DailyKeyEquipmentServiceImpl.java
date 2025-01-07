package com.zhitan.keyequipment.service.impl;

import com.zhitan.basicdata.domain.FacilityArchives;
import com.zhitan.common.enums.TimeType;
import com.zhitan.keyequipment.domain.DailyKeyEquipment;
import com.zhitan.keyequipment.mapper.DailyKeyEquipmentMapper;
import com.zhitan.keyequipment.service.IDailyKeyEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
@Service
public class DailyKeyEquipmentServiceImpl implements IDailyKeyEquipmentService {
    @Autowired
    private DailyKeyEquipmentMapper dailyKeyEquipmentMapper;

    @Override
    public List<DailyKeyEquipment> getdailyKeyEquipmentList(List<String> indexIds, List<DailyKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return dailyKeyEquipmentMapper.getdailyKeyEquipmentList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<DailyKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return dailyKeyEquipmentMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<FacilityArchives> getFacilityArchives() {
        return dailyKeyEquipmentMapper.getFacilityArchives();
    }
    @Override
    public List<FacilityArchives> getPointFacility() {
        return dailyKeyEquipmentMapper.getPointFacility();
    }
}
