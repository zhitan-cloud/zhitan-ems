package com.dingzhuo.energy.data.monitoring.alarm.controller;

import java.util.List;
import java.util.Map;

import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.security.service.TokenService;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.alarm.service.IAlarmItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 预报警设置Controller
 *
 * @author sys
 * @date 2020-03-02
 */
@RestController
@RequestMapping("/system/alarmitem")
public class AlarmItemController extends BaseController {

    @Autowired
    private IAlarmItemService alarmItemService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询预报警设置列表
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:list')")
    @GetMapping("/list")
    public TableDataInfo list(AlarmItem alarmItem) {
        startPage();
        List<AlarmItem> list = alarmItemService.selectAlarmItemList(alarmItem);
        return getDataTable(list);
    }

    /**
     * 导出预报警设置列表
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:export')")
    @Log(title = "预报警设置", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(AlarmItem alarmItem) {
        List<AlarmItem> list = alarmItemService.selectAlarmItemList(alarmItem);
        ExcelUtil<AlarmItem> util = new ExcelUtil<AlarmItem>(AlarmItem.class);
        return util.exportExcel(list, "set");
    }

    /**
     * 获取预报警设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id) {
        return AjaxResult.success(alarmItemService.selectAlarmItemById(id));
    }

    /**
     * 新增预报警设置
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:add')")
    @Log(title = "预报警设置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AlarmItem alarmItem) {
        return toAjax(alarmItemService.insertAlarmItem(alarmItem));
    }

    /**
     * 修改预报警设置启停状态
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:edit')")
    @Log(title = "预报警设置", businessType = BusinessType.UPDATE)
    @PostMapping(value = "/startstop/{flag}")
    public AjaxResult edit(@PathVariable String flag, @RequestBody String[] ids) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String username = loginUser.getUsername();
        return toAjax(alarmItemService.updateStartStop(ids, flag, username));
    }

    /**
     * 删除预报警设置
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:remove')")
    @Log(title = "预报警设置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(alarmItemService.deleteAlarmItemByIds(ids));
    }

    /**
     * 获取预报警设置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:query1')")
    @GetMapping(value = "/getSettingInfo/{id}")
    public AjaxResult getSettingInfo(@PathVariable("id") String id) {
        return AjaxResult.success(alarmItemService.selectAlarmItemtingById(id));
    }

    /**
     * 获取主表每行启停状态值
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:query1')")
    @GetMapping(value = "/getStartStop/{indexid}")
    public AjaxResult getStartStop(@PathVariable("indexid") String indexid) {
        return AjaxResult.success(alarmItemService.getStartStop(indexid));
    }

    /**
     * 修改弹出框限值
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:edit')")
    @Log(title = "修改弹出框限值", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/editLimitVal")
    public AjaxResult editLimitVal(@RequestBody List<Map<String, Object>> listMap) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String username = loginUser.getUsername();
        return toAjax(alarmItemService.updateLimitVal(listMap, username));
    }

    /**
     * 查询预id下设置限值的数量
     */
    @PreAuthorize("@ss.hasPermi('system:alarmitem:query1')")
    @GetMapping(value = "/getSettingCount/{id}")
    public AjaxResult getSettingCount(@PathVariable("id") String[] id) {
        return AjaxResult.success(alarmItemService.getSettingCount(id));
    }
}
