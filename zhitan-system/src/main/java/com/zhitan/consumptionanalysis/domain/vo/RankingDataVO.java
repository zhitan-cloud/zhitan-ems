package com.zhitan.consumptionanalysis.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-18 10:43
 */
@Getter
@Setter
public class RankingDataVO {
    public String nodeId;
    public String nodeName;
    public List<RankingEnergyData> data;
}
