package com.dingzhuo.energy.data.monitoring.alarm.service;

import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 预报警设置Service接口
 *
 * @author sys
 * @date 2020-03-02
 */
public interface IAlarmItemService {

  /**
   * 查询预报警设置
   *
   * @param id 预报警设置ID
   * @return 预报警设置
   */
  AlarmItem selectAlarmItemById(String id);

  /**
   * 查询预报警设置列表
   *
   * @param alarmItem 预报警设置
   * @return 预报警设置集合
   */
  List<AlarmItem> selectAlarmItemList(AlarmItem alarmItem);

  /**
   * 新增预报警设置
   *
   * @param alarmItem 预报警设置
   * @return 结果
   */
  int insertAlarmItem(AlarmItem alarmItem);

  /**
   * 修改预报警设置
   *
   * @param alarmItem 预报警设置
   * @return 结果
   */
  int updateAlarmItem(AlarmItem alarmItem);

  /**
   * 修改预报警设置
   *
   * @param ids,flag 预报警设置
   * @return 结果
   */
  int updateStartStop(String[] ids, String flag, String username);

  /**
   * 批量删除预报警设置
   *
   * @param ids 需要删除的预报警设置ID
   * @return 结果
   */
  int deleteAlarmItemByIds(String[] ids);

  /**
   * 删除预报警设置信息
   *
   * @param id 预报警设置ID
   * @return 结果
   */
  int deleteAlarmItemById(String id);

  /**
   * 查询预报警设置列表
   *
   * @param id 预报警设置
   * @return 预报警设置集合
   */
  List<AlarmItem> selectAlarmItemtingById(String id);

  /**
   * 查询预报警列表行启停值
   *
   * @param indexid 预报警设置
   * @return 预报警设置集合
   */
  String getStartStop(String indexid);

  /**
   * 修改弹出框限值
   *
   * @param data 【修改弹出框限值】
   * @return 结果
   */
  int updateLimitVal(List data, String username);

  /**
   * 查询预id下设置限值的数量
   *
   * @param id 预报警设置
   * @return 数
   */
  ArrayList getSettingCount(String[] id);

  List<AlarmItem> getAllAlarmItem();
}
