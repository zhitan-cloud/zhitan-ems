package com.zhitan.consumptionanalysis.domain.vo;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-16 18:04
 */
public class ConsumptionAnalysisData {

    /**
     * currentTime : 2024-10-01 00:00:00
     * currentValue : 33.97
     * compareTime : 2023-10-01 00:00:00
     * contrastValues : 71.49
     * ratio : -52
     */
    
    private String currentTime;
    private double currentValue;
    private String compareTime;
    private double compareValue;
    private double ratio;

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public String getCompareTime() {
        return compareTime;
    }

    public void setCompareTime(String compareTime) {
        this.compareTime = compareTime;
    }

    public double getCompareValue() {
        return compareValue;
    }

    public void setCompareValue(double compareValue) {
        this.compareValue = compareValue;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
