package com.dingzhuo.energy.project.home.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.domain.vo.ModelNodeIndexInfor;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.data.RealtimeDatabaseManager;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.project.common.CommonConst;
import com.dingzhuo.energy.project.dataEntry.service.IDataItemService;
import com.dingzhuo.energy.project.home.domain.vo.*;
import com.dingzhuo.energy.project.home.service.IHomeService;
import com.dingzhuo.energy.project.system.domain.SysDictData;
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
 * 首页   接口实现层
 *
 * @Author: Zhujw
 * @Date: 2023/3/1
 */
@Service
@AllArgsConstructor
public class HomeServiceImpl implements IHomeService {


    private final IDataItemService dataItemService;

    private final IModelNodeService modelNodeService;

    private final ISysDictDataService dictDataService;

    private final IEnergyIndexService energyIndexService;

    private RealtimeDatabaseManager realtimeDatabaseManager;


    /**
     * 首页获取全厂综合能耗
     *
     * @return
     */
    @Override
    public BigDecimal geEnergyConsumptionSummation(String code) {
        // 获取查询时间
        String timeCode = TimeManager.getTimeCode(DateUtil.date(), TimeType.DAY);
        // 通过code获取modelnode对应indexId信息
        List<String> indexIds = modelNodeService.listIndexIdsByModelCode(code);
        if (CollectionUtils.isEmpty(indexIds)) {
            return BigDecimal.ZERO;
        }
        // 通过indexIds找data_Item数据
        List<DataItem> itemList = dataItemService.getDataItemInforByIndexIds(timeCode, indexIds);
        // 转tce倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_1000);
        double sum = itemList.stream().mapToDouble(DataItem::getValue).sum();
        return BigDecimal.valueOf(sum).divide(multiple, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
    }

    /**
     * 首页获取综合能耗占比分析
     *
     * @param code 查询编号
     * @return
     */
    @Override
    public List<HomeEnergyConsumptionRatioVO> getHomeEnergyConsumptionRatio(String code) {
        List<HomeEnergyConsumptionRatioVO> ratioVOList = new ArrayList<>();
        // 查询字典获取所有能源名字
        List<SysDictData> energyTypeDictist = dictDataService.selectDictDataByType("energy_type");
        energyTypeDictist.forEach(energyType -> {
            HomeEnergyConsumptionRatioVO ratioVO = new HomeEnergyConsumptionRatioVO();
            ratioVO.setItem(energyType.getDictLabel());
            ratioVO.setEnergyType(energyType.getDictValue());
            ratioVOList.add(ratioVO);
        });
        // 通过code获取modelnode对应index信息
        List<String> indexIds = modelNodeService.listIndexIdsByModelCode(code);
        if (CollectionUtils.isEmpty(indexIds)) {
            return ratioVOList;
        }
        // 获取查询时间
        String timeCode = TimeManager.getTimeCode(DateUtil.date(), TimeType.DAY);
        // 通过indexIds找data_Item数据
        List<DataItem> itemList = dataItemService.getDataItemInforByIndexIds(timeCode, indexIds);
        // 转tce倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_1000);
        // 总合计值
        double sum = itemList.stream().mapToDouble(DataItem::getValue).sum();
        BigDecimal totalValue = BigDecimal.valueOf(sum).divide(multiple, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        // 查询点位详细信息
        List<EnergyIndex> energyIndexInforList = energyIndexService.getEnergyIndexByIds(indexIds);
        // 获取点位能源类型
        Map<String, List<String>> energyTypeMap = energyIndexInforList.stream()
                .filter(l -> StringUtils.isNotEmpty(l.getEnergyId()))
                .collect(Collectors.groupingBy(
                        EnergyIndex::getEnergyId, Collectors.mapping(EnergyIndex::getIndexId, Collectors.toList())
                ));
        AtomicReference<BigDecimal> totalRatio = new AtomicReference<>(BigDecimal.ZERO);
        for (HomeEnergyConsumptionRatioVO ratioVO : ratioVOList) {
            List<String> indexs = energyTypeMap.get(ratioVO.getEnergyType());
            ratioVO.setUnit("tce");
            if (CollectionUtils.isEmpty(indexs)) {
                continue;
            }
            // 找到合计值
            double doubleCount = itemList.stream().filter(li -> indexs.contains(li.getIndexId())).mapToDouble(DataItem::getValue).sum();
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
     * 首页获取分类能源统计
     *
     * @param code 查询编号
     * @return
     */
    @Override
    public List<HomeEnergyStatisticsVO> getHomeEnergyStatistic(String code) {
        List<HomeEnergyStatisticsVO> voList;
        // 查询字典获取所有能源名字
        List<SysDictData> energyTypeDictist = dictDataService.selectDictDataByType("energy_type");
        voList = energyTypeDictist.stream().map(dict -> {
            HomeEnergyStatisticsVO vo = new HomeEnergyStatisticsVO();
            vo.setItem(dict.getDictLabel());
            vo.setEnergyType(dict.getDictValue());
            return vo;
        }).sorted(Comparator.comparing(HomeEnergyStatisticsVO::getEnergyType)).collect(Collectors.toList());
        // 通过code获取modelnode对应index信息
        ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(code);
        if (ObjectUtils.isEmpty(modelNode)) {
            return voList;
        }
        List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(modelNode.getNodeId());
        List<String> indexIds = inforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        // 获取查询时间
        Date beginTime = DateUtil.beginOfDay(DateUtil.date());
        Date endTime = DateUtil.endOfDay(beginTime);
        // 通过indexIds找data_Item数据
        List<DataItem> itemList = dataItemService.getDataItemHourInforByIndexIds(beginTime, endTime, TimeType.HOUR.name(), indexIds);
        // 查询点位详细信息
        List<EnergyIndex> energyIndexInforList = energyIndexService.getEnergyIndexByIds(indexIds);
        // 获取点位能源类型
        Map<String, List<String>> energyTypeMap = energyIndexInforList.stream()
                .filter(l -> StringUtils.isNotEmpty(l.getEnergyId())).collect(Collectors.groupingBy(
                        EnergyIndex::getEnergyId, Collectors.mapping(EnergyIndex::getIndexId, Collectors.toList())
                ));
        for (HomeEnergyStatisticsVO vo : voList) {
            List<String> value = energyTypeMap.get(vo.getEnergyType());
            if (CollectionUtils.isEmpty(value)) {
                continue;
            }
            // 找到合计值
            double doubleCount = itemList.stream().filter(li -> value.contains(li.getIndexId())).mapToDouble(DataItem::getValue).sum();

            BigDecimal count = BigDecimal.valueOf(doubleCount).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            switch (vo.getEnergyType()) {
                case "electric":
                    vo.setCount(count + "KWh");
                    break;
//                case "water":
                default:
                    vo.setCount(count + "m³");
                    break;
            }
        }
        return voList;
    }

    /**
     * 首页获取能源分时监测
     *
     * @param code       查询编号
     * @param energyType 能源类型
     * @return
     */
    @Override
    public HomeEnergyMonitoringHistogramVO getHomeEnergyMonitoring(String code, String energyType) {
        HomeEnergyMonitoringHistogramVO vo = new HomeEnergyMonitoringHistogramVO();
        // 通过code获取modelnode对应index信息
        List<String> indexIds = modelNodeService.listIndexIdsByModelCode(code);
        if (CollectionUtils.isEmpty(indexIds)) {
            return vo;
        }
        // 查询点位详细信息
        List<EnergyIndex> energyIndexInforList = energyIndexService.getEnergyIndexByIds(indexIds);
        if (StringUtils.isNotEmpty(energyType)) {
            energyIndexInforList = energyIndexInforList.stream().filter(li -> energyType.equals(li.getEnergyId()))
                    .collect(Collectors.toList());
            indexIds = energyIndexInforList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(indexIds)) {
                return vo;
            }
        }
        Optional<EnergyIndex> first = energyIndexInforList.stream().filter(li -> StringUtils.isNotEmpty(li.getUnitId()))
                .findFirst();
        first.ifPresent(energyIndex -> vo.setUnit(energyIndex.getUnitId()));
        // 获取查询时间
        DateTime beginTime = DateUtil.beginOfDay(DateUtil.date());
        DateTime endTime = DateUtil.endOfDay(beginTime);
        // 对比查询时间
        DateTime lastBeginTime = DateUtil.offsetDay(beginTime, -1);
        DateTime lastEndTime = DateUtil.offsetDay(endTime, -1);
        // 通过indexIds找data_Item数据
        String timeType = TimeType.HOUR.name();
        List<DataItem> itemList = dataItemService.getDataItemHourInforByIndexIds(beginTime, endTime, timeType, indexIds);
        List<DataItem> lastItemList = dataItemService.getDataItemHourInforByIndexIds(lastBeginTime, lastEndTime, timeType, indexIds);
        // 按时间分组
        Map<String, List<DataItem>> timeValueMap = new HashMap<>();
        Map<String, List<DataItem>> lastTimeValueMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(itemList)) {
            timeValueMap = itemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), "yyyy-MM-dd HH")));
        }
        if (CollectionUtils.isNotEmpty(lastItemList)) {
            lastTimeValueMap = lastItemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), "yyyy-MM-dd HH")));
        }
        List<HomeEnergyDetectionChart> chartList = new ArrayList<>();
        // 遍历生成信息
        while (beginTime.isBefore(endTime)) {
            HomeEnergyDetectionChart chart = new HomeEnergyDetectionChart();
            String name = DateUtil.formatDate(beginTime);
            chart.setName(name);
            String date = DateUtil.format(beginTime, "HH:mm");
            chart.setDate(date);
            String format = DateUtil.format(beginTime, "yyyy-MM-dd HH");
            // 求和
            BigDecimal total = BigDecimal.ZERO;
            List<DataItem> dataItemList = timeValueMap.get(format);
            if (CollectionUtils.isNotEmpty(dataItemList)) {
                total = BigDecimal.valueOf(dataItemList.stream().mapToDouble(DataItem::getValue).sum());
            }
            chart.setValue(total.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            // 对比值
            HomeEnergyDetectionChart lastChart = new HomeEnergyDetectionChart();
            String lastName = DateUtil.formatDate(lastBeginTime);
            lastChart.setName(lastName);
            String lastDate = DateUtil.format(lastBeginTime, "HH:mm");
            lastChart.setDate(lastDate);
            // 求和
            String lastFormat = DateUtil.format(lastBeginTime, "yyyy-MM-dd HH");
            BigDecimal lastTotal = BigDecimal.ZERO;
            List<DataItem> lastDataItemList = lastTimeValueMap.get(lastFormat);
            if (CollectionUtils.isNotEmpty(lastDataItemList)) {
                lastTotal = BigDecimal.valueOf(lastDataItemList.stream().mapToDouble(DataItem::getValue).sum());
            }
            lastChart.setValue(lastTotal.setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
            chartList.add(chart);
            chartList.add(lastChart);
            beginTime = DateUtil.offsetHour(beginTime, 1);
            lastBeginTime = DateUtil.offsetHour(lastBeginTime, 1);
        }
        vo.setChartData(chartList);
        return vo;
    }

    /**
     * 首页获取能耗设备占比信息
     *
     * @param code 查询编号
     * @return
     */
    @Override
    public List<HomeEnergyUnitConsumptionChartVO> getHomeEnergyUnitConsumptionRatio(String code) {
        List<HomeEnergyUnitConsumptionChartVO> chartVOList = new ArrayList<>();
        // 通过code获取modelnode对应index信息
        List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByCode(code);
        if (CollectionUtils.isEmpty(inforList)) {
            return chartVOList;
        }
        // 获取时间编码
        String timeCode = TimeManager.getTimeCode(DateUtil.date(), TimeType.DAY);
        List<String> indexIds = inforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        // 转tce倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_1000);
        // 根据点位id查询dataitem
        List<DataItem> dataItemList = dataItemService.getDataItemInforByIndexIds(timeCode, indexIds);
        // 合计值
        double totalSum = dataItemList.stream().mapToDouble(DataItem::getValue).sum();
        BigDecimal totalValue = BigDecimal.valueOf(totalSum).divide(multiple, CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        // 根据用能单元id分组
        Map<String, List<ModelNodeIndexInfor>> nodeMap = inforList.stream().collect(Collectors.groupingBy(ModelNodeIndexInfor::getNodeId));
        nodeMap.forEach((key, value) -> {
            HomeEnergyUnitConsumptionChartVO chartVO = new HomeEnergyUnitConsumptionChartVO();
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

    /**
     * 首页获取室外温湿度
     *
     * @return 结果
     */
    @Override
    public HomeOutdoorTemperatureVO getHomeOutdoorTemperature() {

        HomeOutdoorTemperatureVO outdoorTemperatureVO = new HomeOutdoorTemperatureVO();

        String code = "Outside_Temperature";
        // 通过code获取modelnode对应index信息
        List<ModelNodeIndexInfor> infoList = modelNodeService.getModelNodeIndexIdRelationInforByCode(code);
        if (CollectionUtils.isEmpty(infoList)) {
            return outdoorTemperatureVO;
        }

        List<String> indexIds = infoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());

        List<EnergyIndex> energyIndexList = energyIndexService.selectEnergyIndexByIds(indexIds);
        List<String> codeList = energyIndexList.stream().map(EnergyIndex::getCode).collect(Collectors.toList());

        List<TagValue> tagValueList = realtimeDatabaseManager.retrieve(codeList);

        if (CollectionUtils.isNotEmpty(tagValueList)) {
            for (TagValue tagValue : tagValueList) {
                double value = tagValue.getValue() * 0.1;
                if (tagValue.getTagCode().endsWith("SD")) {
                    outdoorTemperatureVO.setHumidity(String.format("%.2f",value) + CommonConst.PERCENTAGE);
                } else {
                    outdoorTemperatureVO.setTemperature(String.format("%.2f",value) + CommonConst.UNIT_TEMPERATURE);
                }
            }
        }

        return outdoorTemperatureVO;
    }

}