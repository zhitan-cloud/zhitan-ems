package com.dingzhuo.energy.project.keyEquipment.domain;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
public class dailyKeyEquipment implements Serializable {
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

    public Double getValue13() {
        return value13;
    }

    public void setValue13(Double value13) {
        this.value13 = value13;
    }

    public Double getValue14() {
        return value14;
    }

    public void setValue14(Double value14) {
        this.value14 = value14;
    }

    public Double getValue15() {
        return value15;
    }

    public void setValue15(Double value15) {
        this.value15 = value15;
    }

    public Double getValue16() {
        return value16;
    }

    public void setValue16(Double value16) {
        this.value16 = value16;
    }

    public Double getValue17() {
        return value17;
    }

    public void setValue17(Double value17) {
        this.value17 = value17;
    }

    public Double getValue18() {
        return value18;
    }

    public void setValue18(Double value18) {
        this.value18 = value18;
    }

    public Double getValue19() {
        return value19;
    }

    public void setValue19(Double value19) {
        this.value19 = value19;
    }

    public Double getValue20() {
        return value20;
    }

    public void setValue20(Double value20) {
        this.value20 = value20;
    }

    public Double getValue21() {
        return value21;
    }

    public void setValue21(Double value21) {
        this.value21 = value21;
    }

    public Double getValue22() {
        return value22;
    }

    public void setValue22(Double value22) {
        this.value22 = value22;
    }

    public Double getValue23() {
        return value23;
    }

    public void setValue23(Double value23) {
        this.value23 = value23;
    }

    public Double getValue0() {
        return value0;
    }

    public void setValue0(Double value0) {
        this.value0 = value0;
    }

    public String getTimeCode() {
        return timeCode;
    }

    public void setTimeCode(String timeCode) {
        this.timeCode = timeCode;
    }

}
