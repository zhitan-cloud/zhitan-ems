package com.dingzhuo.energy.project.keyEquipment.mapper;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.dailyKeyEquipment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
public interface dailyKeyEquipmentMapper {
    public List<dailyKeyEquipment> getdailyKeyEquipmentList(@Param("indexIds") List<String> indexIds,
                                                            @Param("dataList") List<dailyKeyEquipment> dataList,
                                                            @Param("beginTime") Date beginTime,
                                                            @Param("endTime") Date endTime,
                                                            @Param("timeType") TimeType timeType,
                                                            @Param("indexStorageId") String indexStorageId);
    List<dailyKeyEquipment> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") TimeType timeType,
                                            @Param("indexStorageId")  String indexStorageId);
    List<FacilityArchives> getFacilityArchives();
    List<FacilityArchives> getPointFacility();
}
