package com.dingzhuo.energy.project.comprehensiveStatistics.controller;

import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.processEnergyConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 工序能耗统计
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/statisticalData/processEnergyConsumption")
public class processEnergyConsumptionController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private processEnergyConsumptionService processEnergyConsumptionService;

}