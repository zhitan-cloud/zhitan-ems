package com.dingzhuo.energy.basic.data.meter.controller;

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
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplementCount;
import com.dingzhuo.energy.basic.data.meter.service.IMeterImplementCountService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 计量器具统计查询Controller
 * 
 * @author zhaowei
 * @date 2020-02-21
 */
@RestController
@RequestMapping("/meter/implementCount")
public class MeterImplementCountController extends BaseController
{
    @Autowired
    private IMeterImplementCountService meterImplementCountService;

    /**
     * 查询计量器具统计查询列表
     */
    @PreAuthorize("@ss.hasPermi('meter:implementCount:list')")
    @GetMapping("/list")
    public TableDataInfo list(MeterImplementCount meterImplementCount)
    {
        startPage();
        List<MeterImplementCount> list = meterImplementCountService.selectMeterImplementCountList(meterImplementCount);
        return getDataTable(list);
    }

    /**
     * 导出计量器具统计查询列表
     */
    @PreAuthorize("@ss.hasPermi('meter:implementCount:export')")
    @Log(title = "计量器具统计查询", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(MeterImplementCount meterImplementCount)
    {
        List<MeterImplementCount> list = meterImplementCountService.selectMeterImplementCountList(meterImplementCount);
        ExcelUtil<MeterImplementCount> util = new ExcelUtil<MeterImplementCount>(MeterImplementCount.class);
        return util.exportExcel(list, "implementCount");
    }

    /**
     * 获取计量器具统计查询详细信息
     */
    @PreAuthorize("@ss.hasPermi('meter:implementCount:query')")
    @GetMapping(value = "/{code}")
    public AjaxResult getInfo(@PathVariable("code") String code)
    {
        return AjaxResult.success(meterImplementCountService.selectMeterImplementCountById(code));
    }

    /**
     * 新增计量器具统计查询
     */
    @PreAuthorize("@ss.hasPermi('meter:implementCount:add')")
    @Log(title = "计量器具统计查询", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MeterImplementCount meterImplementCount)
    {
        return toAjax(meterImplementCountService.insertMeterImplementCount(meterImplementCount));
    }

    /**
     * 修改计量器具统计查询
     */
    @PreAuthorize("@ss.hasPermi('meter:implementCount:edit')")
    @Log(title = "计量器具统计查询", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MeterImplementCount meterImplementCount)
    {
        return toAjax(meterImplementCountService.updateMeterImplementCount(meterImplementCount));
    }

    /**
     * 删除计量器具统计查询
     */
    @PreAuthorize("@ss.hasPermi('meter:implementCount:remove')")
    @Log(title = "计量器具统计查询", businessType = BusinessType.DELETE)
	@DeleteMapping("/{codes}")
    public AjaxResult remove(@PathVariable String[] codes)
    {
        return toAjax(meterImplementCountService.deleteMeterImplementCountByIds(codes));
    }
}
