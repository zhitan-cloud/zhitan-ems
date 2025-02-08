package com.zhitan.dataitem.mapper;

import com.zhitan.carbonemission.domain.CarbonEmission;
import com.zhitan.common.enums.TimeType;
import com.zhitan.dataitem.domain.StagseDataEntry;
import com.zhitan.realtimedata.domain.DataItem;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
    public List<StagseDataEntry> getSettingIndex(StagseDataEntry stagseDataEntry);

    public List<StagseDataEntry> getSettingEdit(StagseDataEntry stagseDataEntry);

    List<StagseDataEntry> stagseDataByCode(@Param("nodeId") String nodeId,
                                           @Param("indexCodes") List<String> indexCodes,
                                           @Param("timeType") TimeType timeType,
                                           @Param("beginTime") Date beginTime,
                                           @Param("endTime") Date endTime,
                                           @Param("calcType") String calcType);

    /**
     * 根据indexId与时间范围查询dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    List<DataItem> getDataItemTimeRangeInforByIndexIds(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                                       @Param("timeType") String timeType, @Param("indexIds") List<String> indexIds);

    /**
     * 根据indexId与时间范围查询dataitem合计信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    BigDecimal getDataItemTimeRangeValueByIndexIds(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                                   @Param("timeType") String timeType, @Param("indexIds") List<String> indexIds);

    /**
     * 根据indexId与时间编码查询点位值合计
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    List<DataItem> getDataItemInforByIndexIds(@Param("timeCode") String timeCode, @Param("indexIds") List<String> indexIds);

    /**
     * 根据indexId与时间编码查询合计值
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    BigDecimal getDataItemValueByIndexIds(@Param("timeCode") String timeCode, @Param("indexIds") List<String> indexIds);


    /**
     * 根据indexId与时间范围查询碳排放数据，上半部分
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexId
     * @return
     */
    List<CarbonEmission> getUpCarbonEmission(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                             @Param("timeType") String timeType, @Param("indexId") String indexId);


    /**
     * 根据indexId与时间范围查询碳排放数据，中间部分
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexId
     * @return
     */
    List<CarbonEmission> getMiddleCarbonEmission(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                             @Param("timeType") String timeType, @Param("indexId") String indexId,@Param("emissionType") String emissionType);

    /**
     * 根据indexId与时间范围查询碳排放数据，下半部分
     * @param beginTime
     * @param endTime
     * @param timeType
     * @param indexId
     * @return
     */
    List<CarbonEmission> getDownCarbonEmission(@Param("beginTime") Date beginTime, @Param("endTime") Date endTime,
                                             @Param("timeType") String timeType, @Param("indexId") String indexId);

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
}
