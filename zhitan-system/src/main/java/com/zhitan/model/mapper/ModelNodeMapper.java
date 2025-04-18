package com.zhitan.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.domain.SysProduct;
import com.zhitan.common.enums.TimeType;
import com.zhitan.dataitem.domain.vo.NodeIndexValueVO;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 模型节点Mapper接口
 *
 * @author fanxinfu
 * @date 2020-02-10
 */
public interface ModelNodeMapper  extends BaseMapper<ModelNode> {

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

    Page<EnergyIndex> getSettingIndexByType(@Param("indexType") String indexType,
                                            @Param("nodeId") String nodeId,
                                            @Param("code") String code,
                                            @Param("name") String name,
                                            @Param("page") Page<?> page);

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
    List<ModelNodeIndexInfo> getModelNodeIndexIdRelationInforByCode(@Param("code") String code);

    /**
     * 根据nodeId查询对应详细信息
     *
     * @param nodeId
     * @return
     */
    List<ModelNodeIndexInfo> getModelNodeIndexIdRelationInforByNodeId(@Param("nodeId") String nodeId);

    /**
     * 根据父id查询详细信息
     *
     * @param parentId
     * @return
     */
    List<ModelNodeIndexInfo> listModelNodeIndexIdRelationInforByParentId(@Param("parentId") String parentId);

    /**
     * 根据code查询父级信息
     *
     * @param indexCode
     * @return
     */
    ModelNode getModelNodeByModelCodeByIndexCode(@Param("indexCode") String indexCode);

    /**
     * 根据模型编号和节点id查询点位id
     *
     * @param modelCode 模型编号
     * @param nodeId    节点id
     * @return 结果
     */
    List<ModelNodeIndexInfo> selectIndexByModelCodeAndNodeId(@Param("modelCode") String modelCode, @Param("nodeId") String nodeId);

    void delIndexNodeIdAndIndexType(@Param("nodeId")String nodeId, @Param("indexType")String indexType);

    void setIndexAndNodeId(@Param("nodeId")String nodeId, @Param("indexIds")String[] indexIds);

    List<MeterImplement> getSettingDeviceIndex(@Param("nodeId")String nodeId,@Param("energyType")String energyType);

    /**
     * @description: 根据节点id和能源类型查询点位信息
     * @param nodeId
     * @author: hmj
     * @date: 2024/10/16 19:16
     */
    List<ModelNodeIndexInfo> getModelNodeIndexIdByNodeId(@Param("nodeId")String nodeId, @Param("energyType")String energyType);

    /**
     * @description: 根据nodeId查询子节点的所有统计指标
     * @param parentId
     * @return java.util.List<com.zhitan.model.domain.vo.ModelNodeIndexInfor>
     * @author: hmj
     * @date: 2024/10/18 16:12
     */
    List<ModelNodeIndexInfo> getModelNodeByParentId(String parentId);

    ModelNode getFirstModeNodeInfo(String modelCode);

    List<ModelNodeIndexInfo> selectIndexByNodeIds(@Param("modelCode") String modelCode, @Param("nodeIds") List<String> nodeIds);

    /**
     * 根据父节点id和能源类型查询点位下的累积量
     */
    List<NodeIndexValueVO> getDataItemByParentNodeId(@Param("parentId") String parentId,
                                                     @Param("energyType") String energyType,
                                                     @Param("timeType") TimeType timeType,
                                                     @Param("dateTimeMap") Map<String, LocalDateTime> dateTimeMap);

    /**
     * 根据节点id和能源类型查询点位下的累积量
     */
    List<NodeIndexValueVO> getDataItemByNodeId(@Param("nodeId") String nodeId,
                                                     @Param("energyType") String energyType,
                                                     @Param("timeType") TimeType timeType,
                                                     @Param("dateTimeMap") Map<String, LocalDateTime> dateTimeMap);

    /**
     * 通过节点地址查询节点下及子节点下的所有点位信息
     */
    List<ModelNodeIndexInfo> getAllModelNodeIndexByAddress(@Param("modelCode") String modelCode, @Param("address") String address);
}
