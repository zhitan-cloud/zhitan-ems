package com.dingzhuo.energy.project.benchmarking.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.project.benchmarking.service.IPhaseBenchmarkingService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;

/**
 * 对标Controller
 * 
 * @author sys
 * @date 2020-12-22
 */
@RestController
@RequestMapping("/benchmarking/phaseBenchmarking")
public class PhaseBenchmarkingController extends BaseController
{
    @Autowired
    private IPhaseBenchmarkingService phaseBenchmarkingService;
    @Autowired
    private IModelNodeService modelNodeService;
    /**
     * 查询阶段对标列表
     */
    @PreAuthorize("@ss.hasPermi('benchmarking:phaseBenchmarking:list')")
    @GetMapping("/list")
    public AjaxResult list(BenchmarkingManagement phaseBenchmarking)
    {
        List<BenchmarkingManagement> list = phaseBenchmarkingService.selectPhaseBenchmarkingList(phaseBenchmarking.getIndexId(),phaseBenchmarking.getBeginTime(),phaseBenchmarking.getEndTime(), phaseBenchmarking.getTimeType());
        return AjaxResult.success(list);
    }
    /*实时对标*/
    @PreAuthorize("@ss.hasPermi('benchmarking:phaseBenchmarking:realTimeListrealTime')")
    @GetMapping("/realTimeListrealTime")
    public AjaxResult realTimeListrealTime(BenchmarkingManagement phaseBenchmarking)
    {
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(phaseBenchmarking.getIndexId());
        List<String> indexCode = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<BenchmarkingManagement> list = phaseBenchmarkingService.selectRealTimeListrealTime(indexCode,phaseBenchmarking.getDateTime(), phaseBenchmarking.getTimeType());
        return AjaxResult.success(list);
    }
}
