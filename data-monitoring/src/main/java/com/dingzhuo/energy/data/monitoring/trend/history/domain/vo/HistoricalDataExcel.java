package com.dingzhuo.energy.data.monitoring.trend.history.domain.vo;


import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;

/**
 * 历史监测数据返回 Excel
 *
 * @Author: Zhujw
 * @Date: 2023/3/7
 */
public class HistoricalDataExcel {

    /**
     * 点位名称
     */
    @Excel(name = "点位名称")
    private String indexName;

    /**
     * 值
     */
    @Excel(name = "值")
    private String value;

    /**
     * 使用量
     */
    @Excel(name = "使用量")
    private String usedValue;

    /**
     * 时间
     */
    @Excel(name = "时间")
    private String dataTime;


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