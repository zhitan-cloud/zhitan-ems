package com.zhitan.web.controller.statisticalAnalysis;

import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.statisticalAnalysis.domain.dto.FlowChartsDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 能耗统计分析
 */
@Api(tags = "能耗统计分析")
@RestController
@RequestMapping("/statisticsAnalysis")
public class EnergyDataItemController {

    @Autowired
    private IDataItemService dataItemService;

    /**
     * 获取能流图形分析
     *
     * @param dto 请求参数
     * @return 结果
     */
    @Log(title = "获取能流图形分析")
    @ApiOperation(value = "获取能流图形分析", notes = "获取能流图形分析")
    @GetMapping(value = "/getFlowCharts")
    public AjaxResult getFlowCharts(@Validated FlowChartsDTO dto) {
        return AjaxResult.success(dataItemService.getFlowCharts(dto));
    }
}
