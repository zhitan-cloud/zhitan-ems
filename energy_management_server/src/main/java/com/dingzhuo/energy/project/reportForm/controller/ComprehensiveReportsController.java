package com.dingzhuo.energy.project.reportForm.controller;

import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.reportForm.domain.dto.ComprehensiveReportsDTO;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveConsumptionRatioVO;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveReportsItem;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveUnitConsumptionChartVO;
import com.dingzhuo.energy.project.reportForm.service.IComprehensiveReportsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 综合报表
 *
 * @Author: Zhujw
 * @Date: 2023/3/16
 */
@RestController
@RequestMapping("/report/comprehensive")
class ComprehensiveReportsController extends BaseController {
    @Autowired
    private IComprehensiveReportsService service;

    /**
     * 综合报表查询列表信息
     */
    //    @PreAuthorize("@ss.hasPermi('dailyReport:list')")
    @GetMapping("/list")
    @ApiOperation(value = "综合报表查询列表信息", notes = "首页获取能源分时监测")
    public AjaxResult list(@Validated ComprehensiveReportsDTO dto) {
        List<ComprehensiveReportsItem> reportsItemList = service.listComprehensiveReport(dto);
        return AjaxResult.success(reportsItemList);
    }

    /**
     * 综合报表查询用能单元柱状图信息
     */
    //    @PreAuthorize("@ss.hasPermi('dailyReport:list')")
    @GetMapping("/listEnergyUnitChart")
    @ApiOperation(value = "综合报表查询信息", notes = "首页获取能源分时监测")
    public AjaxResult listUnitChart(@Validated ComprehensiveReportsDTO dto) {
        List<ComprehensiveReportsItem> chart = service.listEnergyUnitComprehensiveReport(dto);
        return AjaxResult.success(chart);
    }

    /**
     * 综合报表查询能耗占比信息
     */
    //    @PreAuthorize("@ss.hasPermi('dailyReport:list')")
    @GetMapping("/getEnergyConsumptionRatio")
    @ApiOperation(value = "综合报表查询能耗占比信息", notes = "综合报表查询能耗占比信息")
    public AjaxResult getEnergyConsumptionRatio(@Validated ComprehensiveReportsDTO dto) {
        List<ComprehensiveConsumptionRatioVO> vo = service.getEnergyConsumptionRatio(dto);
        return AjaxResult.success(vo);
    }

    /**
     * 综合报表获取能耗设备占比信息
     */
    //    @PreAuthorize("@ss.hasPermi('dailyReport:list')")
    @GetMapping("/getEnergyUnitConsumptionRatio")
    @ApiOperation(value = "综合报表获取能耗设备占比信息", notes = "综合报表获取能耗设备占比信息")
    public AjaxResult getEnergyUnitConsumptionRatio(@Validated ComprehensiveReportsDTO dto) {
        List<ComprehensiveUnitConsumptionChartVO> vo = service.getEnergyUnitConsumptionRatio(dto);
        return AjaxResult.success(vo);
    }

}
