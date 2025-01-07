package com.zhitan.alarm.services;

import com.zhitan.alarm.domain.dto.AlarmAnalysisDTO;
import com.zhitan.alarm.domain.vo.AlarmAnalysisVO;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-26 17:33
 */
public interface IAlarmAnalyisisService {
    AlarmAnalysisVO getByNodeId(AlarmAnalysisDTO alarmAnalysisDTO);

    AlarmAnalysisVO getCountInfo(AlarmAnalysisDTO alarmAnalysisDTO);
}
