package com.zhitan.consumptionanalysis.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-18 11:16
 */
@Getter
@Setter
public class RankingChartData {
    public String name;
    public List<Double> data;
}
