package com.dingzhuo.energy.basic.data.workforce.controller;

import com.dingzhuo.energy.basic.data.workforce.domain.RosteringShift;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringShiftService;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.system.domain.SysDept;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 排班管理设置Controller
 * 
 * @author liuli
 * @date 2020-05-12
 */
@RestController
@RequestMapping("/workforce/shiftManagement")
public class RosteringShiftController extends BaseController
{
    @Autowired
    private IRosteringShiftService rosteringShiftService;

    /**
     * 查询排班管理列表
     */
    @PreAuthorize("@ss.hasPermi('workforce:shiftManagement:treeList')")
    @GetMapping("/treeList")
    public AjaxResult treeList(RosteringShift rosteringShift) {
        List<SysDept> list = rosteringShiftService.treeList();
        return AjaxResult.success(rosteringShiftService.buildModelNodeTree(list));
    }

    /**
     * 查询排班管理设置列表
     */
    @PreAuthorize("@ss.hasPermi('workforce:shiftManagement:list')")
    @GetMapping("/list")
    public TableDataInfo list(RosteringShift rosteringShift)
    {
        startPage();
        List<RosteringShift> list = rosteringShiftService.selectRosteringShiftList(rosteringShift);
        return getDataTable(list);
    }

    /**
     * 导出排班管理设置列表
     */
    @PreAuthorize("@ss.hasPermi('workforce:shiftManagement:export')")
    @Log(title = "排班管理设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RosteringShift rosteringShift)
    {
        List<RosteringShift> list = rosteringShiftService.selectRosteringShiftList(rosteringShift);
        ExcelUtil<RosteringShift> util = new ExcelUtil<RosteringShift>(RosteringShift.class);
        return util.exportExcel(list, "shiftManagement");
    }

    /**
     * 获取排班管理设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('workforce:shiftManagement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rosteringShiftService.selectRosteringShiftById(id));
    }

    /**
     * 新增排班管理设置
     */
    @PreAuthorize("@ss.hasPermi('workforce:shiftManagement:add')")
    @Log(title = "排班管理设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@NotNull @RequestBody RosteringShift rosteringShift){
        rosteringShift.setId(UUID.randomUUID().toString());
        rosteringShift.setCreateBy(SecurityUtils.getUsername());
        rosteringShift.setCreateTime(DateUtils.getNowDate());
        return toAjax(rosteringShiftService.insertRosteringShift(rosteringShift));
    }

    /**
     * 修改排班管理设置
     */
    @PreAuthorize("@ss.hasPermi('workforce:shiftManagement:edit')")
    @Log(title = "排班管理设置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RosteringShift rosteringShift)
    {
        rosteringShift.setUpdateBy(SecurityUtils.getUsername());
        rosteringShift.setUpdateTime(DateUtils.getNowDate());
        return toAjax(rosteringShiftService.updateRosteringShift(rosteringShift));
    }

    /**
     * 删除排班管理设置
     */
    @PreAuthorize("@ss.hasPermi('workforce:shiftManagement:remove')")
    @Log(title = "排班管理设置", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rosteringShiftService.deleteRosteringShiftByIds(ids));
    }
}
