package com.dingzhuo.energy.data.monitoring.trend.history.domain.vo;


/**
 * 历史监测数据返回 VO
 *
 * @Author: Zhujw
 * @Date: 2023/3/7
 */
public class HistoricalDataVO {

    /**
     * 点位id
     */
    private String indexId;

    /**
     * 点位名称
     */
    private String indexName;

    /**
     * 时间
     */
    private String dataTime;

    /**
     * 值
     */
    private String value;

    /**
     * 使用量
     */
    private String usedValue;


    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUsedValue() {
        return usedValue;
    }

    public void setUsedValue(String usedValue) {
        this.usedValue = usedValue;
    }
}