package com.dingzhuo.energy.basic.data.policy.controller;



import com.dingzhuo.energy.basic.data.policy.domain.PolicyRegulations;
import com.dingzhuo.energy.basic.data.policy.service.IPolicyRegulationsService;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.security.service.TokenService;
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
 * 政策法规维护Controller
 *
 * @author liuli
 * @date 2020-04-23
 */
@RestController
@RequestMapping("/policy/policyInsert")
@Api(value = "政策法规管理",tags = {"政策法规管理"})
public class PolicyRegulationsController extends BaseController
{
    @Autowired
    private IPolicyRegulationsService policyRegulationsService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询政策法规维护列表
     */
    @ApiOperation(value = "政策法规列表")
    @PreAuthorize("@ss.hasPermi('policy:policyInsert:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyRegulations policyRegulations)
    {
        startPage();
        List<PolicyRegulations> list = policyRegulationsService.selectPolicyRegulationsList(policyRegulations);
        return getDataTable(list);
    }
    /**
     * 导出计量器具档案维护列表
     */
    @ApiOperation(value = "政策法规列表导出")
    @PreAuthorize("@ss.hasPermi('policy:policyInsert:export')")
    @Log(title = "政策法规查询", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PolicyRegulations policyRegulations)
    {
        List<PolicyRegulations> list = policyRegulationsService.selectPolicyRegulationsList(policyRegulations);
        ExcelUtil<PolicyRegulations> util = new ExcelUtil<PolicyRegulations>(PolicyRegulations.class);
        return util.exportExcel(list, "implement");
    }
    /**
     * 获取政策法规维护详细信息
     */
    @ApiOperation(value = "根据id获取政策法规详情")
    @PreAuthorize("@ss.hasPermi('policy:policyInsert:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(policyRegulationsService.selectPolicyRegulationsById(id));
    }

    /**
     * 新增政策法规维护
     */
    @ApiOperation(value = "新增政策法规")
    @PreAuthorize("@ss.hasPermi('policy:policyInsert:add')")
    @Log(title = "政策法规维护", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PolicyRegulations policyRegulations)
    {
        policyRegulations.setId(UUID.randomUUID().toString());
        policyRegulations.setOperator(SecurityUtils.getUsername());
        policyRegulations.setCreationTime(DateUtils.getNowDate());
        return toAjax(policyRegulationsService.insertPolicyRegulations(policyRegulations));
    }

    /**
     * 修改政策法规维护
     */
    @ApiOperation(value = "编辑政策法规")
    @PreAuthorize("@ss.hasPermi('policy:policyInsert:edit')")
    @Log(title = "政策法规维护", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PolicyRegulations policyRegulations)
    {
        return toAjax(policyRegulationsService.updatePolicyRegulations(policyRegulations));
    }

    /**
     * 删除政策法规维护
     */
    @ApiOperation(value = "删除政策法规")
    @PreAuthorize("@ss.hasPermi('policy:policyInsert:remove')")
    @Log(title = "政策法规维护", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(policyRegulationsService.deletePolicyRegulationsByIds(ids));
    }


}
