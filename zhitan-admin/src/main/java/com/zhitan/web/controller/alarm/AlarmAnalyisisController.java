package com.zhitan.web.controller.alarm;

import com.zhitan.alarm.domain.dto.AlarmAnalysisDTO;
import com.zhitan.alarm.domain.vo.AlarmAnalysisVO;
import com.zhitan.alarm.services.IAlarmAnalyisisService;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-26 17:31
 */
@RestController
@RequestMapping("/alarmAnalysis")
public class AlarmAnalyisisController extends BaseController {
    @Autowired
    private IAlarmAnalyisisService alarmAnalyisisService;

    @GetMapping("/getByNodeId")
    public AjaxResult getByNodeId(@Validated AlarmAnalysisDTO alarmAnalysisDTO){
        AlarmAnalysisVO alarmAnalysisVO  = alarmAnalyisisService.getByNodeId(alarmAnalysisDTO);
        return AjaxResult.success(alarmAnalysisVO);
    }

    @GetMapping("/getCountInfo")
    public AjaxResult getCountInfo(@Validated AlarmAnalysisDTO alarmAnalysisDTO){
        AlarmAnalysisVO alarmAnalysisVO  = alarmAnalyisisService.getCountInfo(alarmAnalysisDTO);
        return AjaxResult.success(alarmAnalysisVO);
    }
}
