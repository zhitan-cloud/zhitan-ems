package com.zhitan.realtimedata.domain.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description 实时数据-能源点位数据
 *
 * @Author zhoubg
 * @date 2024-10-16
 **/
@Data
public class RealTimeIndexModel {

    /**
     * 名称
     */
    private String name;

    /**
     * 点位编码
     */
    private String indexCode;

    /**
     * 单位
     */
    private String unit;

    /**
     * 值
     */
    private BigDecimal value;
    
    /**
     * 时间
     */
    private String dataTime;
}
