package com.dingzhuo.energy.project.plannedOutput.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.project.plannedOutput.domain.PlanEnergy;
import com.dingzhuo.energy.project.plannedOutput.domain.energyMonitoring;
import com.dingzhuo.energy.project.plannedOutput.service.energyMonitoringService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;

/**
 * 计划于实绩对比分析Controller
 * 
 * @author sys
 * @date 2020-12-17
 */
@RestController
@RequestMapping("/energyMonitoring")
public class energyMonitoringController extends BaseController {
    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private energyMonitoringService energyMonitoringService;
    /**
     * 查询列表
     */
    @PreAuthorize("@ss.hasPermi('system:energy:list')")
    @GetMapping("/list")
    public AjaxResult list(PlanEnergy planEnergy) {
        List<SysProduct> productList= modelNodeService.getSettingProduct(planEnergy.getIndexCode());
        List<Integer> indexCode = productList.stream().map(SysProduct::getProductid).collect(Collectors.toList());
        List<energyMonitoring> list=energyMonitoringService.selectEnergyMonitoringList(indexCode,planEnergy.getTimeType(),planEnergy.getDataTime());
        return AjaxResult.success(list);
    }

}
