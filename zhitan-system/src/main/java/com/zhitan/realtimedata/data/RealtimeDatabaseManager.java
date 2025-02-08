package com.zhitan.realtimedata.data;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.domain.HealthCheck;
import com.zhitan.common.enums.CollectionModes;
import com.zhitan.common.enums.RetrievalModes;
import com.zhitan.realtimedata.config.RtdbConfig;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.service.RealtimeDatabase;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 范新富 实时数据库访问管理.
 */
@Component
public class RealtimeDatabaseManager {

    private final RealtimeDatabase connection;

    private final Logger logger = LogManager.getLogger(RealtimeDatabaseManager.class);

    public RealtimeDatabaseManager(RtdbConfig config) {
    connection = new VirtualRtdb();
        try {
            InfluxDBClient influxDBClient = InfluxDBClientFactory.create(config.getHost(),
                    config.getToken().toCharArray(), config.getOrg(), config.getBucket());

            logger.error("--------------------实时库连接成功--------------------");
            HealthCheck health = influxDBClient.health();
            if (health.getStatus() == HealthCheck.StatusEnum.FAIL) {
                influxDBClient.close();
            }
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public TagValue retrieve(String tagCode) {
        List<String> tagCodes = new ArrayList<>();
        tagCodes.add(tagCode);
        List<TagValue> tagValues = retrieve(tagCodes);
        if (!tagValues.isEmpty()) {
            return tagValues.get(0);
        }

        return null;
    }

    public List<TagValue> retrieve(List<String> tagCodes) {
        List<TagValue> tagValues = new ArrayList<>();
        try {
            tagValues = connection.retrieve(tagCodes);
        } catch (Exception e) {
            logger.error(e);
        }

        return tagValues;
    }

    public TagValue retrieve(String tagCode, Date dataTime, String timeCode) {
        List<String> tagCodes = new ArrayList<>();
        tagCodes.add(tagCode);
        List<TagValue> tagValues = retrieve(tagCodes, dataTime, timeCode);
        if (!tagValues.isEmpty()) {
            return tagValues.get(0);
        }

        return null;
    }

    public List<TagValue> retrieve(List<String> tagCodes, Date dataTime, String timeCode) {
        List<TagValue> tagValues = new ArrayList<>();
        try {
            List<TagValue> tmp = connection.retrieve(tagCodes, dataTime, timeCode);
            for (String tagCode : tagCodes) {
                Optional<TagValue> tagValue = tmp.stream()
                        .filter(f -> StringUtils.equalsIgnoreCase(f.getTagCode(), tagCode)).findAny();
                TagValue value;
                if (!tagValue.isPresent()) {
                    value = new TagValue();
                    value.setTagCode(tagCode);
                    value.setDataTime(dataTime);
                } else {
                    value = tagValue.get();
                }

                tagValues.add(value);
            }
        } catch (Exception e1) {
            logger.error(e1);
        }

        return tagValues;
    }

    public List<TagValue> retrieve(String tagCode, Date beginTime, Date endTime,
                                   RetrievalModes retrievalModes, int pointCount) {
        List<String> tagCodes = new ArrayList<>();
        tagCodes.add(tagCode);
        return retrieve(tagCodes, beginTime, endTime, retrievalModes, pointCount);
    }

    public List<TagValue> retrieve(List<String> tagCodes, Date beginTime, Date endTime,
                                   RetrievalModes retrievalModes, int pointCount) {
        List<TagValue> tagValues = new ArrayList<>();
        try {
            tagValues = connection.retrieve(tagCodes, beginTime, endTime, retrievalModes, pointCount);
        } catch (Exception e1) {
            logger.error(e1);
        }

        return tagValues;
    }

    public TagValue statistics(String tagCode, Date beginTime, Date endTime,
                               CollectionModes collectionModes) {
        List<String> tagCodes = new ArrayList<>();
        tagCodes.add(tagCode);
        List<TagValue> tagValues = statistics(tagCodes, beginTime, endTime, collectionModes);
        if (!tagValues.isEmpty()) {
            return tagValues.get(0);
        }

        return null;
    }

    public List<TagValue> statistics(List<String> tagCodes, Date beginTime, Date endTime,
                                     CollectionModes collectionModes) {
        List<TagValue> tagValues = new ArrayList<>();
        try {
            tagValues = connection.statistics(tagCodes, beginTime, endTime, collectionModes);
        } catch (Exception e1) {
            logger.error(e1);
        }

        return tagValues;
    }

    public void storeData(List<TagValue> tagValues) {
        try {
            connection.storeData(tagValues);
        } catch (Exception e1) {
            logger.error(e1);
        }
    }

    public void insertData(List<TagValue> tagValues) {
        try {
            connection.insertData(tagValues);
        } catch (Exception e1) {
            logger.error(e1);
        }
    }
}
