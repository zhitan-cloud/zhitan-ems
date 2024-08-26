package com.dingzhuo.energy.data.monitoring.trend.svg.controller;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.data.model.service.IStateTypeService;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.device.domain.EquipmentMeasuringPointParameters;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceStatusLive;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceStatusLiveService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 设备启停实时监测Controller
 * 
 * @author sys
 * @date 2020-03-30
 */
@RestController
@RequestMapping("/dataMonitoring/svgTrendView")
public class SvgTrendView extends BaseController
{
    private final IModelNodeService modelNodeService;
    private final IEnergyIndexService energyIndexService;
    @Autowired
    private IDeviceStatusLiveService deviceStatusLiveService;
    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;
    @Autowired
    private IStateTypeService iStateTypeService;


    @Autowired
    public SvgTrendView(
            IModelNodeService modelNodeService, IEnergyIndexService energyIndexService) {
        this.modelNodeService = modelNodeService;
        this.energyIndexService = energyIndexService;
    }

    @Log(title = "获取模型节点关联采集指标", businessType = BusinessType.UPDATE)
    @GetMapping("/energyIndex/list")
    public AjaxResult getSettingIndex(EnergyIndex energyIndex) {
        try {
            List<EnergyIndex> infoList = energyIndexService.selectEnergyIndexList(energyIndex);
            return AjaxResult.success(infoList);
        } catch (Exception ex) {
            logger.error("获取关联采集指标出错！", ex);
            return AjaxResult.error("获取关联指标出错!");
        }
    }
}
