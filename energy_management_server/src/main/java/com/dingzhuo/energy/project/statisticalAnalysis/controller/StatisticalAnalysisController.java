package com.dingzhuo.energy.project.statisticalAnalysis.controller;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.dto.DataAnalysisMoMDTO;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.vo.DataAnalysisMoMExcel;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.vo.DataAnalysisMoMVO;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.vo.DataAnalysisYoYExcel;
import com.dingzhuo.energy.project.statisticalAnalysis.domain.vo.DataAnalysisYoYVO;
import com.dingzhuo.energy.project.statisticalAnalysis.service.IStatisticalAnalysisService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


/**
 * 统计分析 控制层
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@RestController
@RequestMapping("/statisticalAnalysis")
public class StatisticalAnalysisController extends BaseController {

    @Autowired
    private IStatisticalAnalysisService analysisService;


    @PostMapping("/getElectricDataComparisonYoY")
    @ApiOperation(value = "获取电能耗同比数据", notes = "获取电能耗同比数据")
    public AjaxResult listElectricDataComparisonYoY(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisYoYVO> yoyList = analysisService.listElectricDataComparisonYoY(dto);
        return AjaxResult.success(yoyList);
    }

    @PostMapping("/getElectricDataComparisonMoM")
    @ApiOperation(value = "获取电能耗环比数据", notes = "获取电能耗环比数据")
    public AjaxResult listElectricDataComparisonMoM(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisMoMVO> momList = analysisService.listElectricDataComparisonMoM(dto);
        return AjaxResult.success(momList);
    }

    @PostMapping("/getWaterDataComparisonYoY")
    @ApiOperation(value = "获取水能耗同比数据", notes = "获取水能耗同比数据")
    public AjaxResult listWaterDataComparisonYoY(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisYoYVO> yoyList = analysisService.listWaterDataComparisonYoY(dto);
        return AjaxResult.success(yoyList);
    }

    @PostMapping("/getWaterDataComparisonMoM")
    @ApiOperation(value = "获取水能耗环比数据", notes = "获取水能耗环比数据")
    public AjaxResult listWaterDataComparisonMoM(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisMoMVO> momList = analysisService.listWaterDataComparisonMoM(dto);
        return AjaxResult.success(momList);
    }

    @PostMapping("/exportElectricYoY")
    @ApiOperation(value = "导出电能耗同比数据", notes = "导出电能耗同比数据")
    public AjaxResult exportElectricComparisonYoY(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisYoYExcel> yoYExcelsList = new ArrayList<>();
        List<DataAnalysisYoYVO> yoYList = analysisService.listElectricDataComparisonYoY(dto);
        if (CollectionUtils.isNotEmpty(yoYList)) {
            for (DataAnalysisYoYVO vo : yoYList) {
                DataAnalysisYoYExcel excel = new DataAnalysisYoYExcel();
                BeanUtils.copyProperties(vo, excel);
                yoYExcelsList.add(excel);
            }
        }
        ExcelUtil<DataAnalysisYoYExcel> util = new ExcelUtil<>(DataAnalysisYoYExcel.class);
        return util.exportExcel(yoYExcelsList, "电综合能耗同比信息");
    }

    @PostMapping("/exportElectricMoM")
    @ApiOperation(value = "导出电能耗环比数据", notes = "导出电能耗环比数据")
    public AjaxResult exportElectricComparisonMoM(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisMoMExcel> moMExcelsList = new ArrayList<>();
        List<DataAnalysisMoMVO> moMList = analysisService.listElectricDataComparisonMoM(dto);
        if (CollectionUtils.isNotEmpty(moMList)) {
            for (DataAnalysisMoMVO vo : moMList) {
                DataAnalysisMoMExcel excel = new DataAnalysisMoMExcel();
                BeanUtils.copyProperties(vo, excel);
                moMExcelsList.add(excel);
            }
        }
        ExcelUtil<DataAnalysisMoMExcel> util = new ExcelUtil<>(DataAnalysisMoMExcel.class);
        return util.exportRealTimeDataExcel(moMExcelsList, "电综合能耗环比信息");
    }

    @PostMapping("/exportWaterYoY")
    @ApiOperation(value = "导出水耗同比数据", notes = "导出水耗同比数据")
    public AjaxResult exportWaterComparisonYoY(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisYoYExcel> yoYExcelsList = new ArrayList<>();
        List<DataAnalysisYoYVO> yoYList = analysisService.listWaterDataComparisonYoY(dto);
        if (CollectionUtils.isNotEmpty(yoYList)) {
            for (DataAnalysisYoYVO vo : yoYList) {
                DataAnalysisYoYExcel excel = new DataAnalysisYoYExcel();
                BeanUtils.copyProperties(vo, excel);
                yoYExcelsList.add(excel);
            }
        }
        ExcelUtil<DataAnalysisYoYExcel> util = new ExcelUtil<>(DataAnalysisYoYExcel.class);
        return util.exportExcel(yoYExcelsList, "水综合能耗耗同比信息");
    }

    @PostMapping("/exportWaterMoM")
    @ApiOperation(value = "导出水耗环比数据", notes = "导出水耗环比数据")
    public AjaxResult exportWaterComparisonMoM(@RequestBody @Validated DataAnalysisMoMDTO dto) {
        List<DataAnalysisMoMExcel> moMExcelsList = new ArrayList<>();
        List<DataAnalysisMoMVO> moMList = analysisService.listWaterDataComparisonMoM(dto);
        if (CollectionUtils.isNotEmpty(moMList)) {
            for (DataAnalysisMoMVO vo : moMList) {
                DataAnalysisMoMExcel excel = new DataAnalysisMoMExcel();
                BeanUtils.copyProperties(vo, excel);
                moMExcelsList.add(excel);
            }
        }
        ExcelUtil<DataAnalysisMoMExcel> util = new ExcelUtil<>(DataAnalysisMoMExcel.class);
        return util.exportExcel(moMExcelsList, "水综合能耗耗环比信息");
    }

}
