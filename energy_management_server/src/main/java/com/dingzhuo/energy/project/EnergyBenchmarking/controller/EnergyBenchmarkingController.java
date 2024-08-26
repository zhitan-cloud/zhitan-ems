package com.dingzhuo.energy.project.EnergyBenchmarking.controller;

import java.util.List;
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
import com.dingzhuo.energy.project.EnergyBenchmarking.domain.EnergyBenchmarking;
import com.dingzhuo.energy.project.EnergyBenchmarking.service.IEnergyBenchmarkingService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * energy_benchmarkingController
 * 
 * @author sys
 * @date 2020-11-18
 */
@RestController
@RequestMapping("/energyEenchmarking/energyEenchmarking")
public class EnergyBenchmarkingController extends BaseController
{
    @Autowired
    private IEnergyBenchmarkingService energyBenchmarkingService;

    /**
     * 查询energy_benchmarking列表
     */
    @PreAuthorize("@ss.hasPermi('energyEenchmarking:energyEenchmarking:list')")
    @GetMapping("/list")
    public TableDataInfo list(EnergyBenchmarking energyBenchmarking)
    {
        startPage();
        List<EnergyBenchmarking> list = energyBenchmarkingService.selectEnergyBenchmarkingList(energyBenchmarking);
        return getDataTable(list);
    }

    /**
     * 导出energy_benchmarking列表
     */
    @PreAuthorize("@ss.hasPermi('energyEenchmarking:energyEenchmarking:export')")
    @Log(title = "energy_benchmarking", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EnergyBenchmarking energyBenchmarking)
    {
        List<EnergyBenchmarking> list = energyBenchmarkingService.selectEnergyBenchmarkingList(energyBenchmarking);
        ExcelUtil<EnergyBenchmarking> util = new ExcelUtil<EnergyBenchmarking>(EnergyBenchmarking.class);
        return util.exportExcel(list, "energyEenchmarking");
    }

    /**
     * 获取energy_benchmarking详细信息
     */
    @PreAuthorize("@ss.hasPermi('energyEenchmarking:energyEenchmarking:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(energyBenchmarkingService.selectEnergyBenchmarkingById(id));
    }

    /**
     * 新增energy_benchmarking
     */
    @PreAuthorize("@ss.hasPermi('energyEenchmarking:energyEenchmarking:add')")
    @Log(title = "energy_benchmarking", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EnergyBenchmarking energyBenchmarking)
    {
        return toAjax(energyBenchmarkingService.insertEnergyBenchmarking(energyBenchmarking));
    }

    /**
     * 修改energy_benchmarking
     */
    @PreAuthorize("@ss.hasPermi('energyEenchmarking:energyEenchmarking:edit')")
    @Log(title = "energy_benchmarking", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EnergyBenchmarking energyBenchmarking)
    {
        return toAjax(energyBenchmarkingService.updateEnergyBenchmarking(energyBenchmarking));
    }

    /**
     * 删除energy_benchmarking
     */
    @PreAuthorize("@ss.hasPermi('energyEenchmarking:energyEenchmarking:remove')")
    @Log(title = "energy_benchmarking", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(energyBenchmarkingService.deleteEnergyBenchmarkingByIds(ids));
    }
}
