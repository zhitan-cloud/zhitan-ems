package com.dingzhuo.energy.basic.data.energy.controller;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyProject;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyProjectService;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 节能项目管理Controller
 *
 * @author sys
 * @date 2020-12-07
 */
@RestController
@RequestMapping("/energy/energyProject")
@Api(value = "节能项目管理",tags = {"节能项目管理"})
public class EnergyProjectController extends BaseController
{
    @Autowired
    private IEnergyProjectService energyProjectService;

    /**
     * 查询节能项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('energy:energyProject:list')")
    @GetMapping("/list")
    @ApiOperation(value = "节能项目列表")
    public TableDataInfo list(EnergyProject energyProject)
    {
        startPage();
        List<EnergyProject> list = energyProjectService.selectEnergyProjectList(energyProject);
        return getDataTable(list);
    }

    /**
     * 导出节能项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('energy:energyProject:export')")
    @Log(title = "节能项目管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation(value = "节能项目导出")
    public AjaxResult export(EnergyProject energyProject)
    {
        List<EnergyProject> list = energyProjectService.selectEnergyProjectList(energyProject);
        ExcelUtil<EnergyProject> util = new ExcelUtil<EnergyProject>(EnergyProject.class);
        return util.exportExcel(list, "energyProject");
    }

    /**
     * 获取节能项目管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:energyProject:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取节能项目详情")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(energyProjectService.selectEnergyProjectById(id));
    }

    /**
     * 新增节能项目管理
     */
    @PreAuthorize("@ss.hasPermi('energy:energyProject:add')")
    @Log(title = "节能项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增节能项目")
    public AjaxResult add(@RequestBody EnergyProject energyProject)
    {
        energyProject.setId(UUID.randomUUID().toString());
        energyProject.setCreateOperator(SecurityUtils.getUsername());
        return toAjax(energyProjectService.insertEnergyProject(energyProject));
    }

    /**
     * 修改节能项目管理
     */
    @PreAuthorize("@ss.hasPermi('energy:energyProject:edit')")
    @Log(title = "节能项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "编辑节能项目")
    public AjaxResult edit(@RequestBody EnergyProject energyProject)
    {
        return toAjax(energyProjectService.updateEnergyProject(energyProject));
    }

    /**
     * 删除节能项目管理
     */
    @PreAuthorize("@ss.hasPermi('energy:energyProject:remove')")
    @Log(title = "节能项目管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation(value = "删除节能项目")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(energyProjectService.deleteEnergyProjectByIds(ids));
    }
}
