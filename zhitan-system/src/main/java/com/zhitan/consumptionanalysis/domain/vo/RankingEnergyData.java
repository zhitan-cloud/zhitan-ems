package com.zhitan.consumptionanalysis.domain.vo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class RankingEnergyData {

    public String nodeId;

    public String nodeName;
    public String energyTypeNo;
    public String energyTypeName;
    public Double energyConsumption;
}
