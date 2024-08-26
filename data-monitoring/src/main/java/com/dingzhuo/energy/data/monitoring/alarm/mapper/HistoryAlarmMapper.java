package com.dingzhuo.energy.data.monitoring.alarm.mapper;


import com.dingzhuo.energy.data.monitoring.alarm.domain.HistoryAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkHistoryAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkRealTimeAlarmList;

import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * 实时报警获取Mapper接口
 *
 * @author sys
 * @date 2020-03-02
 */
public interface HistoryAlarmMapper {

  /**
   * 获取历史报警集合
   *
   * @param jkHistoryAlarm 报警参数
   * @return 预报警设置集合
   */
  List<JkHistoryAlarm> selectJkHistoryAlarmList(JkHistoryAlarm jkHistoryAlarm);

  List<JkHistoryAlarm> selectJkHistoryAlarmListExcel(JkHistoryAlarm jkHistoryAlarm);

  /**
   * 实时检测 功能 的多 sheet页  展示 组态图  测点 报警信息
   *
   * @param jkHistoryAlarm
   * @return
   */
  List<JkHistoryAlarm> selectHistoryAlarmNoteList(JkHistoryAlarm jkHistoryAlarm);

  void updateHistoryAlarm(@Param("alarmCode") String alarmCode,
      @Param("historyAlarm") HistoryAlarm historyAlarm);
}
