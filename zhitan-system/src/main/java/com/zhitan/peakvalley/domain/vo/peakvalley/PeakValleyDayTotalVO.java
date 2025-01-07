package com.zhitan.peakvalley.domain.vo.peakvalley;

/**
 * description 尖峰评估
 *
 * @author hmj
 * @date 2024-10-14 9:40
 */
public class PeakValleyDayTotalVO {

    /**
     * totalPowerConsumption : 540.76
     * totalCost : 505.349
     * tipPowerConsumption : 184.85
     * tipPowerProportion : 34.35
     * tipPowerCost : 184.85
     * tipPowerCostProportion : 36.64
     * peakPowerConsumption : 179.2
     * peakPowerProportion : 33.33
     * peakPowerCost : 170.24
     * peakPowerCostProportion : 33.66
     * flatPowerConsumption : 88.91
     * flatPowerProportion : 16.16
     * flatPowerCost : 80.019
     * flatPowerCostProportion : 15.84
     * troughPowerConsumption : 87.8
     * troughPowerProportion : 16.16
     * troughPowerCost : 70.24
     * troughPowerCostProportion : 13.86
     */

    /**
     * 耗电量合计
     */
    private double totalPowerConsumption;

    /**
     * 费用合计
     */
    private double totalCost;
    /**
     * 尖时段用电量
     */
    private double tipPowerConsumption;
    /**
     * 尖时段用电量百分比
     */
    private double tipPowerProportion;
    /**
     * 尖时段费用
     */
    private double tipPowerCost;
    /**
     * 尖时段费用百分比
     */
    private double tipPowerCostProportion;
    /**
     * 峰时段用电量
     */
    private double peakPowerConsumption;
    /**
     * 峰时段用电百分比
     */
    private double peakPowerProportion;
    /**
     * 峰时段费用
     */
    private double peakPowerCost;
    /**
     * 峰时段费用百分比
     */
    private double peakPowerCostProportion;
    /**
     * 平时段用电量
     */
    private double flatPowerConsumption;
    /**
     * 平时段用百分比
     */
    private double flatPowerProportion;
    /**
     * 平时段费用
     */
    private double flatPowerCost;
    /**
     * 平时段费用百分比
     */
    private double flatPowerCostProportion;
    /**
     * 谷时段用电量
     */
    private double troughPowerConsumption;
    /**
     * 谷时段用电百分比
     */
    private double troughPowerProportion;
    /**
     * 谷时段费用
     */
    private double troughPowerCost;
    /**
     * 谷时段费用分不分
     */
    private double troughPowerCostProportion;

    public double getTotalPowerConsumption() {
        return totalPowerConsumption;
    }

    public void setTotalPowerConsumption(double totalPowerConsumption) {
        this.totalPowerConsumption = totalPowerConsumption;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTipPowerConsumption() {
        return tipPowerConsumption;
    }

    public void setTipPowerConsumption(double tipPowerConsumption) {
        this.tipPowerConsumption = tipPowerConsumption;
    }

    public double getTipPowerProportion() {
        return tipPowerProportion;
    }

    public void setTipPowerProportion(double tipPowerProportion) {
        this.tipPowerProportion = tipPowerProportion;
    }

    public double getTipPowerCost() {
        return tipPowerCost;
    }

    public void setTipPowerCost(double tipPowerCost) {
        this.tipPowerCost = tipPowerCost;
    }

    public double getTipPowerCostProportion() {
        return tipPowerCostProportion;
    }

    public void setTipPowerCostProportion(double tipPowerCostProportion) {
        this.tipPowerCostProportion = tipPowerCostProportion;
    }

    public double getPeakPowerConsumption() {
        return peakPowerConsumption;
    }

    public void setPeakPowerConsumption(double peakPowerConsumption) {
        this.peakPowerConsumption = peakPowerConsumption;
    }

    public double getPeakPowerProportion() {
        return peakPowerProportion;
    }

    public void setPeakPowerProportion(double peakPowerProportion) {
        this.peakPowerProportion = peakPowerProportion;
    }

    public double getPeakPowerCost() {
        return peakPowerCost;
    }

    public void setPeakPowerCost(double peakPowerCost) {
        this.peakPowerCost = peakPowerCost;
    }

    public double getPeakPowerCostProportion() {
        return peakPowerCostProportion;
    }

    public void setPeakPowerCostProportion(double peakPowerCostProportion) {
        this.peakPowerCostProportion = peakPowerCostProportion;
    }

    public double getFlatPowerConsumption() {
        return flatPowerConsumption;
    }

    public void setFlatPowerConsumption(double flatPowerConsumption) {
        this.flatPowerConsumption = flatPowerConsumption;
    }

    public double getFlatPowerProportion() {
        return flatPowerProportion;
    }

    public void setFlatPowerProportion(double flatPowerProportion) {
        this.flatPowerProportion = flatPowerProportion;
    }

    public double getFlatPowerCost() {
        return flatPowerCost;
    }

    public void setFlatPowerCost(double flatPowerCost) {
        this.flatPowerCost = flatPowerCost;
    }

    public double getFlatPowerCostProportion() {
        return flatPowerCostProportion;
    }

    public void setFlatPowerCostProportion(double flatPowerCostProportion) {
        this.flatPowerCostProportion = flatPowerCostProportion;
    }

    public double getTroughPowerConsumption() {
        return troughPowerConsumption;
    }

    public void setTroughPowerConsumption(double troughPowerConsumption) {
        this.troughPowerConsumption = troughPowerConsumption;
    }

    public double getTroughPowerProportion() {
        return troughPowerProportion;
    }

    public void setTroughPowerProportion(double troughPowerProportion) {
        this.troughPowerProportion = troughPowerProportion;
    }

    public double getTroughPowerCost() {
        return troughPowerCost;
    }

    public void setTroughPowerCost(double troughPowerCost) {
        this.troughPowerCost = troughPowerCost;
    }

    public double getTroughPowerCostProportion() {
        return troughPowerCostProportion;
    }

    public void setTroughPowerCostProportion(double troughPowerCostProportion) {
        this.troughPowerCostProportion = troughPowerCostProportion;
    }

}
