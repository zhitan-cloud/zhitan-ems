package com.zhitan.web.controller.alarm;

import com.zhitan.alarm.domain.dto.AlarmAnalysisDTO;
import com.zhitan.alarm.domain.vo.AlarmAnalysisVO;
import com.zhitan.alarm.services.IAlarmAnalysisService;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* description 报警分析相关功能
 *
 * @author zhitan
 * @date
 */
@RestController
@RequestMapping("/alarmAnalysis")
public class AlarmAnalysisController extends BaseController {
    @Autowired
    private IAlarmAnalysisService alarmAnalysisService;

    @GetMapping("/getByNodeId")
    public AjaxResult getByNodeId(@Validated AlarmAnalysisDTO alarmAnalysisDTO){
        AlarmAnalysisVO alarmAnalysisVO  = alarmAnalysisService.getByNodeId(alarmAnalysisDTO);
        return AjaxResult.success(alarmAnalysisVO);
    }

    @GetMapping("/getCountInfo")
    public AjaxResult getCountInfo(@Validated AlarmAnalysisDTO alarmAnalysisDTO){

        if(ObjectUtils.isEmpty(alarmAnalysisDTO.getModelCode())){
            return AjaxResult.error("模型编码不能为空");
        }
        AlarmAnalysisVO alarmAnalysisVO  = alarmAnalysisService.getCountInfo(alarmAnalysisDTO);
        return AjaxResult.success(alarmAnalysisVO);
    }
}
