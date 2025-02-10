package com.zhitan.realtimedata.domain.vo;

import lombok.Data;

/**
 * 设备监测参数列表对象
 */
@Data
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
}
