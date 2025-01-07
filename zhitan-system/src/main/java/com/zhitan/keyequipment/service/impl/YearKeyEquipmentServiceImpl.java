package com.zhitan.keyequipment.service.impl;

import com.zhitan.common.enums.TimeType;
import com.zhitan.keyequipment.domain.YearKeyEquipment;
import com.zhitan.keyequipment.mapper.YearKeyEquipmentMapper;
import com.zhitan.keyequipment.service.IYearKeyEquipmentService;
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
public class YearKeyEquipmentServiceImpl implements IYearKeyEquipmentService {
    @Autowired
    private YearKeyEquipmentMapper yearKeyEquipmentMapper;

    public List<YearKeyEquipment> getYearKeyEquipmentList(List<String> indexIds, List<YearKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return yearKeyEquipmentMapper.getYearKeyEquipmentList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<YearKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return yearKeyEquipmentMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
