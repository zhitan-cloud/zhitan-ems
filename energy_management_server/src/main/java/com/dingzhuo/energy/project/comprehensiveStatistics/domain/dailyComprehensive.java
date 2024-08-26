package com.dingzhuo.energy.project.comprehensiveStatistics.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author sys
 * @date 2020-03-25
 */
@Data
public class dailyComprehensive implements Serializable {
    private static final long serialVersionUID = 1L;
    private String indexId;
    @Excel(name = "指标名称")
    private String indexName;
    private String value;
    private Date dataTime;
    private String timeType;
    private String timeCode;
    private String unitId;
    @Excel(name = "0时")
    private String value0;
    @Excel(name = "1时")
    private String value1;
    @Excel(name = "2时")
    private String value2;
    @Excel(name = "3时")
    private String value3;
    @Excel(name = "4时")
    private String value4;
    @Excel(name = "5时")
    private String value5;
    @Excel(name = "6时")
    private String value6;
    @Excel(name = "7时")
    private String value7;
    @Excel(name = "8时")
    private String value8;
    @Excel(name = "9时")
    private String value9;
    @Excel(name = "10时")
    private String value10;
    @Excel(name = "11时")
    private String value11;
    @Excel(name = "12时")
    private String value12;
    @Excel(name = "13时")
    private String value13;
    @Excel(name = "14时")
    private String value14;
    @Excel(name = "15时")
    private String value15;
    @Excel(name = "16时")
    private String value16;
    @Excel(name = "17时")
    private String value17;
    @Excel(name = "18时")
    private String value18;
    @Excel(name = "19时")
    private String value19;
    @Excel(name = "20时")
    private String value20;
    @Excel(name = "21时")
    private String value21;
    @Excel(name = "22时")
    private String value22;
    @Excel(name = "23时")
    private String value23;
    private List<Map> tablehead =new ArrayList<>();
    private List<dailyComprehensive> tabledata =new ArrayList<>();

}
