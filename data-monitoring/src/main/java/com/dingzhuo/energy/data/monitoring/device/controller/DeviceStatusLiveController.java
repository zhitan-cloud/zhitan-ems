package com.dingzhuo.energy.data.monitoring.device.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.data.model.service.IStateTypeService;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.device.domain.EquipmentMeasuringPointParameters;
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
 * @date 2020-03-23
 */
@RestController
@RequestMapping("/realTimeMonitoring/realTimeMonitoring")
public class DeviceStatusLiveController extends BaseController
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
    public DeviceStatusLiveController(
            IModelNodeService modelNodeService, IEnergyIndexService energyIndexService) {
        this.modelNodeService = modelNodeService;
        this.energyIndexService = energyIndexService;
    }

    /**
     * 查询设备启停实时监测列表
     */
    @PreAuthorize("@ss.hasPermi('realTimeMonitoring:realTimeMonitoring:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceStatusLive deviceStatusLive)
    {
        startPage();
        List<DeviceStatusLive> list = deviceStatusLiveService.selectDeviceStatusLiveList(deviceStatusLive);
        return getDataTable(list);
    }

    /**
     * 导出设备启停实时监测列表
     */
    @PreAuthorize("@ss.hasPermi('realTimeMonitoring:realTimeMonitoring:export')")
    @Log(title = "设备启停实时监测", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DeviceStatusLive deviceStatusLive)
    {
        List<DeviceStatusLive> list = deviceStatusLiveService.selectDeviceStatusLiveList(deviceStatusLive);
        ExcelUtil<DeviceStatusLive> util = new ExcelUtil<DeviceStatusLive>(DeviceStatusLive.class);
        return util.exportExcel(list, "realTimeMonitoring");
    }

    /**
     * 获取设备启停实时监测详细信息
     */
//    @PreAuthorize("@ss.hasPermi('realTimeMonitoring:realTimeMonitoring:query')")
    @GetMapping(value = "/{id}")
    public TableDataInfo getInfo(@PathVariable("id") String id)
    {
        List<DeviceStatusLive> list = new ArrayList<>();
        list.add(deviceStatusLiveService.selectDeviceStatusLiveById(id));
        list.stream().forEach(x->{
            if(x!=null)
            x.setStateType(iStateTypeService.selectStateTypeById(x.getStatusId()));
        });
        return getDataTable(list);
    }

    /**
     * 新增设备启停实时监测
     */
    @PreAuthorize("@ss.hasPermi('realTimeMonitoring:realTimeMonitoring:add')")
    @Log(title = "设备启停实时监测", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceStatusLive deviceStatusLive)
    {
        return toAjax(deviceStatusLiveService.insertDeviceStatusLive(deviceStatusLive));
    }

    /**
     * 修改设备启停实时监测
     */
    @PreAuthorize("@ss.hasPermi('realTimeMonitoring:realTimeMonitoring:edit')")
    @Log(title = "设备启停实时监测", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceStatusLive deviceStatusLive)
    {
        return toAjax(deviceStatusLiveService.updateDeviceStatusLive(deviceStatusLive));
    }

    /**
     * 删除设备启停实时监测
     */
    @PreAuthorize("@ss.hasPermi('realTimeMonitoring:realTimeMonitoring:remove')")
    @Log(title = "设备启停实时监测", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(deviceStatusLiveService.deleteDeviceStatusLiveByIds(ids));
    }

    @Log(title = "获取模型节点关联采集指标", businessType = BusinessType.UPDATE)
    @GetMapping("/energyIndex/list")
    public AjaxResult getSettingIndex(EnergyIndex energyIndex) {
        try {
            List<EnergyIndex> infoList = energyIndexService.selectEnergyIndexList(energyIndex);
            List<String> codeList= infoList.stream().map(EnergyIndex::getCode).collect(Collectors.toList());
            List<TagValue> valList = realtimeDatabaseService.retrieve(codeList);
            List resultList = new ArrayList();
            for(int i = 0;i<infoList.size();i++){
                EquipmentMeasuringPointParameters item = new EquipmentMeasuringPointParameters();
                item.setCode(infoList.get(i).getCode());
                item.setIndexName(infoList.get(i).getName());
                item.setIndexUnit(infoList.get(i).getUnitId());

                valList.forEach(x->{
                    if(x.getTagCode().equals(item.getCode()))
                    {
                        item.setValue(new   BigDecimal(x.getValue()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
                    }
                });
                resultList.add(item);
            }
            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            logger.error("获取关联采集指标出错！", ex);
            return AjaxResult.error("获取关联指标出错!");
        }
    }
}
