package com.zhitan.alarm.services;

import com.zhitan.alarm.domain.dto.AlarmAnalysisDTO;
import com.zhitan.alarm.domain.vo.AlarmAnalysisVO;

/**
 * 报警分析接口
 *
 * @author zhitan
 * @date
 */
public interface IAlarmAnalysisService {

    AlarmAnalysisVO getByNodeId(AlarmAnalysisDTO alarmAnalysisDTO);

    /**
     * 报警分析统计
     *
     * @param alarmAnalysisDTO
     * @return
     */
    AlarmAnalysisVO getCountInfo(AlarmAnalysisDTO alarmAnalysisDTO);
}
