package com.dingzhuo.energy.data.monitoring.device.domain.vo;


import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;

/**
 * 设备点位实时数据返回 Excel
 *
 * @Author: Zhujw
 * @Date: 2023/3/6
 */
public class EquipmentPointParametersExcel {

    /**
     * 点位名称
     */
    @Excel(name = "点位名称")
    private String indexName;

    /**
     * 值
     */
    @Excel(name = "当前值")
    private String value;

    /**
     * 单位
     */
    @Excel(name = "单位")
    private String indexUnit;

    /**
     * 时间字符串
     */
    @Excel(name = "时间")
    private String timeString;


    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getIndexUnit() {
        return indexUnit;
    }

    public void setIndexUnit(String indexUnit) {
        this.indexUnit = indexUnit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }
}
