package com.zhitan.web.controller.keyequipment;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.TypeTime;
import com.zhitan.keyequipment.domain.YearKeyEquipment;
import com.zhitan.keyequipment.service.IYearKeyEquipmentService;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.realtimedata.domain.DataItem;
import com.zhitan.realtimedata.domain.dto.DataItemQueryDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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


       Date convertTime = DateTimeUtil.getEndTimeByType(dataItem.getTimeType(),dataItem.getDataTime());
       DateTime beginTime = DateUtil.beginOfYear(convertTime);
       DateTime endTime = DateUtil.endOfYear(convertTime);
       List<TypeTime> typeTimeList = DateTimeUtil.getDateTimeList(dataItem.getTimeType(),convertTime);
        List<YearKeyEquipment> list = yearKeyEquipmentService.getYearKeyEquipmentList(indexIds, typeTimeList,beginTime,endTime, dataItem.getTimeType(),dataItem.getEnergyType());
        return success(list);
    }

    @GetMapping("/listChart")
    @ApiOperation(value = "重点设备能耗统计（年）图表")
    public AjaxResult listChart(DataItemQueryDTO queryDto){

        List<YearKeyEquipment> list = yearKeyEquipmentService.getListChart(queryDto);
        return AjaxResult.success(list);
    }
}
