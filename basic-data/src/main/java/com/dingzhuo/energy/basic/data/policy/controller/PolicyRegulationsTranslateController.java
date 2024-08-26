package com.dingzhuo.energy.basic.data.policy.controller;

import com.dingzhuo.energy.basic.data.policy.domain.PolicyRegulationsTranslate;
import com.dingzhuo.energy.basic.data.policy.service.IPolicyRegulationsTranslateService;
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
 * 政策法规查询Controller
 * 
 * @author liuli
 * @date 2020-04-24
 */
@RestController
@RequestMapping("/policy/policyselete")
public class PolicyRegulationsTranslateController extends BaseController
{
    @Autowired
    private IPolicyRegulationsTranslateService policyRegulationsTranslateService;
    /**
     * 查询政策法规查询列表
     */
    @PreAuthorize("@ss.hasPermi('policy:policyselete:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyRegulationsTranslate policyRegulationsTranslate)
    {
        startPage();
        List<PolicyRegulationsTranslate> list = policyRegulationsTranslateService.selectPolicyRegulationsTranslateList(policyRegulationsTranslate);
        return getDataTable(list);
    }

    /**
     * 导出政策法规查询列表
     */
    @PreAuthorize("@ss.hasPermi('policy:policyselete:export')")
    @Log(title = "政策法规查询", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PolicyRegulationsTranslate policyRegulationsTranslate)
    {
        List<PolicyRegulationsTranslate> list = policyRegulationsTranslateService.selectPolicyRegulationsTranslateList(policyRegulationsTranslate);
        ExcelUtil<PolicyRegulationsTranslate> util = new ExcelUtil<PolicyRegulationsTranslate>(PolicyRegulationsTranslate.class);
        return util.exportExcel(list, "policyselete");

    }

    /**
     * 获取政策法规查询详细信息
     */
    @PreAuthorize("@ss.hasPermi('policy:policyselete:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(policyRegulationsTranslateService.selectPolicyRegulationsTranslateById(id));
    }

    /**
     * 新增政策法规查询
     */
    @PreAuthorize("@ss.hasPermi('policy:policyselete:add')")
    @Log(title = "政策法规查询", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PolicyRegulationsTranslate policyRegulationsTranslate)
    {
        policyRegulationsTranslate.setId(UUID.randomUUID().toString());
        policyRegulationsTranslate.setOperator(SecurityUtils.getUsername());
        policyRegulationsTranslate.setCreationTime(DateUtils.getNowDate());
        return toAjax(policyRegulationsTranslateService.insertPolicyRegulationsTranslate(policyRegulationsTranslate));
    }

    /**
     * 修改政策法规查询
     */
    @PreAuthorize("@ss.hasPermi('policy:policyselete:edit')")
    @Log(title = "政策法规查询", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PolicyRegulationsTranslate policyRegulationsTranslate)
    {
        return toAjax(policyRegulationsTranslateService.updatePolicyRegulationsTranslate(policyRegulationsTranslate));
    }

    /**
     * 删除政策法规查询
     */
    @PreAuthorize("@ss.hasPermi('policy:policyselete:remove')")
    @Log(title = "政策法规查询", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(policyRegulationsTranslateService.deletePolicyRegulationsTranslateByIds(ids));
    }
}
