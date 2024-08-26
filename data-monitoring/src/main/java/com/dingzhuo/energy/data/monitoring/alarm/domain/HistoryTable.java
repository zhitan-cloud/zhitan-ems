package com.dingzhuo.energy.data.monitoring.alarm.domain;


import com.dingzhuo.energy.framework.aspectj.lang.annotation.Excel;

/**
 * 历史表格导出
 *
 * @author
 * @date 2020-03-20
 */
public class HistoryTable {

    /**
     * 报警时间
     */
    @Excel(name = "报警时间",width = 30)
    private String alarmTime;

    /**
     * 指标编码
     */
   @Excel(name = "指标编码")
   private String code;

    /**
     * 指标名称
     */
    @Excel(name = "指标名称")
    private String indexName;

    /**
     * 单位名称
     */
    @Excel(name = "单位名称")
    private String unitName;

    /**
     * 实时值
     */
    @Excel(name = "实时值")
    private Double earlyWarningValue;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getEarlyWarningValue() {
        return earlyWarningValue;
    }

    public void setEarlyWarningValue(Double earlyWarningValue) {
        this.earlyWarningValue = earlyWarningValue;
    }
}
