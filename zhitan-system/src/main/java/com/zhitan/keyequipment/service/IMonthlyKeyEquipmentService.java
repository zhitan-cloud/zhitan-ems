package com.zhitan.keyequipment.service;


import com.zhitan.keyequipment.domain.MonthlyKeyEquipment;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 月
 *
 * @author sys
 * @date 2021-01-11
 */
public interface IMonthlyKeyEquipmentService {

    List<MonthlyKeyEquipment> getMonthlyKeyEquipmentList(List<String> indexIds, List<MonthlyKeyEquipment> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId);

    List<MonthlyKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId);
}
