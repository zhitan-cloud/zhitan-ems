package com.dingzhuo.energy.project.electricityTypeSetting.controller;

import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingAddVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityTypeSettingUpdateVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingItemQueryVO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.ElectricityTypeSettingPageListVO;
import com.dingzhuo.energy.project.electricityTypeSetting.service.IElectricityTypeSettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 计费策略Controller
 *
 * @author ruoyi
 * @date 2024-06-19
 */
@RestController
@RequestMapping("/rule")
@Api(tags = "计费策略")
public class ElectricityTypeSettingController extends BaseController {

    @Resource
    private IElectricityTypeSettingService rulesService;


    /**
     * 查询计费规则列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "分页查询计费规则列表")
    public TableDataInfo list(@ApiParam("计费规则名称") @RequestParam(value = "name", required = false) String name) {
        startPage();
        List<ElectricityTypeSettingPageListVO> responsePage = rulesService.selectPageList(name);
        return getDataTable(responsePage);
    }

    /**
     * 获取计费规则详情
     */
    @GetMapping(value = "/getRuleDetail")
    @ApiOperation(value = "获取计费规则详情")
    public AjaxResult getRuleDetail(@ApiParam("计费规则id") @NotNull(message = "id不能为空!") @RequestParam("id") String id) {
        ElectricityTypeSettingItemQueryVO response = rulesService.getRuleDetail(id);
        return AjaxResult.success(response);
    }

    /**
     * 新增计费策略
     */
    @PostMapping("/addRule")
    @ApiOperation(value = "新增计费策略")
    @Log(title = "新增计费策略", businessType = BusinessType.INSERT)
    public AjaxResult addRule(@RequestBody @Validated ElectricityTypeSettingAddVO request) {
        rulesService.addRule(request);
        return AjaxResult.success();
    }

    /**
     * 修改计费策略
     */
    @PostMapping("/updateRule")
    @ApiOperation(value = "修改计费策略")
    @Log(title = "修改计费策略", businessType = BusinessType.UPDATE)
    public AjaxResult updateRule(@RequestBody @Validated ElectricityTypeSettingUpdateVO request) {
        rulesService.updateRule(request);
        return AjaxResult.success();
    }

    /**
     * 删除计费策略
     */
    @DeleteMapping("/delRule/{id}")
    @ApiOperation(value = "删除计费策略")
    @Log(title = "删除计费策略", businessType = BusinessType.DELETE)
    public AjaxResult delRule(@ApiParam("计费规则id") @NotNull(message = "id不能为空!") @PathVariable("id") String id) {
        rulesService.delRule(id);
        return AjaxResult.success();
    }
}
