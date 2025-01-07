package com.zhitan.web.controller.system;

import com.zhitan.common.annotation.Anonymous;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.system.domain.SysNameConfig;
import com.zhitan.system.service.ISysNameConfigService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 系统名称配置Controller
 * 
 * @author ZhiTan
 * @date 2024-11-05
 */
@RestController
@RequestMapping("/system/nameconfig")
public class SysNameConfigController extends BaseController
{
    @Resource
    private ISysNameConfigService sysNameConfigService;


    /**
     * 新增系统名称配置
     */
    @Log(title = "系统名称配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult save(@RequestBody SysNameConfig sysNameConfig)
    {
        return toAjax(sysNameConfigService.saveOrUpdate(sysNameConfig));
    }

    /**
     * 新增系统名称配置
     */
    @GetMapping
    @Anonymous
    public AjaxResult getSysNameConfig()
    {
        return AjaxResult.success(sysNameConfigService.getSysNameConfig());
    }

}
