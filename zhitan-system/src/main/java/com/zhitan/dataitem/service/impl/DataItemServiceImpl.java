package com.zhitan.dataitem.service.impl;

import com.zhitan.common.enums.TimeType;
import com.zhitan.dataitem.domain.StagseDataEntry;
import com.zhitan.dataitem.mapper.DataItemMapper;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.realtimedata.domain.DataItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * stagseDataEntryService业务层处理
 *
 * @author sys
 * @date 2020-03-25
 */
@Service
@AllArgsConstructor
public class DataItemServiceImpl implements IDataItemService {

    private final DataItemMapper dataItemMapper;


    /**
     * 查询需要手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return 结果
     */
    @Override
    public List<StagseDataEntry> getSettingIndex(StagseDataEntry stagseDataEntry) {
        return dataItemMapper.getSettingIndex(stagseDataEntry);
    }

    /**
     * 查询修改手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return 结果
     */
    @Override
    public List<StagseDataEntry> getSettingEdit(StagseDataEntry stagseDataEntry) {
        return dataItemMapper.getSettingEdit(stagseDataEntry);
    }

    @Override
    public List<StagseDataEntry> stagseDataEntry(String nodeId, List<String> indexCodes, TimeType timeType, Date beginTime, Date endTime, String calcType) {
        return dataItemMapper.stagseDataByCode(nodeId, indexCodes, timeType, beginTime, endTime, calcType);
    }

    /**
     * 根据indexId与时间范围查询dataitem合计信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    @Override
    public BigDecimal getDataItemTimeRangeValueByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds) {
        return dataItemMapper.getDataItemTimeRangeValueByIndexIds(beginTime, endTime, timeType, indexIds);
    }

    /**
     * 根据indexId与时间范围查询dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    @Override
    public List<DataItem> getDataItemTimeRangeInforByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds) {
        return dataItemMapper.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, timeType, indexIds);
    }

    /**
     * 根据indexId与时间编码查询点位值合计
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    @Override
    public List<DataItem> getDataItemInforByIndexIds(String timeCode, List<String> indexIds) {
        return dataItemMapper.getDataItemInforByIndexIds(timeCode, indexIds);
    }

    /**
     * 根据indexId与时间编码查询合计值
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    @Override
    public BigDecimal getDataItemValueByIndexIds(String timeCode, List<String> indexIds) {
        return dataItemMapper.getDataItemValueByIndexIds(timeCode, indexIds);
    }

    /**
     * 根据indexId与时间范围查询小时的dataitem信息
     *
     * @param beginTime 开始时间
     * @param endTime   截止时间
     * @param timeType  时间类型
     * @param indexIds  点位集合
     * @return
     */
    @Override
    public List<DataItem> getDataItemHourInforByIndexIds(Date beginTime, Date endTime, String timeType, List<String> indexIds) {
        return dataItemMapper.getDataItemHourInforByIndexIds(beginTime, endTime, timeType, indexIds);
    }
}
