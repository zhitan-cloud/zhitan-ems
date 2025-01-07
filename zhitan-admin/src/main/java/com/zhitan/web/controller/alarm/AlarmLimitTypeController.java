package com.zhitan.web.controller.alarm;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.LimitType;
import com.zhitan.alarm.services.IAlarmLimitTypeService;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.framework.web.service.TokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Description  报警限值类型管理Controller
 *
 * @Author zhoubg
 * @Date 2024-10-11
 */

@RestController
@RequestMapping("/alarm/limit")
@Api(value = "报警限值类型管理",tags = {"报警限值类型管理"})
public class AlarmLimitTypeController extends BaseController {

    @Autowired
    private IAlarmLimitTypeService alarmLimitTypeService;

    @Autowired
    private TokenService tokenService;


    /**
     * 列表
     *
     **/
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:list')")
    @GetMapping("/list")
    @ApiOperation(value = "报警限值列表")
    public TableDataInfo list(LimitType alarmLimitType, Long pageNum, Long pageSize){
        Page<LimitType> page = alarmLimitTypeService.selectAlarmLimitTypePage(alarmLimitType, pageNum, pageSize);
        return getDataTable(page);
    }

    /**
     * 获取详情
     *
     */
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "获取报警限值类型维护详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(alarmLimitTypeService.selectAlarmLimitTypeById(id));
    }

    /**
     * 新增
     *
     **/
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:add')")
    @Log(title = "新增报警限值", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ApiOperation(value = "新增报警限值")
    public AjaxResult add(@RequestBody LimitType alarmLimitType){
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        alarmLimitType.setCreateBy(loginUser.getUsername());
        return toAjax(alarmLimitTypeService.insertAlarmLimitType(alarmLimitType)) ;
    }

    /**
     * 编辑
     *
     **/
    @PreAuthorize("@ss.hasPermi('facility:annex:edit')")
    @Log(title = "修改报警限值", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "计量器具列表")
    public AjaxResult edit(@RequestBody LimitType alarmLimitType){
        return toAjax(alarmLimitTypeService.updateAlarmLimitType(alarmLimitType));
    }

    /**
     * 删除
     *
     **/
    @PreAuthorize("@ss.hasPermi('basicsetting:limitType:remove')")
    @Log(title = "删除报警限值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除报警限值")
    public AjaxResult remove(@PathVariable String[] ids){
        return toAjax(alarmLimitTypeService.deleteLimitTypeByIds(ids));
    }

}
