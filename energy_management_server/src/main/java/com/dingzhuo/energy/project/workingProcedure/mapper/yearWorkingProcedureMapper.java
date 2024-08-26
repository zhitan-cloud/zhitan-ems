package com.dingzhuo.energy.project.workingProcedure.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.workingProcedure.domain.yearWorkingProcedure;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 工序综合能耗统计 年
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface yearWorkingProcedureMapper {
    public List<yearWorkingProcedure> getYearWorkingProcedureList(@Param("indexIds") List<String> indexIds,
                                                                  @Param("dataList") List<yearWorkingProcedure> dataList,
                                                                  @Param("beginTime") Date beginTime,
                                                                  @Param("endTime") Date endTime,
                                                                  @Param("timeType") TimeType timeType,
                                                                  @Param("indexStorageId") String indexStorageId);
    List<yearWorkingProcedure> getListChart(@Param("indexId") String indexId,
                                            @Param("beginTime") Date beginTime,
                                            @Param("endTime") Date endTime,
                                            @Param("timeType") TimeType timeType,
                                            @Param("indexStorageId") String indexStorageId);

}
