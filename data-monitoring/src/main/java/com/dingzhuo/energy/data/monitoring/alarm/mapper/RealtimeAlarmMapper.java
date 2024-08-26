package com.dingzhuo.energy.data.monitoring.alarm.mapper;


import com.dingzhuo.energy.data.monitoring.alarm.domain.RealTimeAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkRealTimeAlarmList;

import java.util.List;

/**
 * 实时报警监控Mapper接口
 *
 * @author sys
 * @date 2020-03-18
 */
public interface RealtimeAlarmMapper {

  /**
   * 查询实时报警监控
   *
   * @param id 实时报警监控ID
   * @return 实时报警监控
   */
  public RealTimeAlarm selectRealtimeAlarmById(String id);

  /**
   * 查询实时报警监控列表
   *
   * @param realtimeAlarm 实时报警监控
   * @return 实时报警监控集合
   */
  public List<RealTimeAlarm> selectRealtimeAlarmList(RealTimeAlarm realtimeAlarm);

  /**
   * 新增实时报警监控
   *
   * @param realtimeAlarm 实时报警监控
   * @return 结果
   */
  public int insertRealtimeAlarm(RealTimeAlarm realtimeAlarm);

  /**
   * 修改实时报警监控
   *
   * @param realtimeAlarm 实时报警监控
   * @return 结果
   */
  public int updateRealtimeAlarm(RealTimeAlarm realtimeAlarm);

  /**
   * 删除实时报警监控
   *
   * @param id 实时报警监控ID
   * @return 结果
   */
  public int deleteRealtimeAlarmById(String id);

  /**
   * 批量删除实时报警监控
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  public int deleteRealtimeAlarmByIds(String[] ids);

  /**
   * 实时报警监控用列表
   *
   * @param jkRealTimeAlarmList 实时报警监控
   * @return 实时报警监控集合
   */
  public List<JkRealTimeAlarmList> selectRealtimeAlarmJkList(
      JkRealTimeAlarmList jkRealTimeAlarmList);

  RealTimeAlarm getRealTimeAlarmByAlarmCode(String alarmCode);

  List<RealTimeAlarm> getRealTimeAlarm();

  RealTimeAlarm getAlarmByItemIdAndTimeCode(String itemId, String timeCode);
}
