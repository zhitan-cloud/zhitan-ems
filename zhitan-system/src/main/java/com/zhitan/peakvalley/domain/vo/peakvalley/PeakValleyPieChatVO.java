package com.zhitan.peakvalley.domain.vo.peakvalley;

/**
 * description 尖峰平谷饼状图
 *
 * @author hmj
 * @date 2024-09-24 16:39
 */
public class PeakValleyPieChatVO {
    /**
     * tip : 35.69
     * peak : 64.31
     * flat : 0.00
     * trough : 0.00
     */

    private String tip;
    private String peak;
    private String flat;
    private String trough;

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getPeak() {
        return peak;
    }

    public void setPeak(String peak) {
        this.peak = peak;
    }

    public String getFlat() {
        return flat;
    }

    public void setFlat(String flat) {
        this.flat = flat;
    }

    public String getTrough() {
        return trough;
    }

    public void setTrough(String trough) {
        this.trough = trough;
    }
}
