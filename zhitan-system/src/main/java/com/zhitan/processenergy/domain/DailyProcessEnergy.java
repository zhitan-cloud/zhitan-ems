package com.zhitan.processenergy.domain;

import com.zhitan.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *工序能耗 日
 *
 * @author sys
 * @date 2021-01-11
 */
@Data
public class DailyProcessEnergy implements Serializable {
    private static final long serialVersionUID = 1L;
    private String indexId;
    @Excel(name = "指标名称")
    private String indexName;
    private String value;
    private Date dataTime;
    private String timeType;
    private String timeCode;
    private String unitId;
    @Excel(name = "1时")
    private Double value1;
    @Excel(name = "2时")
    private Double value2;
    @Excel(name = "3时")
    private Double value3;
    @Excel(name = "4时")
    private Double value4;
    @Excel(name = "5时")
    private Double value5;
    @Excel(name = "6时")
    private Double value6;
    @Excel(name = "7时")
    private Double value7;
    @Excel(name = "8时")
    private Double value8;
    @Excel(name = "9时")
    private Double value9;
    @Excel(name = "10时")
    private Double value10;
    @Excel(name = "11时")
    private Double value11;
    @Excel(name = "12时")
    private Double value12;
    @Excel(name = "13时")
    private Double value13;
    @Excel(name = "14时")
    private Double value14;
    @Excel(name = "15时")
    private Double value15;
    @Excel(name = "16时")
    private Double value16;
    @Excel(name = "17时")
    private Double value17;
    @Excel(name = "18时")
    private Double value18;
    @Excel(name = "19时")
    private Double value19;
    @Excel(name = "20时")
    private Double value20;
    @Excel(name = "21时")
    private Double value21;
    @Excel(name = "22时")
    private Double value22;
    @Excel(name = "23时")
    private Double value23;
    @Excel(name = "0时")
    private Double value0;
}
