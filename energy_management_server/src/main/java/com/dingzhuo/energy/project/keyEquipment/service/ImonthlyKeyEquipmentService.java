package com.dingzhuo.energy.project.keyEquipment.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.monthlyKeyEquipment;


import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 月
 *
 * @author sys
 * @date 2021-01-11
 */
public interface ImonthlyKeyEquipmentService {
    public List<monthlyKeyEquipment> getMonthlyKeyEquipmentList(List<String> indexIds, List<monthlyKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<monthlyKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
