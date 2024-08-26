package com.dingzhuo.energy.data.model.service.impl;

import java.util.List;

import com.dingzhuo.energy.data.model.domain.DaqTemplate;
import com.dingzhuo.energy.data.model.mapper.DaqTemplateMapper;
import com.dingzhuo.energy.data.model.service.IDaqTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 采集参数模板Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-08
 */
@Service
public class DaqTemplateServiceImpl implements IDaqTemplateService {
  @Autowired
  private DaqTemplateMapper daqTemplateMapper;

  /**
   * 查询采集参数模板
   *
   * @param id 采集参数模板ID
   * @return 采集参数模板
   */
  @Override
  public DaqTemplate selectDaqTemplateById(String id) {
    return daqTemplateMapper.selectDaqTemplateById(id);
  }

  /**
   * 查询采集参数模板列表
   *
   * @param daqTemplate 采集参数模板
   * @return 采集参数模板
   */
  @Override
  public List<DaqTemplate> selectDaqTemplateList(DaqTemplate daqTemplate) {
    return daqTemplateMapper.selectDaqTemplateList(daqTemplate);
  }

  /**
   * 新增采集参数模板
   *
   * @param daqTemplate 采集参数模板
   * @return 结果
   */
  @Override
  public int insertDaqTemplate(DaqTemplate daqTemplate) {
    return daqTemplateMapper.insertDaqTemplate(daqTemplate);
  }

  /**
   * 修改采集参数模板
   *
   * @param daqTemplate 采集参数模板
   * @return 结果
   */
  @Override
  public int updateDaqTemplate(DaqTemplate daqTemplate) {
    return daqTemplateMapper.updateDaqTemplate(daqTemplate);
  }

  /**
   * 批量删除采集参数模板
   *
   * @param ids 需要删除的采集参数模板ID
   * @return 结果
   */
  @Override
  public int deleteDaqTemplateByIds(String[] ids) {
    return daqTemplateMapper.deleteDaqTemplateByIds(ids);
  }

  /**
   * 删除采集参数模板信息
   *
   * @param id 采集参数模板ID
   * @return 结果
   */
  @Override
  public int deleteDaqTemplateById(String id) {
    return daqTemplateMapper.deleteDaqTemplateById(id);
  }

  @Override
  public boolean dapHasExist(String code, String deviceType) {
    int count = daqTemplateMapper.dapHasExist(code, deviceType);
    return count > 0;
  }

  @Override
  public boolean dapHasExist(DaqTemplate daqTemplate) {
    int count = daqTemplateMapper.dapHasExistWhenUpdate(daqTemplate.getId(), daqTemplate.getCode(), daqTemplate.getDeviceType());
    return count > 0;
  }
}
