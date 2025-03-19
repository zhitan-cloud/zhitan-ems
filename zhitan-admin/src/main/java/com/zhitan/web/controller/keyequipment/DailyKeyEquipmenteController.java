package com.zhitan.web.controller.keyequipment;

import com.zhitan.basicdata.domain.FacilityArchives;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.keyequipment.domain.DailyKeyEquipment;
import com.zhitan.keyequipment.service.IDailyKeyEquipmentService;
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
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
@RestController
@RequestMapping("/keyEquipment/dailyKeyEquipment")
@Api(value = "重点设备能耗统计（日）",tags = {"设备单耗分析"})
public class DailyKeyEquipmenteController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IDailyKeyEquipmentService dailykeyEquipment;

    @GetMapping("/list")
    @ApiOperation(value = "重点设备能耗分析（日）列表")
    public AjaxResult list(DataItem dataItem) throws ParseException {
        List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
        if(CollectionUtils.isEmpty(nodeId)){
            return success(new ArrayList<>());
        }
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
        if(CollectionUtils.isEmpty(energyList)){
            return success(new ArrayList<>());
        }
        List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<DailyKeyEquipment> dataList=new ArrayList<>();
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
            DailyKeyEquipment report=new DailyKeyEquipment();
            report.setDataTime(sf.parse(bb));
            report.setValue("value"+i);
            dataList.add(report);
            i++;
        };

        List<DailyKeyEquipment> list = dailykeyEquipment.getdailyKeyEquipmentList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getEnergyType());

        return success(list);
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
        List<DailyKeyEquipment> list = dailykeyEquipment.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getEnergyType());
        return AjaxResult.success(list);
    }
    /*所有设备*/
    @GetMapping("/getFacilityArchives")
    @ApiOperation(value = "查询重点设备列表")
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
