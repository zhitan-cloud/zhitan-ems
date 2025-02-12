package com.zhitan.web.controller.keyequipment;

import cn.hutool.core.date.DateUtil;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.keyequipment.domain.YearKeyEquipment;
import com.zhitan.keyequipment.service.IYearKeyEquipmentService;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.realtimedata.domain.DataItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
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
@RequestMapping("/keyEquipment/YearKeyEquipment")
@Api(value = "重点设备能耗统计（年）",tags = {"设备单耗分析"})
public class YearKeyEquipmentController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IYearKeyEquipmentService yearKeyEquipmentService;

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
        List<YearKeyEquipment> dataList=new ArrayList<>();

        dataItem.setBeginTime(DateUtil.beginOfYear(dataItem.getDataTime()));
        dataItem.setEndTime(DateUtil.endOfYear(dataItem.getDataTime()));

        DateFormat df = new SimpleDateFormat("yyyy");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        String bb="";
        int i = 1;
        while (i <= 12) {
            if(i>9){
                bb=aa+"-"+i+"-01 00:00:00";
            }else{
                bb=aa+"-0"+i+"-01 00:00:00";
            }
            YearKeyEquipment report=new YearKeyEquipment();
            report.setDataTime(sf.parse(bb));
            report.setValue("value"+i);
            dataList.add(report);
            i++;
        }
        startPage();
        List<YearKeyEquipment> list = yearKeyEquipmentService.getYearKeyEquipmentList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getEnergyType());
        return getDataTable(list);
    }

    @GetMapping("/listChart")
    @ApiOperation(value = "重点设备能耗统计（年）图表")
    public AjaxResult listChart(DataItem dataItem){

        List<YearKeyEquipment> list = yearKeyEquipmentService.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getEnergyType());
        return AjaxResult.success(list);
    }
}
