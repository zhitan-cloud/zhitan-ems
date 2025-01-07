package com.zhitan.realtimedata.domain.vo;

/**
 * @Description 设备点位实时数据返回 vo
 * @Author zhoubg
 * @date 2024-10-15
 **/

public class EquipmentPointParametersVO {

    /**
     * 点位编码
     */
    private String code;

    /**
     * 点位名称
     */
    private String indexName;

    /**
     * 时间字符串
     */
    private String timeString;

    /**
     * 点位单位
     */
    private String indexUnit;

    /**
     * 值
     */
    private String value;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
