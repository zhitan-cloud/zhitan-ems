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
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringScheme;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringSchemeService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 轮值方案Controller
 * 
 * @author sys
 * @date 2020-05-12
 */
@RestController
@RequestMapping("/rosteringSrcheme/scheme")
public class RosteringSchemeController extends BaseController
{
    @Autowired
    private IRosteringSchemeService rosteringSchemeService;

    /**
     * 查询轮值方案列表
     */
    @PreAuthorize("@ss.hasPermi('rosteringSrcheme:scheme:list')")
    @GetMapping("/list")
    public TableDataInfo list(RosteringScheme rosteringScheme)
    {
        startPage();
        List<RosteringScheme> list = rosteringSchemeService.selectRosteringSchemeList(rosteringScheme);
        return getDataTable(list);
    }

    /**
     * 导出轮值方案列表
     */
    @PreAuthorize("@ss.hasPermi('rosteringSrcheme:scheme:export')")
    @Log(title = "轮值方案", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(RosteringScheme rosteringScheme)
    {
        List<RosteringScheme> list = rosteringSchemeService.selectRosteringSchemeList(rosteringScheme);
        ExcelUtil<RosteringScheme> util = new ExcelUtil<RosteringScheme>(RosteringScheme.class);
        return util.exportExcel(list, "scheme");
    }

    /**
     * 获取轮值方案详细信息
     */
    @PreAuthorize("@ss.hasPermi('rosteringSrcheme:scheme:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(rosteringSchemeService.selectRosteringSchemeById(id));
    }

    /**
     * 新增轮值方案
     */
    @PreAuthorize("@ss.hasPermi('rosteringSrcheme:scheme:add')")
    @Log(title = "轮值方案", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RosteringScheme rosteringScheme)
    {
        rosteringScheme.setId(UUID.randomUUID().toString());
        rosteringScheme.setCreateBy(SecurityUtils.getUsername());
        rosteringScheme.setCreateTime(DateUtils.getNowDate());
        return toAjax(rosteringSchemeService.insertRosteringScheme(rosteringScheme));
    }

    /**
     * 修改轮值方案
     */
    @PreAuthorize("@ss.hasPermi('rosteringSrcheme:scheme:edit')")
    @Log(title = "轮值方案", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RosteringScheme rosteringScheme)
    {
        rosteringScheme.setUpdateBy(SecurityUtils.getUsername());
        rosteringScheme.setUpdateTime(DateUtils.getNowDate());
        return toAjax(rosteringSchemeService.updateRosteringScheme(rosteringScheme));
    }

    /**
     * 删除轮值方案
     */
    @PreAuthorize("@ss.hasPermi('rosteringSrcheme:scheme:remove')")
    @Log(title = "轮值方案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(rosteringSchemeService.deleteRosteringSchemeByIds(ids));
    }
}
