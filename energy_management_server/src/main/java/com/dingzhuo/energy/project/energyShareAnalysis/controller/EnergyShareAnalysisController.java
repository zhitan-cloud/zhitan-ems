package com.dingzhuo.energy.project.energyShareAnalysis.controller;

import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.energyShareAnalysis.domain.dto.EnergyAnalysisDTO;
import com.dingzhuo.energy.project.energyShareAnalysis.domain.vo.EnergyAnalysisVO;
import com.dingzhuo.energy.project.energyShareAnalysis.service.IEnergyShareAnalysisService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 能耗占比分析 控制层
 */
@RestController
@RequestMapping("/energyShareAnalysis")
public class EnergyShareAnalysisController extends BaseController {

    @Autowired
    private IEnergyShareAnalysisService analysisService;


    @GetMapping("/getElectricityShareAnalysis")
    @ApiOperation(value = "获取电占比统计信息", notes = "获取电占比统计信息")
    public AjaxResult listElectricityShareAnalysis(@Validated EnergyAnalysisDTO dto) {

        List<EnergyAnalysisVO> yoyList = analysisService.listEnergyShareAnalysis(dto);
        return AjaxResult.success(yoyList);
    }

}