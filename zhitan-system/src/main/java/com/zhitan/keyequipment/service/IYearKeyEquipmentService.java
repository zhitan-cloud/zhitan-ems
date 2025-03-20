package com.zhitan.keyequipment.service;


import com.zhitan.common.utils.TypeTime;
import com.zhitan.keyequipment.domain.YearKeyEquipment;
import com.zhitan.realtimedata.domain.dto.DataItemQueryDTO;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 年
 *
 * @author sys
 * @date 2021-01-11
 */
public interface IYearKeyEquipmentService {
    public List<YearKeyEquipment> getYearKeyEquipmentList(List<String> indexIds, List<TypeTime> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId);
    List<YearKeyEquipment> getListChart(DataItemQueryDTO queryDto);
}
