package com.dingzhuo.energy.data.model.service;

import com.dingzhuo.energy.data.model.domain.DaqTemplate;

import java.util.List;

/**
 * 采集参数模板Service接口
 *
 * @author ruoyi
 * @date 2020-02-08
 */
public interface IDaqTemplateService {
  /**
   * 查询采集参数模板
   *
   * @param id 采集参数模板ID
   * @return 采集参数模板
   */
  DaqTemplate selectDaqTemplateById(String id);

  /**
   * 查询采集参数模板列表
   *
   * @param daqTemplate 采集参数模板
   * @return 采集参数模板集合
   */
  List<DaqTemplate> selectDaqTemplateList(DaqTemplate daqTemplate);

  /**
   * 新增采集参数模板
   *
   * @param daqTemplate 采集参数模板
   * @return 结果
   */
  int insertDaqTemplate(DaqTemplate daqTemplate);

  /**
   * 修改采集参数模板
   *
   * @param daqTemplate 采集参数模板
   * @return 结果
   */
  int updateDaqTemplate(DaqTemplate daqTemplate);

  /**
   * 批量删除采集参数模板
   *
   * @param ids 需要删除的采集参数模板ID
   * @return 结果
   */
  int deleteDaqTemplateByIds(String[] ids);

  /**
   * 删除采集参数模板信息
   *
   * @param id 采集参数模板ID
   * @return 结果
   */
  int deleteDaqTemplateById(String id);

  /**
   * 查询相同设备类型下是否有重复的编码
   *
   * @param code       参数编码
   * @param deviceType 设备类型
   * @return 是否存在
   */
  boolean dapHasExist(String code, String deviceType);

  boolean dapHasExist(DaqTemplate daqTemplate);
}
