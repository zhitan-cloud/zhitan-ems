package com.dingzhuo.energy.project.dataEntry.service.impl;

import cn.hutool.core.date.DateTime;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.dataEntry.domain.stagseDataEntry;
import com.dingzhuo.energy.project.dataEntry.mapper.DataItemMapper;
import com.dingzhuo.energy.project.dataEntry.service.IDataItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * stagseDataEntryService业务层处理
 * 
 * @author sys
 * @date 2020-03-25
 */
@Service
public class DataItemServiceImpl implements IDataItemService {
    @Autowired
    private DataItemMapper dataItemMapper;
    /**
     * 查询需要手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return 结果
     */
    @Override
    public List<stagseDataEntry> getSettingIndex(stagseDataEntry stagseDataEntry) {
        return dataItemMapper.getSettingIndex(stagseDataEntry);
    }

    /**
     * 查询修改手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return 结果
     */
    @Override
    public List<stagseDataEntry> getSettingEdit(stagseDataEntry stagseDataEntry) {
        return dataItemMapper.getSettingEdit(stagseDataEntry);
    }

    @Override
    public List<stagseDataEntry> stagseDataEntry(String nodeId, List<String> indexCodes, TimeType timeType, Date beginTime, Date endTime, String calcType) {
        return dataItemMapper.stagseDataByCode(nodeId, indexCodes, timeType, beginTime, endTime, calcType);
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
}
