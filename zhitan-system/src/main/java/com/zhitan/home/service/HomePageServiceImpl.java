package com.zhitan.home.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.common.core.domain.entity.SysDictData;
import com.zhitan.common.enums.TimeType;
import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.home.domain.vo.HomeEnergyConsumptionTrendVO;
import com.zhitan.home.domain.vo.HomeEnergyStatisticsVO;
import com.zhitan.home.domain.vo.HomePeakValleyVO;
import com.zhitan.home.service.impl.IHomePageService;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.model.service.IEnergyIndexService;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.peakvalley.domain.ElectricityDataItem;
import com.zhitan.peakvalley.mapper.PeakValleyMapper;
import com.zhitan.realtimedata.domain.DataItem;
import com.zhitan.system.service.ISysDictDataService;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-31 18:07
 */
@Service
@AllArgsConstructor
public class HomePageServiceImpl implements IHomePageService {

    private final SysEnergyMapper sysEnergyMapper;
    private final IModelNodeService modelNodeService;
    private final IDataItemService dataItemService;
    private final IEnergyIndexService energyIndexService;
    private final ISysDictDataService sysDictDataService;
    private ModelNodeMapper modelNodeMapper;
    private PeakValleyMapper electricityDataItemMapper;


    @Override
    public List<HomeEnergyStatisticsVO> energyConsumptionSummation(String timeType, String modelcode) {
        Date currentTime = new Date();

        DateTime tongbiTime = DateUtil.offsetMonth(currentTime, -12);
        DateTime huanbiTime = DateUtil.offsetMonth(currentTime, -1);
        if (TimeType.DAY.name().equals(timeType)) {
            huanbiTime = DateUtil.offsetDay(currentTime, -1);
        }

        final List<HomeEnergyStatisticsVO> current = getEnergyTotalByTime(timeType, modelcode, currentTime);
        final List<HomeEnergyStatisticsVO> tongbi = getEnergyTotalByTime(timeType, modelcode, tongbiTime);
        final List<HomeEnergyStatisticsVO> huanbi = getEnergyTotalByTime(timeType, modelcode, huanbiTime);

        final Map<String, List<HomeEnergyStatisticsVO>> tongbiMap = tongbi.stream().collect(Collectors.groupingBy(HomeEnergyStatisticsVO::getEnergyNo));
        final Map<String, List<HomeEnergyStatisticsVO>> huanbiMap = huanbi.stream().collect(Collectors.groupingBy(HomeEnergyStatisticsVO::getEnergyNo));

        current.stream().forEach(vo -> {
            final String energyNo = vo.getEnergyNo();
            final Double count = vo.getCount();
            final Double tongbiCount = tongbiMap.get(energyNo).stream().map(HomeEnergyStatisticsVO::getCount).mapToDouble(Double::doubleValue).sum();
            final Double huanbiCount = huanbiMap.get(energyNo).stream().map(HomeEnergyStatisticsVO::getCount).mapToDouble(Double::doubleValue).sum();

            vo.setTonCount(format2Double(count));
            if (tongbiCount != 0) {
                vo.setTongbi(format2Double((count - tongbiCount) / tongbiCount * 100));
            } else {
                vo.setTongbi(0D);
            }

            if (huanbiCount != 0) {
                vo.setHuanbi(format2Double((count - huanbiCount) / huanbiCount * 100));
            } else {
                vo.setHuanbi(0D);
            }
        });
        return current;
    }

    private List<HomeEnergyStatisticsVO> getEnergyTotalByTime(String timeType, String modelcode, Date queryTime) {
        List<HomeEnergyStatisticsVO> voList = new ArrayList<>();

        Date beginTime;
        Date endTime;
        String shixuTimeType;
        if (TimeType.DAY.name().equals(timeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(beginTime);
            shixuTimeType = TimeType.HOUR.name();
            // 月
        } else if (TimeType.MONTH.name().equals(timeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(beginTime);
            shixuTimeType = TimeType.DAY.name();
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(beginTime);
            shixuTimeType = TimeType.MONTH.name();
        }

        // 查询所有能源类型
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(new SysEnergy());
        voList = sysEnergies.stream().map(dict -> {
            HomeEnergyStatisticsVO vo = new HomeEnergyStatisticsVO();
            vo.setEnergyName(dict.getEnername());
            vo.setEnergyNo(dict.getEnersno());
            vo.setEnergyUnit(dict.getMuid());
            vo.setCount(0D);
            vo.setTonCount(0D);
            vo.setCoefficient(dict.getCoefficient().toString());
            return vo;
        }).sorted(Comparator.comparing(HomeEnergyStatisticsVO::getEnergyName)).collect(Collectors.toList());
        // 通过code获取modelnode对应index信息
        ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(modelcode);
        if (ObjectUtils.isEmpty(modelNode)) {
            return voList;
        }
        List<ModelNodeIndexInfo> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(modelNode.getNodeId());
        List<String> indexIds = inforList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toList());
        // 通过indexIds找data_Item数据
        List<DataItem> itemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, shixuTimeType, indexIds);
        // 查询点位详细信息
        List<EnergyIndex> energyIndexInforList = energyIndexService.getEnergyIndexByIds(indexIds);
        // 获取点位能源类型
        Map<String, List<String>> energyTypeMap = energyIndexInforList.stream()
                .filter(l -> StringUtils.isNotEmpty(l.getEnergyId())).collect(Collectors.groupingBy(
                        EnergyIndex::getEnergyId, Collectors.mapping(EnergyIndex::getIndexId, Collectors.toList())
                ));

        for (HomeEnergyStatisticsVO ratioVO : voList) {
            List<String> indexs = energyTypeMap.get(ratioVO.getEnergyNo());
            if (CollectionUtils.isEmpty(indexs)) {
                ratioVO.setCount(0D);
            } else {
                // 找到合计值
                double doubleCount = itemList.stream().filter(li -> indexs.contains(li.getIndexId())).mapToDouble(DataItem::getValue).sum();
                ratioVO.setCount(format2Double(doubleCount));
            }
        }
        return voList;
    }

    private double format2Double(double averageEnergy) {
        // 创建DecimalFormat对象，设置保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");

        // 格式化结果
        String formattedResult = df.format(averageEnergy);
        return Double.valueOf(formattedResult);
    }

    @Override
    public List<HomePeakValleyVO> peakValley(String timeType, String modelcode) {
        List<HomePeakValleyVO> voList = new ArrayList<>();
        // 查询器具类型
        final List<SysDictData> electricityPrice = sysDictDataService.selectDictDataByType("electricity_price");
        electricityPrice.stream().forEach(v -> {
            HomePeakValleyVO vo = new HomePeakValleyVO();
            vo.setTimeName(v.getDictLabel());
            vo.setTimeType(v.getDictValue());
            vo.setCount(0D);
            vo.setPercentage(0D);
            voList.add(vo);
        });


        Date queryTime = new Date();
//        Date queryTime = DateUtil.parseDateTime("2024-08-28 00:00:00");
        Date beginTime;
        Date endTime;
        String shixuTimeType;
        if (TimeType.DAY.name().equals(timeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(beginTime);
            shixuTimeType = TimeType.HOUR.name();
            // 月
        } else if (TimeType.MONTH.name().equals(timeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(beginTime);
            shixuTimeType = TimeType.DAY.name();
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(beginTime);
            shixuTimeType = TimeType.MONTH.name();
        }

        Map<String, List<ElectricityDataItem>> electricityDataMap;
        // 查询点位信息
        final ModelNode firstModeNodeInfo = modelNodeMapper.getFirstModeNodeInfo(modelcode);
        if (null == firstModeNodeInfo) {
            return voList;
        }

        double totalElectric;
        // 查询点位信息
        List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(modelcode, firstModeNodeInfo.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, beginTime, endTime, shixuTimeType);
            if (null != dataItemList) {
                totalElectric = dataItemList.stream().map(ElectricityDataItem::getElectricity).mapToDouble(BigDecimal::doubleValue).sum();
            } else {
                totalElectric = 0;
            }
            electricityDataMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(ElectricityDataItem::getElectricityType));
        } else {
            totalElectric = 0;
            electricityDataMap = null;
        }
        if (null != electricityDataMap) {
            voList.stream().forEach(vo -> {
                final List<ElectricityDataItem> electricityDataItems = electricityDataMap.get(vo.getTimeType());
                if (null != electricityDataItems) {
                    final double sum = electricityDataItems.stream().map(ElectricityDataItem::getElectricity).mapToDouble(BigDecimal::doubleValue).sum();
                    vo.setCount(format2Double(sum));
                    if (totalElectric != 0) {
                        vo.setPercentage(format2Double(sum / totalElectric * 100));
                    }
                }
            });
        }
        return voList;
    }

    @Override
    public HomeEnergyConsumptionTrendVO energyConsumptionTrend(String timeType, String modelcode) {
        HomeEnergyConsumptionTrendVO vo = new HomeEnergyConsumptionTrendVO();
        List<List<Double>> ydataList = new ArrayList<>();
        List<String> xdataList = new ArrayList<>();
        // 查询所有能源类型
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(new SysEnergy());

        Date queryTime = new Date();
        Date beginTime;
        Date endTime;
        String shixuTimeType;
        String timeFormat;
        if (TimeType.DAY.name().equals(timeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(beginTime);
            shixuTimeType = TimeType.HOUR.name();
            timeFormat = "yyyy-MM-dd HH";
            // 月
        } else if (TimeType.MONTH.name().equals(timeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(beginTime);
            shixuTimeType = TimeType.DAY.name();
            timeFormat = "yyyy-MM-dd";
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(beginTime);
            shixuTimeType = TimeType.MONTH.name();
            timeFormat = "yyyy-MM";
        }


        // 通过code获取modelnode对应index信息
        ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(modelcode);
        if (ObjectUtils.isEmpty(modelNode)) {
            return vo;
        }
        List<ModelNodeIndexInfo> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(modelNode.getNodeId());
        List<String> indexIds = inforList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toList());
        // 通过indexIds找data_Item数据
        List<DataItem> itemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, shixuTimeType, indexIds);
        final Map<String, List<DataItem>> dataItemMap = itemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), timeFormat)));
        // 查询点位详细信息
        List<EnergyIndex> energyIndexInforList = energyIndexService.getEnergyIndexByIds(indexIds);
        // 获取点位能源类型
        Map<String, List<String>> energyTypeMap = energyIndexInforList.stream()
                .filter(l -> StringUtils.isNotEmpty(l.getEnergyId())).collect(Collectors.groupingBy(
                        EnergyIndex::getEnergyId, Collectors.mapping(EnergyIndex::getIndexId, Collectors.toList())
                ));

        List<String> lengList = new ArrayList<>();

        while (!beginTime.after(endTime)) {
            final String currentTime = DateUtil.format(beginTime, timeFormat);
            xdataList.add(currentTime);
            final List<DataItem> dataItems = dataItemMap.get(currentTime);
            List<Double> energyCount = new ArrayList<>();

            sysEnergies.forEach(x -> {
                if(!lengList.contains(x.getEnername())){
                    lengList.add(x.getEnername());
                }
                List<String> indexIdList = energyTypeMap.get(x.getEnersno());
                double sum;
                if (null == dataItems || CollectionUtils.isEmpty(indexIdList)) {
                    sum = 0;
                } else {
                    sum = dataItems.stream().filter(li -> indexIdList.contains(li.getIndexId())).mapToDouble(DataItem::getValue).sum();
                }
                energyCount.add(sum * x.getCoefficient().doubleValue());
            });
            ydataList.add(energyCount);

            switch (TimeType.valueOf(timeType)) {
                case DAY:
                    beginTime = DateUtil.offsetHour(beginTime, 1);
                    break;
                case MONTH:
                    beginTime = DateUtil.offsetDay(beginTime, 1);
                    break;
                default:
                    beginTime = DateUtil.offsetMonth(beginTime, 1);
                    break;
            }
        }
        vo.setXdata(xdataList.toArray(new String[0]));
        Double[][] array = new Double[sysEnergies.size()][xdataList.size()];

        for (int i = 0; i < ydataList.size(); i++) {
            final List<Double> doubleList = ydataList.get(i);
            for (int n = 0; n < doubleList.size(); n++) {
                array[n][i] = format2Double(doubleList.get(n));
            }
        }
        vo.setYdata(array);
        vo.setLegend(lengList.toArray(new String[0]));
        return vo;
    }

    @Override
    public List<RankingEnergyData> energyConsumptionRanking(String modelCode, String timeType) {

        List<RankingEnergyData> energyDataList = new ArrayList<>();
        Date queryTime = new Date();
        Date beginTime;
        Date endTime;
        if (TimeType.DAY.name().equals(timeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            timeType = TimeType.HOUR.name();
            // 月
        } else if (TimeType.MONTH.name().equals(timeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            timeType = TimeType.DAY.name();

            // 年
        } else if (TimeType.YEAR.name().equals(timeType)) {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(queryTime);
            timeType = TimeType.MONTH.name();

        } else {
            return energyDataList;
        }

        LambdaQueryWrapper<ModelNode> parentNodeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        parentNodeLambdaQueryWrapper.eq(ModelNode::getModelCode, modelCode);
        List<ModelNode> modelNodeList = modelNodeMapper.selectList(parentNodeLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(modelNodeList)) {
            return energyDataList;
        }
        ModelNode parentNode = modelNodeList.stream().filter(x -> ObjectUtils.isEmpty(x.getParentId())).findFirst().orElse(null);
        if (ObjectUtils.isEmpty(parentNode)) {
            return energyDataList;
        }
        List<String> nodeIds = modelNodeList.stream().filter(x -> ObjectUtils.isNotEmpty(x.getParentId()) && parentNode.getNodeId().equals(x.getParentId()))
                .map(ModelNode::getNodeId).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(nodeIds)) {
            return energyDataList;
        }
        energyDataList = dataItemService.getHomePageConsumptionRanking(nodeIds, timeType, beginTime, endTime);
        energyDataList.forEach(x -> {
            x.energyConsumption = new BigDecimal(x.energyConsumption).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        });
        return energyDataList;
    }
}
