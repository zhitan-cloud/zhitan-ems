package com.zhitan.web.controller.statisticalAnalysis;

import com.zhitan.common.annotation.Log;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.statisticalAnalysis.domain.dto.FlowChartsDTO;
import com.zhitan.statisticalAnalysis.domain.vo.QueryCompareRequest;
import com.zhitan.statisticalAnalysis.service.IEnergyConsumeDataService;
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

    @Autowired
    private IEnergyConsumeDataService energyConsumeDataService;

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

    @Log(title = "能耗统计分析-获取同比分析列表数据")
    @ApiOperation(value = "能耗统计分析-获取同比分析列表数据", notes = "能耗统计分析-获取同比分析列表数据")
    @GetMapping(value = "/querySameCompareList")
    public AjaxResult querySameCompareList(@Validated QueryCompareRequest queryCompareRequest) {
        return AjaxResult.success(energyConsumeDataService.listEnergyTypeYoyInfo(queryCompareRequest, CommonConst.ENERGY_COMPARISON_YOY));
    }

    /**
     * 获取环比分析数据
     * <p>
     * 通过自己的服务访问地址：http://localhost:7005/fengniao/energyDataItem/queryLoopCompare?timeType=1
     * 通过网关访问地址：http://localhost:9999/jeecg-fengniao/fengniao/energyDataItem/queryLoopCompare?timeType=1
     *
     * @param queryCompareRequest
     * @return
     */
    @Log(title = "能耗统计分析-获取环比分析列表数据")
    @ApiOperation(value = "能耗统计分析-获取环比分析列表数据", notes = "能耗统计分析-获取环比分析列表数据")
    @GetMapping(value = "/queryLoopCompareList")
    public AjaxResult queryLoopCompareList(@Validated QueryCompareRequest queryCompareRequest) {

        return AjaxResult.success(energyConsumeDataService.listEnergyTypeYoyInfo(queryCompareRequest, CommonConst.ENERGY_COMPARISON_MOM));
    }
}
