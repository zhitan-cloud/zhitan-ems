package com.zhitan.keyequipment.mapper;

import com.zhitan.basicdata.domain.FacilityArchives;
import com.zhitan.keyequipment.domain.DailyKeyEquipment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
public interface DailyKeyEquipmentMapper {
    public List<DailyKeyEquipment> getdailyKeyEquipmentList(@Param("indexIds") List<String> indexIds,
                                                            @Param("dataList") List<DailyKeyEquipment> dataList,
                                                            @Param("beginTime") Date beginTime,
                                                            @Param("endTime") Date endTime,
                                                            @Param("timeType") String timeType,
                                                            @Param("indexStorageId") String indexStorageId);
    List<DailyKeyEquipment> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") String timeType,
                                            @Param("indexStorageId")  String indexStorageId);
    List<FacilityArchives> getFacilityArchives();
    List<FacilityArchives> getPointFacility();
}
