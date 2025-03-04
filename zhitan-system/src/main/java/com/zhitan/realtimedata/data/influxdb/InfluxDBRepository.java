package com.zhitan.realtimedata.data.influxdb;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.HealthCheck;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.zhitan.common.enums.CollectionModes;
import com.zhitan.common.enums.GroupTimeType;
import com.zhitan.common.enums.Quality;
import com.zhitan.realtimedata.config.RtdbConfig;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.domain.WritePoint;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 实时库初连接
 *
 * @Author: Zhujw
 * @Date: 2023/5/24
 */
@Repository
@Slf4j
public class InfluxDBRepository {

    private final RtdbConfig config;

    private InfluxDBClient client;

    public InfluxDBRepository(RtdbConfig config) {
        this.config = config;
        connectInfluxDB();
    }

    private void connectInfluxDB() {
        char[] token = config.getToken().toCharArray();
        if (client == null) {
            client = InfluxDBClientFactory.create(config.getHost(), token, config.getOrg(),
                    config.getBucket());
        }
        log.error("--------------------实时数据库连接成功--------------------");
        HealthCheck health = client.health();
        if (health.getStatus() == HealthCheck.StatusEnum.FAIL) {
            client.close();
            client = InfluxDBClientFactory.create(config.getHost(), token, config.getOrg(),
                    config.getBucket());
        }
    }

    public void store(List<TagValue> tagValues) {
        WriteApiBlocking write = client.getWriteApiBlocking();
        List<WritePoint> writePoints = new ArrayList<>();
        tagValues.forEach(tagValue -> {
            WritePoint point = new WritePoint(tagValue.getTagCode(), tagValue.getValue(),
                    Instant.ofEpochMilli(tagValue.getDataTime().getTime()));
            writePoints.add(point);
        });
        write.writeMeasurements(WritePrecision.MS, writePoints);
    }

    public TagValue query(String tagCode, Date time) {
        List<TagValue> values = query(Collections.singletonList(tagCode), time);
        return !values.isEmpty() ? values.get(0) : new TagValue();
    }

    public TagValue query(String tagCode) {
        List<TagValue> values = query(Collections.singletonList(tagCode));
        return !values.isEmpty() ? values.get(0) : new TagValue();
    }

    public List<TagValue> query(List<String> tagCodes, Date time) {
        DateTime endTime = new DateTime(time);
        DateTime beginTime = endTime.plusDays(-1);
        return getTagValues(beginTime, endTime, tagCodes);
    }

    public List<TagValue> query(List<String> tagCodes) {
        DateTime beginTime = DateTime.now().plusDays(-1);
        DateTime endTime = DateTime.now();
        return getTagValues(beginTime, endTime, tagCodes);
    }

    /**
     * 获取一个小时内所有数据
     *
     * @param tagCodes 测点编号
     * @param time     开始时间
     * @return 测点统计结果
     */
    public List<TagValue> queryOneHour(List<String> tagCodes, Date time) {
        DateTime beginTime = new DateTime(time);
        DateTime endTime = beginTime.plusHours(1);
        return getHistoryData(tagCodes, beginTime.toDate(), endTime.toDate());
    }

    private List<TagValue> getHistoryData(List<String> tagCodes, Date beginTime, Date endTime) {
        DateTime begin = new DateTime(beginTime);
        DateTime end = new DateTime(endTime);
        StringBuilder timeRange = new StringBuilder()
                .append("|> range(start: ").append(Instant.ofEpochMilli(begin.getMillis()).toString())
                .append(", stop: ").append(Instant.ofEpochMilli(end.getMillis()).toString())
                .append(")");
        StringBuilder fluxSql = getStringBuilder(timeRange,
                tagCodes);
        log.info(String.valueOf(fluxSql));
        return getTagValues(fluxSql);
    }

    public List<TagValue> getHistoryData(List<String> tagCodes, Date beginTime, Date endTime,
                                         long interval) {
        DateTime begin = new DateTime(beginTime);
        DateTime end = new DateTime(endTime);
        StringBuilder timeRange = new StringBuilder()
                .append("|> range(start: ").append(Instant.ofEpochMilli(begin.getMillis()).toString())
                .append(", stop: ").append(Instant.ofEpochMilli(end.getMillis()).toString())
                .append(")");
        StringBuilder fluxSql = getStringBuilder(timeRange, tagCodes);
        fluxSql.append("|> aggregateWindow(every: ").append(interval)
                .append("s, fn: last, createEmpty:  false)");
        log.info(String.valueOf(fluxSql));
        return getTagValues(fluxSql);
    }

    public List<TagValue> getHistoryData(List<String> tagCodes, Date beginTime, Date endTime,
                                         int pointCount) {
        DateTime begin = new DateTime(beginTime);
        DateTime end = new DateTime(endTime);
        long millis = new Duration(begin, end).getMillis();
        long interval = millis / pointCount / 1000;
        return getHistoryData(tagCodes, beginTime, endTime, interval);
    }

    private StringBuilder getStringBuilder(StringBuilder timeRange,
                                           List<String> tagCodes) {
        StringBuilder fluxSql = new StringBuilder();
        fluxSql.append("from(bucket: \"").append(config.getBucket()).append("\")")
                .append(timeRange).append("|> filter(fn: (r) => r[\"_measurement\"] == \"")
                .append(config.getMeasurement()).append(")");
        fluxSql.append("|> filter(fn: (r) => r[\"_field\"] == \"value\")");
        if (!tagCodes.isEmpty()) {
            fluxSql.append("|> filter(fn: (r) => r[\"tag\"] =~ /");
            List<String> filter = new ArrayList<>(tagCodes);
            fluxSql.append(String.join("|", filter));
            fluxSql.append("/)");
        }
        fluxSql.append("|> group(columns: [\"tag\"])");
        log.info(String.valueOf(fluxSql));
        return fluxSql;
    }

    private List<TagValue> getTagValues(StringBuilder fluxSql) {
        QueryApi query = client.getQueryApi();

        List<FluxTable> tables = query.query(fluxSql.toString());

        List<TagValue> values = new ArrayList<>();
        for (FluxTable fluxTable : tables) {
            List<FluxRecord> records = fluxTable.getRecords();
            for (FluxRecord fluxRecord : records) {
                String tag = String.valueOf(fluxRecord.getValueByKey("tag"));
                Double value = (Double) fluxRecord.getValueByKey("_value");
                Date time = fluxRecord.getTime() == null ? null
                        : new DateTime(fluxRecord.getTime().toString()).toDate();
                TagValue tagValue = new TagValue();
                tagValue.setValue(value);
                tagValue.setDataTime(time);
                tagValue.setTagCode(tag);
                tagValue.setQuality(Quality.GOOD);
                values.add(tagValue);
            }
        }
        return values;
    }

    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime,
                                     CollectionModes queryType) {
        DateTime begin = new DateTime(beginTime);
        DateTime end = new DateTime(endTime);
        return getTagValues(begin, end, tagCodes, queryType);
    }

    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime,
                                     CollectionModes queryType, GroupTimeType timeType) {
        DateTime begin = new DateTime(beginTime);
        DateTime end = new DateTime(endTime);
        StringBuilder timeRange = new StringBuilder()
                .append("|> range(start: ").append(Instant.ofEpochMilli(begin.getMillis()).toString())
                .append(", stop: ").append(Instant.ofEpochMilli(end.getMillis()).toString())
                .append(")");
        StringBuilder fluxSql = getStringBuilder(timeRange, tagCodes);
        fluxSql.append("|> aggregateWindow(every: 1")
                .append(timeType.name())
                .append(", fn: ")
                .append(queryType.name())
                .append(", createEmpty: false)")
                .append("|>yield(name: \"")
                .append(queryType.name())
                .append("\")");
        log.info(String.valueOf(fluxSql));
        return getTagValues(fluxSql);
    }

    private List<TagValue> getTagValues(DateTime begin, DateTime end, List<String> tagCodes) {
        return getTagValues(begin, end, tagCodes, CollectionModes.last);
    }

    private List<TagValue> getTagValues(DateTime begin, DateTime end, List<String> tagCodes,
                                        CollectionModes queryType) {
        StringBuilder timeRange = new StringBuilder()
                .append("|> range(start: ").append(Instant.ofEpochMilli(begin.getMillis()).toString())
                .append(", stop: ").append(Instant.ofEpochMilli(end.getMillis()).toString())
                .append(")");
        StringBuilder fluxSql = getStringBuilder(timeRange, tagCodes);
        fluxSql.append("|> ");
        fluxSql.append(queryType.name());
        fluxSql.append("()");
        log.info(String.valueOf(fluxSql));
        return getTagValues(fluxSql);
    }
}