package com.dingzhuo.energy.data.model.mapper;

import com.dingzhuo.energy.data.model.domain.DaqTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 采集参数模板Mapper接口
 *
 * @author ruoyi
 * @date 2020-02-08
 */
public interface DaqTemplateMapper {
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
   * 删除采集参数模板
   *
   * @param id 采集参数模板ID
   * @return 结果
   */
  int deleteDaqTemplateById(String id);

  /**
   * 批量删除采集参数模板
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  int deleteDaqTemplateByIds(String[] ids);

  int dapHasExist(@Param("code") String code, @Param("deviceType") String deviceType);

  int dapHasExistWhenUpdate(@Param("id") String id, @Param("code") String code, @Param("deviceType") String deviceType);
}
