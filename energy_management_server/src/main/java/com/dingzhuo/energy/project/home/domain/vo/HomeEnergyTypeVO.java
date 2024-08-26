package com.dingzhuo.energy.project.home.domain.vo;


import lombok.Data;


/**
 * 首页能源类型返回vo
 *
 * @Author: Zhujw
 * @Date: 2023/3/2
 */
@Data
public class HomeEnergyTypeVO {

    /**
     * 能源类型
     */
    private String energy;

    /**
     * 能源类型名称
     */
    private String energyName;
}