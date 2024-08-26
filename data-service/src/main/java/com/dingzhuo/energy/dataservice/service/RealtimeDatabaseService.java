package com.dingzhuo.energy.dataservice.service;

import com.dingzhuo.energy.dataservice.domain.CollectionModes;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;

import java.util.Date;
import java.util.List;

/**
 * @author 智碳未来 实时数据库访问接口.
 */
public interface RealtimeDatabaseService {

    /**
     * 获取单个测点的实时数据.
     *
     * @param tagCode 测点编号
     * @return 测试实时数据
     */
    TagValue retrieve(String tagCode);

    /**
     * 获取批量测点的实时数据.
     *
     * @param tagCodes 测点编码集合
     * @return 实时数据集合
     */
    List<TagValue> retrieve(List<String> tagCodes);

    /**
     * 获取测点的历史时刻值.
     *
     * @param tagCode  测点编号
     * @param dataTime 历史时刻
     * @param timeCode 时间类型的TIME CODE
     * @return 测点历史时刻值
     */
    TagValue retrieve(String tagCode, Date dataTime, String timeCode);

    /**
     * 获取批量测点的历史时刻值.
     *
     * @param tagCodes 测点编号集合
     * @param dataTime 历史时刻
     * @param timeCode 时间类型的TIME CODE
     * @return 测试历史时刻数据集合
     */
    List<TagValue> retrieve(List<String> tagCodes, Date dataTime, String timeCode);

    /**
     * 获取一段时间内测点的历史数据.
     *
     * @param tagCode        测点编号
     * @param beginTime      开始时间
     * @param endTime        结束时间
     * @param retrievalModes 查询数据方式
     * @param pointCount     测点得到的数据个数
     * @return 测点历史数据
     */
    List<TagValue> retrieve(String tagCode, Date beginTime, Date endTime,
                            RetrievalModes retrievalModes, int pointCount);

    /**
     * 获取一段时间内批量测点的历史数据.
     *
     * @param tagCodes       测点编号集合
     * @param beginTime      开始时间
     * @param endTime        结束时间
     * @param retrievalModes 查询数据方式
     * @param pointCount     每个测点得到的数据个数
     * @return 测点历史数据
     */
    List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime,
                            RetrievalModes retrievalModes, int pointCount);

    /**
     * 获取测点在一段时间内的统计数据.
     *
     * @param tagCode         测点编号
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @param collectionModes 统计类型
     * @return 测点统计结果
     */
    TagValue statistics(String tagCode, Date beginTime, Date endTime,
                        CollectionModes collectionModes);

    /**
     * 获取测点在一段时间内的统计数据.
     *
     * @param tagCodes        测点编号集合
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @param collectionModes 统计类型
     * @return 测点统计结果
     */
    List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime,
                              CollectionModes collectionModes);

    /**
     * 存储测点的实时数据.
     *
     * @param tagValues 测点实时数据
     */
    void storeData(List<TagValue> tagValues);

    /**
     * 插入测点历史时刻数据.
     *
     * @param tagValues 测点历史时刻数据
     */
    void insertData(List<TagValue> tagValues);
}
