package com.dingzhuo.energy.data.monitoring.historical.controller;


import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.data.monitoring.historical.service.IHistoryDataService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 历史数据统计  控制层
 *
 * @Author: Zhujw
 * @Date: 2023/3/6
 */
@Api(value = "报警检测—历史报警检测", tags = {"报警检测"})
@RestController
@RequestMapping("/historical")
public class HistoryDataController extends BaseController {

    @Autowired
    private IHistoryDataService dataService;

    @Autowired
    private IModelNodeService modelNodeService;


}
