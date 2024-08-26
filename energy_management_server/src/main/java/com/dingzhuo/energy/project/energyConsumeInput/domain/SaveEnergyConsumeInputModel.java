package com.dingzhuo.energy.project.energyConsumeInput.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年03月17日 11:13
 */
@Data
@ApiModel(value = "阶段录入model")
public class SaveEnergyConsumeInputModel {

    @ApiModelProperty(value = "录入数据列表")
    private List<EnergyConsumeInput> models;
}
