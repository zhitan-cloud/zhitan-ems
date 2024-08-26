package com.dingzhuo.energy.project.keyEquipment.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.yearKeyEquipment;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 年
 *
 * @author sys
 * @date 2021-01-11
 */
public interface IyearKeyEquipmentService {
    public List<yearKeyEquipment> getYearKeyEquipmentList(List<String> indexIds, List<yearKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<yearKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
