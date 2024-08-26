package com.dingzhuo.energy.data.monitoring.device.service;

import com.dingzhuo.energy.data.monitoring.device.domain.DeviceFormula;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 设备计算公式Service接口
 *
 * @author zhaow
 * @date 2020-03-20
 */
public interface IDeviceFormulaService {

  /**
   * 查询设备计算公式
   *
   * @param id 设备计算公式ID
   * @return 设备计算公式
   */
  DeviceFormula selectDeviceFormulaById(String id);

  /**
   * 查询设备计算公式列表
   *
   * @param deviceFormula 设备计算公式
   * @return 设备计算公式集合
   */
  List<DeviceFormula> selectDeviceFormulaList(DeviceFormula deviceFormula);

  /**
   * 新增设备计算公式
   *
   * @param deviceFormula 设备计算公式
   * @return 结果
   */
  int insertDeviceFormula(DeviceFormula deviceFormula);

  /**
   * 修改设备计算公式
   *
   * @param deviceFormula 设备计算公式
   * @return 结果
   */
  int updateDeviceFormula(DeviceFormula deviceFormula);

  /**
   * 批量删除设备计算公式
   *
   * @param ids 需要删除的设备计算公式ID
   * @return 结果
   */
  int deleteDeviceFormulaByIds(String[] ids);

  /**
   * 删除设备计算公式信息
   *
   * @param id 设备计算公式ID
   * @return 结果
   */
  int deleteDeviceFormulaById(String id);

  /**
   * 取根据 设备模型 节点ID  和  设备状态主键 ID  取  设备公式信息
   *
   * @param nodeId
   * @param stateId
   * @return
   */
  DeviceFormula getDeviceFormula(String nodeId, String stateId);

  /**
   * 设备状态 配置 公式保存
   *
   * @param deviceFormula
   * @return
   */
  void saveDeviceFormula(DeviceFormula deviceFormula);

  /**
   * 设备状态 公式 启用设置
   */
  int editDeviceFormulaIsEnable(String nodeId, String isEnable, String[] ids);

  List<DeviceFormula> getAllDeviceFormula();
}
