package com.dingzhuo.energy.basic.data.enerInfoManage.controller;

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
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnercoefficient;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysEnercoefficientService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 能源折标系数Controller
 * 
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/enerInfoManage/enercoefficient")
public class SysEnercoefficientController extends BaseController
{
    @Autowired
    private ISysEnercoefficientService sysEnercoefficientService;

    /**
     * 查询能源折标系数列表
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:enercoefficient:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysEnercoefficient sysEnercoefficient)
    {
        startPage();
        List<SysEnercoefficient> list = sysEnercoefficientService.selectSysEnercoefficientList(sysEnercoefficient);
        return getDataTable(list);
    }

    /**
     * 导出能源折标系数列表
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:enercoefficient:export')")
    @Log(title = "能源折标系数", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysEnercoefficient sysEnercoefficient)
    {
        List<SysEnercoefficient> list = sysEnercoefficientService.selectSysEnercoefficientList(sysEnercoefficient);
        ExcelUtil<SysEnercoefficient> util = new ExcelUtil<SysEnercoefficient>(SysEnercoefficient.class);
        return util.exportExcel(list, "enercoefficient");
    }

    /**
     * 获取能源折标系数详细信息
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:enercoefficient:query')")
    @GetMapping(value = "/{ecid}")
    public AjaxResult getInfo(@PathVariable("ecid") Integer ecid)
    {
        return AjaxResult.success(sysEnercoefficientService.selectSysEnercoefficientById(ecid));
    }

    /**
     * 新增能源折标系数
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:enercoefficient:add')")
    @Log(title = "能源折标系数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysEnercoefficient sysEnercoefficient)
    {
        return toAjax(sysEnercoefficientService.insertSysEnercoefficient(sysEnercoefficient));
    }

    /**
     * 修改能源折标系数
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:enercoefficient:edit')")
    @Log(title = "能源折标系数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysEnercoefficient sysEnercoefficient)
    {
        return toAjax(sysEnercoefficientService.updateSysEnercoefficient(sysEnercoefficient));
    }

    /**
     * 删除能源折标系数
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:enercoefficient:remove')")
    @Log(title = "能源折标系数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ecids}")
    public AjaxResult remove(@PathVariable Integer[] ecids)
    {
        return toAjax(sysEnercoefficientService.deleteSysEnercoefficientByIds(ecids));
    }
}
