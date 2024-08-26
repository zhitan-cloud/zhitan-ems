package com.dingzhuo.energy.project.keyEquipment.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.yearKeyEquipment;
import com.dingzhuo.energy.project.keyEquipment.mapper.yearKeyEquipmentMapper;
import com.dingzhuo.energy.project.keyEquipment.service.IyearKeyEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 年
 *
 * @author sys
 * @date 2021-01-11
 */
@Service
public class yearKeyEquipmentServiceImpl implements IyearKeyEquipmentService {
    @Autowired
    private yearKeyEquipmentMapper yearMapper;

    public List<yearKeyEquipment> getYearKeyEquipmentList(List<String> indexIds, List<yearKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return yearMapper.getYearKeyEquipmentList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<yearKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return yearMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
