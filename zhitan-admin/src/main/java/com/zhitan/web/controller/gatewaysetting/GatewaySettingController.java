package com.zhitan.web.controller.gatewaysetting;

import javax.servlet.http.HttpServletResponse;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.zhitan.common.annotation.Anonymous;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.gatewaysetting.domain.GatewaySetting;
import com.zhitan.gatewaysetting.service.IGatewaySettingService;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;

import org.springframework.web.bind.annotation.*;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.enums.BusinessType;

import java.util.List;
import java.util.UUID;

/**
 * 网关配置信息Controller
 *
 * @author ZhiTan
 * @date 2024-10-30
 */
@RestController
@RequestMapping("/gatewaySetting")
public class GatewaySettingController extends BaseController
{
    @Resource
    private IGatewaySettingService gatewaySettingService;

    /**
     * 查询网关配置信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:setting:list')")
    @GetMapping("/list")
    public TableDataInfo list(GatewaySetting gatewaySetting,@RequestParam Long pageNum,@RequestParam Long pageSize)
    {
        Page<GatewaySetting> list = gatewaySettingService.selectGatewaySettingPage(gatewaySetting,pageNum,pageSize);
        return getDataTable(list);
    }

    /**
     * 查询计量器具数量和点位数量
     * @param gatewaySetting
     * @return
     */
    @GetMapping("/ptNum")
    public AjaxResult ptNum(GatewaySetting gatewaySetting)
    {
        return success(gatewaySettingService.ptNum(gatewaySetting));
    }


    /**
     * 导出网关配置信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:setting:export')")
    @Log(title = "网关配置信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, GatewaySetting gatewaySetting)
    {
        List<GatewaySetting> list = gatewaySettingService.selectGatewaySettingList(gatewaySetting);
        ExcelUtil<GatewaySetting> util = new ExcelUtil<GatewaySetting>(GatewaySetting.class);
        util.exportExcel(response, list, "网关配置信息数据");
    }

    /**
     * 获取网关配置信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:setting:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(gatewaySettingService.selectGatewaySettingById(id));
    }

    /**
     * 新增网关配置信息
     */
    @PreAuthorize("@ss.hasPermi('system:setting:add')")
    @Log(title = "网关配置信息", businessType = BusinessType.INSERT)
        @PostMapping
    public AjaxResult add(@RequestBody GatewaySetting gatewaySetting)
    {
        int repeatCount =  gatewaySettingService.checkOne(gatewaySetting);
        if(repeatCount>0){
            return AjaxResult.error("网关编号重复");
        }
        gatewaySetting.setId(UUID.randomUUID().toString());
        return toAjax(gatewaySettingService.insertGatewaySetting(gatewaySetting));
    }

    /**
     * 修改网关配置信息
     */
    @PreAuthorize("@ss.hasPermi('system:setting:edit')")
    @Log(title = "网关配置信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GatewaySetting gatewaySetting)
    {
        if(ObjectUtils.isEmpty(gatewaySetting.getId())){
            return AjaxResult.error("无网关主键");
        }
        int repeatCount =  gatewaySettingService.checkOne(gatewaySetting);
        if(repeatCount>0){
            return AjaxResult.error("网关编号重复");
        }
        return toAjax(gatewaySettingService.updateGatewaySetting(gatewaySetting));
    }

    /**
     * 删除网关配置信息
     */
    @PreAuthorize("@ss.hasPermi('system:setting:remove')")
    @Log(title = "网关配置信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(gatewaySettingService.deleteGatewaySettingByIds(ids));
    }

    /**
     * 查询所有网关编码或名称
     * @param gatewaySetting
     * @return
     */
    @GetMapping("/baseList")
    public AjaxResult baseList(GatewaySetting gatewaySetting)
    {
        return success(gatewaySettingService.selectGatewaySettingList(gatewaySetting));
    }
}
