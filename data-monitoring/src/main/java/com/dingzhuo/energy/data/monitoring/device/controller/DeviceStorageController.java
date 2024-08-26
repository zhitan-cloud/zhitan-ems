package com.dingzhuo.energy.data.monitoring.device.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.data.model.domain.StateType;
import com.dingzhuo.energy.data.model.service.IStateTypeService;
import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceFormulaParamService;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceFormulaService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * 设备状态配置Controller
 * 
 * @author zhaow
 * @date 2020-03-02
 */
@RestController
@RequestMapping("/basicsetting/deviceStorage")
public class DeviceStorageController extends BaseController
{

    @Autowired
    private  IDeviceFormulaService deviceFormulaService;

    @Autowired
    private IDeviceFormulaParamService deviceFormulaParamService;

    @Autowired
    private IStateTypeService stateTypeService;
    /**
     * 设备状态管理点击状态进行配置时的 初始化页面数据
     * @param deviceFormula
     * @return
     */
    @GetMapping("/getDeviceStateByNodeIds")
    public TableDataInfo getDeviceStateByNodeIds(DeviceFormula deviceFormula) {

        startPage();
        //节点设备已设置的 状态信息集合
        List<DeviceFormula> deviceFormulas = new ArrayList<>();
        //取系统默认设置所有的设备状态
        List<StateType> list = stateTypeService.selectStateTypeList(new StateType());
        //迭代系统参数数量填充 已设置的状态
        list.forEach(param ->{
            DeviceFormula tempDev = deviceFormulaService.getDeviceFormula(deviceFormula.getDeviceId(),param.getStateId());
            if(tempDev.getIsEnable()==null || tempDev.getIsEnable().isEmpty())
            {
                tempDev.setIsEnable("N");
            }
            tempDev.setStateType(param);
            deviceFormulas.add(tempDev);
        });
        return getDataTable(deviceFormulas);
    }

    /**
     * 设备状态管理点击状态进行配置时的 初始化页面数据
     * @param nodeId
     * @return
     */
    @GetMapping("/{nodeId}/{stateId}")
    public AjaxResult getDeviceStorage(@PathVariable String nodeId,@PathVariable String stateId) {
        JSONObject result = new JSONObject();
        //1、取根据 设备模型 节点ID  和  设备状态主键 ID  取  设备公式信息
        DeviceFormula deviceFormula = deviceFormulaService.getDeviceFormula(nodeId,stateId);

        result.put("deviceFormula", deviceFormula);
        return AjaxResult.success(result);
    }

    /**
     * 设备状态 配置 公式保存
     * @param param
     * @param nodeId
     * @param stateId
     * @return
     */
    @PostMapping("/{nodeId}/{stateId}")
    public AjaxResult saveDeviceStorage(@RequestBody JSONObject param, @PathVariable String nodeId, @PathVariable String stateId) {
        try {
            DeviceFormula deviceFormula = param.getObject("deviceFormula", DeviceFormula.class);
            deviceFormula.setDeviceId(nodeId);
            deviceFormula.setStateId(stateId);
            deviceFormulaService.saveDeviceFormula(deviceFormula);
        } catch (Exception ex) {
            logger.error("", ex);
            return AjaxResult.error();
        }

        return AjaxResult.success();
    }

    /**
     * 设备状态 公式 启用设置
     */
    @Log(title = "备状态公式启用设置", businessType = BusinessType.UPDATE)
    @PostMapping(value="/setIsEnable/{nodeId}/{isEnable}")
    public AjaxResult editDeviceFormulaIsEnable(@PathVariable String nodeId,@PathVariable String isEnable,@RequestBody String[] ids)
    {
        return toAjax(deviceFormulaService.editDeviceFormulaIsEnable(nodeId,isEnable,ids));
    }
}
