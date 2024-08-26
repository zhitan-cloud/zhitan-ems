package com.dingzhuo.energy.project.basicSetup.controller;

import com.dingzhuo.energy.framework.config.SystemConfig;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 首页配置Controller
 *
 * @author sys
 * @date 2021-04-09
 */
@RestController
@RequestMapping("/HomePage")
public class HomePageIndexController extends BaseController
{

    /**
     * 获取 首页 IFRAME的内嵌URL地址
     * @return
     */
    @GetMapping(value = "/indexIframeUrl")
    public AjaxResult indexIframeUrl(){
        Map<String,String> map = new HashMap<>();
        //从系统配置文件中获取 配置的  url地址
        map.put("INDEXIFRAMEURL", SystemConfig.getIndexUrl());
        return AjaxResult.success(map);
    }
}
