package com.dingzhuo.energy.data.model.mapper;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysEnergy;
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplement;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.domain.vo.ModelNodeIndexInfor;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 模型节点Mapper接口
 *
 * @author fanxinfu
 * @date 2020-02-10
 */
public interface ModelNodeMapper {

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
   * 删除模型节点
   *
   * @param nodeId 模型节点ID
   * @return 结果
   */
  int deleteModelNodeById(String nodeId);

  /**
   * 批量删除模型节点
   *
   * @param nodeIds 需要删除的数据ID
   * @return 结果
   */
  int deleteModelNodeByIds(String[] nodeIds);

  List<ModelNode> getModelNodeByModelCode(String modelCode);

  int getMaxOrder(String parentId);

  void updateModelNodeParent(@Param("nodeId") String nodeId, @Param("parentId") String parentId);

  void updateModelNodeOrder(@Param("orders") Map<String, Integer> orders);

  int modelNodeHasExist(@Param("code") String code, @Param("modelCode") String modelCode);

  int modelNodeHasExistWhenUpdate(@Param("nodeId") String nodeId, @Param("code") String code,
      @Param("modelCode") String modelCode);

  void setDevice(@Param("nodeId") String nodeId, @Param("deviceIds") String[] deviceIds);

  List<MeterImplement> getSettingDevice(String nodeId);

  List<EnergyIndex> getSettingIndex(String nodeId);


  void delDevice(@Param("nodeId") String nodeId, @Param("deviceIds") String[] deviceIds);

  List<SysEnergy> getSettingEnergy(String nodeId);

  void setEnergy(@Param("nodeId") String nodeId, @Param("energyIds") Integer[] energyIds);

  void delEnergy(@Param("nodeId") String nodeId, @Param("energyIds") Integer[] energyIds);

  List<SysProduct> getSettingProduct(String nodeId);

  void setProduct(@Param("nodeId") String nodeId, @Param("productIds") Integer[] productIds);

  void delProduct(@Param("nodeId") String nodeId, @Param("productIds") Integer[] productIds);

  void setIndex(@Param("nodeId") String nodeId, @Param("indexType") String indexType,
      @Param("indexIds") String[] indexIds);

  void delIndex(@Param("nodeId") String nodeId, @Param("indexIds") String[] indexIds);

  List<EnergyIndex> getSettingIndexByType(@Param("indexType") String indexType,
                                          @Param("nodeId") String nodeId);

  List<ModelNode> getModelNodeByNodeCodes(List<String> nodeCodes);

  List<ModelNode> getModelNodeByModelCodeWithAuth(@Param("modelCode") String modelCode,
                                                  @Param("userId") String userId);

  List<EnergyIndex> getSettingIndexByWhere(@Param("nodeId") String nodeId, @Param("indexName") String indexName);

  /**
   * 根据nodeIds获取idexId信息
   *
   * @param nodeIds
   * @return
   */
  List<String> listIndesxByCodeList(@Param("nodeIds") List<String> nodeIds);

  /**
   * 根据nodeCode查询模型节点与点位id之间的关系信息
   *
   * @param code
   * @return
   */
  List<ModelNodeIndexInfor> getModelNodeIndexIdRelationInforByCode(@Param("code") String code);

  /**
   * 根据nodeId查询对应详细信息
   *
   * @param nodeId
   * @return
   */
  List<ModelNodeIndexInfor> getModelNodeIndexIdRelationInforByNodeId(@Param("nodeId") String nodeId);

  /**
   * 根据父id查询详细信息
   *
   * @param parentId
   * @return
   */
  List<ModelNodeIndexInfor> listModelNodeIndexIdRelationInforByParentId(@Param("parentId") String parentId);

  /**
   * 根据code查询父级信息
   *
   * @param indexCode
   * @return
   */
  ModelNode getModelNodeByModelCodeByIndexCode(@Param("indexCode") String indexCode);
}
