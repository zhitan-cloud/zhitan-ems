package com.dingzhuo.energy.project.workingProcedure.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.dailyWorkingProcedure;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *工序能耗统计 日
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface dailyWorkingProcedureMapper {
    public List<dailyWorkingProcedure> getdailyWorkingProcedureList(@Param("indexIds") List<String> indexIds,
                                                                @Param("dataList") List<dailyWorkingProcedure> dataList,
                                                                @Param("beginTime") Date beginTime,
                                                                @Param("endTime") Date endTime,
                                                                @Param("timeType") TimeType timeType,
                                                                @Param("indexStorageId") String indexStorageId);
    List<dailyWorkingProcedure> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") TimeType timeType,
                                            @Param("indexStorageId")  String indexStorageId);

}
