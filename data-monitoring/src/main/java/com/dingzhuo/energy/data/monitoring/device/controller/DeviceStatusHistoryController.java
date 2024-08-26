package com.dingzhuo.energy.data.monitoring.device.controller;

import java.util.ArrayList;
import java.util.List;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
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
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceStatusHistory;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceStatusHistoryService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 设备启停历史监测Controller
 * 
 * @author sys
 * @date 2020-03-25
 */
@RestController
@RequestMapping("/historyMonitoring/historyMonitoring")
public class DeviceStatusHistoryController extends BaseController
{
    private final IEnergyIndexService energyIndexService;
    private final IModelNodeService modelNodeService;

    @Autowired
    private IDeviceStatusHistoryService deviceStatusHistoryService;

    @Autowired
    public DeviceStatusHistoryController(
            IModelNodeService modelNodeService, IEnergyIndexService energyIndexService) {
        this.modelNodeService = modelNodeService;
        this.energyIndexService = energyIndexService;
    }

    /**
     * 查询设备启停历史监测列表
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceStatusHistory deviceStatusHistory)
    {
        startPage();
        List<DeviceStatusHistory> list = deviceStatusHistoryService.selectDeviceStatusHistoryList(deviceStatusHistory);
        return getDataTable(list);
    }

    /**
     * 导出设备启停历史监测列表
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:export')")
    @Log(title = "设备启停历史监测", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DeviceStatusHistory deviceStatusHistory)
    {
        List<DeviceStatusHistory> list = deviceStatusHistoryService.selectDeviceStatusHistoryList(deviceStatusHistory);
        ExcelUtil<DeviceStatusHistory> util = new ExcelUtil<DeviceStatusHistory>(DeviceStatusHistory.class);
        return util.exportExcel(list, "historyMonitoring");
    }

    /**
     * 获取设备启停历史监测详细信息
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(deviceStatusHistoryService.selectDeviceStatusHistoryById(id));
    }

    /**
     * 新增设备启停历史监测
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:add')")
    @Log(title = "设备启停历史监测", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceStatusHistory deviceStatusHistory)
    {
        return toAjax(deviceStatusHistoryService.insertDeviceStatusHistory(deviceStatusHistory));
    }

    /**
     * 修改设备启停历史监测
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:edit')")
    @Log(title = "设备启停历史监测", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceStatusHistory deviceStatusHistory)
    {
        return toAjax(deviceStatusHistoryService.updateDeviceStatusHistory(deviceStatusHistory));
    }

    /**
     * 删除设备启停历史监测
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:remove')")
    @Log(title = "设备启停历史监测", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(deviceStatusHistoryService.deleteDeviceStatusHistoryByIds(ids));
    }

    /**
     * 查询设备启停历史监测列表
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:list')")
    @GetMapping("/history/list")
    public TableDataInfo listTable(DeviceStatusHistory deviceStatusHistory)
    {
        startPage();
        List<DeviceStatusHistory> historyList = deviceStatusHistoryService.selectDeviceStatusHistoryListNew(deviceStatusHistory);
        return getDataTable(historyList);
    }

    /**
     * 导出设备启停历史监测列表(新)
     */
    @PreAuthorize("@ss.hasPermi('historyMonitoring:historyMonitoring:export')")
    @Log(title = "设备启停历史监测", businessType = BusinessType.EXPORT)
    @GetMapping("/export/tableList")
    public AjaxResult exportNew(DeviceStatusHistory deviceStatusHistory)
    {
        List<DeviceStatusHistory> historyList = deviceStatusHistoryService.selectDeviceStatusHistoryListNew(deviceStatusHistory);
        ExcelUtil<DeviceStatusHistory> util = new ExcelUtil<DeviceStatusHistory>(DeviceStatusHistory.class);
        return util.exportExcel(historyList, "historyMonitoring");
    }
}
