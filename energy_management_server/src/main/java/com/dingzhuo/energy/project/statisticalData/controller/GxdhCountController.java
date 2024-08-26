package com.dingzhuo.energy.project.statisticalData.controller;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.project.statisticalData.domain.GxdhCountIndex;
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
import com.dingzhuo.energy.project.statisticalData.domain.GxdhCount;
import com.dingzhuo.energy.project.statisticalData.service.IGxdhCountService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 工序单耗统计功能Controller
 * 
 * @author zhaow
 * @date 2020-12-26
 */
@RestController
@RequestMapping("/statisticalData/gxdhcount")
public class GxdhCountController extends BaseController
{
    @Autowired
    private IGxdhCountService gxdhCountService;

    /**
     * 查询工序单耗统计指标设置功能列表
     */
    @PreAuthorize("@ss.hasPermi('statisticalData:gxdhcount:list')")
    @GetMapping("/list")
    public TableDataInfo list(GxdhCount gxdhCount)
    {
        startPage();
        List<GxdhCount> list = gxdhCountService.selectGxdhCountList(gxdhCount);
        return getDataTable(list);
    }

    /**
     * 导出工序单耗统计指标设置功能列表
     */
    @PreAuthorize("@ss.hasPermi('statisticalData:gxdhcount:export')")
    @Log(title = "工序单耗统计指标设置功能", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GxdhCount gxdhCount)
    {
        List<GxdhCount> list = gxdhCountService.selectGxdhCountList(gxdhCount);
        ExcelUtil<GxdhCount> util = new ExcelUtil<GxdhCount>(GxdhCount.class);
        return util.exportExcel(list, "gxdhcount");
    }

    /**
     * 获取工序单耗统计指标设置功能详细信息
     */
    @PreAuthorize("@ss.hasPermi('statisticalData:gxdhcount:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(gxdhCountService.selectGxdhCountById(id));
    }

    /**
     * 新增工序单耗统计指标设置功能
     */
    @PreAuthorize("@ss.hasPermi('statisticalData:gxdhcount:add')")
    @Log(title = "工序单耗统计指标设置功能", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GxdhCount gxdhCount)
    {
        gxdhCount.setId(UUID.randomUUID().toString());
        return toAjax(gxdhCountService.insertGxdhCount(gxdhCount));
    }

    /**
     * 修改工序单耗统计指标设置功能
     */
    @PreAuthorize("@ss.hasPermi('statisticalData:gxdhcount:edit')")
    @Log(title = "工序单耗统计指标设置功能", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GxdhCount gxdhCount)
    {
        return toAjax(gxdhCountService.updateGxdhCount(gxdhCount));
    }

    /**
     * 删除工序单耗统计指标设置功能
     */
    @PreAuthorize("@ss.hasPermi('statisticalData:gxdhcount:remove')")
    @Log(title = "工序单耗统计指标设置功能", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(gxdhCountService.deleteGxdhCountByIds(ids));
    }

    /**
     * 查询工序单耗统计指标设置功能列表
     */
    @GetMapping("/listReportIndex")
    public TableDataInfo list(GxdhCountIndex gxdhCountIndex)
    {
        List<GxdhCountIndex> list = gxdhCountService.selectGxdhCountIndexList(gxdhCountIndex);
        return getDataTable(list);
    }
}
