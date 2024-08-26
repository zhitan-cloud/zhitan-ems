package com.dingzhuo.energy.basic.data.enerInfoManage.controller;

import java.util.List;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysPrice;
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
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysPriceService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 单价设置Controller
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@RestController
@RequestMapping("/price/price")
public class SysPriceController extends BaseController
{
    @Autowired
    private ISysPriceService sysPriceService;

    /**
     * 查询单价设置列表
     */
    @PreAuthorize("@ss.hasPermi('price:price:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPrice sysPrice)
    {
        startPage();
        List<SysPrice> list = sysPriceService.selectSysPriceList(sysPrice);
        return getDataTable(list);
    }

    /**
     * 导出单价设置列表
     */
    @PreAuthorize("@ss.hasPermi('price:price:export')")
    @Log(title = "单价设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysPrice sysPrice)
    {
        List<SysPrice> list = sysPriceService.selectSysPriceList(sysPrice);
        ExcelUtil<SysPrice> util = new ExcelUtil<SysPrice>(SysPrice.class);
        return util.exportExcel(list, "price");
    }

    /**
     * 获取单价设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('price:price:query')")
    @GetMapping(value = "/{priceid}")
    public AjaxResult getInfo(@PathVariable("priceid") Integer priceid)
    {
        return AjaxResult.success(sysPriceService.selectSysPriceById(priceid));
    }

    /**
     * 新增单价设置
     */
    @PreAuthorize("@ss.hasPermi('price:price:add')")
    @Log(title = "单价设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPrice sysPrice)
    {
        return toAjax(sysPriceService.insertSysPrice(sysPrice));
    }

    /**
     * 修改单价设置
     */
    @PreAuthorize("@ss.hasPermi('price:price:edit')")
    @Log(title = "单价设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPrice sysPrice)
    {
        return toAjax(sysPriceService.updateSysPrice(sysPrice));
    }

    /**
     * 删除单价设置
     */
    @PreAuthorize("@ss.hasPermi('price:price:remove')")
    @Log(title = "单价设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{priceids}")
    public AjaxResult remove(@PathVariable Integer[] priceids)
    {
        return toAjax(sysPriceService.deleteSysPriceByIds(priceids));
    }
}
