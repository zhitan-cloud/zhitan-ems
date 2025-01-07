package com.zhitan.home.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.common.constant.CommonConst;
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
import com.zhitan.model.domain.vo.ModelNodeIndexInfor;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Resource
    private ModelNodeMapper modelNodeMapper;
    @Resource
    private PeakValleyMapper electricityDataItemMapper;


    @Override
    public List<HomeEnergyStatisticsVO> energyConsumptionSummation(String timeType, String modelcode) {
        Date currentTime = new Date();
//        Date currentTime = DateUtil.parseDateTime("2023-04-11 00:00:00");

        DateTime tongbiTime = DateUtil.offsetMonth(currentTime, -12);
        DateTime huanbiTime = DateUtil.offsetMonth(currentTime, -1);
        final List<HomeEnergyStatisticsVO> current = getEnergyTotalByTime(timeType, modelcode, currentTime);
        final List<HomeEnergyStatisticsVO> tongbi = getEnergyTotalByTime(timeType, modelcode, tongbiTime);
        final List<HomeEnergyStatisticsVO> huanbi = getEnergyTotalByTime(timeType, modelcode, huanbiTime);

        final Map<String, List<HomeEnergyStatisticsVO>> tongbiMap = tongbi.stream().collect(Collectors.groupingBy(HomeEnergyStatisticsVO::getEnergyNo));
        final Map<String, List<HomeEnergyStatisticsVO>> huanbiMap = huanbi.stream().collect(Collectors.groupingBy(HomeEnergyStatisticsVO::getEnergyNo));

        current.stream().forEach(vo->{
            final String energyNo = vo.getEnergyNo();
            final Double count = vo.getCount();
            final Double tongbiCount = tongbiMap.get(energyNo).stream().map(HomeEnergyStatisticsVO::getCount).mapToDouble(Double::doubleValue).sum();
            final Double huanbiCount = huanbiMap.get(energyNo).stream().map(HomeEnergyStatisticsVO::getCount).mapToDouble(Double::doubleValue).sum();

            vo.setTonCount(format2Double( vo.getCount() * Double.valueOf(vo.getCoefficient())));
            if (tongbiCount != 0) {
                vo.setTongbi(format2Double( (count - tongbiCount) / tongbiCount * 100));
            }else {
                vo.setTongbi(0D);
            }

            if (huanbiCount != 0) {
                vo.setHuanbi (format2Double((count - huanbiCount) / huanbiCount * 100));
            }else {
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
        List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(modelNode.getNodeId());
        List<String> indexIds = inforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
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
            }else {
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
        electricityPrice.stream().forEach(v->{
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
        if(null == firstModeNodeInfo){
            return voList;
        }

        double totalElectric;
        // 查询点位信息
        List<ModelNodeIndexInfor> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(modelcode, firstModeNodeInfo.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, beginTime, endTime, shixuTimeType);
            if(null != dataItemList){
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
        if(null != electricityDataMap) {
            voList.stream().forEach(vo -> {
                final List<ElectricityDataItem> electricityDataItems = electricityDataMap.get(vo.getTimeType());
                if(null != electricityDataItems) {
                    final double sum = electricityDataItems.stream().map(ElectricityDataItem::getElectricity).mapToDouble(BigDecimal::doubleValue).sum();
                    vo.setCount(format2Double(sum));
                    if(totalElectric != 0) {
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
        final Map<String, Object> energyCollectMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getCoefficient));
        final Map<String, String> energyNameMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getEnername));


        Date queryTime = new Date();
//        Date queryTime = DateUtil.parseDateTime("2023-03-28 00:00:00");
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
        List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(modelNode.getNodeId());
        List<String> indexIds = inforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
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
        while (!beginTime.after(endTime)) {
            final String currentTime = DateUtil.format(beginTime, timeFormat);
            xdataList.add(currentTime);
            final List<DataItem> dataItems = dataItemMap.get(currentTime);
            List<Double> energyCount = new ArrayList<>();
            energyTypeMap.forEach((energyType,IndexIdList)->{
                double sum;
                if(null == dataItems){
                    sum = 0;
                }else {
                    sum = dataItems.stream().filter(li -> IndexIdList.contains(li.getIndexId())).mapToDouble(DataItem::getValue).sum();
                }
                final BigDecimal coefficient = (BigDecimal) energyCollectMap.get(energyType);
                energyCount.add(sum * coefficient.doubleValue());
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
        List<String> lengList = new ArrayList<>();
        energyCollectMap.keySet().forEach(key->{
            final String name = energyNameMap.get(key);
            lengList.add(name);
        });

        for(int i = 0; i < ydataList.size(); i++){
            final List<Double> doubleList = ydataList.get(i);
            for(int n = 0; n < doubleList.size(); n++){
                array[n][i] = format2Double(doubleList.get(n));
            }
        }
        vo.setYdata(array);
        vo.setLegend(lengList.toArray(new String[0]));
        return vo;
    }

    @Override
    public List<RankingEnergyData> energyConsumptionRanking(String modelcode, String timeType) {
        List<RankingEnergyData> energyDataList = new ArrayList<>();
        String nodeCategory = "2";
        LambdaQueryWrapper<ModelNode> modelNodeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        modelNodeLambdaQueryWrapper.eq(ModelNode::getModelCode,modelcode);
        modelNodeLambdaQueryWrapper.eq(ModelNode::getNodeCategory,nodeCategory);
        List<ModelNode> modelNodeList = modelNodeMapper.selectList(modelNodeLambdaQueryWrapper);
        if(CollectionUtils.isEmpty(modelNodeList)){
            return energyDataList;
        }
        final List<String> nodeIds = modelNodeList.stream().map(ModelNode::getNodeId).collect(Collectors.toList());
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.selectIndexByNodeIds(modelcode ,nodeIds);

        final Map<String, String> nodeNameMap = new HashMap<>();
        nodeIndexInforList.forEach(n->{
            final String id = n.getNodeId();
            final String name = n.getName();
            if(!nodeNameMap.containsKey(id)){
                nodeNameMap.put(id,name);
            }
        });

        // 按照点位进行分组
        Map<String, List<ModelNodeIndexInfor>> nodeIndexMap = nodeIndexInforList.stream().collect(
                Collectors.groupingBy(ModelNodeIndexInfor::getNodeId));
        final List<String> eneryIdList = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getEnergyId).distinct().collect(Collectors.toList());
        final LambdaQueryWrapper<SysEnergy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(eneryIdList),SysEnergy::getEnersno,eneryIdList);
        final List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(queryWrapper);
        //能源编号和能源折标系数
        final Map<String, Object> energyCoefficientMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getCoefficient));
        //index和能源
        final Map<String, String> indexIdEnergyIdMap = new HashMap<>();
        nodeIndexInforList.forEach(n->{
            final String indexId = n.getIndexId();
            final String energyId = n.getEnergyId();
            if(!indexIdEnergyIdMap.containsKey(indexId)){
                indexIdEnergyIdMap.put(indexId,energyId);
            }
        });
        List<String> indexIds = nodeIndexInforList.stream().filter(l -> StringUtils.isNotEmpty(l.getIndexId())).map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        Date queryTime = new Date();
//        Date queryTime = DateUtil.parseDateTime("2023-03-28 00:00:00");
        Date beginTime;
        Date endTime;
        String shixuTimeType;
        if (TimeType.DAY.name().equals(timeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            shixuTimeType = TimeType.HOUR.name();
            // 月
        } else if (TimeType.MONTH.name().equals(timeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            shixuTimeType = TimeType.DAY.name();
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(queryTime);
            shixuTimeType = TimeType.MONTH.name();
        }
        // 根据indexId查询dataItem
        List<DataItem> dataItemList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(indexIds)) {
            dataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, shixuTimeType, indexIds);
        }
        Map<String, List<DataItem>> dataItemMap = dataItemList.stream().collect(Collectors.groupingBy(DataItem::getIndexId));

        Map<String,BigDecimal> resultMap = new HashMap<>();
        nodeIndexMap.forEach((key, value) -> {
            // 找出indexIds
            List<String> indexIdList = value.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());

            indexIdList.forEach(indexId->{
                final List<DataItem> dataItems = dataItemMap.get(indexId);
                final String energyId = indexIdEnergyIdMap.get(indexId);
                final BigDecimal coefficient = (BigDecimal) energyCoefficientMap.get(energyId);

                if(CollectionUtils.isNotEmpty(dataItems) ){
                    BigDecimal sum = BigDecimal.valueOf(dataItems.stream()
                            .mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP).multiply(coefficient);

                    if (resultMap.containsKey(key)) {
                        resultMap.put(key, resultMap.get(key).add(sum));
                    } else {
                        resultMap.put(key, sum);
                    }
                }
            });
        });

        resultMap.forEach((key,value)->{
            RankingEnergyData rankingEnergyData = new RankingEnergyData();
            rankingEnergyData.setNodeId(key);
            rankingEnergyData.setNodeName(nodeNameMap.get(key));
            rankingEnergyData.setEnergyConsumption(value.doubleValue());
            energyDataList.add(rankingEnergyData);
        });
        Collections.sort(energyDataList, Comparator.comparingDouble((RankingEnergyData item) -> item.getEnergyConsumption()).reversed());
        // 获取前5条记录
        List<RankingEnergyData> top5Items = energyDataList.subList(0, Math.min(5, energyDataList.size()));

        return top5Items;
    }
}
