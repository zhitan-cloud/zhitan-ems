package com.zhitan.consumptionanalysis.domain.vo;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-16 18:04
 */
public class ProductEnergyAnalysisData {
    
    private String dateTime;
    private double productCount;
    private double energyCount;
    private double average;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getProductCount() {
        return productCount;
    }

    public void setProductCount(double productCount) {
        this.productCount = productCount;
    }

    public double getEnergyCount() {
        return energyCount;
    }

    public void setEnergyCount(double energyCount) {
        this.energyCount = energyCount;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
