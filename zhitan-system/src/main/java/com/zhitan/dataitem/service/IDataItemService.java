package com.zhitan.dataitem.service;


import com.zhitan.common.enums.TimeType;
import com.zhitan.dataitem.domain.StagseDataEntry;
import com.zhitan.realtimedata.domain.DataItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 阶段数据录入接口
 * 
 * @author sys
 * @date 2020-03-25
 */
public interface IDataItemService {
    /**
     * 查询需要手动录入的阶段数据
     * 
     * @param stagseDataEntry
     * @return stagseDataEntry集合
     */
    public List<StagseDataEntry> getSettingIndex(StagseDataEntry stagseDataEntry);

    /**
     * 查询修改手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return stagseDataEntry集合
     */
    public List<StagseDataEntry> getSettingEdit(StagseDataEntry stagseDataEntry);

    public List<StagseDataEntry> stagseDataEntry(String nodeId, List<String> indexCodes,
                                                 TimeType timeType,
                                                 Date beginTime,
                                                 Date endTime,
                                                 String calcType);

    /**
     * 根据indexId与时间范围查询dataitem合计信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    BigDecimal getDataItemTimeRangeValueByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds);

    /**
     * 根据indexId与时间范围查询dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    List<DataItem> getDataItemTimeRangeInforByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds);

    /**
     * 根据indexId与时间编码查询点位值合计
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    List<DataItem> getDataItemInforByIndexIds(String timeCode, List<String> indexIds);

    /**
     * 根据indexId与时间编码查询合计值
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    BigDecimal getDataItemValueByIndexIds(String timeCode, List<String> indexIds);

    /**
     * 根据indexId与时间范围查询小时的dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    List<DataItem> getDataItemHourInforByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds);
}
