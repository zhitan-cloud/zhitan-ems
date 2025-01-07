package com.zhitan.web.controller.realtimedata;

import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.realtimedata.domain.dto.EnergyIndexMonitorDTO;
import com.zhitan.realtimedata.domain.vo.EquipmentPointParametersExcel;
import com.zhitan.realtimedata.domain.vo.ExportrealtimeTrendVO;
import com.zhitan.realtimedata.service.RealtimeTrendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description 实时监测控制类
 *
 * @Author zhoubg
 * @date 2024-10-15
 **/
@RestController
@RequestMapping("rtdb/realtimeTrend")
@Api(value = "实时监控",tags = {"实时监控"})
public class RealtimeTrendController extends BaseController {

    @Autowired
    private RealtimeTrendService realtimeTrendService;

    /**
     * 获取模型节点关联采集指标
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取模型节点关联采集指标")
    public AjaxResult list(@Validated EnergyIndexMonitorDTO energyIndexMonitorDTO){
        return AjaxResult.success(realtimeTrendService.list(energyIndexMonitorDTO));
    }

    /**
     * 获取历史模型节点关联采集指标数据
     * @param tagCode
     * @param dataTime
     * @return
     */
    @Log(title = "获取历史模型节点关联采集指标数据", businessType = BusinessType.UPDATE)
    @GetMapping("/chartByDay")
    @ApiOperation(value = "获取历史模型节点关联采集指标数据")
    public AjaxResult lineList(@RequestParam String tagCode, @RequestParam String dataTime){
        return AjaxResult.success(realtimeTrendService.chartByDay(tagCode,dataTime));
    }

    /**
     * 导出实时监测Excel信息
     * @return
     */
    @Log(title = "导出实时监测Excel信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "导出实时监测Excel信息")
    public void export(HttpServletResponse response , ExportrealtimeTrendVO exportrealtimeTrendVO){
        List<EquipmentPointParametersExcel> list = realtimeTrendService.export(exportrealtimeTrendVO);
        ExcelUtil<EquipmentPointParametersExcel> util = new ExcelUtil<EquipmentPointParametersExcel>(EquipmentPointParametersExcel.class);
        util.exportExcel(response,list, "实时监测");
    }

}
