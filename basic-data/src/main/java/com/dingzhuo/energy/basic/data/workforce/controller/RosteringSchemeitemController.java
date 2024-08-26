package com.dingzhuo.energy.basic.data.workforce.controller;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.common.utils.DateUtils;
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
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringSchemeitem;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringSchemeitemService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 轮值方案Controller
 * 
 * @author sys
 * @date 2020-05-13
 */
@RestController
@RequestMapping("/rosteringSchemeitem/schemeItem")
public class RosteringSchemeitemController extends BaseController
{
    @Autowired
    private IRosteringSchemeitemService rosteringSchemeitemService;

    /**
     * 查询轮值方案列表
     */
    @PreAuthorize("@ss.hasPermi('rosteringSchemeitem:schemeItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(RosteringSchemeitem rosteringSchemeitem)
    {
        startPage();
        List<RosteringSchemeitem> list = rosteringSchemeitemService.selectRosteringSchemeitemList(rosteringSchemeitem);
        return getDataTable(list);
    }

    /**
     * 导出轮值方案列表
     */
    @PreAuthorize("@ss.hasPermi('rosteringSchemeitem:schemeItem:export')")
    @Log(title = "轮值方案", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RosteringSchemeitem rosteringSchemeitem)
    {
        List<RosteringSchemeitem> list = rosteringSchemeitemService.selectRosteringSchemeitemList(rosteringSchemeitem);
        ExcelUtil<RosteringSchemeitem> util = new ExcelUtil<RosteringSchemeitem>(RosteringSchemeitem.class);
        return util.exportExcel(list, "schemeItem");
    }

    /**
     * 获取轮值方案详细信息
     */
    @PreAuthorize("@ss.hasPermi('rosteringSchemeitem:schemeItem:query')")
    @GetMapping(value = "/{description}")
    public AjaxResult getInfo(@PathVariable("description") String description)
    {
        return AjaxResult.success(rosteringSchemeitemService.selectRosteringSchemeitemById(description));
    }

    /**
     * 新增轮值方案
     */
    @PreAuthorize("@ss.hasPermi('rosteringSchemeitem:schemeItem:add')")
    @Log(title = "轮值方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RosteringSchemeitem rosteringSchemeitem)
    {
        rosteringSchemeitem.setId(UUID.randomUUID().toString());
        rosteringSchemeitem.setCreateBy(SecurityUtils.getUsername());
        rosteringSchemeitem.setCreateTime(DateUtils.getNowDate());
        return toAjax(rosteringSchemeitemService.insertRosteringSchemeitem(rosteringSchemeitem));
    }

    /**
     * 修改轮值方案
     */
    @PreAuthorize("@ss.hasPermi('rosteringSchemeitem:schemeItem:edit')")
    @Log(title = "轮值方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RosteringSchemeitem rosteringSchemeitem)
    {
        rosteringSchemeitem.setUpdateBy(SecurityUtils.getUsername());
        rosteringSchemeitem.setUpdateTime(DateUtils.getNowDate());
        return toAjax(rosteringSchemeitemService.updateRosteringSchemeitem(rosteringSchemeitem));
    }

    /**
     * 删除轮值方案
     */
    @PreAuthorize("@ss.hasPermi('rosteringSchemeitem:schemeItem:remove')")
    @Log(title = "轮值方案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{descriptions}")
    public AjaxResult remove(@PathVariable String[] descriptions)
    {
        return toAjax(rosteringSchemeitemService.deleteRosteringSchemeitemByIds(descriptions));
    }
}
