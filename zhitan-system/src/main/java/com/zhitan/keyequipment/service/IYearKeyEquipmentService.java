package com.zhitan.keyequipment.service;


import com.zhitan.common.enums.TimeType;
import com.zhitan.keyequipment.domain.YearKeyEquipment;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 年
 *
 * @author sys
 * @date 2021-01-11
 */
public interface IYearKeyEquipmentService {
    public List<YearKeyEquipment> getYearKeyEquipmentList(List<String> indexIds, List<YearKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    List<YearKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
}
