package com.zhitan.realtimedata.service.impl;

import com.google.common.collect.Lists;
import com.zhitan.common.enums.CollectionModes;
import com.zhitan.common.enums.RetrievalModes;
import com.zhitan.realtimedata.data.RealtimeDatabaseManager;
import com.zhitan.realtimedata.data.influxdb.InfluxDBRepository;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.service.RealtimeDatabaseService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 实时数据库取数服务实现类.
 */
@Service
public class RealtimeDatabaseServiceImpl implements RealtimeDatabaseService {

    private final InfluxDBRepository repository;

    private final RealtimeDatabaseManager realtimeDatabaseManager;

    public RealtimeDatabaseServiceImpl(InfluxDBRepository repository, RealtimeDatabaseManager realtimeDatabaseManager) {
        this.repository = repository;
        this.realtimeDatabaseManager = realtimeDatabaseManager;
    }

    /**
     * 获取单个测点的实时数据.
     *
     * @param tagCode 测点编号
     * @return 测试实时数据
     */
    @Override
    public TagValue retrieve(String tagCode) {
        return repository.query(tagCode);
    }

    /**
     * 获取批量测点的实时数据.
     *
     * @param tagCodes 测点编码集合
     * @return 实时数据集合
     */
    @Override
    public List<TagValue> retrieve(List<String> tagCodes) {
        return repository.query(tagCodes);
    }

    /**
     * 获取测点的历史时刻值.
     *
     * @param tagCode  测点编号
     * @param dataTime 历史时刻
     * @return 测点历史时刻值
     */
    @Override
    public TagValue retrieve(String tagCode, Date dataTime) {
        return repository.query(tagCode, dataTime);
    }

    /**
     * 获取批量测点的历史时刻值.
     *
     * @param tagCodes 测点编号集合
     * @param dataTime 历史时刻
     * @return 测试历史时刻数据集合
     */
    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date dataTime) {
        return repository.query(tagCodes, dataTime);
    }

    /**
     * 获取一段时间内测点的历史数据.
     *
     * @param tagCode    测点编号
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @param pointCount 测点得到的数据个数
     * @return 测点历史数据
     */
    @Override
    public List<TagValue> retrieve(String tagCode, Date beginTime, Date endTime, int pointCount) {
        return repository.getHistoryData(Lists.newArrayList(tagCode), beginTime, endTime, pointCount);
    }

    /**
     * 获取一段时间内批量测点的历史数据.
     *
     * @param tagCodes  测点编号集合
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return 测点历史数据
     */
    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime, int pointCount) {
        return repository.getHistoryData(tagCodes, beginTime, endTime, pointCount);
    }

    /**
     * 获取测点在一段时间内的统计数据.
     *
     * @param tagCode         测点编号
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @param collectionModes 统计类型
     * @return 测点统计结果
     */
    @Override
    public TagValue statistics(String tagCode, Date beginTime, Date endTime, CollectionModes collectionModes) {
        List<TagValue> tagValues = repository.statistics(Collections.singletonList(tagCode), beginTime, endTime, collectionModes);
        return CollectionUtils.isEmpty(tagValues) ? tagValues.get(0) : null;
    }

    /**
     * 获取测点在一段时间内的统计数据.
     *
     * @param tagCodes        测点编号集合
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @param collectionModes 统计类型
     * @return 测点统计结果
     */
    @Override
    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime, CollectionModes collectionModes) {
        return repository.statistics(tagCodes, beginTime, endTime, collectionModes);
    }

    /**
     * 存储测点的实时数据.
     *
     * @param tagValues 测点实时数据
     */
    @Override
    public void storeData(List<TagValue> tagValues) {
        repository.store(tagValues);
    }

    /**
     * 插入测点历史时刻数据.
     *
     * @param tagValues 测点历史时刻数据
     */
    @Override
    public void insertData(List<TagValue> tagValues) {
        repository.store(tagValues);
    }

    @Override
    public List<TagValue> retrieve(String tagCode, Date beginTime, Date endTime,
                                   RetrievalModes retrievalModes, int pointCount) {
        return realtimeDatabaseManager.retrieve(tagCode, beginTime, endTime, retrievalModes, pointCount);
    }
}
