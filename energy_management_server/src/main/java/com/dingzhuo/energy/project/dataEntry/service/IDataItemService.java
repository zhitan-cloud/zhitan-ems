package com.dingzhuo.energy.project.dataEntry.service;

import cn.hutool.core.date.DateTime;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.dataEntry.domain.stagseDataEntry;

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
    public List<stagseDataEntry> getSettingIndex(stagseDataEntry stagseDataEntry);

    /**
     * 查询修改手动录入的阶段数据
     *
     * @param stagseDataEntry
     * @return stagseDataEntry集合
     */
    public List<stagseDataEntry> getSettingEdit(stagseDataEntry stagseDataEntry);

    public List<stagseDataEntry> stagseDataEntry(String nodeId, List<String> indexCodes,
                                                 TimeType timeType,
                                                 Date beginTime,
                                                 Date endTime,
                                                 String calcType);

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

    /**
     * 根据indexId与时间编码查询点位值合计
     *
     * @param timeCode 时间编码
     * @param indexIds 点位id集合
     * @return
     */
    List<DataItem> getDataItemInforByIndexIds(String timeCode, List<String> indexIds);
}
