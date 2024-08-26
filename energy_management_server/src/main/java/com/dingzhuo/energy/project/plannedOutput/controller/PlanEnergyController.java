package com.dingzhuo.energy.project.plannedOutput.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.project.plannedOutput.domain.PlanEnergy;
import com.dingzhuo.energy.project.plannedOutput.service.IPlanEnergyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;

/**
 * 【请填写功能名称】Controller
 * 
 * @author sys
 * @date 2020-12-17
 */
@RestController
@RequestMapping("/PlanEnergy")
public class PlanEnergyController extends BaseController {
    @Autowired
    private IPlanEnergyService planEnergyService;
    @Autowired
    private IModelNodeService modelNodeService;
    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:energy:list')")
    @GetMapping("/list")
    public AjaxResult list(PlanEnergy planEnergy)
    {

        List<SysProduct> productList= modelNodeService.getSettingProduct(planEnergy.getIndexCode());
        List<Integer> indexCode = productList.stream().map(SysProduct::getProductid).collect(Collectors.toList());
        List<PlanEnergy> list;
        list = planEnergyService.PlanEnergyList(indexCode,planEnergy.getTimeType(),planEnergy.getDataTime(),planEnergy.getType());
        if(list.size()==0){
            list = planEnergyService.selectPlanEnergyList(indexCode,planEnergy.getTimeType(),planEnergy.getDataTime());
        }
        return AjaxResult.success(list);
    }
    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:energy:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody List<PlanEnergy> listMap){
        List<PlanEnergy> dataItems = new ArrayList<>();
        listMap.forEach(data -> {
            String timeCode = TimeManager.getTimeCode(data.getDataTime(), data.getTimeType());
            PlanEnergy dataItem = new PlanEnergy();
            dataItem.setTimeCode(timeCode);
            dataItem.setProductId(data.getProductId());
            dataItem.setTimeType(data.getTimeType());
            dataItem.setActualValue(data.getActualValue());
            dataItem.setValue(data.getValue());
            dataItem.setEnergyValue(data.getEnergyValue());
            dataItem.setCoalValue(data.getCoalValue());
            data.setCoalEnergyValue(data.getCoalEnergyValue());
            dataItem.setSteamValue(data.getSteamValue());
            dataItem.setSteamEnereyValue(data.getSteamEnereyValue());
            dataItem.setWaterValue(data.getWaterValue());
            dataItem.setWaterEnergyValue(data.getWaterEnergyValue());
            dataItem.setDataTime(dataItem.getDataTime());
            dataItems.add(dataItem);
        });
        try {
            this.planEnergyService.insertPlanEnergy(dataItems);
            return AjaxResult.success("保存成功！");
        }catch (Exception ex) {
            logger.error("保存失败！" + ex.getMessage());
            return AjaxResult.success(ex.getMessage());
        }
        //return toAjax(planEnergyService.insertPlanEnergy(planEnergy));
    }
}
