package com.dingzhuo.energy.project.keyEquipment.controller;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.keyEquipment.domain.yearKeyEquipment;
import com.dingzhuo.energy.project.keyEquipment.service.IyearKeyEquipmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *重点设备能耗统计 年
 *
 * @author sys
 * @date 2021-01-11
 */
@RestController
@RequestMapping("/keyEquipment/yearKeyEquipment")
@Api(value = "重点设备能耗统计（年）",tags = {"设备单耗分析"})
public class yearKeyEquipmentController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IyearKeyEquipmentService yearKeyEquipment;

    @GetMapping("/list")
    @ApiOperation(value = "重点设备能耗统计（年）列表")
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
        List<yearKeyEquipment> dataList=new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getBeginTime());
        String bb="";
        int i = 1;
        while (i <= 12) {
            if(i>9){
                bb=aa+"-"+i+"-01 00:00:00";
            }else{
                bb=aa+"-0"+i+"-01 00:00:00";
            }
            yearKeyEquipment report=new yearKeyEquipment();
            report.setDataTime(sf.parse(bb));
            report.setValue("value"+i);
            dataList.add(report);
            i++;
        }
        startPage();
        List<yearKeyEquipment> list = yearKeyEquipment.getYearKeyEquipmentList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return getDataTable(list);
    }

    @GetMapping("/listChart")
    @ApiOperation(value = "重点设备能耗统计（年）图表")
    public AjaxResult listChart(DataItem dataItem){
        List<yearKeyEquipment> list = yearKeyEquipment.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return AjaxResult.success(list);
    }
}
