package com.zhitan.model.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.services.IMeterImplementService;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.enums.IndexType;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.model.domain.DaqTemplate;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.EnergyIndexQuery;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.EnergyIndexMapper;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.model.service.IDaqTemplateService;
import com.zhitan.model.service.IEnergyIndexService;
import com.zhitan.model.service.IModelNodeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 指标信息Service业务层处理
 *
 * @author fanxinfu
 * @date 2020-02-14
 */
@Service
public class EnergyIndexServiceImpl implements IEnergyIndexService {

    @Autowired
    private EnergyIndexMapper energyIndexMapper;
    @Autowired
    private IDaqTemplateService daqTemplateService;
    @Autowired
    private IMeterImplementService meterImplementService;
    @Autowired
    private IModelNodeService modelNodeService;

    @Autowired
    private ModelNodeMapper modelNodeMapper;

    @Override
    public EnergyIndex getiEnergyIndexByCode(String code) {
        return energyIndexMapper.getiEnergyIndexByCode(code);
    }

    @Override
    public List<EnergyIndex> listIndexByMeterIds(String nodeId, List<String> meterId) {
        return energyIndexMapper.listIndexByMeterIds(nodeId, meterId);
    }

    /**
     * 查询指标信息
     *
     * @param indexId 指标信息ID
     * @return 指标信息
     */
    @Override
    public EnergyIndex selectEnergyIndexById(String indexId) {
        return energyIndexMapper.selectEnergyIndexById(indexId);
    }

    @Override
    public List<EnergyIndex> selectEnergyIndexByIds(List<String> indexId) {
        if (CollectionUtils.isEmpty(indexId)) {
            return Collections.emptyList();
        }
        return energyIndexMapper.selectEnergyIndexByIds(indexId);
    }

    @Override
    public List<EnergyIndex> selectEnergyIndexList(EnergyIndex energyIndex) {
        return energyIndexMapper.selectEnergyIndexList(energyIndex);
    }

    /**
     * 查询指标信息列表
     *
     * @return 指标信息
     */
    @Override
    public List<EnergyIndex> selectEnergyIndexList(EnergyIndexQuery query) {
        return energyIndexMapper.selectEnergyIndex(query);
    }

    /**
     * 新增指标信息
     *
     * @param nodeId
     * @param energyIndex 指标信息
     * @return 结果
     */
    @Override
    public int insertEnergyIndex(String nodeId, EnergyIndex energyIndex) {
        energyIndex.setNodeId(nodeId);
        energyIndexMapper.insertEnergyIndex(energyIndex);
        return energyIndexMapper.insertNodeIndex(nodeId, energyIndex.getIndexId());
    }

    /**
     * 修改指标信息
     *
     * @param energyIndex 指标信息
     * @return 结果
     */
    @Override
    public int updateEnergyIndex(EnergyIndex energyIndex) {
        return energyIndexMapper.updateEnergyIndex(energyIndex);
    }

    /**
     * 批量删除指标信息
     *
     * @param nodeId
     * @param indexIds 需要删除的指标信息ID
     * @return 结果
     */
    @Override
    public int deleteEnergyIndexByIds(String nodeId, String[] indexIds) {
        return energyIndexMapper.deleteEnergyIndexByIds(nodeId, indexIds);
    }

    @Override
    public boolean energyIndexHasExist(String code) {
        int count = energyIndexMapper.energyIndexHasExist(code);
        return count > 0;
    }

    @Override
    public boolean energyIndexHasExist(String indexId, String code) {
        int count = energyIndexMapper.energyIndexHasExistWhenUpdate(indexId, code);
        return count > 0;
    }

    @Override
    public AjaxResult addMeterIndex(String meterId) {
        MeterImplement meterImplement = meterImplementService.selectMeterImplementById(meterId);
        if (meterImplement == null) {
            return AjaxResult.error("找不到对应的计量器具！");
        }

        DaqTemplate query = new DaqTemplate();
        query.setDeviceType(meterImplement.getMeterType());
        List<DaqTemplate> daqTemplates = daqTemplateService.selectDaqTemplateList(query);
        if (daqTemplates.isEmpty()) {
            return AjaxResult.error("计量器具所属的类型没有找到对应的模板！");
        }

        List<EnergyIndex> energyIndices = new ArrayList<>();
        daqTemplates.forEach(daqTemplate -> {
            EnergyIndex energyIndex = new EnergyIndex();
            energyIndex.setIndexId(UUID.randomUUID().toString());
            energyIndex.setCode(meterImplement.getCode() + "_" + daqTemplate.getCode());
            energyIndex.setName(daqTemplate.getName());
            energyIndex.setUnitId(daqTemplate.getUnit());
            energyIndex.setIndexType(IndexType.COLLECT);
            energyIndex.setMeterId(meterId);
            energyIndices.add(energyIndex);
        });

        energyIndexMapper.deleteIndexByMeterId(meterId);
        energyIndexMapper.insertEnergyIndices(energyIndices);
        return AjaxResult.success();
    }

    @Override
    public List<EnergyIndex> getMeterIndex(String meterId) {
        return energyIndexMapper.getMeterIndex(meterId);
    }

    @Override
    public boolean modelHasConfig(String modelCode) {
        int count = energyIndexMapper.modelHasConfig(modelCode);
        return count > 0;
    }

    @Override
    public List<EnergyIndex> selectCollectIndex(String deviceId) {
        return energyIndexMapper.selectCollectIndex(deviceId);
    }

    @Override
    public List<EnergyIndex> getEnergyIndexByIds(List<String> indexIds) {
        return energyIndexMapper.getEnergyIndexByIds(indexIds);
    }

    @Override
    public List<EnergyIndex> getEnergyIndexByCodes(List<String> indexCodes) {
        if (indexCodes.isEmpty()) {
            return new ArrayList<>();
        }

        return energyIndexMapper.getEnergyIndexByCodes(indexCodes);
    }

    @Override
    public List<EnergyIndex> getIndexByNodeAndChildrenNode(String nodeId) {
        return energyIndexMapper.getIndexByNodeAndChildrenNode(nodeId);
    }

    @Override
    public List<EnergyIndex> searchIndexByNodeAndChildrenNode(String nodeId, String filter) {
        return energyIndexMapper.searchIndexByNodeAndChildrenNode(nodeId, filter);
    }

    @Override
    public void removeEnergyIndex(List<String> removeLink) {
        energyIndexMapper.removeEnergyIndex(removeLink);
    }

    @Override
    public AjaxResult importEnergyIndex(List<EnergyIndex> energyIndexList, boolean updateSupport) {
        List<String> codes = energyIndexList.stream().map(EnergyIndex::getCode)
                .collect(Collectors.toList());
        List<String> nodeCodes = energyIndexList.stream().map(EnergyIndex::getNodeId)
                .collect(Collectors.toList());
        List<EnergyIndex> indexList = energyIndexMapper.getEnergyIndexByCodes(codes);
        List<ModelNode> modelNodes = modelNodeService.getModelNodeByNodeCodes(nodeCodes);
        Map<String, String> nodeCodeToId = modelNodes.stream()
                .collect(Collectors.toMap(ModelNode::getCode, ModelNode::getNodeId));
        Set<String> cacheIndexCodes = indexList.stream().map(EnergyIndex::getCode)
                .collect(Collectors.toSet());

        List<EnergyIndex> insertData = new ArrayList<>();
        List<EnergyIndex> updateData = new ArrayList<>();
        List<String> errorNodeCode = new ArrayList<>();
        List<String> insertIndexCode = new ArrayList<>();
        AtomicBoolean existDuplicateCode = new AtomicBoolean(false);
        energyIndexList.forEach(energyIndex -> {
            if (cacheIndexCodes.contains(energyIndex.getCode())) {
                updateData.add(energyIndex);
            } else {
                String nodeCode = energyIndex.getNodeId();
                if (nodeCodeToId.containsKey(nodeCode)) {
                    if (!insertIndexCode.contains(energyIndex.getCode())) {
                        insertIndexCode.add(energyIndex.getCode());
                        energyIndex.setIndexId(UUID.randomUUID().toString());
                        energyIndex.setNodeId(nodeCodeToId.get(nodeCode));
                        insertData.add(energyIndex);
                    } else {
                        existDuplicateCode.set(true);
                    }
                } else if (!errorNodeCode.contains(nodeCode)) {
                    errorNodeCode.add(nodeCode);
                }
            }
        });

        if (updateSupport && !updateData.isEmpty()) {
            updateData.forEach(energyIndexMapper::updateEnergyIndex);
        }

        if (!insertData.isEmpty()) {
            energyIndexMapper.saveEnergyIndex(insertData);
        }

        String errMsg = "";
        if (existDuplicateCode.get()) {
            errMsg += "存在重复的指标编码，已自动过滤！";
        }

        if (!errorNodeCode.isEmpty()) {
            errMsg += String.join(",", errorNodeCode) + "没有找到对应的节点数据，因此其下的所有指标导入失败！";
        }

        if (StringUtils.isNotBlank(errMsg)) {
            return AjaxResult.error(errMsg);
        }
        return AjaxResult.success("导入成功！");
    }

    @Override
    public List<EnergyIndex> getEnergyIndexMeterByCodes(List<String> indexCodes) {
        if (indexCodes.isEmpty()) {
            return new ArrayList<>();
        }

        return energyIndexMapper.getEnergyIndexMeterByCodes(indexCodes);
    }

    /**
     * @param query
     * @param pageNum
     * @param pageSize
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.zhitan.model.domain.EnergyIndex>
     * @description: hmj 分页查询
     * @author: hmj
     * @date: 2024/10/11 23:56
     */
    @Override
    public Page<EnergyIndex> selectEnergyIndexPage(EnergyIndexQuery query, Long pageNum, Long pageSize) {
        return energyIndexMapper.selectEnergyIndexPage(new Page<>(pageNum, pageSize), query);
    }

    @Override
    public Page<EnergyIndex> getMeterIndexList(List<String> meterIndexIds, String code,
                                               String name, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<EnergyIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(EnergyIndex::getMeterId, meterIndexIds);
        queryWrapper.like(StringUtils.isNotEmpty(code), EnergyIndex::getCode, code);
        queryWrapper.like(StringUtils.isNotEmpty(name), EnergyIndex::getName, name);
        return energyIndexMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public List<EnergyIndex> getIndexByCode(String code, String nodeId) {
        List<EnergyIndex> energyIndexList = energyIndexMapper.getIndexByCode(code, nodeId);

        return energyIndexList;
    }

    /**
     * 根据用能单元id和设备id，以及点位编码获取点位
     *
     * @param nodeId    节点id
     * @param meterId   设备id
     * @param indexCode 点位编码或者点位编码的一部分
     * @return
     */
    @Override
    public EnergyIndex getDeviceIndexByCode(String nodeId, String meterId, String indexCode) {

        List<EnergyIndex> energyIndexList = listDeviceIndexByCode(nodeId, meterId, indexCode);
        EnergyIndex energyIndex = energyIndexList.stream().findFirst().orElse(null);
        if (ObjectUtils.isEmpty(energyIndex)) {
            energyIndex = new EnergyIndex();
        }
        return energyIndex;
    }

    /**
     * 根据用能单元id和设备id，以及点位编码获取点位
     *
     * @param nodeId    节点id
     * @param meterId   设备id
     * @param indexCode 点位编码或者点位编码的一部分
     * @return
     */
    public List<EnergyIndex> listDeviceIndexByCode(String nodeId, String meterId, String indexCode) {
        List<EnergyIndex> energyIndexList = energyIndexMapper.getIndexByMeterIdIndexCode(meterId,indexCode,nodeId);
        return energyIndexList;
    }

    /**
     * 根据用能单元id和设备id，以及点位编码获取点位
     *
     * @param nodeId  节点id
     * @param meterId 设备id
     * @return
     */
    @Override
    public List<EnergyIndex> listDeviceIndex(String nodeId, String meterId) {
        List<EnergyIndex> energyIndexList = energyIndexMapper.getIndexByMeterIdIndexCode(meterId,null,nodeId);
        return energyIndexList;
    }

    @Override
    public List<ModelNodeIndexInfo> getModelNodeIndexInfoListByIndexIds(String[] indexIds) {
        return energyIndexMapper.getModelNodeIndexInfoListByIndexIds(indexIds);
    }
}
