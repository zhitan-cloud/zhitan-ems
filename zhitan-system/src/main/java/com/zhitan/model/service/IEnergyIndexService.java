package com.zhitan.model.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.EnergyIndexQuery;

import java.util.List;

/**
 * 指标信息Service接口
 *
 * @author fanxinfu
 * @date 2020-02-14
 */
public interface IEnergyIndexService {

    EnergyIndex getiEnergyIndexByCode(String code);

    /**
     * 根据计量器具id集合查询点位信息
     *
     * @param meterId 计量器具id集合
     * @return
     */
    List<EnergyIndex> listIndexByMeterIds(String nodeId, List<String> meterId);

    /**
     * 查询指标信息
     *
     * @param indexId 指标信息ID
     * @return 指标信息
     */
    EnergyIndex selectEnergyIndexById(String indexId);

    /**
     * 查询指标信息
     *
     * @param indexId 指标信息ID
     * @return 指标信息
     */
    List<EnergyIndex> selectEnergyIndexByIds(List<String> indexId);

    /**
     * 查询指标信息列表
     *
     * @param energyIndex 指标信息
     * @return 指标信息集合
     */
    List<EnergyIndex> selectEnergyIndexList(EnergyIndex energyIndex);

    /**
     * 查询指标信息列表
     *
     * @param query 指标信息
     * @return 指标信息集合
     */
    List<EnergyIndex> selectEnergyIndexList(EnergyIndexQuery query);

    /**
     * 新增指标信息
     *
     * @param nodeId
     * @param energyIndex 指标信息
     * @return 结果
     */
    int insertEnergyIndex(String nodeId, EnergyIndex energyIndex);

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
     * @param nodeId
     * @param indexIds 需要删除的指标信息ID
     * @return 结果
     */
    int deleteEnergyIndexByIds(String nodeId, String[] indexIds);

    boolean energyIndexHasExist(String code);

    boolean energyIndexHasExist(String indexId, String code);

    AjaxResult addMeterIndex(String meterId);

    List<EnergyIndex> getMeterIndex(String meterId);

    boolean modelHasConfig(String modelCode);

    List<EnergyIndex> selectCollectIndex(String deviceId);

    List<EnergyIndex> getEnergyIndexByIds(List<String> indexIds);

    List<EnergyIndex> getEnergyIndexByCodes(List<String> indexCodes);

    List<EnergyIndex> getIndexByNodeAndChildrenNode(String nodeId);

    List<EnergyIndex> searchIndexByNodeAndChildrenNode(String nodeId, String filter);

    void removeNodeIndex(String nodeId, List<String> removeLink);

    AjaxResult importEnergyIndex(List<EnergyIndex> energyIndexList, boolean updateSupport);

    List<EnergyIndex> getEnergyIndexMeterByCodes(List<String> indexCodes);

    Page<EnergyIndex> selectEnergyIndexPage(EnergyIndexQuery query, Long pageNum, Long pageSize);

    Page<EnergyIndex> getMeterIndexList(List<String> meterIndexIds, String code,
                                        String name, Long pageNum, Long pageSize);

    List<EnergyIndex> getIndexByCode(String code, String nodeId);

    /**
     * 根据用能单元id和设备id，以及点位编码获取点位
     *
     * @param nodeId    节点id
     * @param meterId   设备id
     * @param indexCode 点位编码或者点位编码的一部分
     * @return
     */
    EnergyIndex getDeviceIndexByCode(String nodeId, String meterId, String indexCode);

    /**
     * 根据用能单元id和设备id，以及点位编码获取点位
     *
     * @param nodeId  节点id
     * @param meterId 设备id
     * @return
     */
    List<EnergyIndex> listDeviceIndex(String nodeId, String meterId);
}
