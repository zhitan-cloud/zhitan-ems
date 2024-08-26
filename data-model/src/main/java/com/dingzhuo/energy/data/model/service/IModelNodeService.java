package com.dingzhuo.energy.data.model.service;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnergy;
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplement;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.domain.TreeObject;
import com.dingzhuo.energy.data.model.domain.vo.ModelNodeIndexInfor;

import java.util.List;

/**
 * 模型节点Service接口
 *
 * @author fanxinfu
 * @date 2020-02-10
 */
public interface IModelNodeService {
  /**
   * 查询模型节点
   *
   * @param nodeId 模型节点ID
   * @return 模型节点
   */
  ModelNode selectModelNodeById(String nodeId);

  /**
   * 查询模型节点列表
   *
   * @param modelNode 模型节点
   * @return 模型节点集合
   */
  List<ModelNode> selectModelNodeList(ModelNode modelNode);

  /**
   * 新增模型节点
   *
   * @param modelNode 模型节点
   * @return 结果
   */
  int insertModelNode(ModelNode modelNode);

  /**
   * 修改模型节点
   *
   * @param modelNode 模型节点
   * @return 结果
   */
  int updateModelNode(ModelNode modelNode);

  /**
   * 批量删除模型节点
   *
   * @param nodeIds 需要删除的模型节点ID
   * @return 结果
   */
  int deleteModelNodeByIds(String[] nodeIds);

  /**
   * 删除模型节点信息
   *
   * @param nodeId 模型节点ID
   * @return 结果
   */
  int deleteModelNodeById(String nodeId);

  List<ModelNode> getModelNodeByModelCode(String modelCode);

  List<TreeObject> buildModelNodeTree(List<ModelNode> list);

  void updateOrder(String nodeId, String parentId, String[] children);

  boolean modelNodeHasExist(String code, String modelCode);

  boolean modelNodeHasExist(ModelNode modelNode);

  boolean hasEnergyIndex(String nodeId);

  void setDevice(String nodeId, String[] deviceIds);

  List<MeterImplement> getSettingDevice(String nodeId);

  List<EnergyIndex> getSettingIndex(String nodeId);

  List<EnergyIndex> getSettingIndexByWhere(String nodeId,String indexName);

  void delDevice(String nodeId, String[] deviceIds);

  List<SysEnergy> getSettingEnergy(String nodeId);

  void setEnergy(String nodeId, Integer[] energyIds);

  void delEnergy(String nodeId, Integer[] energyIds);

  List<SysProduct> getSettingProduct(String nodeId);

  void setProduct(String nodeId, Integer[] productIds);

  void delProduct(String nodeId, Integer[] productIds);

  void setIndex(String nodeId, String indexType, String[] indexIds);

  void delIndex(String nodeId, String[] indexIds);

  List<EnergyIndex> getSettingIndex(String indexType, String nodeId);

  List<ModelNode> getModelNodeByNodeCodes(List<String> nodeCodes);

  List<ModelNode> getModelNodeByModelCodeWithAuth(String modelCode, Long userId);

  /**
   * 根据nodeCode获取对应点位信息
   *
   * @param nodeCode
   * @return
   */
  List<String> listIndexIdsByModelCode(String nodeCode);

  /**
   * 根据nodeCode查询模型节点与子点位id之间的关系信息
   *
   * @param code
   * @return
   */
  List<ModelNodeIndexInfor> getModelNodeIndexIdRelationInforByCode(String code);

  /**
   * 根据nodeId查询对应详细信息
   *
   * @param nodeId
   * @return
   */
  List<ModelNodeIndexInfor> getModelNodeIndexIdRelationInforByNodeId(String nodeId);

  /**
   * 根据父id查询详细信息
   *
   * @param parentId
   * @return
   */
  List<ModelNodeIndexInfor> listModelNodeIndexIdRelationInforByParentId(String parentId);

  /**
   * 根据code查询父级信息
   *
   * @param indexCode
   * @return
   */
  ModelNode getModelNodeByModelCodeByIndexCode(String indexCode);
}
