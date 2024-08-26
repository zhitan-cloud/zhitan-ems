package com.dingzhuo.energy.project.energyStatistics.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class energyAnalysis implements Serializable {
    private static final long serialVersionUID = -2777479013884125925L;
    private String prop;
    private Double value;
    private Date dataTime;
    private List<energyAnalysis> analysisTableDate = new ArrayList<>();
    private List<Map> analysisTableHead = new ArrayList<Map>();
    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
    public Date getDataTime() {
        return dataTime;
    }

    public void setDataTime(Date dataTime) {
        this.dataTime = dataTime;
    }

    public List<energyAnalysis> getAnalysisTableDate() {
        return analysisTableDate;
    }

    public void setAnalysisTableDate(List<energyAnalysis> analysisTableDate) {
        this.analysisTableDate = analysisTableDate;
    }

    public List<Map> getAnalysisTableHead() {
        return analysisTableHead;
    }

    public void setAnalysisTableHead(List<Map> analysisTableHead) {
        this.analysisTableHead = analysisTableHead;
    }
}
