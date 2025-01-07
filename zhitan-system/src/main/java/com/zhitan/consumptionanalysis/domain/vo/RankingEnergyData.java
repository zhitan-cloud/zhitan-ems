package com.zhitan.consumptionanalysis.domain.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RankingEnergyData {
    public String nodeId;
    public String nodeName;
    public String energyTypeNo;
    public String energyTypeName;
    public Double energyConsumption;
}
