package com.dingzhuo.energy.data.model.controller;

import java.util.List;

import com.dingzhuo.energy.common.utils.IdUtils;
import com.dingzhuo.energy.data.model.domain.StateType;
import com.dingzhuo.energy.data.model.service.IStateTypeService;
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
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 系统状态维护Controller
 * 
 * @author sys
 * @date 2020-03-18
 */
@RestController
@RequestMapping("/basicsetting/statetype")
public class StateTypeController extends BaseController
{
    @Autowired
    private IStateTypeService stateTypeService;

    /**
     * 查询系统状态维护列表
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:statetype:list')")
    @GetMapping("/list")
    public TableDataInfo list(StateType stateType)
    {
        startPage();
        List<StateType> list = stateTypeService.selectStateTypeList(stateType);
        return getDataTable(list);
    }

    /**
     * 导出系统状态维护列表
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:statetype:export')")
    @Log(title = "系统状态维护", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(StateType stateType)
    {
        List<StateType> list = stateTypeService.selectStateTypeList(stateType);
        ExcelUtil<StateType> util = new ExcelUtil<StateType>(StateType.class);
        return util.exportExcel(list, "statetype");
    }

    /**
     * 获取系统状态维护详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:statetype:query')")
    @GetMapping(value = "/{stateId}")
    public AjaxResult getInfo(@PathVariable("stateId") String stateId)
    {
        return AjaxResult.success(stateTypeService.selectStateTypeById(stateId));
    }

    /**
     * 新增系统状态维护
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:statetype:add')")
    @Log(title = "系统状态维护", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StateType stateType)
    {
        stateType.setStateId(IdUtils.fastUUID());
        return toAjax(stateTypeService.insertStateType(stateType));
    }

    /**
     * 修改系统状态维护
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:statetype:edit')")
    @Log(title = "系统状态维护", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StateType stateType)
    {
        return toAjax(stateTypeService.updateStateType(stateType));
    }

    /**
     * 删除系统状态维护
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:statetype:remove')")
    @Log(title = "系统状态维护", businessType = BusinessType.DELETE)
	@DeleteMapping("/{stateIds}")
    public AjaxResult remove(@PathVariable String[] stateIds)
    {
        return toAjax(stateTypeService.deleteStateTypeByIds(stateIds));
    }
}
