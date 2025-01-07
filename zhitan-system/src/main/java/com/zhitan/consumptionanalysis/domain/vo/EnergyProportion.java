package com.zhitan.consumptionanalysis.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * description 个介质能耗占比
 *
 * @author hmj
 * @date 2024-10-21 17:26
 */
@Getter
@Setter
public class EnergyProportion {
    public String energyNo;
    public String energyName;
    public Double count;
    public Double percentage;
}
