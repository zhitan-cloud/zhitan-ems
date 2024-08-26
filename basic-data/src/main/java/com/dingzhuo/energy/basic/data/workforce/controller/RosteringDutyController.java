package com.dingzhuo.energy.basic.data.workforce.controller;

import com.dingzhuo.energy.basic.data.workforce.domain.RosteringDuty;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringDutyService;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 排班管理Controller
 * 
 * @author liuli
 * @date 2020-05-12
 */
@RestController
@RequestMapping("/workforce/dutyManagement")
public class RosteringDutyController extends BaseController
{
    @Autowired
    private IRosteringDutyService rosteringDutyService;

    /**
     * 查询排班管理列表
     */
    @PreAuthorize("@ss.hasPermi('workforce:dutyManagement:list')")
    @GetMapping("/list")
    public TableDataInfo list(RosteringDuty rosteringDuty)
    {
        startPage();
        List<RosteringDuty> list = rosteringDutyService.selectRosteringDutyList(rosteringDuty);
        return getDataTable(list);
    }

    /**
     * 导出排班管理列表
     */
    @PreAuthorize("@ss.hasPermi('workforce:dutyManagement:export')")
    @Log(title = "排班管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RosteringDuty rosteringDuty)
    {
        List<RosteringDuty> list = rosteringDutyService.selectRosteringDutyList(rosteringDuty);
        ExcelUtil<RosteringDuty> util = new ExcelUtil<RosteringDuty>(RosteringDuty.class);
        return util.exportExcel(list, "dutyManagement");
    }

    /**
     * 获取排班管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('workforce:dutyManagement:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rosteringDutyService.selectRosteringDutyById(id));
    }

    /**
     * 新增排班管理
     */
    @PreAuthorize("@ss.hasPermi('workforce:dutyManagement:add')")
    @Log(title = "排班管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RosteringDuty rosteringDuty)
    {
        rosteringDuty.setId(UUID.randomUUID().toString());
        rosteringDuty.setCreateBy(SecurityUtils.getUsername());
        rosteringDuty.setCreateTime(DateUtils.getNowDate());
        return toAjax(rosteringDutyService.insertRosteringDuty(rosteringDuty));
    }

    /**
     * 修改排班管理
     */
    @PreAuthorize("@ss.hasPermi('workforce:dutyManagement:edit')")
    @Log(title = "排班管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RosteringDuty rosteringDuty)
    {
        rosteringDuty.setUpdateBy(SecurityUtils.getUsername());
        rosteringDuty.setUpdateTime(DateUtils.getNowDate());
        return toAjax(rosteringDutyService.updateRosteringDuty(rosteringDuty));
    }

    /**
     * 删除排班管理
     */
    @PreAuthorize("@ss.hasPermi('workforce:dutyManagement:remove')")
    @Log(title = "排班管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rosteringDutyService.deleteRosteringDutyByIds(ids));
    }
}
