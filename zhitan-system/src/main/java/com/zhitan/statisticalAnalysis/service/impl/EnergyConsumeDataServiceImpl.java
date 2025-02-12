package com.zhitan.statisticalAnalysis.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.carbonemission.domain.CarbonEmission;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.dataitem.mapper.DataItemMapper;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.NodeIndex;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.model.mapper.NodeIndexMapper;
import com.zhitan.peakvalley.domain.ElectricityDataItem;
import com.zhitan.peakvalley.mapper.PeakValleyMapper;
import com.zhitan.statisticalAnalysis.common.DateTimeUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import com.zhitan.statisticalAnalysis.domain.vo.*;
import com.zhitan.statisticalAnalysis.service.IEnergyConsumeDataService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @author: yxw
 * @date: 2022年04月12日 14:15
 */
@Service
@AllArgsConstructor
public class EnergyConsumeDataServiceImpl implements IEnergyConsumeDataService {

    private DataItemMapper dataItemMapper;
    private ModelNodeMapper modelNodeMapper;
    private NodeIndexMapper nodeIndexMapper;
    private PeakValleyMapper peakValleyMapper;
    private SysEnergyMapper sysEnergyMapper;

    /**
     * 成本趋势分析（能源消耗成本）- 获取表格列表数据
     *
     * @param pageNo     页码数
     * @param pageSize   每页数据多少
     * @param timeCode   时间值   与时间类型对应：2022-03-21/2022-03/2022
     * @param timeType   时间类型 DAY/MONTH/YEAR
     * @param energyType 能源类型
     * @param modelCode  模型Code
     * @return
     */
    @Override
    public EnergyCostTrendPage listEnergyCostTrend(int pageNo, int pageSize, String timeCode, String timeType, String energyType,
                                                   String modelCode) {
        //能源类型信息
        SysEnergy sysEnergy = new SysEnergy();
        sysEnergy.setEnersno(energyType);
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(sysEnergy);
        if (sysEnergies.isEmpty()) {
            throw new RuntimeException("未查询到能源信息");
        }
        SysEnergy sysEnergyInfo = sysEnergies.stream().findFirst().get();
        //节点信息
        List<ModelNode> modelNodes = modelNodeMapper.selectList(Wrappers.<ModelNode>lambdaQuery().eq(ModelNode::getModelCode, modelCode)
                .isNull(ModelNode::getParentId));
        if (ObjectUtils.isEmpty(modelNodes)) {
            throw new RuntimeException("未查询到节点信息");
        }
        ModelNode modelNodeInfo = modelNodes.stream().findFirst().get();
        //点位信息
        List<NodeIndex> nodeIndices = nodeIndexMapper.selectList(Wrappers.<NodeIndex>lambdaQuery()
                .eq(NodeIndex::getNodeId, modelNodeInfo.getNodeId()));
        if (nodeIndices.isEmpty()) {
            throw new RuntimeException("未查询到点位信息");
        }

        // 遍历用能单元获取表格中的数据
        List<EnergyCostTrendItem> trendItemList = new ArrayList<>();
        EnergyCostTrendItem energyCostTrendItem = new EnergyCostTrendItem();
        energyCostTrendItem.setDateCode(timeCode);
        // 总费用
        BigDecimal totalCost = BigDecimal.ZERO;
        // 遍历能源类型
        List<CostTrendEnergyTypeItem> itemList = new ArrayList<>();
        CostTrendEnergyTypeItem item = new CostTrendEnergyTypeItem();
        item.setEnergyType(sysEnergyInfo.getEnersno());
        item.setEnergyName(sysEnergyInfo.getEnername());
        // 处理时间
        Date bsTime = DateTimeUtil.getTime(timeType, timeCode);
        Date endTime = DateTimeUtil.getEndTimeByType(timeType, bsTime);
        totalCost = getEnergyUnitCostTrendAnalysisValueInfo(timeType, bsTime, endTime, totalCost, nodeIndices, sysEnergyInfo.getEnersno(), item);
        itemList.add(item);
        energyCostTrendItem.setTotal(totalCost.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
        energyCostTrendItem.setItemList(itemList);
        trendItemList.add(energyCostTrendItem);

        EnergyCostTrendPage energyCostTrendPage = new EnergyCostTrendPage();
        energyCostTrendPage.setTotal(1);
        energyCostTrendPage.setItemList(trendItemList);
        return energyCostTrendPage;
    }

    /**
     * 获取用能单元成本趋势分析累积量、费用信息
     *
     * @param timeType    时间类型
     * @param bsTime      开始时间
     * @param endTime     结束时间
     * @param totalCost   总费用
     * @param nodeIndices 节点id集合
     * @param energyType  能源类型
     * @param item        返回对象
     * @return
     */
    private BigDecimal getEnergyUnitCostTrendAnalysisValueInfo(String timeType, Date bsTime, Date endTime, BigDecimal totalCost,
                                                                List<NodeIndex> nodeIndices, String energyType,
                                                                CostTrendEnergyTypeItem item) {
        BigDecimal costValue;
        BigDecimal accumulationValue = BigDecimal.ZERO;
        switch (energyType) {
            case "electric":
                List<ElectricityDataItem> electricityDataItems = peakValleyMapper.getDataStatistics(nodeIndices.stream().map(NodeIndex::getIndexId).collect(Collectors.toSet()), bsTime, endTime, timeType);
                costValue = electricityDataItems.stream().map(ElectricityDataItem::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
                accumulationValue = electricityDataItems.stream().map(ElectricityDataItem::getElectricity).reduce(BigDecimal.ZERO, BigDecimal::add);
                break;
            default:
                costValue = dataItemMapper.getDataItemTimeRangeValueByIndexIds(bsTime, endTime, timeType, nodeIndices.stream().map(NodeIndex::getIndexId).collect(Collectors.toList()));
                break;
        }
        costValue = costValue.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        totalCost = totalCost.add(costValue);
        item.setCost(costValue);
        item.setAccumulation(accumulationValue.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
        return totalCost;
    }

    /**
     * 成本趋势分析（能源消耗成本）
     *
     * @param timeCode   时间值   与时间类型对应：2022-03-21/2022-03/2022
     * @param timeType   时间类型 DAY/MONTH/YEAR
     * @param modelCode  模型Code
     * @param energyType 能源类型
     * @return
     */
    @Override
    public List<EnergyConsumeTrendDetailItem> listEnergyCostTrendDetail(String timeCode, String timeType, String modelCode, String energyType) {
        //能源类型信息
        SysEnergy sysEnergy = new SysEnergy();
        sysEnergy.setEnersno(energyType);
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(sysEnergy);
        if (sysEnergies.isEmpty()) {
            throw new RuntimeException("未查询到能源信息");
        }
        SysEnergy sysEnergyInfo = sysEnergies.stream().findFirst().get();

        //节点信息
        List<ModelNode> modelNodes = modelNodeMapper.selectList(Wrappers.<ModelNode>lambdaQuery().eq(ModelNode::getModelCode, modelCode)
                .isNull(ModelNode::getParentId));
        if (modelNodes.isEmpty()) {
            throw new RuntimeException("未查询到节点信息");
        }
        String nodeId = modelNodes.stream().findFirst().get().getNodeId();

        // 能耗信息
        List<EnergyConsumeVO> energyConsumeVOList = new ArrayList<>();
        Date startTime = DateTimeUtil.getTime(timeType, timeCode);
        Date endTime = DateTimeUtil.getEndTimeByType(timeType, startTime);
        switch (sysEnergyInfo.getEnersno()) {
            case "electric":
                List<ElectricityDataItem> electricityDataItems = peakValleyMapper.getCostTrends(startTime, endTime, timeType, nodeId, energyType);
                if (!electricityDataItems.isEmpty()) {
                    electricityDataItems.forEach(electricityDataItem -> {
                        EnergyConsumeVO temp = new EnergyConsumeVO();
                        temp.setDataTime(electricityDataItem.getDataTime());
                        temp.setCostValue(electricityDataItem.getCost());
                        temp.setAccumulationValue(electricityDataItem.getElectricity());
                        energyConsumeVOList.add(temp);
                    });
                }
                break;
            default:
                List<CarbonEmission> dataItems = dataItemMapper.getMiddleCarbonEmission(startTime, endTime, timeType, nodeId, energyType);
                if (!dataItems.isEmpty()) {
                    dataItems.forEach(electricityDataItem -> {
                        EnergyConsumeVO temp = new EnergyConsumeVO();
                        temp.setDataTime(electricityDataItem.getDataTime());
                        temp.setCostValue(new BigDecimal(electricityDataItem.getValue()));
                        temp.setAccumulationValue(new BigDecimal(electricityDataItem.getValue()).multiply(sysEnergyInfo.getPrice()));
                        energyConsumeVOList.add(temp);
                    });
                }
                break;
        }

        // 组装统计图信息
        EnergyConsumeTrendDetailItem item = new EnergyConsumeTrendDetailItem();
        item.setEnergyType(energyType);
        item.setCostLabel(sysEnergyInfo.getEnername() + "费");
        item.setAccumulationLabel(sysEnergyInfo.getEnername() + "用量");
        // 组装图表信息
        getTrendAnalysisCharInfoByEnergyType(startTime, timeType, energyConsumeVOList, item);

        List<EnergyConsumeTrendDetailItem> itemList = new ArrayList<>();
        itemList.add(item);
        return itemList;
    }

    /**
     * 组装成本趋势分析-统计图信息
     *
     * @param bsTime    时间
     * @param timeType  时间类型
     * @param dataItems 能耗
     * @param item      返回对象
     */
    private void getTrendAnalysisCharInfoByEnergyType(Date bsTime, String timeType,
                                                       List<EnergyConsumeVO> dataItems, EnergyConsumeTrendDetailItem item) {
        List<String> costKeyList = new ArrayList<>();
        List<String> accumulationKeyList = new ArrayList<>();
        List<BigDecimal> costValueList = new ArrayList<>();
        List<BigDecimal> accumulationValueList = new ArrayList<>();
        Map<String, List<EnergyConsumeVO>> energyConsumeVOMap;
        //按时间类型组织返回数据
        switch (timeType){
            case TimeTypeConst.TIME_TYPE_DAY:
                energyConsumeVOMap = dataItems.stream().collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
                for (int i = 0; i < CommonConst.DIGIT_24; i++) {
                    String formatDate = i + CommonConst.TIME_UNIT_SHOW_HOUR;
                    costKeyList.add(formatDate);
                    accumulationKeyList.add(formatDate);
                    String key = DateUtil.formatDateTime(DateUtil.offsetHour(bsTime, i));
                    calculateCostAndAccumulation(energyConsumeVOMap, key, costValueList, accumulationValueList);
                }
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                energyConsumeVOMap = dataItems.stream().collect(Collectors.groupingBy(li -> DateUtil.formatDate(li.getDataTime())));
                Date endTime = DateTimeUtil.getEndTimeByType(timeType, bsTime);
                while (bsTime.before(endTime)) {
                    String formatDate = DateUtil.formatDate(bsTime);
                    costKeyList.add(formatDate);
                    accumulationKeyList.add(formatDate);
                    calculateCostAndAccumulation(energyConsumeVOMap, formatDate, costValueList, accumulationValueList);
                    bsTime = DateUtil.offsetDay(bsTime, CommonConst.DIGIT_1);
                }
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
                energyConsumeVOMap = dataItems.stream().collect(Collectors.groupingBy(li -> formatter.format(li.getDataTime())));
                for (int i = 0; i < CommonConst.DIGIT_12; i++) {
                    Date newDate = DateUtil.offsetMonth(bsTime, i);
                    String formatDate = DateUtil.format(newDate, DateTimeUtil.COMMON_PATTERN_TO_MONTH_ZH);
                    costKeyList.add(formatDate);
                    accumulationKeyList.add(formatDate);
                    calculateCostAndAccumulation(energyConsumeVOMap, formatDate, costValueList, accumulationValueList);
                }
                break;
            default:
                break;
        }

        item.setCostKeyList(costKeyList);
        item.setCostValueList(costValueList);
        item.setAccumulationKeyList(accumulationKeyList);
        item.setAccumulationValueList(accumulationValueList);
    }

    /**
     * 计算费用和用量
     * @param energyConsumeVOMap
     * @param formatDate
     * @param costValueList
     * @param accumulationValueList
     */
    private static void calculateCostAndAccumulation(Map<String, List<EnergyConsumeVO>> energyConsumeVOMap, String formatDate, List<BigDecimal> costValueList, List<BigDecimal> accumulationValueList) {
        List<EnergyConsumeVO> energyConsumeList = Optional.ofNullable(energyConsumeVOMap.get(formatDate))
                .orElse(Collections.emptyList());
        BigDecimal totalCost = energyConsumeList.stream()
                .map(EnergyConsumeVO::getCostValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);

        BigDecimal totalAccumulation = energyConsumeList.stream()
                .map(EnergyConsumeVO::getAccumulationValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        costValueList.add(totalCost);
        accumulationValueList.add(totalAccumulation);
    }
}
