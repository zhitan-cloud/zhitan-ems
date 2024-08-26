package com.dingzhuo.energy.basic.data.energy.controller;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.common.utils.SecurityUtils;
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
import com.dingzhuo.energy.basic.data.energy.domain.EnergyReport;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyReportService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 节能分析报告管理Controller
 * 
 * @author zhaow
 * @date 2020-12-21
 */
@RestController
@RequestMapping("/energy/report")
public class EnergyReportController extends BaseController
{
    @Autowired
    private IEnergyReportService energyReportService;

    /**
     * 查询节能分析报告管理列表
     */
    @PreAuthorize("@ss.hasPermi('energy:report:list')")
    @GetMapping("/list")
    public TableDataInfo list(EnergyReport energyReport)
    {
        startPage();
        List<EnergyReport> list = energyReportService.selectEnergyReportList(energyReport);
        return getDataTable(list);
    }

    /**
     * 导出节能分析报告管理列表
     */
    @PreAuthorize("@ss.hasPermi('energy:report:export')")
    @Log(title = "节能分析报告管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EnergyReport energyReport)
    {
        List<EnergyReport> list = energyReportService.selectEnergyReportList(energyReport);
        ExcelUtil<EnergyReport> util = new ExcelUtil<EnergyReport>(EnergyReport.class);
        return util.exportExcel(list, "report");
    }

    /**
     * 获取节能分析报告管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:report:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(energyReportService.selectEnergyReportById(id));
    }

    /**
     * 新增节能分析报告管理
     */
    @PreAuthorize("@ss.hasPermi('energy:report:add')")
    @Log(title = "节能分析报告管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EnergyReport energyReport)
    {
        energyReport.setId(UUID.randomUUID().toString());
        energyReport.setCreateBy(SecurityUtils.getUsername());
        return toAjax(energyReportService.insertEnergyReport(energyReport));
    }

    /**
     * 修改节能分析报告管理
     */
    @PreAuthorize("@ss.hasPermi('energy:report:edit')")
    @Log(title = "节能分析报告管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EnergyReport energyReport)
    {
        return toAjax(energyReportService.updateEnergyReport(energyReport));
    }

    /**
     * 删除节能分析报告管理
     */
    @PreAuthorize("@ss.hasPermi('energy:report:remove')")
    @Log(title = "节能分析报告管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(energyReportService.deleteEnergyReportByIds(ids));
    }
}
