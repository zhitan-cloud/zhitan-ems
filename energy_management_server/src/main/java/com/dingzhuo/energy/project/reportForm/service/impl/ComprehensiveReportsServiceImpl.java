package com.dingzhuo.energy.project.reportForm.service.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.domain.vo.ModelNodeIndexInfor;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.common.CommonConst;
import com.dingzhuo.energy.project.dataEntry.service.IDataItemService;
import com.dingzhuo.energy.project.reportForm.domain.dto.ComprehensiveReportsDTO;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveConsumptionRatioVO;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveReportsItem;
import com.dingzhuo.energy.project.reportForm.domain.vo.ComprehensiveUnitConsumptionChartVO;
import com.dingzhuo.energy.project.reportForm.service.IComprehensiveReportsService;
import com.dingzhuo.energy.project.system.domain.SysDictData;
import com.dingzhuo.energy.project.system.service.ISysConfigService;
import com.dingzhuo.energy.project.system.service.ISysDictDataService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * 综合报表接口实现层
 *
 * @Author: Zhujw
 * @Date: 2023/3/16
 */
@Service
@AllArgsConstructor
public class ComprehensiveReportsServiceImpl implements IComprehensiveReportsService {


    private final IDataItemService dataItemService;

    private final IModelNodeService modelNodeService;

    private final ISysConfigService sysConfigService;

    private final ISysDictDataService dictDataService;

    private final IEnergyIndexService energyIndexService;


    /**
     * 综合报表查询列表信息
     *
     * @param dto 请求参数
     * @return
     */
    @Override
    public List<ComprehensiveReportsItem> listComprehensiveReport(ComprehensiveReportsDTO dto) {
        List<ComprehensiveReportsItem> ratio = new ArrayList<>();
        // 根据code查询model信息
        ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dto.getModeCode());
        if (ObjectUtils.isEmpty(modelNode)) {
            return ratio;
        }
        // 通过code获取modelnode对应index信息
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeService.listModelNodeIndexIdRelationInforByParentId(modelNode.getNodeId());
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(indexIds)) {
            return ratio;
        }
        // 如果填入了能源类型则需要进行筛选
        if (StringUtils.isNotEmpty(dto.getEnergyType())) {
            List<EnergyIndex> energyIndexList = energyIndexService.getEnergyIndexByIds(indexIds).stream()
                    .filter(li -> dto.getEnergyType().equals(li.getEnergyId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(energyIndexList)) {
                return ratio;
            }
            indexIds = energyIndexList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        }
        // 分组
        Map<String, List<String>> nodeIndexMap = nodeIndexInforList.stream().collect(
                Collectors.groupingBy(ModelNodeIndexInfor::getNodeId,
                        Collectors.mapping(ModelNodeIndexInfor::getIndexId, Collectors.toList())));
        // 查询数据
        Date time = dto.getTime();
        AtomicReference<List<DataItem>> dataItemList = new AtomicReference<>();
        AtomicReference<List<DataItem>> dataItemList2 = new AtomicReference<>();
        AtomicReference<List<DataItem>> dataItemList3 = new AtomicReference<>();
        AtomicReference<List<DataItem>> dataItemList4 = new AtomicReference<>();
        for (int i = 0; i < CommonConst.DIGIT_4; i++) {
            int offset = i * CommonConst.DIGIT_MINUS_1;
            // 获取查询时间
            Date newDate = DateUtil.offset(time, DateField.YEAR, offset);
            Date newEndDate = DateUtil.endOfMonth(newDate);
            // 查询dataItem 数据
            switch (i) {
                case 0:
                    dataItemList.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
                case 1:
                    dataItemList2.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
                case 2:
                    dataItemList3.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
                default:
                    dataItemList4.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
            }
        }
        // 寻找关系
        nodeIndexMap.forEach((key, value) -> {
            ComprehensiveReportsItem reportsItem = new ComprehensiveReportsItem();
            Optional<ModelNodeIndexInfor> first = nodeIndexInforList.stream().filter(li -> key.equals(li.getNodeId())).findFirst();
            ModelNodeIndexInfor infor = first.get();
            // 设置名称
            reportsItem.setEnergyUnit(infor.getName());
            // 设置值
            BigDecimal sum = BigDecimal.valueOf(dataItemList.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            BigDecimal sum2 = BigDecimal.valueOf(dataItemList2.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            BigDecimal sum3 = BigDecimal.valueOf(dataItemList3.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            BigDecimal sum4 = BigDecimal.valueOf(dataItemList4.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            reportsItem.setValue(sum.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            reportsItem.setValue2(sum2.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            reportsItem.setValue3(sum3.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            reportsItem.setValue4(sum4.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            ratio.add(reportsItem);
        });
        return ratio;
    }

    /**
     * 综合报表查询用能单元柱状图信息
     *
     * @param dto   请求参数
     * @return
     */
    @Override
    public List<ComprehensiveReportsItem> listEnergyUnitComprehensiveReport(ComprehensiveReportsDTO dto) {
        List<ComprehensiveReportsItem> ratio = new ArrayList<>();
        // 根据code查询model信息
        ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dto.getModeCode());
        if (ObjectUtils.isEmpty(modelNode)) {
            return ratio;
        }
        // 通过code获取modelnode对应index信息
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeService.listModelNodeIndexIdRelationInforByParentId(modelNode.getNodeId());
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(indexIds)) {
            return ratio;
        }
        // 如果填入了能源类型则需要进行筛选
        if (StringUtils.isNotEmpty(dto.getEnergyType())) {
            List<EnergyIndex> energyIndexList = energyIndexService.getEnergyIndexByIds(indexIds).stream()
                    .filter(li -> dto.getEnergyType().equals(li.getEnergyId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(energyIndexList)) {
                return ratio;
            }
            indexIds = energyIndexList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        }
        // 分组
        Map<String, List<String>> nodeIndexMap = nodeIndexInforList.stream().collect(
                Collectors.groupingBy(ModelNodeIndexInfor::getNodeId,
                        Collectors.mapping(ModelNodeIndexInfor::getIndexId, Collectors.toList())));
        // 查询数据
        Date time = dto.getTime();
        AtomicReference<List<DataItem>> dataItemList = new AtomicReference<>();
        AtomicReference<List<DataItem>> dataItemList2 = new AtomicReference<>();
        AtomicReference<List<DataItem>> dataItemList3 = new AtomicReference<>();
        AtomicReference<List<DataItem>> dataItemList4 = new AtomicReference<>();
        for (int i = 0; i < CommonConst.DIGIT_4; i++) {
            int offset = i * CommonConst.DIGIT_MINUS_1;
            // 获取查询时间
            Date newDate = DateUtil.offset(time, DateField.YEAR, offset);
            Date newEndDate = DateUtil.endOfMonth(newDate);
            // 查询dataItem 数据
            switch (i) {
                case 0:
                    dataItemList.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
                case 1:
                    dataItemList2.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
                case 2:
                    dataItemList3.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
                default:
                    dataItemList4.set(dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds));
                    break;
            }
        }
        // 寻找关系
        nodeIndexMap.forEach((key, value) -> {
            ComprehensiveReportsItem charItem = new ComprehensiveReportsItem();
            Optional<ModelNodeIndexInfor> first = nodeIndexInforList.stream().filter(li -> key.equals(li.getNodeId())).findFirst();
            ModelNodeIndexInfor infor = first.get();
            // 设置名称
            charItem.setEnergyUnit(infor.getName());
            // 设置值
            BigDecimal sum = BigDecimal.valueOf(dataItemList.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            BigDecimal sum2 = BigDecimal.valueOf(dataItemList2.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            BigDecimal sum3 = BigDecimal.valueOf(dataItemList3.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            BigDecimal sum4 = BigDecimal.valueOf(dataItemList4.get().stream().filter(li -> value.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum());
            charItem.setValue(sum.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            charItem.setValue2(sum2.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            charItem.setValue3(sum3.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            charItem.setValue4(sum4.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            ratio.add(charItem);
        });
        return ratio;
    }

    /**
     * 综合报表查询能耗占比信息
     *
     * @param dto 请求参数
     * @return
     */
    @Override
    public List<ComprehensiveConsumptionRatioVO> getEnergyConsumptionRatio(ComprehensiveReportsDTO dto) {
        List<ComprehensiveConsumptionRatioVO> ratioVOList = new ArrayList<>();
        // 查询字典获取所有能源名字
        List<SysDictData> energyTypeDictist = dictDataService.selectDictDataByType("energy_type");
        energyTypeDictist.forEach(energyType -> {
            ComprehensiveConsumptionRatioVO ratioVO = new ComprehensiveConsumptionRatioVO();
            ratioVO.setItem(energyType.getDictLabel());
            ratioVO.setEnergyType(energyType.getDictValue());
            ratioVOList.add(ratioVO);
        });
        // 通过code获取modelnode对应index信息
        List<String> indexIds = modelNodeService.listIndexIdsByModelCode(dto.getModeCode());
        if (CollectionUtils.isEmpty(indexIds)) {
            return ratioVOList;
        }
        // 查询点位详细信息
        List<EnergyIndex> energyIndexInforList = energyIndexService.getEnergyIndexByIds(indexIds);
        // 获取查询时间
        Date newDate = dto.getTime();
        Date newEndDate = DateUtil.endOfMonth(newDate);
        // 通过indexIds找data_Item数据
        List<DataItem> itemList = dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds);
        // 转tce倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_1000);
        // 总合计值
        double sum = itemList.stream().mapToDouble(DataItem::getValue).sum();
        BigDecimal totalValue = BigDecimal.valueOf(sum).divide(multiple, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        // 获取点位能源类型
        Map<String, List<String>> energyTypeMap = energyIndexInforList.stream()
                .filter(l -> StringUtils.isNotEmpty(l.getEnergyId()))
                .collect(Collectors.groupingBy(
                        EnergyIndex::getEnergyId, Collectors.mapping(EnergyIndex::getIndexId, Collectors.toList())
                ));
        AtomicReference<BigDecimal> totalRatio = new AtomicReference<>(BigDecimal.ZERO);
        for (ComprehensiveConsumptionRatioVO ratioVO : ratioVOList) {
            List<String> indexs = energyTypeMap.get(ratioVO.getEnergyType());
            ratioVO.setUnit("tce");
            if (CollectionUtils.isEmpty(indexs)) {
                continue;
            }
            // 找到合计值
            double doubleCount = itemList.stream().filter(li -> indexs.contains(li.getIndexId()))
                    .mapToDouble(DataItem::getValue).sum();
            BigDecimal count = BigDecimal.valueOf(doubleCount).divide(multiple, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            BigDecimal ratio = BigDecimal.ZERO;
            if (totalValue.compareTo(BigDecimal.ZERO) != 0 || count.compareTo(BigDecimal.ZERO) != 0) {
                // 计算比例
                ratio = count.divide(totalValue, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
                // 超过1则用1剪掉
                if (totalRatio.get().add(ratio).compareTo(BigDecimal.ONE) > 0) {
                    ratio = BigDecimal.ONE.subtract(totalRatio.get());
                }
            }
            ratioVO.setCount(count);
            ratioVO.setPercent(ratio);
            totalRatio.set(totalRatio.get().add(ratio));
        }
        return ratioVOList;
    }

    /**
     * 综合报表获取能耗设备占比信息
     *
     * @param dto 请求参数
     * @return
     */
    @Override
    public List<ComprehensiveUnitConsumptionChartVO> getEnergyUnitConsumptionRatio(ComprehensiveReportsDTO dto) {
        List<ComprehensiveUnitConsumptionChartVO> chartVOList = new ArrayList<>();
        // 通过code获取modelnode对应index信息
        List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByCode(dto.getModeCode());
        if (CollectionUtils.isEmpty(inforList)) {
            return chartVOList;
        }
        List<String> indexIds = inforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        // 如果填入了能源类型则需要进行筛选
        if (StringUtils.isNotEmpty(dto.getEnergyType())) {
            List<EnergyIndex> energyIndexList = energyIndexService.getEnergyIndexByIds(indexIds).stream().filter(li -> dto.getEnergyType().equals(li.getEnergyId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(energyIndexList)) {
                return chartVOList;
            }
            indexIds = energyIndexList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        }
        // 获取时间编码
        Date newDate = dto.getTime();
        Date newEndDate = DateUtil.endOfMonth(newDate);
        // 根据点位id查询dataitem
        List<DataItem> dataItemList = dataItemService.getDataItemHourInforByIndexIds(newDate, newEndDate, TimeType.HOUR.name(), indexIds);
        // 合计值
        double totalSum = dataItemList.stream().mapToDouble(DataItem::getValue).sum();
        BigDecimal totalValue = BigDecimal.valueOf(totalSum).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        // 转tce倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_1000);
        // 根据用能单元id分组
        Map<String, List<ModelNodeIndexInfor>> nodeMap = inforList.stream().collect(Collectors.groupingBy(ModelNodeIndexInfor::getNodeId));
        nodeMap.forEach((key, value) -> {
            ComprehensiveUnitConsumptionChartVO chartVO = new ComprehensiveUnitConsumptionChartVO();
            // 设置名字
            Optional<ModelNodeIndexInfor> first = value.stream().findFirst();
            first.ifPresent(modelNodeIndexInfor -> chartVO.setName(modelNodeIndexInfor.getName()));
            // 设置当前值
            List<String> indexIdList = value.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
            double sum = dataItemList.stream().filter(li -> indexIdList.contains(li.getIndexId())).mapToDouble(DataItem::getValue).sum();
            BigDecimal energyValue = BigDecimal.valueOf(sum).divide(multiple, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            chartVO.setValue(energyValue);
            chartVO.setUnit("tce");
            // 设置合计值
            chartVO.setTotal(totalValue);
            chartVOList.add(chartVO);
        });
        return chartVOList;
    }

}
