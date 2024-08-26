package com.dingzhuo.energy.project.keyEquipment.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.monthlyKeyEquipment;
import com.dingzhuo.energy.project.keyEquipment.mapper.monthlyKeyEquipmentMapper;
import com.dingzhuo.energy.project.keyEquipment.service.ImonthlyKeyEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 月
 *
 * @author sys
 * @date 2021-01-11
 */
@Service
public class monthlyKeyEquipmentServiceImpl implements ImonthlyKeyEquipmentService {
    @Autowired
    private monthlyKeyEquipmentMapper monthMapper;

    public List<monthlyKeyEquipment> getMonthlyKeyEquipmentList(List<String> indexIds, List<monthlyKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return monthMapper.getMonthlyKeyEquipmentList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<monthlyKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return monthMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
