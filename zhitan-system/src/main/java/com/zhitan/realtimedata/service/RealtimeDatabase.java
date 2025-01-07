package com.zhitan.realtimedata.service;

import com.zhitan.common.enums.CollectionModes;
import com.zhitan.common.enums.RetrievalModes;
import com.zhitan.realtimedata.domain.TagValue;

import java.util.Date;
import java.util.List;

public interface RealtimeDatabase {

    long id = System.currentTimeMillis();

    /**
     * 关闭连接
     */
    void close();

    /**
     * 根据点位号获取实时数据
     *
     * @param tagCodes 点位号列表
     * @return
     */
    List<TagValue> retrieve(List<String> tagCodes) throws Exception;

    /**
     * 根据点位号获取某一时刻的历史数据
     *
     * @param tagCodes 点位号集合
     * @param dataTime 历史时刻
     * @param timeCode 区分时间类型的timeCode
     * @return
     */
    List<TagValue> retrieve(List<String> tagCodes, Date dataTime,String timeCode) throws Exception;

    /**
     * 根据查询方式获取一段时间内的历史数据
     *
     * @param tagCodes       点位号集合
     * @param beginTime      开始时间
     * @param endTime        结束时间
     * @param retrievalModes 查询方式（循环、拟合、全部）
     * @param pointCount     要查询的数据个数
     * @return
     */
    List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime,
                            RetrievalModes retrievalModes, int pointCount) throws Exception;

    /**
     * 汇总实时数据
     *
     * @param tagCodes        点位号集合
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @param collectionModes 汇总方式（最大值、最小值、平均值、求和等）
     * @return
     */
    List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime,
                              CollectionModes collectionModes) throws Exception;

    /**
     * 插入实时数据
     *
     * @param tagValues 实时数据集合
     * @return
     */
    Boolean storeData(List<TagValue> tagValues) throws Exception;

    /**
     * 插入历史数据
     *
     * @param tagValues 历史数据集合
     * @return
     */
    Boolean insertData(List<TagValue> tagValues) throws Exception;

    /**
     * 打开连接
     *
     * @param host     实时数据库地址
     * @param port     端口号
     * @param userName 登录用户名
     * @param pwd      登录密码
     * @return 是否连接成功
     */
    boolean open(String host, int port, String userName, String pwd) throws Exception;
}
