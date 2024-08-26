package com.dingzhuo.energy.project.plannedOutput.domain;

import com.dingzhuo.energy.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * plannedOutput对象 planned_output
 * 
 * @author sys
 * @date 2020-12-16
 */
public class energyMonitoring extends BaseEntity {
    private static final long serialVersionUID = 1L;
    private String peValue;
    private String peWaterValue;
    private String peCoalValue;
    private String peSteamValue;
    private String actualValue;
    private String planValue;
    private String poValue;
    private String poVaterValue;
    private String poCoalValue;
    private String poSteamValue;
    private String productid;
    private String productname;
    private String muid;

    public String getPeValue() {
        return peValue;
    }

    public void setPeValue(String peValue) {
        this.peValue = peValue;
    }

    public String getPeWaterValue() {
        return peWaterValue;
    }

    public void setPeWaterValue(String peWaterValue) {
        this.peWaterValue = peWaterValue;
    }

    public String getPeCoalValue() {
        return peCoalValue;
    }

    public void setPeCoalValue(String peCoalValue) {
        this.peCoalValue = peCoalValue;
    }

    public String getPeSteamValue() {
        return peSteamValue;
    }

    public void setPeSteamValue(String peSteamValue) {
        this.peSteamValue = peSteamValue;
    }

    public String getActualValue() {
        return actualValue;
    }

    public void setActualValue(String actualValue) {
        this.actualValue = actualValue;
    }

    public String getPlanValue() {
        return planValue;
    }

    public void setPlanValue(String planValue) {
        this.planValue = planValue;
    }

    public String getPoValue() {
        return poValue;
    }

    public void setPoValue(String poValue) {
        this.poValue = poValue;
    }

    public String getPoVaterValue() {
        return poVaterValue;
    }

    public void setPoVaterValue(String poVaterValue) {
        this.poVaterValue = poVaterValue;
    }

    public String getPoCoalValue() {
        return poCoalValue;
    }

    public void setPoCoalValue(String poCoalValue) {
        this.poCoalValue = poCoalValue;
    }

    public String getPoSteamValue() {
        return poSteamValue;
    }

    public void setPoSteamValue(String poSteamValue) {
        this.poSteamValue = poSteamValue;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getMuid() {
        return muid;
    }

    public void setMuid(String muid) {
        this.muid = muid;
    }
}