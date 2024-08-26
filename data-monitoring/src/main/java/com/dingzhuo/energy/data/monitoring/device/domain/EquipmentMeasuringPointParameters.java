package com.dingzhuo.energy.data.monitoring.device.domain;


/**
 * 设备监测参数列表对象
 *
 * @author
 * @date 2020-03-24
 */
public class EquipmentMeasuringPointParameters {


    private String code;

    private String indexName;

    private String indexUnit;

    private Double value;

    private String yValue;

    private String meteName;

    public String getyValue() {
        return yValue;
    }

    public void setyValue(String yValue) {
        this.yValue = yValue;
    }

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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getMeteName() {
        return meteName;
    }

    public void setMeteName(String meteName) {
        this.meteName = meteName;
    }
}
