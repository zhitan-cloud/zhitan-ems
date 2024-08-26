package com.dingzhuo.energy.project.keyEquipment.service;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.dailyKeyEquipment;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
public interface IdailyKeyEquipmentService {
    public List<dailyKeyEquipment> getdailyKeyEquipmentList(List<String> indexIds, List<dailyKeyEquipment> dataList, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    public List<dailyKeyEquipment> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId);
    public List<FacilityArchives> getFacilityArchives();
    public List<FacilityArchives> getPointFacility();
}
