package com.dingzhuo.energy.project.dataEntry.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.dataEntry.domain.stagseDataEntry;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 阶段数据录入接口
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface DataItemMapper {
    /**
     * 阶段数据录入
     *
     * @param stagseDataEntry
     * @return 结果
     */
    public List<stagseDataEntry> getSettingIndex(stagseDataEntry stagseDataEntry);

    public List<stagseDataEntry> getSettingEdit(stagseDataEntry stagseDataEntry);

    List<stagseDataEntry> stagseDataByCode(@Param("nodeId") String nodeId,
                                           @Param("indexCodes") List<String> indexCodes,
                                           @Param("timeType") TimeType timeType,
                                           @Param("beginTime") Date beginTime,
                                           @Param("endTime") Date endTime,
                                           @Param("calcType") String calcType);

    /**
     * 根据indexId与时间范围查询小时的dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    List<DataItem> getDataItemHourInforByIndexIds(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                                  @Param("timeType") String timeType, @Param("indexIds") List<String> indexIds);

    /**
     * 根据indexId与时间编码查询点位值合计
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    List<DataItem> getDataItemInforByIndexIds(@Param("timeCode") String timeCode, @Param("indexIds") List<String> indexIds);
}
