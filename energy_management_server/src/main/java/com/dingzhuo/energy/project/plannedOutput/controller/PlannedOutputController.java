package com.dingzhuo.energy.project.plannedOutput.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.project.plannedOutput.domain.PlannedOutput;
import com.dingzhuo.energy.project.plannedOutput.service.IPlannedOutputService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;

/**
 * plannedOutputController
 * 
 * @author sys
 * @date 2020-12-16
 */
@RestController
@RequestMapping("/planned/plannedOutput")
public class PlannedOutputController extends BaseController {
    @Autowired
    private IPlannedOutputService plannedOutputService;
    @Autowired
    private IModelNodeService modelNodeService;

    /**
     * 查询plannedOutput列表
     */
    @PreAuthorize("@ss.hasPermi('planned:plannedOutput:list')")
    @GetMapping("/list")
    public AjaxResult list(PlannedOutput plannedOutput) {
        //startPage();
        List<SysProduct> productList= modelNodeService.getSettingProduct(plannedOutput.getIndexCode());
        List<Integer> indexCode = productList.stream().map(SysProduct::getProductid).collect(Collectors.toList());
        List<PlannedOutput> list= plannedOutputService.selectPlannedOutputList(indexCode,plannedOutput.getTimeType(),plannedOutput.getDataTime(),plannedOutput.getPalnType());
        if(list.size()==0){
            list= plannedOutputService.selectPlanList(indexCode);
        }
        //return getDataTable(list);
        return AjaxResult.success(list);
    }

    /**
     * 新增plannedOutput
     */
    @PreAuthorize("@ss.hasPermi('planned:plannedOutput:add')")
    @Log(title = "plannedOutput", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<PlannedOutput> listMap) {
        List<PlannedOutput> dataItems = new ArrayList<>();
        listMap.forEach(data -> {
            String timeCode = TimeManager.getTimeCode(data.getDataTime(), data.getTimeType());
            PlannedOutput dataItem = new PlannedOutput();
            dataItem.setPalnType(data.getPalnType());
            dataItem.setTimeCode(timeCode);
            dataItem.setDataTime(data.getDataTime());
            dataItem.setProductId(data.getProductId());
            dataItem.setPlanValue(data.getPlanValue());
            dataItem.setValue(data.getValue());
            dataItem.setCoalValue(data.getCoalValue());
            dataItem.setSteamValue(data.getSteamValue());
            dataItem.setWaterValue(data.getWaterValue());
            dataItems.add(dataItem);
        });
        try {
            this.plannedOutputService.insertPlannedOutput(dataItems);
            return AjaxResult.success("保存成功！");
        }catch (Exception ex) {
            logger.error("保存失败！" + ex.getMessage());
            return AjaxResult.success(ex.getMessage());
        }
        //return toAjax(plannedOutputService.insertPlannedOutput(plannedOutput));
    }

    /**
     * 修改plannedOutput
     */
    @PreAuthorize("@ss.hasPermi('planned:plannedOutput:edit')")
    @Log(title = "plannedOutput", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PlannedOutput plannedOutput) {
        return toAjax(plannedOutputService.updatePlannedOutput(plannedOutput));
    }


    /**
     * 导出plannedOutput列表
     */
    @PreAuthorize("@ss.hasPermi('planned:plannedOutput:export')")
    @Log(title = "plannedOutput", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PlannedOutput plannedOutput) {
        List<SysProduct> productList= modelNodeService.getSettingProduct(plannedOutput.getIndexCode());
        List<Integer> indexCode = productList.stream().map(SysProduct::getProductid).collect(Collectors.toList());
        List<PlannedOutput> list= plannedOutputService.selectPlannedOutputList(indexCode,plannedOutput.getTimeType(),plannedOutput.getDataTime(),plannedOutput.getPalnType());
        ExcelUtil<PlannedOutput> util = new ExcelUtil<PlannedOutput>(PlannedOutput.class);
        return util.exportExcel(list, "plannedOutput");
    }
}