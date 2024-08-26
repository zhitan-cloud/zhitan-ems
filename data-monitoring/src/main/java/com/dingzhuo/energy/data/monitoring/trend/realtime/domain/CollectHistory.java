package com.dingzhuo.energy.data.monitoring.trend.realtime.domain;

/**
 * 组态图展示的趋势选项卡下 数据表格对象
 */
public class CollectHistory {

    private String tagCode;
    private String tagName;
    private String unitId;
    private String dataTime;
    private Double value;
    private int quality;
    private String meteName;

    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public String getMeteName() {
        return meteName;
    }

    public void setMeteName(String meteName) {
        this.meteName = meteName;
    }
}
