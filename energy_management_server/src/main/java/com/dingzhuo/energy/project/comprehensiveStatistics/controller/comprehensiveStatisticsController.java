package com.dingzhuo.energy.project.comprehensiveStatistics.controller;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.comprehensiveStatistics;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.IComprehensiveStatisticsService;
import com.dingzhuo.energy.project.keyEquipment.service.IdailyKeyEquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * electricityPriceController
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/statisticalData/comprehensiveStatistics")
public class comprehensiveStatisticsController extends BaseController {

    @Autowired
    private PeriodDataService periodDataService;
    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IComprehensiveStatisticsService IComprehensiveStatisticsService;

    @Autowired
    private IdailyKeyEquipmentService dailykeyEquipment;

    @GetMapping("/getList")
    public AjaxResult getList(comprehensiveStatistics dataItem) {
        try {
            /*List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());*/
            List<comprehensiveStatistics> list =IComprehensiveStatisticsService.getEnergyByIndex(dataItem.getIndexType());
            List<String> indexCode = list.stream().map(comprehensiveStatistics::getIndexId).collect(Collectors.toList());
            List<comprehensiveStatistics> resultList = IComprehensiveStatisticsService.getDatasByIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
            Map<String, List<comprehensiveStatistics>> groupBy = resultList.stream().collect(Collectors.groupingBy(comprehensiveStatistics::getName));
            List<Map> energyHeadList = new ArrayList<Map>();
            groupBy.forEach((IndexName,value)->{
                Map tableColumn =new HashMap<>();//表数据
                tableColumn.put("name",IndexName);
                for(comprehensiveStatistics result:value){
                    if(result.getIndexCode().contains("electric")){
                        tableColumn.put("electric",result.getValue());
                    }else if(result.getIndexCode().contains("water")){
                        tableColumn.put("water",result.getValue());
                    }else if(result.getIndexCode().contains("coal")){
                        tableColumn.put("coal",result.getValue());
                    }else if(result.getIndexCode().contains("steam")){
                        tableColumn.put("steam",result.getValue());
                    }
                }
                energyHeadList.add(tableColumn);
            });
            return AjaxResult.success(energyHeadList);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
    @GetMapping("/energyList")
    public AjaxResult getEnergyList(comprehensiveStatistics dataItem) {
        try {
            List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<comprehensiveStatistics> resultList = IComprehensiveStatisticsService.getDatasIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
            Map<String, List<comprehensiveStatistics>> groupBy = resultList.stream().collect(Collectors.groupingBy(comprehensiveStatistics::getFacilityName));
            List<Map> energyHeadList = new ArrayList<Map>();
            groupBy.forEach((IndexName,value)->{
                Map tableColumn =new HashMap<>();//表数据
                tableColumn.put("name",IndexName);
                tableColumn.put("value",value);
                energyHeadList.add(tableColumn);
            });
            return AjaxResult.success(energyHeadList);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
    @GetMapping("/energyDevice")
    public AjaxResult energyDevice(comprehensiveStatistics dataItem) {
        try {
            List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<comprehensiveStatistics> resultList = IComprehensiveStatisticsService.getDatasIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
    @GetMapping("/getDeviceList")
    public AjaxResult getDeviceList(comprehensiveStatistics dataItem) {
        try {
            List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<comprehensiveStatistics> resultList = IComprehensiveStatisticsService.getDatasIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
            List<Map> energyHeadList = new ArrayList<Map>();
            if(dataItem.getIndexType().equals("1")){
                resultList.forEach(value->{
                    if(!value.getIndexCode().contains("scale")){
                        Map tableColumn =new HashMap<>();//表数据
                        tableColumn.put("value",value.getValue());
                        tableColumn.put("facilityName",value.getFacilityName());
                        energyHeadList.add(tableColumn);
                    }
                });
            }else{
                resultList.forEach(value->{
                    if(value.getIndexCode().contains("scale")){
                        Map tableColumn =new HashMap<>();//表数据
                        tableColumn.put("facilityName",value.getFacilityName());
                        tableColumn.put("value",value.getValue());
                        energyHeadList.add(tableColumn);
                    }
                });
            }
            return AjaxResult.success(energyHeadList);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
    @GetMapping("/getFacilityArchives")
    public AjaxResult getFacilityArchives() {
        try {
            List<FacilityArchives> list=IComprehensiveStatisticsService.getFacilityArchives();
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取标题出错！", ex);
            return AjaxResult.error("获取标题出错!");
        }
    }

    /**
     * 重点能耗设备
     * @return
     */
    @GetMapping("/getPointFacility")
    public AjaxResult getPointFacility() {
        try {
            List<FacilityArchives> list=dailykeyEquipment.getPointFacility();
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取设备出错！", ex);
            return AjaxResult.error("获取标题出错!");
        }
    }

    @GetMapping("/export")
    public AjaxResult export(comprehensiveStatistics dataItem) {
        List<comprehensiveStatistics> list =IComprehensiveStatisticsService.getEnergyByIndex(dataItem.getIndexType());
        List<String> indexCode = list.stream().map(comprehensiveStatistics::getIndexId).collect(Collectors.toList());
        List<comprehensiveStatistics> resultList = IComprehensiveStatisticsService.getDatasByList(indexCode, dataItem.getBeginTime(),dataItem.getEndTime(),dataItem.getTimeType());
        ExcelUtil<comprehensiveStatistics> util = new ExcelUtil<comprehensiveStatistics>(comprehensiveStatistics.class);
        return util.exportExcel(resultList, "set");
    }

}
