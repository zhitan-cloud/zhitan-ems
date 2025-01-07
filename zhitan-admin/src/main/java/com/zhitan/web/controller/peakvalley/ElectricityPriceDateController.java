package com.zhitan.web.controller.peakvalley;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.peakvalley.domain.ElectricityPriceDate;
import com.zhitan.peakvalley.service.IElectricityPriceDateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 尖峰平谷电价时间段Controller
 * 
 * @author ZhiTan
 * @date 2024-10-10
 */
@RestController
@RequestMapping("/electricitypricedate")
public class ElectricityPriceDateController extends BaseController
{
    @Resource
    private IElectricityPriceDateService electricityPriceDateService;

    /**
     * 查询尖峰平谷电价时间段列表
     */
    @PreAuthorize("@ss.hasPermi('system:date:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectricityPriceDate electricityPriceDate, @RequestParam Long pageNum, @RequestParam Long pageSize)
    {
        Page<ElectricityPriceDate> list = electricityPriceDateService.selectElectricityPriceDatePage(electricityPriceDate,pageNum,pageSize);
        return getDataTable(list);
    }



    /**
     * 导出尖峰平谷电价时间段列表
     */
    @PreAuthorize("@ss.hasPermi('system:date:export')")
    @Log(title = "尖峰平谷电价时间段", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ElectricityPriceDate electricityPriceDate)
    {
        List<ElectricityPriceDate> list = electricityPriceDateService.selectElectricityPriceDateList(electricityPriceDate);
        ExcelUtil<ElectricityPriceDate> util = new ExcelUtil<ElectricityPriceDate>(ElectricityPriceDate.class);
        util.exportExcel(response, list, "尖峰平谷电价时间段数据");
    }

    /**
     * 获取尖峰平谷电价时间段详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:date:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(electricityPriceDateService.selectElectricityPriceDateById(id));
    }

    /**
     * 新增尖峰平谷电价时间段
     */
    @PreAuthorize("@ss.hasPermi('system:date:add')")
    @Log(title = "尖峰平谷电价时间段", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectricityPriceDate electricityPriceDate)
    {
        return toAjax(electricityPriceDateService.insertElectricityPriceDate(electricityPriceDate));
    }

    /**
     * 修改尖峰平谷电价时间段
     */
    @PreAuthorize("@ss.hasPermi('system:date:edit')")
    @Log(title = "尖峰平谷电价时间段", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectricityPriceDate electricityPriceDate)
    {
        return toAjax(electricityPriceDateService.updateElectricityPriceDate(electricityPriceDate));
    }

    /**
     * 删除尖峰平谷电价时间段
     */
    @PreAuthorize("@ss.hasPermi('system:date:remove')")
    @Log(title = "尖峰平谷电价时间段", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(electricityPriceDateService.deleteElectricityPriceDateByIds(ids));
    }
}
