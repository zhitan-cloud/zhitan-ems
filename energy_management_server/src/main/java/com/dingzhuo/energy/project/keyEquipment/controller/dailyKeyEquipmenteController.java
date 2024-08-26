package com.dingzhuo.energy.project.keyEquipment.controller;
import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.keyEquipment.domain.dailyKeyEquipment;
import com.dingzhuo.energy.project.keyEquipment.service.IdailyKeyEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
@RestController
@RequestMapping("/keyEquipment/dailyKeyEquipment")
@Api(value = "重点设备能耗统计（日）",tags = {"设备单耗分析"})
public class dailyKeyEquipmenteController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IdailyKeyEquipmentService dailykeyEquipment;

    @GetMapping("/list")
    @ApiOperation(value = "重点设备能耗分析（日）列表")
    public TableDataInfo list(DataItem dataItem) throws ParseException {
        List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
        if(CollectionUtils.isEmpty(nodeId)){
            return getDataTable(new ArrayList<>());
        }
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
        if(CollectionUtils.isEmpty(energyList)){
            return getDataTable(new ArrayList<>());
        }
        List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<dailyKeyEquipment> dataList=new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        String bb="";
        int i = 0;
        dataItem.setBeginTime(dataItem.getDataTime());
        String endTime=aa+" 24:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        while (i < 24) {
            if(i>9){
                bb=aa+" "+i+":00:00";
            }else{
                bb=aa+" 0"+i+":00:00";
            }
            dailyKeyEquipment report=new dailyKeyEquipment();
            report.setDataTime(sf.parse(bb));
            report.setValue("value"+i);
            dataList.add(report);
            i++;
        };
        startPage();
        List<dailyKeyEquipment> list = dailykeyEquipment.getdailyKeyEquipmentList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        //return AjaxResult.success(list);
        return getDataTable(list);
    }

    @GetMapping("/listChart")
    @ApiOperation(value = "重点设备能耗分析（日）图表")
    public AjaxResult listChart(DataItem dataItem) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        dataItem.setBeginTime(dataItem.getDataTime());
        String endTime=aa+" 24:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        List<dailyKeyEquipment> list = dailykeyEquipment.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return AjaxResult.success(list);
    }
    /*所有设备*/
    @GetMapping("/getFacilityArchives")
    @ApiOperation(value = "查询所有设备列表")
    public AjaxResult getFacilityArchives() {
        try {
            List<FacilityArchives> list=dailykeyEquipment.getFacilityArchives();
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取设备出错！", ex);
            return AjaxResult.error("获取标题出错!");
        }
    }

    /**
     * 重点能耗设备
     * @return
     */
    @GetMapping("/getPointFacility")
    @ApiOperation(value = "查询重点设备列表")
    public AjaxResult getPointFacility() {
        try {
            List<FacilityArchives> list=dailykeyEquipment.getPointFacility();
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取设备出错！", ex);
            return AjaxResult.error("获取标题出错!");
        }
    }
}
