package com.zhitan.home.domain.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * description 首页-能源趋势
 *
 * @author hmj
 * @date 2024-11-01 17:10
 */
@Getter
@Setter
public class HomeEnergyConsumptionTrendVO {
    private String[] xdata;
    private Double[][] ydata;
    private String[] legend;
}
