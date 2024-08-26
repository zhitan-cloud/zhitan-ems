package com.dingzhuo.energy.project.benchmarking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;
import com.dingzhuo.energy.project.benchmarking.service.IBenchmarkingManagementService;
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
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;

/**
 * 标杆管理Controller
 * 
 * @author sys
 * @date 2020-12-21
 */
@RestController
@RequestMapping("/benchmarking/BenchmarkingManagement")
public class BenchmarkingManagementController extends BaseController
{
    @Autowired
    private IBenchmarkingManagementService benchmarkingManagementService;
    @Autowired
    private IModelNodeService modelNodeService;
    /**
     * 查询标杆管理列表
     */
    @PreAuthorize("@ss.hasPermi('benchmarking:BenchmarkingManagement:list')")
    @GetMapping("/list")
    public AjaxResult list(BenchmarkingManagement benchmarkingManagement)
    {
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(benchmarkingManagement.getIndexId());
        List<String> indexCode = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<BenchmarkingManagement> list;
        list = benchmarkingManagementService.selectBenchmarkingManagementList(indexCode,benchmarkingManagement.getDateTime(), benchmarkingManagement.getTimeType());
        if(list.size()==0){
            list = benchmarkingManagementService.selectBenchmarkingList(indexCode,benchmarkingManagement);
        }
        return AjaxResult.success(list);
    }

    /**
     * 导出标杆管理列表
     */
    @PreAuthorize("@ss.hasPermi('benchmarking:BenchmarkingManagement:export')")
    @Log(title = "标杆管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(BenchmarkingManagement benchmarkingManagement)
    {
        String timeCode = TimeManager.getTimeCode(benchmarkingManagement.getDateTime(), benchmarkingManagement.getTimeType());
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(benchmarkingManagement.getIndexId());
        List<String> indexCode = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<BenchmarkingManagement> list = benchmarkingManagementService.selectBenchmarkingManagementList(indexCode,benchmarkingManagement.getDateTime(), benchmarkingManagement.getTimeType());
        ExcelUtil<BenchmarkingManagement> util = new ExcelUtil<BenchmarkingManagement>(BenchmarkingManagement.class);
        return util.exportExcel(list, "BenchmarkingManagement");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:management:query')")
    @GetMapping(value = "/{indexId}")
    public AjaxResult getInfo(@PathVariable("indexId") String indexId)
    {
        return AjaxResult.success(benchmarkingManagementService.selectBenchmarkingManagementById(indexId));
    }
    /**
     * 新增标杆管理
     */
    @PreAuthorize("@ss.hasPermi('benchmarking:BenchmarkingManagement:add')")
    @Log(title = "标杆管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<BenchmarkingManagement> listMap) {
        List<BenchmarkingManagement> benchmarking = new ArrayList<>();
        listMap.forEach(data -> {
            String timeCode = TimeManager.getTimeCode(data.getDateTime(), data.getTimeType());
            BenchmarkingManagement dataItem = new BenchmarkingManagement();
            dataItem.setTimeCode(timeCode);
            dataItem.setIndexId(data.getIndexId());
            dataItem.setValue(data.getValue());
            dataItem.setDateTime(data.getDateTime());
            dataItem.setTimeType(data.getTimeType());
            dataItem.setActualValue(data.getActualValue());
            dataItem.setBenchmarkingRange(data.getBenchmarkingRange());
            dataItem.setBenchmarkingType(data.getBenchmarkingType());
            benchmarking.add(dataItem);
        });
        try {
            this.benchmarkingManagementService.save(benchmarking);
            return AjaxResult.success("保存成功！");
        }catch (Exception ex) {
            logger.error("保存失败！" + ex.getMessage());
            return AjaxResult.success(ex.getMessage());
        }
        //return toAjax(benchmarkingManagementService.insertBenchmarkingManagement(benchmarkingManagement));
    }

    /**
     * 新增【请填写功能名称】
     */
    /*@PreAuthorize("@ss.hasPermi('system:management:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BenchmarkingManagement benchmarkingManagement)
    {
        return toAjax(benchmarkingManagementService.insertBenchmarkingManagement(benchmarkingManagement));
    }*/

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:management:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BenchmarkingManagement benchmarkingManagement)
    {
        return toAjax(benchmarkingManagementService.updateBenchmarkingManagement(benchmarkingManagement));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:management:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{indexIds}")
    public AjaxResult remove(@PathVariable String[] indexIds)
    {
        return toAjax(benchmarkingManagementService.deleteBenchmarkingManagementByIds(indexIds));
    }


}
