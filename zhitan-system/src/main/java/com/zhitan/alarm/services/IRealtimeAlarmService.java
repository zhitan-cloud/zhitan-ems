package com.zhitan.alarm.services;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.JkRealTimeAlarmList;
import com.zhitan.alarm.domain.RealTimeAlarm;
import com.zhitan.common.enums.TimeType;

import java.util.List;

public interface IRealtimeAlarmService {
  /**
   * 获取当前所有实时报警记录
   *
   * @return 实时报警
   */
  List<RealTimeAlarm> getRealTimeAlarm();

  /**
   * @param timeType 报警事件类型
   * @return
   */
  List<RealTimeAlarm> getRealTimeAlarm(TimeType timeType);

  /**
   * @param alarmLevel 报警级别上限、上上限、下限下下限
   * @return
   */
  List<RealTimeAlarm> getRealTimeAlarm(String alarmLevel);

  /**
   * @param jkRealTimeAlarmList 实时报警监控用列表
   * @return
   */
  List<JkRealTimeAlarmList> selectRealtimeAlarmJkList(JkRealTimeAlarmList jkRealTimeAlarmList);


  void insert(RealTimeAlarm realTimeAlarm);

  RealTimeAlarm getRealTimeAlarmByAlarmCode(String alarmCode);

  RealTimeAlarm getAlarmByItemIdAndTimeCode(String itemId, String timeCode);

  Page<JkRealTimeAlarmList> selectRealtimeAlarmJkPage(JkRealTimeAlarmList jkRealTimeAlarmList, Long pageNum, Long pageSize);
}
