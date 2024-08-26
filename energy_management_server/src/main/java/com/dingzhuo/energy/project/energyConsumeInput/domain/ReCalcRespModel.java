package com.dingzhuo.energy.project.energyConsumeInput.domain;

import lombok.Data;

/**
 * @Description: 计算引擎重算接口返回结果
 * @author: yxw
 * @date: 2022年03月18日 18:07
 */
@Data
public class ReCalcRespModel {
    /** 返回信息 */
    private String msg;
    /**
     * 接口请求结果
     */
    private int code;
}
