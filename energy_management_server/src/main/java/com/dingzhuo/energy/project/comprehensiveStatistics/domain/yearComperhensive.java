package com.dingzhuo.energy.project.comprehensiveStatistics.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;

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
public class yearComperhensive implements Serializable {
    private static final long serialVersionUID = 1L;
    private String indexId;
    @Excel(name = "指标名称")
    private String indexName;
    private String value;
    private Date dataTime;
    private String timeType;
    private String unitId;
    @Excel(name = "1月")
    private Double value1;
    @Excel(name = "2月")
    private Double value2;
    @Excel(name = "3月")
    private Double value3;
    @Excel(name = "4月")
    private Double value4;
    @Excel(name = "5月")
    private Double value5;
    @Excel(name = "6月")
    private Double value6;
    @Excel(name = "7月")
    private Double value7;
    @Excel(name = "8月")
    private Double value8;
    @Excel(name = "9月")
    private Double value9;
    @Excel(name = "10月")
    private Double value10;
    @Excel(name = "11月")
    private Double value11;
    @Excel(name = "12月")
    private Double value12;
    private String timeCode;
    private List<Map> tablehead =new ArrayList<>();
    private List<dailyReport> tabledata =new ArrayList<>();
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
    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTimeType() {
        return timeType;
    }

    public void setTimeType(String timeType) {
        this.timeType = timeType;
    }

    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public Double getValue1() {
        return value1;
    }

    public void setValue1(Double value1) {
        this.value1 = value1;
    }

    public Double getValue2() {
        return value2;
    }

    public void setValue2(Double value2) {
        this.value2 = value2;
    }

    public Double getValue3() {
        return value3;
    }

    public void setValue3(Double value3) {
        this.value3 = value3;
    }

    public Double getValue4() {
        return value4;
    }

    public void setValue4(Double value4) {
        this.value4 = value4;
    }

    public Double getValue5() {
        return value5;
    }

    public void setValue5(Double value5) {
        this.value5 = value5;
    }

    public Double getValue6() {
        return value6;
    }

    public void setValue6(Double value6) {
        this.value6 = value6;
    }

    public Double getValue7() {
        return value7;
    }

    public void setValue7(Double value7) {
        this.value7 = value7;
    }

    public Double getValue8() {
        return value8;
    }

    public void setValue8(Double value8) {
        this.value8 = value8;
    }

    public Double getValue9() {
        return value9;
    }

    public void setValue9(Double value9) {
        this.value9 = value9;
    }

    public Double getValue10() {
        return value10;
    }

    public void setValue10(Double value10) {
        this.value10 = value10;
    }

    public Double getValue11() {
        return value11;
    }

    public void setValue11(Double value11) {
        this.value11 = value11;
    }

    public Double getValue12() {
        return value12;
    }

    public void setValue12(Double value12) {
        this.value12 = value12;
    }
    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }
    public List<Map> getTablehead() {
        return tablehead;
    }

    public void setTablehead(List<Map> tablehead) {
        this.tablehead = tablehead;
    }
    public List<dailyReport> getTabledata() {
        return tabledata;
    }

    public void setTabledata(List<dailyReport> tabledata) {
        this.tabledata = tabledata;
    }
}
