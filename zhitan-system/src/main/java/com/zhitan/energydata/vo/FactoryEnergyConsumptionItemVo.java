package com.zhitan.energydata.vo;

import com.zhitan.home.domain.vo.HomeEnergyStatisticsVO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class FactoryEnergyConsumptionItemVo {

    /**
     * 厂区主键
     */
    private String nodeId;
    /**
     * 厂区名称
     */
    private String nodeName;

    private BigDecimal kgceCount;

    private BigDecimal kgceTonCount;

    List<HomeEnergyStatisticsVO> energyItemList;



}
