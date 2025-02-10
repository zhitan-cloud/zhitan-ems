package com.zhitan.web.controller.realtimedata;

import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.domain.dto.EnergyIndexMonitorDTO;
import com.zhitan.realtimedata.domain.vo.EquipmentMeasuringPointParameters;
import com.zhitan.realtimedata.domain.vo.EquipmentPointParametersExcel;
import com.zhitan.realtimedata.domain.vo.ExportrealtimeTrendVO;
import com.zhitan.realtimedata.service.ISvgTrendService;
import com.zhitan.realtimedata.service.RealtimeDatabaseService;
import com.zhitan.realtimedata.service.RealtimeTrendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 实时监测控制类
 **/
@RestController
@RequestMapping("rtdb/realtimeTrend")
@Api(value = "实时监控",tags = {"实时监控"})
public class RealtimeTrendController extends BaseController {

    @Autowired
    private RealtimeTrendService realtimeTrendService;
    @Resource
    private ISvgTrendService svgTrendService;
    @Resource
    private RealtimeDatabaseService realtimeDatabaseService;

    /**
     * 获取模型节点关联采集指标
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取模型节点关联采集指标")
    public AjaxResult list(@Validated EnergyIndexMonitorDTO energyIndexMonitorDTO){
        return AjaxResult.success(realtimeTrendService.list(energyIndexMonitorDTO));
    }

    /**
     * 获取历史模型节点关联采集指标数据
     */
    @Log(title = "获取历史模型节点关联采集指标数据", businessType = BusinessType.UPDATE)
    @GetMapping("/chartByDay")
    @ApiOperation(value = "获取历史模型节点关联采集指标数据")
    public AjaxResult lineList(@RequestParam String tagCode, @RequestParam String dataTime){
        return AjaxResult.success(realtimeTrendService.chartByDay(tagCode,dataTime));
    }

    /**
     * 导出实时监测Excel信息
     */
    @Log(title = "导出实时监测Excel信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ApiOperation(value = "导出实时监测Excel信息")
    public void export(HttpServletResponse response , ExportrealtimeTrendVO exportrealtimeTrendVO){
        List<EquipmentPointParametersExcel> list = realtimeTrendService.export(exportrealtimeTrendVO);
        ExcelUtil<EquipmentPointParametersExcel> util = new ExcelUtil<EquipmentPointParametersExcel>(EquipmentPointParametersExcel.class);
        util.exportExcel(response,list, "实时监测");
    }

    /**
     * 获取模型节点关联采集指标
     */
    @Log(title = "获取模型节点关联采集指标", businessType = BusinessType.UPDATE)
    @GetMapping("/svgTrendView/energyIndex/list")
    @ApiOperation(value = "获取模型节点关联采集指标")
    public AjaxResult getSvgTrendViewSettingIndex(EnergyIndex energyIndex) {
        try {
            List<EnergyIndex> infoList = svgTrendService.selectSvgList(energyIndex);
            if (infoList == null || infoList.isEmpty()){
                return AjaxResult.success(Collections.emptyList());
            }
            List<String> codeList = infoList.stream().map(EnergyIndex::getCode).collect(Collectors.toList());

            List<TagValue> valList = realtimeDatabaseService.retrieve(codeList);
            if (valList == null || valList.isEmpty()) {
                return AjaxResult.success(Collections.emptyList());
            }

            Map<String, List<TagValue>> tagValueMap = valList.stream().collect(Collectors.groupingBy(TagValue::getTagCode));

            List<EquipmentMeasuringPointParameters> resultList = infoList.stream().map(index -> {
                EquipmentMeasuringPointParameters item = new EquipmentMeasuringPointParameters();
                item.setCode(index.getCode());
                item.setIndexName(index.getName());
                item.setIndexUnit(index.getUnitId());
                item.setMeteName(index.getMeterName());
                item.setyValue("y0");

                List<TagValue> tagValueList = tagValueMap.getOrDefault(index.getCode(), Collections.emptyList());
                if (!tagValueList.isEmpty()){
                    Optional<Double> sumOptional = tagValueList.stream()
                            .map(TagValue::getValue)
                            .reduce(Double::sum);
                    sumOptional.ifPresent(sum -> item.setValue(BigDecimal.valueOf(sum)
                            .setScale(2, RoundingMode.HALF_UP).doubleValue()));
                }
                return item;
            }).collect(Collectors.toList());

            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            logger.error("获取关联采集指标出错！", ex);
            return AjaxResult.error("获取关联指标出错!");
        }
    }
}
