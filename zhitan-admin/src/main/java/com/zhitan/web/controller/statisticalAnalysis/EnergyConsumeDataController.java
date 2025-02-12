package com.zhitan.web.controller.statisticalAnalysis;

import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.zhitan.statisticalAnalysis.service.IEnergyConsumeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @Description: 能源消耗统计分析
 * @author: yxw
 * @date: 2022年04月12日 14:11
 */
@Api(tags = "能耗统计分析")
@RestController
@RequestMapping("/energyTypeAnalysis")
@Slf4j
public class EnergyConsumeDataController {
    @Autowired
    private IEnergyConsumeDataService energyConsumeDataService;

    /**
     * 成本趋势分析（能源消耗成本）- 获取表格列表数据
     *
     * @param pageNo   页码数
     * @param pageSize 每页数据多少
     * @param timeCode 时间值   与时间类型对应：2022-03-21/2022-03/2022
     * @param timeType 时间类型 DAY/MONTH/YEAR
     * @param energyType 能源类型
     * @param modelCode 模型Code
     * @return
     */
    @Log(title = "能耗统计分析-成本趋势分析（能源消耗成本）- 获取表格列表数据")
    @ApiOperation(value = "能耗统计分析-成本趋势分析（能源消耗成本）- 获取表格列表数据", notes = "能耗统计分析-成本趋势分析（能源消耗成本）- 获取表格列表数据")
    @GetMapping(value = "/listEnergyCostTrend")
    public AjaxResult listEnergyCostTrend(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                          @RequestParam(name = "timeCode") String timeCode,
                                          @RequestParam(name = "timeType") String timeType,
                                          @RequestParam(name = "energyType") String energyType,
                                          @RequestParam(name = "modelCode") String modelCode) {
        return AjaxResult.success(energyConsumeDataService.listEnergyCostTrend(pageNo, pageSize, timeCode, timeType,energyType,
                modelCode));
    }

    /**
     * 成本趋势分析（能源消耗成本）
     *
     * @param timeCode 时间值   与时间类型对应：2022-03-21/2022-03/2022
     * @param timeType 时间类型 DAY/MONTH/YEAR
     * @param modelCode   模型Code
     * @param energyType 能源类型
     * @return
     */
    @Log(title = "能耗统计分析-成本趋势分析（能源消耗成本）")
    @ApiOperation(value = "能耗统计分析-成本趋势分析（能源消耗成本）", notes = "能耗统计分析-成本趋势分析（能源消耗成本）")
    @GetMapping(value = "/listEnergyCostTrendDetail")
    public AjaxResult listEnergyCostTrendDetail(@RequestParam(name = "timeCode") String timeCode,
                                                                                @RequestParam(name = "timeType") String timeType,
                                                                                @RequestParam(name = "modelCode") String modelCode,
                                                                                @RequestParam("energyType") String energyType) {
        return AjaxResult.success(energyConsumeDataService.listEnergyCostTrendDetail(timeCode, timeType, modelCode, energyType));
    }

}
