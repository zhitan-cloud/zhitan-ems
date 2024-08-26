package com.dingzhuo.energy.project.gateway.controller;

import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.gateway.domain.GatewayHbtLog;
import com.dingzhuo.energy.project.gateway.service.IGatewayHbtLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 网关心跳日志Controller
 *
 * @author zhw
 * @date 2022-04-15
 */
@RestController
@RequestMapping("/gateway/gatewaylog")
public class GatewayHbtLogController extends BaseController
{
    @Autowired
    private IGatewayHbtLogService gatewayHbtLogService;

    /**
     * 查询网关心跳日志列表
     */
    @GetMapping("/list")
    public TableDataInfo list(GatewayHbtLog gatewayHbtLog)
    {
        startPage();
        List<GatewayHbtLog> list = gatewayHbtLogService.selectGatewayHbtLogList(gatewayHbtLog);
        return getDataTable(list);
    }

    /**
     * 导出网关心跳日志列表
     */
    @Log(title = "网关心跳日志", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(GatewayHbtLog gatewayHbtLog)
    {
        List<GatewayHbtLog> list = gatewayHbtLogService.selectGatewayHbtLogList(gatewayHbtLog);
        ExcelUtil<GatewayHbtLog> util = new ExcelUtil<GatewayHbtLog>(GatewayHbtLog.class);
        return util.exportExcel(list, "gatewaylog");
    }

    /**
     * 获取网关心跳日志详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(gatewayHbtLogService.selectGatewayHbtLogById(id));
    }

    /**
     * 新增网关心跳日志
     */
    @Log(title = "网关心跳日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody GatewayHbtLog gatewayHbtLog)
    {
        gatewayHbtLog.setId(UUID.fastUUID().toString());
        return toAjax(gatewayHbtLogService.insertGatewayHbtLog(gatewayHbtLog));
    }

    /**
     * 修改网关心跳日志
     */
    @Log(title = "网关心跳日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody GatewayHbtLog gatewayHbtLog)
    {
        return toAjax(gatewayHbtLogService.updateGatewayHbtLog(gatewayHbtLog));
    }

    /**
     * 删除网关心跳日志
     */
    @Log(title = "网关心跳日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(gatewayHbtLogService.deleteGatewayHbtLogByIds(ids));
    }
}
