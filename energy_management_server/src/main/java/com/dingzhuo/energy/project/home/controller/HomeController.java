package com.dingzhuo.energy.project.home.controller;

import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.home.domain.vo.*;
import com.dingzhuo.energy.project.home.service.IHomeService;
import com.dingzhuo.energy.project.system.domain.SysDictData;
import com.dingzhuo.energy.project.system.service.ISysDictDataService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 首页 控制层
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@RestController
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Autowired
    private IHomeService homeService;

    @Autowired
    private ISysDictDataService dictDataService;

    @GetMapping("/getHomeEnergyConsumptionSummation")
    @ApiOperation(value = "首页获取全厂综合能耗", notes = "首页获取全厂综合能耗")
    public AjaxResult getHomeenergyConsumptionSummation() {
        String code = "Home_Equipment";
        return AjaxResult.success(homeService.geEnergyConsumptionSummation(code));
    }

    @GetMapping("/getHomeEnergyConsumptionRatio")
    @ApiOperation(value = "首页获取综合能耗占比分析", notes = "首页获取综合能耗占比分析")
    public AjaxResult getHomeEnergyConsumptionRatio() {
        // 全场综合能耗 code
        String code = "Home_Equipment";
        List<HomeEnergyConsumptionRatioVO> ratioList = homeService.getHomeEnergyConsumptionRatio(code);
        return AjaxResult.success(ratioList);
    }

    @GetMapping("/getHomeEnergyMonitoring")
    @ApiOperation(value = "首页获取能源分时监测", notes = "首页获取能源分时监测")
    public AjaxResult getHomeEnergyMonitoring(String energyType) {
        // 分类能源统计code
        String code  = "Home_TimeMonitoring";
        HomeEnergyMonitoringHistogramVO vo = homeService.getHomeEnergyMonitoring(code, energyType);
        return AjaxResult.success(vo);
    }

    @GetMapping("/getHomeEnergyStatistic")
    @ApiOperation(value = "首页获取分类能源统计", notes = "首页获取分类能源统计")
    public AjaxResult getHomeEnergyStatistic() {
        // 分类能源统计code
        String code = "Home_Cumulative";
        List<HomeEnergyStatisticsVO> voList = homeService.getHomeEnergyStatistic(code);
        return AjaxResult.success(voList);
    }

    @GetMapping("/getHomeEnergyUnitConsumptionRatio")
    @ApiOperation(value = "首页获取能耗设备占比信息", notes = "首页获取能耗设备占比信息")
    public AjaxResult getHomeEnergyUnitConsumptionRatio() {
        String code = "Home_Equipment";
        List<HomeEnergyUnitConsumptionChartVO> voList = homeService.getHomeEnergyUnitConsumptionRatio(code);
        return AjaxResult.success(voList);
    }

    @GetMapping("/listEnergyType")
    @ApiOperation(value = "首页获取能源类型", notes = "首页获取能源类型")
    public AjaxResult listEnergyType() {
        String dictType = "energy_type";
        List<SysDictData> energyTypeList = dictDataService.selectDictDataByType(dictType);
        List<HomeEnergyTypeVO> voList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(energyTypeList)) {
            voList = energyTypeList.stream().map(data-> {
                HomeEnergyTypeVO vo = new HomeEnergyTypeVO();
                vo.setEnergy(data.getDictValue());
                vo.setEnergyName(data.getDictLabel());
                return vo;
            }).sorted(Comparator.comparing(HomeEnergyTypeVO::getEnergy)).collect(Collectors.toList());
        }
        return AjaxResult.success(voList);
    }


    @GetMapping("/getHomeOutdoorTemperature")
    @ApiOperation(value = "首页获取室外温湿度", notes = "首页获取室外温湿度")
    public AjaxResult getHomeOutdoorTemperature() {
        return AjaxResult.success(homeService.getHomeOutdoorTemperature());
    }

}
