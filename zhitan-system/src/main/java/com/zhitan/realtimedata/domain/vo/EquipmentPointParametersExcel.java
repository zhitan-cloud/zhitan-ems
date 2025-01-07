package com.zhitan.realtimedata.domain.vo;

import com.zhitan.common.annotation.Excel;

/**
 * @Description 设备点位实时数据返回 Excel
 * @Author zhoubg
 * @date 2024-10-17
 **/

public class EquipmentPointParametersExcel {

    /**
     * 点位名称
     *
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
