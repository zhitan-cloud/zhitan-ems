package com.dingzhuo.energy.data.model.controller;

import java.util.List;

import com.dingzhuo.energy.common.utils.IdUtils;
import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.security.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
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
import com.dingzhuo.energy.data.model.domain.LimitType;
import com.dingzhuo.energy.data.model.service.ILimitTypeService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 报警限值类型维护Controller
 *
 * @author zw
 * @date 2020-03-07
 */
@RestController
@RequestMapping("/basicsetting/limitType")
@Api(value = "预报警管理",tags = {"预报警管理"})
public class LimitTypeController extends BaseController
{
    @Autowired
    private ILimitTypeService limitTypeService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询报警限值类型维护列表
     */
    //@PreAuthorize("@ss.hasPermi('basicsetting:limitType:list')")
    @GetMapping("/list")
    @ApiOperation(value = "报警限值列表")
    public TableDataInfo list(LimitType limitType)
    {
        startPage();
        List<LimitType> list = limitTypeService.selectLimitTypeList(limitType);
        return getDataTable(list);
    }

    /**
     * 导出报警限值类型维护列表
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:export')")
    @Log(title = "报警限值类型维护", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation(value = "报警限值列表导出")
    public AjaxResult export(LimitType limitType)
    {
        List<LimitType> list = limitTypeService.selectLimitTypeList(limitType);
        ExcelUtil<LimitType> util = new ExcelUtil<LimitType>(LimitType.class);
        return util.exportExcel(list, "limitType");
    }

    /**
     * 获取报警限值类型维护详细信息
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取报警限值类型维护详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(limitTypeService.selectLimitTypeById(id));
    }

    /**
     * 新增报警限值类型维护
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:add')")
    @Log(title = "报警限值类型维护", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增报警限值")
    public AjaxResult add(@RequestBody LimitType limitType)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        limitType.setId(IdUtils.fastUUID());
        limitType.setCreateBy(loginUser.getUsername());
        return toAjax(limitTypeService.insertLimitType(limitType));
    }

    /**
     * 修改报警限值类型维护
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:edit')")
    @Log(title = "报警限值类型维护", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "修改报警限值")
    public AjaxResult edit(@RequestBody LimitType limitType)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        limitType.setUpdateBy(loginUser.getUsername());
        return toAjax(limitTypeService.updateLimitType(limitType));
    }

    /**
     * 删除报警限值类型维护
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:remove')")
    @Log(title = "报警限值类型维护", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation(value = "删除报警限值")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(limitTypeService.deleteLimitTypeByIds(ids));
    }
}
