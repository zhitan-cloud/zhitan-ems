package com.dingzhuo.energy.project.home.domain.vo;


import lombok.Data;

/**
 * 首页室外温度返回 VO
 */
@Data
public class HomeOutdoorTemperatureVO {


    /**
     * 温度
     */
    private String temperature = "--";

    /**
     * 湿度
     */
    private String humidity = "--";

}