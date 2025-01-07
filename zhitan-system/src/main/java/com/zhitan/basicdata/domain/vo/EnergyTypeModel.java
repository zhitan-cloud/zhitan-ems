package com.zhitan.basicdata.domain.vo;

import com.zhitan.common.annotation.Excel;
import lombok.Data;


/**
 * 能源类型信息类
 *
 * @Author: Zhujw
 * @Date: 2023/5/23
 */
@Data
public class EnergyTypeModel {

    /**
     * 能源名称
     */
    @Excel(name = "能源名称")
    private String enername;

    /**
     * 能源编号
     */
    @Excel(name = "能源编号")
    private String enersno;

}