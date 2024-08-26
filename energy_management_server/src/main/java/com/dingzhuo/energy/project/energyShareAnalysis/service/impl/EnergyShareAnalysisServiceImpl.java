package com.dingzhuo.energy.project.energyShareAnalysis.service.impl;

import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.data.model.domain.vo.ModelNodeIndexInfor;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.common.CommonConst;
import com.dingzhuo.energy.project.common.TimeTypeConst;
import com.dingzhuo.energy.project.dataEntry.service.IDataItemService;
import com.dingzhuo.energy.project.energyShareAnalysis.domain.dto.EnergyAnalysisDTO;
import com.dingzhuo.energy.project.energyShareAnalysis.domain.vo.EnergyAnalysisVO;
import com.dingzhuo.energy.project.energyShareAnalysis.service.IEnergyShareAnalysisService;
import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 能耗占比分析  接口层实现层
 */
@Service
@AllArgsConstructor
public class EnergyShareAnalysisServiceImpl implements IEnergyShareAnalysisService {


    private final IDataItemService dataItemService;

    private final IModelNodeService modelNodeService;

    private final IEnergyIndexService energyIndexService;


    /**
     * 获取电占比统计信息
     *
     * @param dto 查询参数
     * @return 结果
     */
    @Override
    public List<EnergyAnalysisVO> listEnergyShareAnalysis(EnergyAnalysisDTO dto) {

        List<ModelNodeIndexInfor> nodeInforList = modelNodeService.getModelNodeIndexIdRelationInforByCode(dto.getModelCode());
        if (CollectionUtils.isEmpty(nodeInforList)) {
            return new ArrayList<>();
        }

        List<String> indexIds = nodeInforList.stream()
                .map(ModelNodeIndexInfor::getIndexId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        // 按照点位进行分组
        Map<String, List<ModelNodeIndexInfor>> nodeIndexMap = nodeInforList.stream().collect(
                Collectors.groupingBy(ModelNodeIndexInfor::getName));
        // 获取查询时间
        Date beginTime = dto.getBeginTime();
        Date endTime = dto.getEndTime();
        if (!TimeTypeConst.TIME_TYPE_HOUR.equals(dto.getTimeType())) {
            endTime = DateUtil.endOfDay(endTime);
        }
        // 查询对应indexIds，找到对应dataItem信息
        List<DataItem> dataItemList = dataItemService.getDataItemHourInforByIndexIds(beginTime, endTime, TimeTypeConst.TIME_TYPE_HOUR, indexIds);
        // 求总和
        BigDecimal sum = BigDecimal.valueOf(dataItemList.stream().mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        AtomicReference<BigDecimal> sumValue = new AtomicReference<>(sum);
        if ("electric".equals(dto.getEnergyType())) {
            List<String> sumIndexIds = Lists.newArrayList("e632a819-7115-483c-9215-b562ccfa8437", "3df75c95-d179-4e58-985d-637b94554d70");
            List<DataItem> sumDataItem = dataItemService.getDataItemHourInforByIndexIds(beginTime, endTime, TimeTypeConst.TIME_TYPE_HOUR, sumIndexIds);
            sumValue.set(BigDecimal.valueOf(sumDataItem.stream().mapToDouble(DataItem::getValue).sum()));
        }
        // 如果是水的7001的话 需要有个其他的占比
        if ("WaterShareAnalysis_1".equals(dto.getModelCode())) {
            List<String> sumIndexIds = Lists.newArrayList("07d6b073-f8d7-4e4b-b8fb-a899c64d245c");
            List<DataItem> sumDataItem = dataItemService.getDataItemHourInforByIndexIds(beginTime, endTime, TimeTypeConst.TIME_TYPE_HOUR, sumIndexIds);
            sumValue.set(BigDecimal.valueOf(sumDataItem.stream().mapToDouble(DataItem::getValue).sum()));
        }

        //  倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_100);
        AtomicReference<BigDecimal> totalRatio = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> totalValue = new AtomicReference<>(BigDecimal.ZERO);

        List<EnergyAnalysisVO> analysisVOList = new ArrayList<>();
        nodeIndexMap.forEach((key, value) -> {
            EnergyAnalysisVO analysisVO = new EnergyAnalysisVO();

            BigDecimal valueTotal = sumValue.get();

            analysisVO.setSumValue(valueTotal);
            analysisVO.setEnergyUnitName(key);

            List<String> nodeIndexIds = value.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
            BigDecimal currentValue = BigDecimal.valueOf(dataItemList.stream()
                            .filter(li -> nodeIndexIds.contains(li.getIndexId()))
                            .mapToDouble(DataItem::getValue).sum())
                    .setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);

            analysisVO.setValue(currentValue);
            //  同比值
            BigDecimal ratio = BigDecimal.ZERO;
            if (valueTotal.compareTo(BigDecimal.ZERO) != 0) {
                ratio = currentValue.divide(valueTotal, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            }
            // 超过1则用1剪掉
            if (totalRatio.get().add(ratio).compareTo(BigDecimal.ONE) > 0) {
                ratio = BigDecimal.ONE.subtract(totalRatio.get());
            }

            analysisVO.setRatio(ratio.multiply(multiple));
            analysisVOList.add(analysisVO);
            totalValue.set(totalValue.get().add(currentValue));
            totalRatio.set(totalRatio.get().add(ratio));
        });

        if ("WaterShareAnalysis_1".equals(dto.getModelCode())) {
            BigDecimal valueTotal = sumValue.get();
            EnergyAnalysisVO analysisVO = new EnergyAnalysisVO();
            analysisVO.setEnergyUnitName("其他");
            analysisVO.setValue(valueTotal.subtract(totalValue.get()));
            analysisVO.setRatio(BigDecimal.ONE.subtract(totalRatio.get()).multiply(multiple));
            analysisVO.setSumValue(valueTotal);

            analysisVOList.add(analysisVO);
        }

        // 占比排序
        return analysisVOList.stream().sorted(Comparator.comparing(EnergyAnalysisVO::getRatio).reversed()).collect(Collectors.toList());
    }
}