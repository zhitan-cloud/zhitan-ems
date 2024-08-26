package com.dingzhuo.energy.project.keyEquipment.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.keyEquipment.domain.monthlyKeyEquipment;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 月
 *
 * @author sys
 * @date 2021-01-11
 */
public interface monthlyKeyEquipmentMapper {
    public List<monthlyKeyEquipment> getMonthlyKeyEquipmentList(@Param("indexIds") List<String> indexIds,
                                                                    @Param("dataList") List<monthlyKeyEquipment> dataList,
                                                                    @Param("beginTime") Date beginTime,
                                                                    @Param("endTime") Date endTime,
                                                                    @Param("timeType") TimeType timeType,
                                                                    @Param("indexStorageId") String indexStorageId);
    List<monthlyKeyEquipment> getListChart(@Param("indexId") String indexId,
                                               @Param("beginTime") Date beginTime,
                                               @Param("endTime") Date endTime,
                                               @Param("timeType") TimeType timeType,
                                               @Param("indexStorageId") String indexStorageId);

}
