package com.dingzhuo.energy.project.keyEquipment.service.impl;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.dailyKeyEquipment;
import com.dingzhuo.energy.project.keyEquipment.mapper.dailyKeyEquipmentMapper;
import com.dingzhuo.energy.project.keyEquipment.service.IdailyKeyEquipmentService;
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
public class dailyKeyEquipmentServiceImpl implements IdailyKeyEquipmentService {
    @Autowired
    private dailyKeyEquipmentMapper dailyMapper;

    @Override
    public List<dailyKeyEquipment> getdailyKeyEquipmentList(List<String> indexIds, List<dailyKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return dailyMapper.getdailyKeyEquipmentList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<dailyKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return dailyMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<FacilityArchives> getFacilityArchives() {
        return dailyMapper.getFacilityArchives();
    }
    @Override
    public List<FacilityArchives> getPointFacility() {
        return dailyMapper.getPointFacility();
    }
}
