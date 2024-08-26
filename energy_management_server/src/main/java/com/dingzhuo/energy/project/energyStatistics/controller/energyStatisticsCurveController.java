package com.dingzhuo.energy.project.energyStatistics.controller;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.domain.StatisticResult;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.energyStatistics.service.IEnergyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/energyCurve")
public class energyStatisticsCurveController extends BaseController {

    @Autowired
    private PeriodDataService periodDataService;
    @Autowired
    private IEnergyStatisticsService IEnergyStatisticsService;
    /**
     * 导出能源指标趋势分析列表
     */
    @PreAuthorize("@ss.hasPermi('energyCurve:energyCurveTrend:export')")
    @Log(title = "能耗指标趋势分析", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DataItem dataItem) {
        List<StatisticResult> list = periodDataService.statisticById(dataItem.getIndexId(), dataItem.getTimeType(), dataItem.getBeginTime(),dataItem.getEndTime());
        ExcelUtil<StatisticResult> util = new ExcelUtil<StatisticResult>(StatisticResult.class);
        return util.exportExcel(list, "set");
    }

    /**
     * 能源指标趋势分析
     */
    @GetMapping("/getEnergyCurveTrend")
    public TableDataInfo getEnergyCurveTrend(DataItem dataItem) {
        startPage();
        List<StatisticResult> list = periodDataService.statisticById(dataItem.getIndexId(), dataItem.getTimeType(), dataItem.getBeginTime(),dataItem.getEndTime());
        return getDataTable(list);
    }

    /*
用能单元能耗分析
 */
    @GetMapping("/getEnergyUnit")
    public AjaxResult getEnergyUnit(DataItem dataItem) {
        try {
            List<String> indexCode =  Collections.singletonList(dataItem.getIndexCode());
            // List<StatisticResult> list= periodDataService.statisticByCode(dataItem.getIndexCode(),timeType, dataItem.getBeginTime(), dataItem.getEndTime());
            List<StatisticResult> list= periodDataService.statisticByCode(indexCode,dataItem.getTimeType(),dataItem.getDataTime());
            list.forEach(statisticResult -> statisticResult.setFormatdate("yyyy-MM-dd"));
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
   /* @GetMapping("/getEnergyConstitute")
    public AjaxResult getEnergyConstitute(DataItem dataItem) {
        energyTable tabledata =new energyTable();
        List<dataTimeSVG> tabledatalist= IEnergyStatisticsService.statisticByCode(dataItem);
        tabledata.setTabledata(tabledatalist);
        return AjaxResult.success(tabledata);
    }
*/

}
