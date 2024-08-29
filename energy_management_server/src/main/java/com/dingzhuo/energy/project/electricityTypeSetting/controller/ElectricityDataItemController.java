package com.dingzhuo.energy.project.electricityTypeSetting.controller;


import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityDataItemListDTO;
import com.dingzhuo.energy.project.electricityTypeSetting.service.IElectricityDataItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 尖峰平谷数据Controller
 *
 * @author ruoyi
 * @date 2024-06-19
 */
@RestController
@RequestMapping("/electricityDataItem")
@Api(tags = "尖峰平谷数据")
public class ElectricityDataItemController extends BaseController {

    @Resource
    private IElectricityDataItemService rulesService;


    /**
     * 获取尖峰平谷数据统计
     */
    @GetMapping("/getDataStatistics")
    @ApiOperation(value = "获取尖峰平谷数据统计")
    public AjaxResult getDataStatistics(ElectricityDataItemListDTO dto) {
        return AjaxResult.success(rulesService.getDataStatistics(dto));
    }

}
