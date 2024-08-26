package com.dingzhuo.energy.project.reportForm.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.domain.reportForm;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 阶段数据录入接口
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface dailyReportMapper {
    /*public List<DataItem> getDailyReportList(*//*@Param("indexIds") List<String> indexIds,
                                             @Param("beginTime") Date beginTime,
                                             @Param("endTime") Date endTime,
                                             @Param("timeType") TimeType timeType*//*);*/
    public List<dailyReport> getDailyReportList(@Param("indexIds") List<String> indexIds,
                                                @Param("dataList") List<dailyReport> dataList,
                                                @Param("beginTime") Date beginTime,
                                                @Param("endTime") Date endTime,
                                                @Param("timeType") TimeType timeType,
                                                @Param("indexStorageId")  String indexStorageId);

    public List<dailyReport> listDailyReportList(@Param("nodeId") String nodeId,
                                                @Param("dataList") List<dailyReport> dataList,
                                                @Param("beginTime") Date beginTime,
                                                @Param("endTime") Date endTime,
                                                @Param("timeType") TimeType timeType,
                                                @Param("indexStorageId")  String indexStorageId);

}
