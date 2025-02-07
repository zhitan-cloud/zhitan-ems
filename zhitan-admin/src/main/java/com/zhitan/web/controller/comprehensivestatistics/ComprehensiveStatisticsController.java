package com.zhitan.web.controller.comprehensivestatistics;

import com.zhitan.basicdata.domain.FacilityArchives;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.comprehensivestatistics.domain.ComprehensiveStatistics;
import com.zhitan.comprehensivestatistics.service.IComprehensiveStatisticsService;
import com.zhitan.keyequipment.service.IDailyKeyEquipmentService;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.service.IModelNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * electricityPriceController
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/statisticalData/comprehensiveStatistics")
public class ComprehensiveStatisticsController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IComprehensiveStatisticsService comprehensiveStatisticsService;

    @Autowired
    private IDailyKeyEquipmentService dailykeyEquipment;

    @GetMapping("/getList")
    public AjaxResult getList(ComprehensiveStatistics dataItem) {
        try {
            /*List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());*/
            List<ComprehensiveStatistics> list = comprehensiveStatisticsService.getEnergyByIndex(dataItem.getIndexType());
            List<String> indexCode = list.stream().map(ComprehensiveStatistics::getIndexId).collect(Collectors.toList());
            List<ComprehensiveStatistics> resultList = comprehensiveStatisticsService.getDatasByIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
            Map<String, List<ComprehensiveStatistics>> groupBy = resultList.stream().collect(Collectors.groupingBy(ComprehensiveStatistics::getName));
            List<Map> energyHeadList = new ArrayList<Map>();
            groupBy.forEach((IndexName,value)->{
                Map tableColumn =new HashMap<>();//表数据
                tableColumn.put("name",IndexName);
                for(ComprehensiveStatistics result:value){
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
    public AjaxResult getEnergyList(ComprehensiveStatistics dataItem) {
        try {
            List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<ComprehensiveStatistics> resultList = comprehensiveStatisticsService.getDatasIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
            Map<String, List<ComprehensiveStatistics>> groupBy = resultList.stream().collect(Collectors.groupingBy(ComprehensiveStatistics::getFacilityName));
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
    public AjaxResult energyDevice(ComprehensiveStatistics dataItem) {
        try {
            List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<ComprehensiveStatistics> resultList = comprehensiveStatisticsService.getDatasIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }
    @GetMapping("/getDeviceList")
    public AjaxResult getDeviceList(ComprehensiveStatistics dataItem) {
        try {
            List<EnergyIndex> list = modelNodeService.getSettingIndex(dataItem.getIndexCode());
            List<String> indexCode = list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<ComprehensiveStatistics> resultList = comprehensiveStatisticsService.getDatasIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
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
            List<FacilityArchives> list= comprehensiveStatisticsService.getFacilityArchives();
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
    public AjaxResult export(ComprehensiveStatistics dataItem) {
        List<ComprehensiveStatistics> list = comprehensiveStatisticsService.getEnergyByIndex(dataItem.getIndexType());
        List<String> indexCode = list.stream().map(ComprehensiveStatistics::getIndexId).collect(Collectors.toList());
        List<ComprehensiveStatistics> resultList = comprehensiveStatisticsService.getDatasByList(indexCode, dataItem.getBeginTime(),dataItem.getEndTime(),dataItem.getTimeType());
        ExcelUtil<ComprehensiveStatistics> util = new ExcelUtil<ComprehensiveStatistics>(ComprehensiveStatistics.class);
        return util.exportExcel(resultList, "set");
    }

}
