package com.zhitan.web.controller.processenergy;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.TypeTime;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.processenergy.domain.YearProcessEnergy;
import com.zhitan.processenergy.service.IYearProcessEnergyService;
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
 * 工序能耗 年
 *
 *
 */
@RestController
@RequestMapping("/processEnergy/YearProcessEnergy")
@Api(value = "工序能耗统计（年）", tags = {"工序能耗统计"})
public class YearProcessEnergyController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;

    @Autowired
    private IYearProcessEnergyService yearProcessEnergyService;

    @GetMapping("/list")
    @ApiOperation(value = "工序能耗统计（年）列表")
    private AjaxResult list(DataItemQueryDTO dataItem) throws ParseException{
        List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
        if(CollectionUtils.isEmpty(nodeId)){
            return success(new ArrayList<>());
        }
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
        if(CollectionUtils.isEmpty(energyList)){
            return success(new ArrayList<>());
        }
        List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());

        Date convertTime = DateTimeUtil.getTypeTime(dataItem.getTimeType(), dataItem.getDataTime());
        DateTime beginTime = DateUtil.beginOfYear(convertTime);
        DateTime endTime = DateUtil.endOfYear(convertTime);

        List<TypeTime> typeTimeList = DateTimeUtil.getDateTimeList(dataItem.getTimeType(),convertTime);

        List<YearProcessEnergy> list = yearProcessEnergyService.getYearProcessEnergy(indexIds, typeTimeList,beginTime,endTime, dataItem.getTimeType(),dataItem.getEnergyType());
        return success(list);
    }

    @GetMapping("/listChart")
    @ApiOperation(value = "工序能耗（年）图表")
    public AjaxResult listChart(DataItemQueryDTO queryDto) {
        List<YearProcessEnergy> list = yearProcessEnergyService.getListChart(queryDto);
        return AjaxResult.success(list);
    }
}
