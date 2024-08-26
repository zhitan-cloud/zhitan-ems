package com.dingzhuo.energy.project.reportForm.controller;

import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.alarm.service.IAlarmItemService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.security.service.TokenService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.reportForm.domain.reportSet;
import com.dingzhuo.energy.project.reportForm.domain.reportSetRequestModel;
import com.dingzhuo.energy.project.reportForm.service.IreportSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 上报设置Controller
 *
 * @author sys
 * @date 2020-03-02
 */
@RestController
@RequestMapping("/system/report")
public class ReportSetController extends BaseController {

    @Autowired
    private IAlarmItemService alarmItemService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IreportSetService reportSetService;

    /**
     * 查询上报设置列表
     */
    @PreAuthorize("@ss.hasPermi('system:reportSet:list')")
    @GetMapping("/list")
    public TableDataInfo list(reportSetRequestModel requestModel) {
        List<reportSet> list = reportSetService.listReportSet(requestModel);
        return getDataTable(list);
    }

    /**
     * 保存上报配置
     *
     * @param listMap
     * @return
     */
    @PutMapping(value = "/save")
    public AjaxResult save(@RequestBody List<reportSet> listMap) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String username = loginUser.getUsername();

        return toAjax(reportSetService.saveReportSet(listMap, username));
    }

    /**
     * 查询节点下的点位
     *
     * @param nodeId    节点id
     * @param indexType 点位类型
     * @return
     */
    @GetMapping(value = "/listNodeEnergyIndex")
    public AjaxResult listNodeEnergyIndex(String nodeId, String indexType) {

        return AjaxResult.success(reportSetService.listNodeIndex(nodeId, indexType));
    }

    /**
     * 查询节点下的点位
     *
     * @param nodeId       节点id
     * @param indexId      点位id
     * @param enableStatus 启用限值状态
     * @return
     */
    @GetMapping(value = "/updateEnableStatus")
    public AjaxResult updateEnableStatus(String nodeId, String indexId, boolean enableStatus) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String username = loginUser.getUsername();
//        int res = reportSetService.updateEnableStatus(nodeId, indexId, enableStatus, username);
//        if (res <= 0) {
//            return AjaxResult.error("请先设置报警值以及替换值！");
//        }
        return AjaxResult.success(reportSetService.updateEnableStatus(nodeId, indexId, enableStatus, username));
    }
}
