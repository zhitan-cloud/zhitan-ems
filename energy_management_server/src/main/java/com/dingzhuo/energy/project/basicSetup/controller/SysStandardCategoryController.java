package com.dingzhuo.energy.project.basicSetup.controller;

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
import com.dingzhuo.energy.project.basicSetup.domain.SysStandardCategory;
import com.dingzhuo.energy.project.basicSetup.service.ISysStandardCategoryService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * categoryController
 * 
 * @author ruoyi
 * @date 2020-02-12
 */
@RestController
@RequestMapping("/basicSetup/category")
public class SysStandardCategoryController extends BaseController
{
    @Autowired
    private ISysStandardCategoryService sysStandardCategoryService;

    /**
     * 查询category列表
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:category:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysStandardCategory sysStandardCategory)
    {
        startPage();
        List<SysStandardCategory> list = sysStandardCategoryService.selectSysStandardCategoryList(sysStandardCategory);
        return getDataTable(list);
    }

    /**
     * 导出category列表
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:category:export')")
    @Log(title = "category", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysStandardCategory sysStandardCategory)
    {
        List<SysStandardCategory> list = sysStandardCategoryService.selectSysStandardCategoryList(sysStandardCategory);
        ExcelUtil<SysStandardCategory> util = new ExcelUtil<SysStandardCategory>(SysStandardCategory.class);
        return util.exportExcel(list, "category");
    }

    /**
     * 获取category详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:category:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(sysStandardCategoryService.selectSysStandardCategoryById(id));
    }

    /**
     * 新增category
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:category:add')")
    @Log(title = "category", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysStandardCategory sysStandardCategory)
    {
        return toAjax(sysStandardCategoryService.insertSysStandardCategory(sysStandardCategory));
    }

    /**
     * 修改category
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:category:edit')")
    @Log(title = "category", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysStandardCategory sysStandardCategory)
    {
        return toAjax(sysStandardCategoryService.updateSysStandardCategory(sysStandardCategory));
    }

    /**
     * 删除category
     */
    @PreAuthorize("@ss.hasPermi('basicSetup:category:remove')")
    @Log(title = "category", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(sysStandardCategoryService.deleteSysStandardCategoryByIds(ids));
    }
}
