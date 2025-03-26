package com.zhitan.web.controller.branchanalysis;

import com.zhitan.branchanalysis.domain.BranchAnalysisVO;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.branchanalysis.service.IBranchAnalysisService;

import com.zhitan.realtimedata.domain.dto.BranchAnalysisDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *支路用能分析
 *
 * @author zt
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/branchanalysis")
@Api(value = "支路用能能耗统计",tags = {"支路用能分析"})
public class BranchEnergyAnalysisController extends BaseController {

    @Autowired
    private IBranchAnalysisService branchAnalysisService;

    @GetMapping("/list")
    @ApiOperation(value = "支路用能能耗统计列表")
    public AjaxResult list(BranchAnalysisDTO dataItem)  {
        BranchAnalysisVO vo = branchAnalysisService.getBranchAnalysisService(dataItem);
        return success(vo);
    }
}

