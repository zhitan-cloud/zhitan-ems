package com.dingzhuo.energy.data.monitoring.alarm.mapper;


import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预报警设置Mapper接口
 *
 * @author sys
 * @date 2020-03-02
 */
public interface AlarmItemMapper {

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
   * 删除预报警设置
   *
   * @param id 预报警设置ID
   * @return 结果
   */
  int deleteAlarmItemById(String id);

  /**
   * 批量删除预报警设置
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  int deleteAlarmItemByIds(String[] ids);

  /**
   * 查询预报警设置
   *
   * @param id 预报警设置ID
   * @return 预报警设置
   */
  List<AlarmItem> selectAlarmItemtingById(String id);

  /**
   * 修改预报警设置
   *
   * @param ids       预报警设置
   * @param update_by 预报警设置
   * @param flag      预报警设置
   * @return 结果
   */
  int updateStartStop(String[] ids, String flag, String update_by);

  /**
   * @return 结果
   */
  String getStartStop(String indexid);

  /**
   * 修改【请填写功能名称】
   *
   * @param data 【请填写功能名称】
   * @param username 当前登录人用户名
   * @return 结果
   */
  int updateLimitVal(@Param("data") List data, @Param("id") String id, String username);

  /**
   * 修改【查询是否存在测点设置】
   *
   * @return 结果
   */
  int selectCountById(String id);

  /**
   * 修改【请填写功能名称】
   *
   * @param data 【请填写功能名称】
   * @return 结果
   */
  int updateLimitValNoDel(@Param("data") List data, @Param("id") String id, String username);

  /**
   * 修改【查询是否存在测点设置】
   *
   * @return 结果
   */
  int deleteAllLimitVal(String id);

  /**
   * 查询预报警设置
   *
   * @param id 预报警设置ID
   * @return 预报警设置
   */
  int getSettingCount(String id);

  List<AlarmItem> getAllAlarmItem();
}
