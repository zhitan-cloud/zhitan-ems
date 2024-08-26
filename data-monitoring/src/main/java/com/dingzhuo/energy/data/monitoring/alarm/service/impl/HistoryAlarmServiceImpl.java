package com.dingzhuo.energy.data.monitoring.alarm.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.monitoring.alarm.domain.HistoryAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkHistoryAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkRealTimeAlarmList;
import com.dingzhuo.energy.data.monitoring.alarm.mapper.HistoryAlarmMapper;
import com.dingzhuo.energy.data.monitoring.alarm.service.IHistoryAlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HistoryAlarmServiceImpl implements IHistoryAlarmService {

  @Autowired
  private HistoryAlarmMapper historyAlarmMapper;

  @Override
  public List<HistoryAlarm> getHistoryAlarm(Date beginTime, Date endTime) {
    return null;
  }

  @Override
  public List<HistoryAlarm> getHistoryAlarm(Date beginTime, Date endTime, TimeType timeType) {
    return null;
  }

  @Override
  public List<HistoryAlarm> getHistoryAlarm(Date beginTime, Date endTime, String alarmType) {
    return null;
  }

  @Override
  public List<JkHistoryAlarm> selectJkHistoryAlarmList(JkHistoryAlarm jkHistoryAlarm) {
    return historyAlarmMapper.selectJkHistoryAlarmList(jkHistoryAlarm);
  }

  @Override
  public List<JkHistoryAlarm> selectJkHistoryAlarmListExcel(JkHistoryAlarm jkHistoryAlarm) {
    return historyAlarmMapper.selectJkHistoryAlarmListExcel(jkHistoryAlarm);
  }

  /**
   * 实时检测 功能 的多 sheet页  展示 组态图  测点 报警信息
   *
   * @param jkHistoryAlarm
   * @return
   */
  @Override
  public List<JkHistoryAlarm> selectHistoryAlarmNoteList(JkHistoryAlarm jkHistoryAlarm) {
    return historyAlarmMapper.selectHistoryAlarmNoteList(jkHistoryAlarm);
  }

  @Override
  public void updateHistoryAlarm(String alarmCode, HistoryAlarm historyAlarm) {
    historyAlarmMapper.updateHistoryAlarm(alarmCode, historyAlarm);
  }
}
