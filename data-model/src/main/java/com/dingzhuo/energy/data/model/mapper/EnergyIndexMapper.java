package com.dingzhuo.energy.data.model.mapper;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.EnergyIndexQuery;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

/**
 * 指标信息Mapper接口
 *
 * @author fanxinfu
 * @date 2020-02-14
 */
public interface EnergyIndexMapper {
  /**
   * 查询指标信息
   *
   * @param indexId 指标信息ID
   * @return 指标信息
   */
  EnergyIndex selectEnergyIndexById(String indexId);

  List<EnergyIndex> selectEnergyIndexByIds(@Param("indexIds") List<String> indexIds);

  /**
   * 查询指标信息列表
   *
   * @param energyIndex 指标信息
   * @return 指标信息集合
   */
  List<EnergyIndex> selectEnergyIndexList(EnergyIndex energyIndex);

  /**
   * 新增指标信息
   *
   * @param nodeId
   * @param energyIndex 指标信息
   * @return 结果
   */
  int insertEnergyIndex(@Param("nodeId") String nodeId, @Param("energyIndex") EnergyIndex energyIndex);

  /**
   * 修改指标信息
   *
   * @param energyIndex 指标信息
   * @return 结果
   */
  int updateEnergyIndex(EnergyIndex energyIndex);

  /**
   * 批量删除指标信息
   *
   *
   * @param nodeId
   * @param indexIds 需要删除的数据ID
   * @return 结果
   */
  int deleteEnergyIndexByIds(String nodeId, String[] indexIds);

  int energyIndexHasExist(String code);

  int energyIndexHasExistWhenUpdate(@Param("indexId") String indexId, @Param("code") String code);

  void insertEnergyIndices(@Param("energyIndices") List<EnergyIndex> energyIndices);

  List<EnergyIndex> getMeterIndex(String meterId);

  void deleteIndexByMeterId(String meterId);

  int modelHasConfig(String modelCode);

  List<EnergyIndex> selectCollectIndex(String deviceId);

  List<EnergyIndex> getEnergyIndexByIds(List<String> indexIds);

  List<EnergyIndex> getEnergyIndexByCodes(List<String> indexCodes);

  List<EnergyIndex> getIndexByNodeAndChildrenNode(String nodeId);

  List<EnergyIndex> searchIndexByNodeAndChildrenNode(String nodeId, String filter);

  List<EnergyIndex> selectEnergyIndex(EnergyIndexQuery query);

  void removeNodeIndex(String nodeId, List<String> indexIds);

  void saveEnergyIndex(List<EnergyIndex> insertData);

  List<EnergyIndex> getEnergyIndexMeterByCodes(List<String> indexCodes);

}
