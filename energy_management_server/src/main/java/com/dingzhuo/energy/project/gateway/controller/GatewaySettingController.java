package com.dingzhuo.energy.project.gateway.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.gateway.domain.GatewaySetting;
import com.dingzhuo.energy.project.gateway.service.IGatewaySettingService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 网关配置信息Controller
 *
 * @author zhw
 * @date 2022-04-15
 */
@RestController
@RequestMapping("/gateway/gatewaysetting")
public class GatewaySettingController extends BaseController
{
    @Autowired
    private IGatewaySettingService gatewaySettingService;

    /**
     * 查询网关配置信息列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GatewaySetting gatewaySetting)
    {
        startPage();
        List<GatewaySetting> list = gatewaySettingService.selectGatewaySettingList(gatewaySetting);
        return getDataTable(list);
    }

    /**
     * 导出网关配置信息列表
     */
    @Log(title = "网关配置信息", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GatewaySetting gatewaySetting)
    {
        List<GatewaySetting> list = gatewaySettingService.selectGatewaySettingList(gatewaySetting);
        ExcelUtil<GatewaySetting> util = new ExcelUtil<GatewaySetting>(GatewaySetting.class);
        return util.exportExcel(list, "gatewaysetting");
    }

    /**
     * 获取网关配置信息详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(gatewaySettingService.selectGatewaySettingById(id));
    }

    /**
     * 新增网关配置信息
     */
    @Log(title = "新增网关配置信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GatewaySetting gatewaySetting)
    {
        List<GatewaySetting> gatewaySettingList =  gatewaySettingService.checkOne(gatewaySetting);
        if(!CollectionUtil.isEmpty(gatewaySettingList)){
            return AjaxResult.error("网关编号重复");
        }
        gatewaySetting.setId(UUID.fastUUID().toString());
        return toAjax(gatewaySettingService.insertGatewaySetting(gatewaySetting));
    }

    /**
     * 修改网关配置信息
     */
    @Log(title = "网关配置信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GatewaySetting gatewaySetting)
    {
        if(ObjectUtils.isEmpty(gatewaySetting.getId())){
            return AjaxResult.error("无网关主键");
        }
        List<GatewaySetting> gatewaySettingList =  gatewaySettingService.checkOne(gatewaySetting);
        if(!CollectionUtil.isEmpty(gatewaySettingList)){
            return AjaxResult.error("网关编号重复");
        }
        return toAjax(gatewaySettingService.updateGatewaySetting(gatewaySetting));
    }

    /**
     * 删除网关配置信息
     */
    @Log(title = "网关配置信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(gatewaySettingService.deleteGatewaySettingByIds(ids));
    }

    /**
     * 查询网关配置信息列表
     */
    @GetMapping("/monitor")
    public TableDataInfo monitor()
    {
        startPage();
        List<GatewaySetting> list = gatewaySettingService.selectGatewaySettingMonitorList();

        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(f->{
                Date dd = f.getHbtTime();
                //3分钟没有心跳就超时
                if(ObjectUtils.isEmpty(dd)){
                    f.setState("未知");
                }else  if(dd.getTime()+60000*5>(new Date()).getTime())
                {
                    f.setState("在线");
                }else{
                    f.setState("离线");
                }
            });
        }
        list.stream().sorted(Comparator.comparing(GatewaySetting::getHbtTime));
        return getDataTable(list);
    }
}
