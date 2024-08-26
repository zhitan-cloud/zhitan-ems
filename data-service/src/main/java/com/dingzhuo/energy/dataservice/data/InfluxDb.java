package com.dingzhuo.energy.dataservice.data;

import com.dingzhuo.energy.dataservice.data.influxdb.FastDB;
import com.dingzhuo.energy.dataservice.data.influxdb.QueryType;
import com.dingzhuo.energy.dataservice.data.influxdb.RtdbResult;
import com.dingzhuo.energy.dataservice.domain.CollectionModes;
import com.dingzhuo.energy.dataservice.domain.Quality;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabase;
import com.google.common.base.Function;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author fanxinfu
 */
public class InfluxDb implements RealtimeDatabase {
    private Logger logger = LogManager.getLogger(InfluxDb.class);

    private FastDB fastDB;
    private static final String DB_NAME = "daq";

    /**
     * 关闭连接
     */
    @Override
    public void close() {
        if (!Objects.isNull(fastDB)) {
            fastDB.close();
        }
    }

    /**
     * 根据点位号获取实时数据
     *
     * @param tagCodes 点位号列表
     * @return
     */
    @Override
    public List<TagValue> retrieve(List<String> tagCodes) throws Exception {
        List<RtdbResult> results = fastDB.snapShot(tagCodes);
        return convertTagValue(results);
    }

    /**
     * 根据点位号获取某一时刻的历史数据
     *
     * @param tagCodes 点位号集合
     * @param dataTime 历史时刻
     * @param timeCode 区分时间类型的time code
     * @return
     */
    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date dataTime, String timeCode) throws Exception {
        List<RtdbResult> results = fastDB.getDataByTime(tagCodes, dataTime, timeCode);
        return convertTagValue(results);
    }

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
    @Override
    public List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime, RetrievalModes retrievalModes, int pointCount) throws Exception {
        QueryType queryType = null;
        switch (retrievalModes) {
            case BestFit:
                queryType = QueryType.FIRST;
                break;
            default:
                queryType = QueryType.FULL;
                break;
        }
        List<RtdbResult> results = fastDB.getHistoryData(tagCodes, beginTime, endTime, queryType, pointCount);
        return convertTagValue(results);
    }

    /**
     * 汇总实时数据
     *
     * @param tagCodes        点位号集合
     * @param beginTime       开始时间
     * @param endTime         结束时间
     * @param collectionModes 汇总方式（最大值、最小值、平均值、求和等）
     * @return
     */
    @Override
    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime, CollectionModes collectionModes) throws Exception {
        DateTime begin = new DateTime(beginTime).withMillisOfSecond(0);
        DateTime end = new DateTime(endTime).withMillisOfSecond(0);
        QueryType queryType = null;
        switch (collectionModes) {
            case Sum:
                queryType = QueryType.SUM;
                break;
            case Maximum:
                queryType = QueryType.MAX;
                break;
            case Minimum:
                queryType = QueryType.MIN;
                break;
            case Mean:
                queryType = QueryType.MEAN;
                break;
            case Integral:
                queryType = QueryType.INTEGRAL;
                break;
            default:
                return new ArrayList<>();
        }
        List<RtdbResult> results = fastDB.statistics(tagCodes, begin.toDate(), end.toDate(), queryType);
        return convertTagValue(results);
    }

    /**
     * 插入实时数据
     *
     * @param tagValues 实时数据集合
     * @return
     */
    @Override
    public Boolean storeData(List<TagValue> tagValues) throws Exception {
        List<RtdbResult> results = convertToRtdbResult(tagValues);
        return fastDB.storeData(results);
    }

    /**
     * 插入历史数据
     *
     * @param tagValues 历史数据集合
     * @return
     */
    @Override
    public Boolean insertData(List<TagValue> tagValues) throws Exception {
        List<RtdbResult> results = convertToRtdbResult(tagValues);
        return fastDB.insertData(results);
    }

    /**
     * 打开连接
     *
     * @param host     实时数据库地址
     * @param port     端口号
     * @param userName 登录用户名
     * @param pwd      登录密码
     * @return 是否连接成功
     */
    @Override
    public boolean open(String host, int port, String userName, String pwd) {
        try {
            fastDB = new FastDB(host, port, DB_NAME);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return true;
    }


    private List<TagValue> convertTagValue(List<RtdbResult> results) {
        Function<RtdbResult, TagValue> convertFun = input -> {
            TagValue tagValue = new TagValue();
            tagValue.setDataTime(Date.from(input.getTime().atZone(ZoneOffset.ofHours(8)).toInstant()));
            tagValue.setQuality(input.getQuality() > 0 ? Quality.GOOD : Quality.BAD);
            tagValue.setTagCode(input.getTagCode());
            tagValue.setValue(input.getValue());
            if (tagValue.getQuality().equals(Quality.BAD)) {
                logger.info(input.toString());
                logger.info(tagValue.toString());
            }
            return tagValue;
        };
        return results.stream().map(convertFun).collect(Collectors.toList());
    }

    private List<RtdbResult> convertToRtdbResult(List<TagValue> results) {
        Function<TagValue, RtdbResult> convertFun = input -> {
            RtdbResult rtdbResult = new RtdbResult();
            rtdbResult.setTime(input.getDataTime().toInstant());
            rtdbResult.setQuality(input.getQuality() == Quality.GOOD ? 1 : 0);
            rtdbResult.setTagCode(input.getTagCode());
            rtdbResult.setValue(input.getValue());
            return rtdbResult;
        };
        return results.stream().map(convertFun).collect(Collectors.toList());
    }
}
