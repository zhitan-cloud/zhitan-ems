package com.dingzhuo.energy.project.energyStatistics.controller;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.energyStatistics.domain.energyAnalysis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * electricityPriceController
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/energyAnalysis")
public class energyAnalysisController extends BaseController {

    @Autowired
    private PeriodDataService periodDataService;
    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private com.dingzhuo.energy.project.energyStatistics.service.IEnergyStatisticsService IEnergyStatisticsService;

    @GetMapping("/getEnergyAnalysis")
    public AjaxResult getEnergyAnalysis(DataItem dataItem) {
        try {
            List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<DataItem> result = periodDataService.getDatasByIndex(indexCode, dataItem.getBeginTime(), dataItem.getEndTime(), dataItem.getTimeType());
            Map<String, List<DataItem>> groupBy = result.stream().collect(Collectors.groupingBy(DataItem::getIndexCode));
            energyAnalysis tableData =new energyAnalysis();
            List<energyAnalysis> tableDataMaps = new ArrayList<>();
            List<Map> energyHeadList = new ArrayList<Map>();
            AtomicInteger colcount= new AtomicInteger(1);//表格列的数量
            /*groupBy.forEach((IndexName,value)->{
                Map tableColumn =new HashMap<>();//表数据
                Map table =new HashMap<>();//表数据
                for(DataItem re:value){
                    energyAnalysis energyHead = new energyAnalysis();
                    energyHead.setDataTime(re.getDataTime());
                    energyHead.setValue(re.getValue());
                    energyHead.setProp("col"+ colcount);
                    tableDataMaps.add(energyHead);
                }
                colcount.getAndIncrement();
                tableColumn.put("IndexName",IndexName);
                tableColumn.put("prop",IndexName);
                energyHeadList.add(tableColumn);
                tableData.setAnalysisTableHead(energyHeadList);
                tableData.setAnalysisTableDate(tableDataMaps);
            });*/
            return AjaxResult.success(result);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
    @Log(title = "能耗统计分析", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DataItem dataItem) { {
        List<EnergyIndex> indexCodeList = modelNodeService.getSettingIndex(dataItem.getIndexCode());
        List<String> indexCode = indexCodeList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<DataItem> list = periodDataService.getDatasByIndex(indexCode, dataItem.getBeginTime(), dataItem.getEndTime(), dataItem.getTimeType());
        ExcelUtil<DataItem> util = new ExcelUtil<DataItem>(DataItem.class);
        return util.exportExcel(list, "exportEnergyAnalysis");
    }
}}