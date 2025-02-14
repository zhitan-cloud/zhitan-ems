package com.zhitan.statisticalAnalysis.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.carbonemission.domain.CarbonEmission;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.common.utils.StringUtils;
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
import org.jetbrains.annotations.NotNull;
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
        if (StringUtils.isNotEmpty(energyType)) {
            sysEnergy.setEnersno(energyType);
        }
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(sysEnergy);
        if (sysEnergies.isEmpty()) {
            throw new RuntimeException("未查询到能源信息");
        }
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

        // 总费用
        BigDecimal totalCost = BigDecimal.ZERO;
        // 遍历能源类型
        List<CostTrendEnergyTypeItem> itemList = new ArrayList<>();
        for (SysEnergy sysEnergyInfo : sysEnergies) {
            CostTrendEnergyTypeItem item = new CostTrendEnergyTypeItem();
            item.setEnergyType(sysEnergyInfo.getEnersno());
            item.setEnergyName(sysEnergyInfo.getEnername());
            // 处理时间
            Date bsTime = DateTimeUtil.getTime(timeType, timeCode);
            Date endTime = DateTimeUtil.getEndTimeByType(timeType, bsTime);
            totalCost = getEnergyUnitCostTrendAnalysisValueInfo(timeType, bsTime, endTime, totalCost, nodeIndices, modelNodeInfo.getNodeId(), sysEnergyInfo, item);
            itemList.add(item);
        }
        // 遍历用能单元获取表格中的数据
        List<EnergyCostTrendItem> trendItemList = new ArrayList<>();
        EnergyCostTrendItem energyCostTrendItem = new EnergyCostTrendItem();
        energyCostTrendItem.setDateCode(timeCode);
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
     * @param timeType      时间类型
     * @param bsTime        开始时间
     * @param endTime       结束时间
     * @param totalCost     总费用
     * @param nodeIndices   节点点位集合
     * @param nodeId        节点id
     * @param sysEnergyInfo 能源类型信息
     * @param item          返回对象
     * @return
     */
    private BigDecimal getEnergyUnitCostTrendAnalysisValueInfo(String timeType, Date bsTime, Date endTime, BigDecimal totalCost,
                                                               List<NodeIndex> nodeIndices, String nodeId, SysEnergy sysEnergyInfo,
                                                               CostTrendEnergyTypeItem item) {
        BigDecimal costValue = BigDecimal.ZERO;
        BigDecimal accumulationValue = BigDecimal.ZERO;
        //电：只有HOUR数据有效；其他能源类型：HOUR、DAY有数据
        switch (sysEnergyInfo.getEnersno()) {
            case "electric":
                List<ElectricityDataItem> electricityDataItems = peakValleyMapper.getDataStatistics(nodeIndices.stream().map(NodeIndex::getIndexId).collect(Collectors.toSet()), bsTime, endTime, TimeTypeConst.TIME_TYPE_HOUR);
                costValue = electricityDataItems.stream().map(ElectricityDataItem::getCost).reduce(BigDecimal.ZERO, BigDecimal::add);
                accumulationValue = electricityDataItems.stream().map(ElectricityDataItem::getElectricity).reduce(BigDecimal.ZERO, BigDecimal::add);
                break;
            default:
                accumulationValue = dataItemMapper.getDataItemTimeRangeValueByNodeId(bsTime, endTime, TimeTypeConst.TIME_TYPE_DAY, nodeId, sysEnergyInfo.getEnersno());
                costValue = accumulationValue.multiply(sysEnergyInfo.getPrice());
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
        if (StringUtils.isNotEmpty(energyType)) {
            sysEnergy.setEnersno(energyType);
        }
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(sysEnergy);
        if (sysEnergies.isEmpty()) {
            throw new RuntimeException("未查询到能源信息");
        }

        //节点信息
        List<ModelNode> modelNodes = modelNodeMapper.selectList(Wrappers.<ModelNode>lambdaQuery().eq(ModelNode::getModelCode, modelCode)
                .isNull(ModelNode::getParentId));
        if (modelNodes.isEmpty()) {
            throw new RuntimeException("未查询到节点信息");
        }
        String nodeId = modelNodes.stream().findFirst().get().getNodeId();

        // 能耗信息
        List<EnergyConsumeTrendDetailItem> itemList = new ArrayList<>();
        Date startTime = DateTimeUtil.getTime(timeType, timeCode);
        Date endTime = DateTimeUtil.getEndTimeByType(timeType, startTime);
        //电：只有HOUR数据有效；其他能源类型：HOUR、DAY有数据
        String queryTimeType = TimeTypeConst.TIME_TYPE_HOUR;
        for (SysEnergy sysEnergyInfo : sysEnergies) {
            List<EnergyConsumeVO> energyConsumeVOList = new ArrayList<>();
            switch (sysEnergyInfo.getEnersno()) {
                case "electric":
                    List<ElectricityDataItem> electricityDataItems = peakValleyMapper.getCostTrends(startTime, endTime, queryTimeType, nodeId, sysEnergyInfo.getEnersno());
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
                    if (timeType.equals(TimeTypeConst.TIME_TYPE_MONTH) || timeType.equals(TimeTypeConst.TIME_TYPE_YEAR)) {
                        queryTimeType = TimeTypeConst.TIME_TYPE_DAY;
                    }
                    List<CarbonEmission> dataItems = dataItemMapper.getMiddleCarbonEmission(startTime, endTime, queryTimeType, nodeId, sysEnergyInfo.getEnersno());
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
            BigDecimal cost = energyConsumeVOList.stream().map(EnergyConsumeVO::getCostValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            BigDecimal accumulation = energyConsumeVOList.stream().map(EnergyConsumeVO::getAccumulationValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            // 组装统计图信息
            EnergyConsumeTrendDetailItem item = new EnergyConsumeTrendDetailItem();
            item.setEnergyType(sysEnergyInfo.getEnersno());
            item.setEnergyUnit(sysEnergyInfo.getMuid());
            item.setCostLabel(sysEnergyInfo.getEnername() + "费");
            item.setAccumulationLabel(sysEnergyInfo.getEnername() + "用量");
            item.setCost(cost);
            item.setAccumulation(accumulation);
            // 组装图表信息
            getTrendAnalysisCharInfoByEnergyType(startTime, timeType, energyConsumeVOList, item);
            itemList.add(item);
        }
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
        switch (timeType) {
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
                SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtil.COMMON_PATTERN_TO_MONTH_ZH);
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
     *
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

    /**
     * 同步环比分析
     *
     * @param req            请求参数
     * @param comparisonType 对比类型
     * @return
     */
    @Override
    public List<EnergyTypeValueContrastedVO> listEnergyTypeYoyInfo(QueryCompareRequest req, String comparisonType) {
        String energyType = req.getEnergyType();
        String timeType = req.getTimeType();
        String timeCode = req.getTimeCode();
        String nodeId = req.getNodeId();
        //能源类型信息
        SysEnergy sysEnergy = new SysEnergy();
        sysEnergy.setEnersno(energyType);
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(sysEnergy);
        if (sysEnergies.isEmpty()) {
            throw new RuntimeException("未查询到能源信息");
        }
        SysEnergy sysEnergyInfo = sysEnergies.get(0);

        // 能耗信息
        Date startTime = DateTimeUtil.getTime(timeType, timeCode);
        Date endTime = DateTimeUtil.getEndTimeByType(timeType, startTime);
        //是否同比
        boolean isYoy = comparisonType.equals(CommonConst.ENERGY_COMPARISON_YOY);
        // 计算上一年的同期时间
        Date lastBeginTime = DateUtil.offset(startTime, DateField.YEAR, CommonConst.DIGIT_MINUS_1);
        Date lastEndTime = DateUtil.offset(endTime, DateField.YEAR, CommonConst.DIGIT_MINUS_1);
        if (!isYoy) {
            switch (timeType) {
                case TimeTypeConst.TIME_TYPE_DAY:
                    lastBeginTime = DateUtil.offsetDay(startTime, CommonConst.DIGIT_MINUS_1);
                    lastEndTime = DateUtil.offsetDay(endTime, CommonConst.DIGIT_MINUS_1);
                    break;
                case TimeTypeConst.TIME_TYPE_MONTH:
                    lastBeginTime = DateUtil.offsetMonth(startTime, CommonConst.DIGIT_MINUS_1);
                    lastEndTime = DateUtil.offsetMonth(endTime, CommonConst.DIGIT_MINUS_1);
                    break;
            }
        }

        //电：只有HOUR数据有效；其他能源类型：HOUR、DAY有数据
        String queryTimeType = TimeTypeConst.TIME_TYPE_HOUR;
        List<EnergyConsumeVO> energyConsumeVOList = new ArrayList<>();
        switch (sysEnergyInfo.getEnersno()) {
            case "electric":
                List<ElectricityDataItem> electricityDataItems = peakValleyMapper.getCostTrends(startTime, endTime, queryTimeType, nodeId, sysEnergyInfo.getEnersno());
                List<ElectricityDataItem> lastDataItemList = peakValleyMapper.getCostTrends(lastBeginTime, lastEndTime, queryTimeType, nodeId, sysEnergyInfo.getEnersno());
                if (!lastDataItemList.isEmpty()) {
                    electricityDataItems.addAll(lastDataItemList);
                }
                if (!electricityDataItems.isEmpty()) {
                    electricityDataItems.forEach(electricityDataItem -> {
                        EnergyConsumeVO temp = new EnergyConsumeVO();
                        temp.setDataTime(electricityDataItem.getDataTime());
                        temp.setAccumulationValue(electricityDataItem.getElectricity());
                        energyConsumeVOList.add(temp);
                    });
                }
                break;
            default:
                if (timeType.equals(TimeTypeConst.TIME_TYPE_MONTH) || timeType.equals(TimeTypeConst.TIME_TYPE_YEAR)) {
                    queryTimeType = TimeTypeConst.TIME_TYPE_DAY;
                }
                List<CarbonEmission> dataItems = dataItemMapper.getMiddleCarbonEmission(startTime, endTime, queryTimeType, nodeId, sysEnergyInfo.getEnersno());
                List<CarbonEmission> lastDataItems = dataItemMapper.getMiddleCarbonEmission(lastBeginTime, lastEndTime, queryTimeType, nodeId, sysEnergyInfo.getEnersno());
                if (!lastDataItems.isEmpty()) {
                    dataItems.addAll(lastDataItems);
                }
                dataItems.addAll(lastDataItems);
                if (!dataItems.isEmpty()) {
                    dataItems.forEach(dataItem -> {
                        EnergyConsumeVO temp = new EnergyConsumeVO();
                        temp.setDataTime(dataItem.getDataTime());
                        temp.setAccumulationValue(new BigDecimal(dataItem.getValue()));
                        energyConsumeVOList.add(temp);
                    });
                }
                break;
        }
        // 组装统计图信息
        return getEnergyTypeValueContrastedVOList(startTime, timeType, energyConsumeVOList, sysEnergyInfo.getEnersno(), isYoy);
    }

    /**
     * 组装成本趋势分析-统计图信息
     *
     * @param bsTime     时间
     * @param timeType   时间类型
     * @param dataItems  能耗
     * @param energyType 能源类型
     * @param isYoy      是否同比
     */
    private List<EnergyTypeValueContrastedVO> getEnergyTypeValueContrastedVOList(Date bsTime, String timeType,
                                                                                 List<EnergyConsumeVO> dataItems, String energyType, boolean isYoy) {
        Map<String, List<EnergyConsumeVO>> energyConsumeVOMap;
        Map<String, List<EnergyConsumeVO>> lastEnergyConsumeVOMap;
        List<EnergyTypeValueContrastedVO> itemList = new ArrayList<>();
        //按时间类型组织返回数据
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                energyConsumeVOMap = dataItems.stream().collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
                for (int i = 0; i < CommonConst.DIGIT_24; i++) {
                    Date currentTime = DateUtil.offsetHour(bsTime, i);
                    Date compareTime = isYoy ? DateUtil.offset(currentTime, DateField.YEAR, CommonConst.DIGIT_MINUS_1) : DateUtil.offsetDay(currentTime, CommonConst.DIGIT_MINUS_1);
                    String keyCurrentTime = DateUtil.formatDateTime(currentTime);
                    String keyCompareTime = DateUtil.formatDateTime(compareTime);
                    EnergyTypeValueContrastedVO item = getEnergyTypeValueContrastedVO(energyType, energyConsumeVOMap, keyCurrentTime, keyCompareTime, currentTime, compareTime);
                    itemList.add(item);
                }
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                energyConsumeVOMap = dataItems.stream().collect(Collectors.groupingBy(li -> DateUtil.formatDate(li.getDataTime())));
                Date endTime = DateTimeUtil.getEndTimeByType(timeType, bsTime);
                while (bsTime.before(endTime)) {
                    Date currentTime = bsTime;
                    Date compareTime = isYoy ? DateUtil.offset(currentTime, DateField.YEAR, CommonConst.DIGIT_MINUS_1) : DateUtil.offsetMonth(currentTime, CommonConst.DIGIT_MINUS_1);
                    String keyCurrentTime = DateUtil.formatDate(currentTime);
                    String keyCompareTime = DateUtil.formatDate(compareTime);
                    EnergyTypeValueContrastedVO item = getEnergyTypeValueContrastedVO(energyType, energyConsumeVOMap, keyCurrentTime, keyCompareTime, currentTime, compareTime);
                    itemList.add(item);
                    bsTime = DateUtil.offsetDay(bsTime, CommonConst.DIGIT_1);
                }
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                SimpleDateFormat formatter = new SimpleDateFormat(DateTimeUtil.COMMON_PATTERN_TO_MONTH_ZH);
                energyConsumeVOMap = dataItems.stream().collect(Collectors.groupingBy(li -> formatter.format(li.getDataTime())));
                for (int i = 0; i < CommonConst.DIGIT_12; i++) {
                    Date currentTime = DateUtil.offsetMonth(bsTime, i);
                    Date compareTime = DateUtil.offset(currentTime, DateField.YEAR, CommonConst.DIGIT_MINUS_1);
                    String keyCurrentTime = formatter.format(currentTime);
                    String keyCompareTime = formatter.format(compareTime);
                    EnergyTypeValueContrastedVO item = getEnergyTypeValueContrastedVO(energyType, energyConsumeVOMap, keyCurrentTime, keyCompareTime, currentTime, compareTime);
                    itemList.add(item);
                }
                break;
            default:
                break;
        }
        return itemList;
    }

    private @NotNull EnergyTypeValueContrastedVO getEnergyTypeValueContrastedVO(String energyType, Map<String, List<EnergyConsumeVO>> energyConsumeVOMap,
                                                                                String keyCurrentTime, String keyCompareTime, Date currentTime, Date compareTime) {
        List<EnergyConsumeVO> energyConsumeList = Optional.ofNullable(energyConsumeVOMap.get(keyCurrentTime))
                .orElse(Collections.emptyList());
        BigDecimal currentValue = calculateSum(energyConsumeList);
        List<EnergyConsumeVO> lastEnergyConsumeList = Optional.ofNullable(energyConsumeVOMap.get(keyCompareTime))
                .orElse(Collections.emptyList());
        BigDecimal contrastValues = calculateSum(lastEnergyConsumeList);
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_100);
        BigDecimal ratio = calculateRatio(currentValue, contrastValues, multiple);
        EnergyTypeValueContrastedVO item = new EnergyTypeValueContrastedVO();
        item.setEnergyType(energyType);
        item.setCurrentTime(DateUtil.formatDateTime(currentTime));
        item.setCompareTime(DateUtil.formatDateTime(compareTime));
        item.setCurrentValue(currentValue);
        item.setContrastValues(contrastValues);
        item.setRatio(ratio);
        return item;
    }

    private BigDecimal calculateSum(List<EnergyConsumeVO> dataItemList) {
        return dataItemList.stream()
                .map(EnergyConsumeVO::getAccumulationValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateRatio(BigDecimal currentSum, BigDecimal lastSum, BigDecimal multiple) {
        if (lastSum.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return currentSum.subtract(lastSum)
                .divide(lastSum, CommonConst.DIGIT_2, RoundingMode.HALF_UP)
                .multiply(multiple)
                .setScale(CommonConst.DIGIT_0, RoundingMode.HALF_UP);
    }
}
