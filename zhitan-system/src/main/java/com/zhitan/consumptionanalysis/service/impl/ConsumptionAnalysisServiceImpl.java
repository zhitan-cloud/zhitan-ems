package com.zhitan.consumptionanalysis.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.consumptionanalysis.domain.dto.ConsumptionAnalysisDTO;
import com.zhitan.consumptionanalysis.domain.vo.*;
import com.zhitan.consumptionanalysis.service.IConsumptionAnalysisService;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.energyIndicators.domain.EnergyIndicators;
import com.zhitan.energyIndicators.mapper.EnergyIndicatorsMapper;
import com.zhitan.model.domain.vo.ModelNodeIndexInfor;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.productoutput.domain.ProductOutput;
import com.zhitan.productoutput.mapper.ProductOutputMapper;
import com.zhitan.realtimedata.domain.DataItem;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-10-16 18:25
 */
@Service
@AllArgsConstructor
public class ConsumptionAnalysisServiceImpl implements IConsumptionAnalysisService {

    private final ModelNodeMapper modelNodeMapper;

    private final EnergyIndicatorsMapper energyIndicatorsMapper;
    
    private final ProductOutputMapper productOutputMapper;
    
    private final SysEnergyMapper sysEnergyMapper;

    private final IDataItemService dataItemService;

    private final IModelNodeService modelNodeService;
    
    @Override
    public ConsumptionAnalysisVO getByArea(ConsumptionAnalysisDTO dto) {
        List<ConsumptionAnalysisData> dataList = new ArrayList<>();
        List<ChartData> chartDataList = new ArrayList<>();
        
        ConsumptionAnalysisVO consumptionAnalysisVO = new ConsumptionAnalysisVO();
        final String analysisType = dto.getAnalysisType();
        final String nodeId = dto.getNodeId();
        final String energyType = dto.getEnergyType();
        final Date queryTime = dto.getDataTime();

        /**
         * 查询点位与用能单元信息
         */
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.getModelNodeIndexIdByNodeId(nodeId,energyType);

//        if (CollectionUtils.isEmpty(nodeIndexInforList)) {
//            return consumptionAnalysisVO;
//        }
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        Date beginTime;
        Date endTime;
        Date lastTime;
        Date lastEndTime;
        String queryTimeType = dto.getTimeType();
        String shixuTimeType;
        String timeFormat;
        if (TimeType.DAY.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            shixuTimeType = TimeType.HOUR.name();
            if ("YOY".equals(analysisType)) {
                //同比分析，时间取去年的
                lastTime = DateUtil.offsetMonth(beginTime, -12);
                lastEndTime = DateUtil.offsetMonth(endTime, -12);
            }else {
                //环比分析，时间取 昨天
                lastTime = DateUtil.offsetDay(beginTime, -1);
                lastEndTime = DateUtil.offsetDay(endTime, -1);
            }
            timeFormat = "yyyy-MM-dd HH";
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            shixuTimeType = TimeType.DAY.name();
            if ("YOY".equals(analysisType)) {
                //同比分析，时间取去年的
                lastTime = DateUtil.offsetMonth(beginTime, -12);
                lastEndTime = DateUtil.offsetMonth(endTime, -12);
            }else {
                //环比分析，时间取 昨天
                lastTime = DateUtil.offsetMonth(beginTime, -1);
                lastEndTime = DateUtil.offsetMonth(endTime, -1);
            }
            timeFormat = "yyyy-MM-dd";
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(queryTime);
            shixuTimeType = TimeType.MONTH.name();
            if ("YOY".equals(analysisType)) {
                //同比分析，时间取去年的
                lastTime = DateUtil.offsetMonth(beginTime, -12);
                lastEndTime = DateUtil.offsetMonth(endTime, -12);
            }else {
                //环比分析，时间取 昨天
                lastTime = DateUtil.offsetMonth(beginTime,  -1);
                lastEndTime = DateUtil.offsetMonth(endTime,  -1);
            }
            
            timeFormat = "yyyy-MM";
        }
        // 根据indexId查询dataItem
        List<DataItem> dataItemList = new ArrayList<>();
        List<DataItem> lastDataItemList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(indexIds)) {
            dataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, shixuTimeType, indexIds);
            lastDataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(lastTime, lastEndTime, shixuTimeType, indexIds);
        }
        Map<String, List<DataItem>> dataItemMap = dataItemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), timeFormat)));
        Map<String, List<DataItem>> lastDataItemMap = lastDataItemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), timeFormat)));
        //  倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_100);

        while (!beginTime.after(endTime)) {
            ConsumptionAnalysisData data = new ConsumptionAnalysisData();
            final String currentTime = DateUtil.format(beginTime, timeFormat);
            final String compareTime;
            DateTime dateTime;
            if (TimeType.DAY.name().equals(queryTimeType)) {
               
                if ("YOY".equals(analysisType)) {
                    //同比分析，时间取去年的
                    dateTime = DateUtil.offsetMonth(beginTime, -12);
                }else {
                    //环比分析，时间取 昨天
                    dateTime = DateUtil.offsetDay(beginTime, -1);
                }
                compareTime = DateUtil.format(dateTime, timeFormat);
                // 月
            } else if (TimeType.MONTH.name().equals(queryTimeType)) {
                if ("YOY".equals(analysisType)) {
                    //同比分析，时间取去年的
                    dateTime = DateUtil.offsetMonth(beginTime, -12);
                }else {
                    //环比分析，时间取 昨天
                    dateTime = DateUtil.offsetMonth(beginTime, -1);
                }
                compareTime = DateUtil.format(dateTime, timeFormat);
                // 年
            } else {
                if ("YOY".equals(analysisType)) {
                    //同比分析，时间取去年的
                    dateTime = DateUtil.offsetMonth(beginTime, -12);
                }else {
                    //环比分析，时间取 昨天
                    dateTime = DateUtil.offsetMonth(beginTime,  -1);
                }
                compareTime = DateUtil.format(dateTime, timeFormat);
            }
            
            
            final List<DataItem> dataItems = dataItemMap.get(currentTime);
            final List<DataItem> lastDataItems = lastDataItemMap.get(compareTime);
            BigDecimal sum = new BigDecimal(0);
            BigDecimal lastSum = new BigDecimal(0);
            if (CollectionUtils.isNotEmpty(dataItems)) {
                 // 求和
                 sum = BigDecimal.valueOf(dataItems.stream()
                        .mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            }
            
            if(CollectionUtils.isNotEmpty(lastDataItems)) {
                lastSum = BigDecimal.valueOf(lastDataItems.stream()
                        .mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            }
            data.setCurrentValue(sum.doubleValue());
            data.setCompareValue(lastSum.doubleValue());
            data.setCurrentTime(currentTime);
            data.setCompareTime(compareTime);
            

            if ("YOY".equals(analysisType)) {
                //同比分析
                BigDecimal ratio = BigDecimal.ZERO;
                if (lastSum.compareTo(BigDecimal.ZERO) != 0) {
                    ratio = sum.subtract(lastSum).divide(lastSum, CommonConst.DIGIT_2, RoundingMode.HALF_UP).multiply(multiple);
                }
                data.setRatio(ratio.doubleValue());
            } else if ("QOQ".equals(analysisType)) {
                //环比分析

                BigDecimal ratio = BigDecimal.ZERO;
                if (lastSum.compareTo(BigDecimal.ZERO) != 0) {
                    ratio = sum.subtract(lastSum).divide(lastSum, CommonConst.DIGIT_2, RoundingMode.HALF_UP).multiply(multiple);
                }
                data.setRatio(ratio.doubleValue());
            }

            dataList.add(data);

            switch (TimeType.valueOf(queryTimeType)) {
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

        dataList.stream().forEach(dataItem -> {
            ChartData chartData = new ChartData();
            chartData.setXData(dataItem.getCurrentTime());
            chartData.setYQOQ(dataItem.getRatio());
            chartData.setYCompareValue(dataItem.getCompareValue());
            chartData.setYValue(dataItem.getCurrentValue());

            chartDataList.add(chartData);

        });
        
        consumptionAnalysisVO.setDataList(dataList);
        consumptionAnalysisVO.setChartDataList(chartDataList);
        return consumptionAnalysisVO;
    }

    @Override
    public List<RankingDataVO> getByDepartment(ConsumptionAnalysisDTO dto) {
        List<RankingDataVO> rankingDataVOArrayList = new ArrayList<>();
        List<RankingEnergyData> rankingEnergyData = new ArrayList<>();
        final Date queryTime = dto.getDataTime();
        String queryTimeType = dto.getTimeType();
        // 创建一个Map来存储总和
        Map<String, BigDecimal> result = new HashMap<>();
        
        
        //根据模型名称查询modelCode
        final String parentId = dto.getNodeId();
        
        //根据总结点查询
        final List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.getModelNodeByParentId(parentId);

        final List<String> eneryIdList = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getEnergyId).distinct().collect(Collectors.toList());
        final LambdaQueryWrapper<SysEnergy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(eneryIdList),SysEnergy::getEnersno,eneryIdList);
        final List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(queryWrapper);
        final Map<String, String> energyNameMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getEnername));
        final Map<String, String> nodeNameMap = new HashMap<>();
        nodeIndexInforList.forEach(n->{
            final String nodeId = n.getNodeId();
            final String name = n.getName();
            if(!nodeNameMap.containsKey(nodeId)){
                nodeNameMap.put(nodeId,name);
            }
        });

        // 按照点位进行分组
        Map<String, List<ModelNodeIndexInfor>> nodeIndexMap = nodeIndexInforList.stream().collect(
                Collectors.groupingBy(ModelNodeIndexInfor::getNodeId));
        
        // 根据nodeId获取能源类型
        // 所有点位信息
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).distinct().collect(Collectors.toList());

        Date beginTime;
        Date endTime;
        String shixuTimeType;
        if (TimeType.DAY.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            shixuTimeType = TimeType.HOUR.name();
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
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
        final Map<String, List<DataItem>> dataItemMap = dataItemList.stream().collect(Collectors.groupingBy(DataItem::getIndexId));
        
        // 根据点位分组，求和
        Map<String,BigDecimal> dataItemTotalMap = new HashMap<>();
        dataItemMap.forEach((key,value)->{
            BigDecimal sum = BigDecimal.valueOf(value.stream()
                    .mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
            dataItemTotalMap.put(key,sum);
        });

        
        
        nodeIndexMap.forEach((nodeId,indexList)->{
            indexList.forEach(index->{
                final String energyId = index.getEnergyId();
                final String indexId = index.getIndexId();
                final BigDecimal total = dataItemTotalMap.getOrDefault(indexId, BigDecimal.ZERO);
                String key = nodeId + "@@" + energyId; // 使用nodeId和energyId作为键
                result.put(key, result.getOrDefault(key, BigDecimal.ZERO).add(total)); // 累加能耗量
            });
        });

        // 打印结果
        for (Map.Entry<String, BigDecimal> entry : result.entrySet()) {
            RankingEnergyData chartData = new RankingEnergyData();
            String[] keys = entry.getKey().split("@@");
            String nodeId = keys[0];
            String energyId = keys[1];
            BigDecimal totalEnergy = entry.getValue();
            chartData.setEnergyTypeNo(energyId);
            chartData.setEnergyTypeName(energyNameMap.getOrDefault(energyId,""));
            chartData.setNodeName(nodeNameMap.getOrDefault(nodeId,""));
            chartData.setEnergyConsumption(totalEnergy.doubleValue());
            chartData.setNodeId(nodeId);
            rankingEnergyData.add(chartData);
            
        }
        final Map<String, List<RankingEnergyData>> collect = rankingEnergyData.stream().collect(Collectors.groupingBy(RankingEnergyData::getNodeId));
        nodeNameMap.forEach((key,value)->{
            final List<RankingEnergyData> rankingEnergyData1 = collect.get(key);
            RankingDataVO rankingDataVO = new RankingDataVO();
            rankingDataVO.setNodeId(key);
            rankingDataVO.setNodeName(value);
            rankingDataVO.setData(rankingEnergyData1);
            rankingDataVOArrayList.add(rankingDataVO);
        });
        
        return rankingDataVOArrayList;
    }

    /**
     * 查询点位与用能单元信息
     *
     * @param nodeId
     * @return
     */
    private List<ModelNodeIndexInfor> listModelNodeIndexIdRelationInfor(String nodeId) {
        List<ModelNodeIndexInfor> nodeInforList = modelNodeService.listModelNodeIndexIdRelationInforByParentId(nodeId);
        // 如果是空存在两种情况，1：id有问题，2：最底层
        if (CollectionUtils.isEmpty(nodeInforList)) {
            List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(nodeId);
            if (CollectionUtils.isNotEmpty(inforList)) {
                nodeInforList.addAll(inforList);
            }
        }
        return nodeInforList;
    }

    @Override
    public ConsumptionAnalysisVO getComprehensiveEnergy(ConsumptionAnalysisDTO dto) {
        List<ConsumptionAnalysisData> dataList = new ArrayList<>();
        List<ChartData> chartDataList = new ArrayList<>();
        List<EnergyProportion> energyProportionList = new ArrayList<>();

        ConsumptionAnalysisVO consumptionAnalysisVO = new ConsumptionAnalysisVO();
        
        //todo hmj 综合能耗先默认取同比
        final String analysisType = "YOY";
        final String nodeId = dto.getNodeId();
        final String energyType = dto.getEnergyType();
        final Date queryTime = dto.getDataTime();

        /**
         * 查询点位与用能单元信息
         */
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.getModelNodeIndexIdByNodeId(nodeId,energyType);

//        if (CollectionUtils.isEmpty(nodeIndexInforList)) {
//            return consumptionAnalysisVO;
//        }

        final List<String> eneryIdList = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getEnergyId).distinct().collect(Collectors.toList());
        final LambdaQueryWrapper<SysEnergy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(eneryIdList),SysEnergy::getEnersno,eneryIdList);
        final List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(queryWrapper);
        final Map<String, BigDecimal> energyCoefficientMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getCoefficient));
        final Map<String, String> energyNameMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno,SysEnergy::getEnername));
        final Map<String, String> indexIdEnergyIdMap = new HashMap<>();
        nodeIndexInforList.forEach(n->{
            final String indexId = n.getIndexId();
            final String energyId = n.getEnergyId();
            if(!indexIdEnergyIdMap.containsKey(indexId)){
                indexIdEnergyIdMap.put(indexId,energyId);
            }
        });
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        Date beginTime;
        Date endTime;
        Date lastTime;
        Date lastEndTime;
        String queryTimeType = dto.getTimeType();
        String shixuTimeType;
        String timeFormat;
        if (TimeType.DAY.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            shixuTimeType = TimeType.HOUR.name();
            if ("YOY".equals(analysisType)) {
                //同比分析，时间取去年的
                lastTime = DateUtil.offsetMonth(beginTime, -12);
                lastEndTime = DateUtil.offsetMonth(endTime, -12);
            }else {
                //环比分析，时间取 昨天
                lastTime = DateUtil.offsetDay(beginTime, -1);
                lastEndTime = DateUtil.offsetDay(endTime, -1);
            }
            timeFormat = "yyyy-MM-dd HH";
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            shixuTimeType = TimeType.DAY.name();
            if ("YOY".equals(analysisType)) {
                //同比分析，时间取去年的
                lastTime = DateUtil.offsetMonth(beginTime, -12);
                lastEndTime = DateUtil.offsetMonth(endTime, -12);
            }else {
                //环比分析，时间取 昨天
                lastTime = DateUtil.offsetMonth(beginTime, -1);
                lastEndTime = DateUtil.offsetMonth(endTime, -1);
            }
            timeFormat = "yyyy-MM-dd";
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(queryTime);
            shixuTimeType = TimeType.MONTH.name();
            if ("YOY".equals(analysisType)) {
                //同比分析，时间取去年的
                lastTime = DateUtil.offsetMonth(beginTime, -12);
                lastEndTime = DateUtil.offsetMonth(endTime, -12);
            }else {
                //环比分析，时间取 昨天
                lastTime = DateUtil.offsetMonth(beginTime,  -1);
                lastEndTime = DateUtil.offsetMonth(endTime,  -1);
            }

            timeFormat = "yyyy-MM";
        }
        // 根据indexId查询dataItem
        List<DataItem> dataItemList = new ArrayList<>();
        List<DataItem> lastDataItemList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(indexIds)) {
            dataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, shixuTimeType, indexIds);
            lastDataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(lastTime, lastEndTime, shixuTimeType, indexIds);
        }
        Map<String, List<DataItem>> dataItemMap = dataItemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), timeFormat)));
        Map<String, List<DataItem>> lastDataItemMap = lastDataItemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), timeFormat)));
        //  倍率
        BigDecimal multiple = BigDecimal.valueOf(CommonConst.DIGIT_100);

        Map<String,Double> energyProportionMap = new HashMap<>();
        while (!beginTime.after(endTime)) {
            ConsumptionAnalysisData data = new ConsumptionAnalysisData();
            final String currentTime = DateUtil.format(beginTime, timeFormat);
            final String compareTime;
            DateTime dateTime;
            if (TimeType.DAY.name().equals(queryTimeType)) {

                if ("YOY".equals(analysisType)) {
                    //同比分析，时间取去年的
                    dateTime = DateUtil.offsetMonth(beginTime, -12);
                }else {
                    //环比分析，时间取 昨天
                    dateTime = DateUtil.offsetDay(beginTime, -1);
                }
                compareTime = DateUtil.format(dateTime, timeFormat);
                // 月
            } else if (TimeType.MONTH.name().equals(queryTimeType)) {
                if ("YOY".equals(analysisType)) {
                    //同比分析，时间取去年的
                    dateTime = DateUtil.offsetMonth(beginTime, -12);
                }else {
                    //环比分析，时间取 昨天
                    dateTime = DateUtil.offsetMonth(beginTime, -1);
                }
                compareTime = DateUtil.format(dateTime, timeFormat);
                // 年
            } else {
                if ("YOY".equals(analysisType)) {
                    //同比分析，时间取去年的
                    dateTime = DateUtil.offsetMonth(beginTime, -12);
                }else {
                    //环比分析，时间取 昨天
                    dateTime = DateUtil.offsetMonth(beginTime,  -1);
                }
                compareTime = DateUtil.format(dateTime, timeFormat);
            }


            final List<DataItem> dataItems = dataItemMap.get(currentTime);
            final List<DataItem> lastDataItems = lastDataItemMap.get(compareTime);
            BigDecimal sum = new BigDecimal(0);
            BigDecimal lastSum = new BigDecimal(0);
            if (CollectionUtils.isNotEmpty(dataItems)) {
                // 求和
                for (int i=0;i<dataItems.size(); i++){
                    final DataItem dataItem = dataItems.get(i);
                    final String indexId = dataItem.getIndexId();
                    final String energyId = indexIdEnergyIdMap.get(indexId);
                    
                    final BigDecimal coefficient = energyCoefficientMap.get(energyId);
                    if(coefficient == null){
                        throw new RuntimeException("能源类型" + energyId + "没有配置折标系数，无法计算");
                    }
                    
                    sum = sum.add(new BigDecimal(dataItem.getValue()).multiply(coefficient)).setScale(2, RoundingMode.HALF_UP);;
//                    if(energyProportionMap.containsKey(energyId)) {
//                        energyProportionMap.put(energyId,energyProportionMap.get(energyId) + sum.doubleValue());
//                    }else {
//                        energyProportionMap.put(energyId, sum.doubleValue());
//                    }
                }
                
            }

            if(CollectionUtils.isNotEmpty(lastDataItems)) {
                for (int i=0;i<lastDataItems.size(); i++){
                    final DataItem dataItem = lastDataItems.get(i);
                    final String indexId = dataItem.getIndexId();
                    final String energyId = indexIdEnergyIdMap.get(indexId);

                    final BigDecimal coefficient = energyCoefficientMap.get(energyId);
                    if(coefficient == null){
                        throw new RuntimeException("能源类型" + energyId + "没有配置折标系数，无法计算");
                    }

                    lastSum = lastSum.add(new BigDecimal(dataItem.getValue()).multiply(coefficient)).setScale(2, RoundingMode.HALF_UP);;
                }
            }
            data.setCurrentValue(sum.doubleValue());
            data.setCompareValue(lastSum.doubleValue());
            data.setCurrentTime(currentTime);
            data.setCompareTime(compareTime);


            if ("YOY".equals(analysisType)) {
                //同比分析
                BigDecimal ratio = BigDecimal.ZERO;
                if (lastSum.compareTo(BigDecimal.ZERO) != 0) {
                    ratio = sum.subtract(lastSum).divide(lastSum, CommonConst.DIGIT_2, RoundingMode.HALF_UP).multiply(multiple);
                }
                data.setRatio(ratio.doubleValue());
            } else if ("QOQ".equals(analysisType)) {
                //环比分析

                BigDecimal ratio = BigDecimal.ZERO;
                if (lastSum.compareTo(BigDecimal.ZERO) != 0) {
                    ratio = sum.subtract(lastSum).divide(lastSum, CommonConst.DIGIT_2, RoundingMode.HALF_UP).multiply(multiple);
                }
                data.setRatio(ratio.doubleValue());
            }

            dataList.add(data);

            switch (TimeType.valueOf(queryTimeType)) {
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

        dataList.stream().forEach(dataItem -> {
            ChartData chartData = new ChartData();
            chartData.setXData(dataItem.getCurrentTime());
            chartData.setYQOQ(dataItem.getRatio());
            chartData.setYCompareValue(dataItem.getCompareValue());
            chartData.setYValue(dataItem.getCurrentValue());

            chartDataList.add(chartData);

        });
        
        Double eneryTotal =  energyProportionMap.values().stream().mapToDouble(Double::doubleValue).sum();

        Map<String, List<DataItem>> indexDataItemMap = dataItemList.stream().collect(Collectors.groupingBy(DataItem::getIndexId));
        
        indexDataItemMap.forEach((indexId,value)->{
            final String energyId = indexIdEnergyIdMap.get(indexId);

            final BigDecimal coefficient = energyCoefficientMap.get(energyId);
            if(coefficient == null){
                throw new RuntimeException("能源类型" + energyId + "没有配置折标系数，无法计算");
            }

            final double sum = value.stream().map(DataItem::getValue).mapToDouble(Double::doubleValue).sum();
            if(energyProportionMap.containsKey(energyId)) {
                energyProportionMap.put(energyId,energyProportionMap.get(energyId) + sum * coefficient.doubleValue());
            }else {
                energyProportionMap.put(energyId, sum * coefficient.doubleValue());
            }
        });
       
        energyProportionMap.forEach((key,value)->{
            EnergyProportion energyProportion = new EnergyProportion();
            energyProportion.setEnergyNo(key);
            energyProportion.setEnergyName(energyNameMap.getOrDefault(key,""));
            energyProportion.setCount(value);
            energyProportion.setPercentage(value/eneryTotal * 100);
            
            energyProportionList.add(energyProportion);
        });

        consumptionAnalysisVO.setEnergyProportion(energyProportionList);
        consumptionAnalysisVO.setDataList(dataList);
        consumptionAnalysisVO.setChartDataList(chartDataList);
        return consumptionAnalysisVO;
    }

    @Override
    public ConsumptionAnalysisVO getYOY(ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = new ConsumptionAnalysisVO();
        ConsumptionAnalysisData tongbi = new ConsumptionAnalysisData();
        ConsumptionAnalysisData huanbi = new ConsumptionAnalysisData();
        final String queryTimeType = dto.getTimeType();
        final Date queryTime = dto.getDataTime();

        String timeFormat;
        if (TimeType.DAY.name().equals(queryTimeType)) {
            timeFormat = "yyyy-MM-dd";
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
            timeFormat = "yyyy-MM";
            // 年
        } else {
            timeFormat = "yyyy";
        }
        
        final Double currTotalEnergy = getTotalEnergy(dto);

        DateTime tongbiTime = DateUtil.offsetMonth(queryTime, -12);
        dto.setDataTime(tongbiTime);
        final Double tongbiTotalEnergy = getTotalEnergy(dto);
        
        DateTime huanbiTime = DateUtil.offsetMonth(queryTime, -1);
        dto.setDataTime(huanbiTime);
        final Double huanbiTotalEnergy = getTotalEnergy(dto);

        
        tongbi.setCurrentTime(DateUtil.format(queryTime,timeFormat));
        tongbi.setCompareTime(DateUtil.format(tongbiTime,timeFormat));
        tongbi.setCurrentValue(currTotalEnergy);
        tongbi.setCompareValue(tongbiTotalEnergy);
        if(tongbiTotalEnergy != 0) {
            final double tongbiRatio = (currTotalEnergy - tongbiTotalEnergy) / tongbiTotalEnergy * 100;
            BigDecimal bd = new BigDecimal(tongbiRatio);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            tongbi.setRatio(bd.doubleValue());
        }else {
            tongbi.setRatio(0);
        }
        
        huanbi.setCurrentTime(DateUtil.format(queryTime,timeFormat));
        huanbi.setCompareTime(DateUtil.format(huanbiTime,timeFormat));
        huanbi.setCompareValue(huanbiTotalEnergy);
        huanbi.setCurrentValue(currTotalEnergy);
     
        if(huanbiTotalEnergy != 0) {
            final double huanbiRatio = (currTotalEnergy - huanbiTotalEnergy) / huanbiTotalEnergy * 100;
            BigDecimal bd = new BigDecimal(huanbiRatio);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            huanbi.setRatio(bd.doubleValue());
        }else {
            huanbi.setRatio(0);
        }
        consumptionAnalysisVO.setTongbi(tongbi);
        consumptionAnalysisVO.setHuanbi(huanbi);
        return consumptionAnalysisVO;
    }
    
    public Double getTotalEnergy(ConsumptionAnalysisDTO dto){
        //todo hmj 综合能耗先默认取同比
        final String nodeId = dto.getNodeId();
        final String energyType = dto.getEnergyType();
        final Date queryTime = dto.getDataTime();

        /**
         * 查询点位与用能单元信息
         */
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.getModelNodeIndexIdByNodeId(nodeId,energyType);

//        if (CollectionUtils.isEmpty(nodeIndexInforList)) {
//            return consumptionAnalysisVO;
//        }

        final List<String> eneryIdList = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getEnergyId).distinct().collect(Collectors.toList());
        final LambdaQueryWrapper<SysEnergy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(eneryIdList),SysEnergy::getEnersno,eneryIdList);
        final List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(queryWrapper);
        final Map<String, BigDecimal> energyCoefficientMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getCoefficient));
        final Map<String, String> indexIdEnergyIdMap = new HashMap<>();
        nodeIndexInforList.forEach(n->{
            final String indexId = n.getIndexId();
            final String energyId = n.getEnergyId();
            if(!indexIdEnergyIdMap.containsKey(indexId)){
                indexIdEnergyIdMap.put(indexId,energyId);
            }
        });
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        Date beginTime;
        Date endTime;
        String queryTimeType = dto.getTimeType();
        String shixuTimeType;
        String timeFormat;
        if (TimeType.DAY.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            shixuTimeType = TimeType.HOUR.name();
            timeFormat = "yyyy-MM-dd HH";
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            shixuTimeType = TimeType.DAY.name();
            timeFormat = "yyyy-MM-dd";
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(queryTime);
            shixuTimeType = TimeType.MONTH.name();
            timeFormat = "yyyy-MM";
        }
        // 根据indexId查询dataItem
        List<DataItem> dataItemList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(indexIds)) {
            dataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, shixuTimeType, indexIds);
        }
        Map<String, List<DataItem>> dataItemMap = dataItemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), timeFormat)));

        Map<String,Double> energyProportionMap = new HashMap<>();
        while (!beginTime.after(endTime)) {
            final String currentTime = DateUtil.format(beginTime, timeFormat);

            final List<DataItem> dataItems = dataItemMap.get(currentTime);
            BigDecimal sum = new BigDecimal(0);
            if (CollectionUtils.isNotEmpty(dataItems)) {
                // 求和
                for (int i=0;i<dataItems.size(); i++){
                    final DataItem dataItem = dataItems.get(i);
                    final String indexId = dataItem.getIndexId();
                    final String energyId = indexIdEnergyIdMap.get(indexId);

                    final BigDecimal coefficient = energyCoefficientMap.get(energyId);
                    if(coefficient == null){
                        throw new RuntimeException("能源类型" + energyId + "没有配置折标系数，无法计算");
                    }

                    sum = sum.add(new BigDecimal(dataItem.getValue()).multiply(coefficient)).setScale(2, RoundingMode.HALF_UP);;
//                    if(energyProportionMap.containsKey(energyId)) {
//                        energyProportionMap.put(energyId,energyProportionMap.get(energyId) + sum.doubleValue());
//                    }else {
//                        energyProportionMap.put(energyId, sum.doubleValue());
//                    }
                }

            }

            switch (TimeType.valueOf(queryTimeType)) {
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



        
        Map<String, List<DataItem>> indexDataItemMap = dataItemList.stream().collect(Collectors.groupingBy(DataItem::getIndexId));

        indexDataItemMap.forEach((indexId,value)->{
            final String energyId = indexIdEnergyIdMap.get(indexId);

            final BigDecimal coefficient = energyCoefficientMap.get(energyId);
            if(coefficient == null){
                throw new RuntimeException("能源类型" + energyId + "没有配置折标系数，无法计算");
            }

            final double sum = value.stream().map(DataItem::getValue).mapToDouble(Double::doubleValue).sum();
            if(energyProportionMap.containsKey(energyId)) {
                energyProportionMap.put(energyId,energyProportionMap.get(energyId) + sum * coefficient.doubleValue());
            }else {
                energyProportionMap.put(energyId, sum * coefficient.doubleValue());
            }
        });
        Double eneryTotal =  energyProportionMap.values().stream().mapToDouble(Double::doubleValue).sum();
        return eneryTotal.doubleValue();
    }

    @Override
    public List<RankingEnergyData> getEnergyRanking(ConsumptionAnalysisDTO dto) {
        List<RankingEnergyData> energyDataList = new ArrayList<>();
        //todo hmj 综合能耗先默认取同比
        final String nodeId = dto.getNodeId();
        final Date queryTime = dto.getDataTime();

        /**
         * 查询点位与用能单元信息
         */
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.getModelNodeByParentId(nodeId);
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
        final Map<String, BigDecimal> energyCoefficientMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getCoefficient));
        //index和能源
        final Map<String, String> indexIdEnergyIdMap = new HashMap<>();
        nodeIndexInforList.forEach(n->{
            final String indexId = n.getIndexId();
            final String energyId = n.getEnergyId();
            if(!indexIdEnergyIdMap.containsKey(indexId)){
                indexIdEnergyIdMap.put(indexId,energyId);
            }
        });
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        Date beginTime;
        Date endTime;
        String queryTimeType = dto.getTimeType();
        String shixuTimeType;
        if (TimeType.DAY.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            shixuTimeType = TimeType.HOUR.name();
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
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
                final BigDecimal coefficient = energyCoefficientMap.get(energyId);
                
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

    @Override
    public ConsumptionAnalysisVO getPlanAndProdCount(ConsumptionAnalysisDTO dto) {
        ConsumptionAnalysisVO consumptionAnalysisVO = new ConsumptionAnalysisVO();
        final Date queryTime = dto.getDataTime();
        String queryTimeType = dto.getTimeType();

        Date beginTime;
        Date endTime;
        long between;
        String timeFormat;
        if (TimeType.MONTH.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            between = DateUtil.between(beginTime, endTime, DateUnit.DAY) + 1;
            timeFormat = "yyyy-MM";
            // 年
        } else {
            between = 12;
            timeFormat = "yyyy";
        }

        LambdaQueryWrapper<EnergyIndicators> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnergyIndicators::getTimeType,queryTimeType);
        queryWrapper.eq(EnergyIndicators::getDataTime,DateUtil.format(dto.getDataTime(),timeFormat));
        queryWrapper.eq(EnergyIndicators::getNodeId,dto.getNodeId());
        queryWrapper.eq(EnergyIndicators::getEnergyType,dto.getEnergyType());
        // 0 是计划量
        queryWrapper.eq(EnergyIndicators::getIndicatorsType,"0");
        final EnergyIndicators plan = energyIndicatorsMapper.selectOne(queryWrapper);
        if(null != plan && null!= plan.getNumber()) {
            consumptionAnalysisVO.setPlanCount(plan.getNumber().divide(new BigDecimal(between), CommonConst.DIGIT_2, RoundingMode.HALF_UP).doubleValue());
        }else {
            consumptionAnalysisVO.setPlanCount(0D);
        }
        queryWrapper.clear();


        queryWrapper.eq(EnergyIndicators::getTimeType,queryTimeType);
        queryWrapper.eq(EnergyIndicators::getDataTime,DateUtil.format(dto.getDataTime(),timeFormat));
        queryWrapper.eq(EnergyIndicators::getNodeId,dto.getNodeId());
        queryWrapper.eq(EnergyIndicators::getEnergyType,dto.getEnergyType());
        queryWrapper.eq(EnergyIndicators::getIndicatorsType,"1");
        final EnergyIndicators prod = energyIndicatorsMapper.selectOne(queryWrapper);
        if(null != prod && null!= prod.getNumber()) {
            consumptionAnalysisVO.setPlanCount(prod.getNumber().divide(new BigDecimal(between), CommonConst.DIGIT_2, RoundingMode.HALF_UP).doubleValue());
        }else {
            consumptionAnalysisVO.setProdCount(0D);
        }
        return consumptionAnalysisVO;
    }

    @Override
    public ProductEnergyAnalysisVO getProdEnergy(ConsumptionAnalysisDTO dto) {
        List<ProductEnergyAnalysisData> chart = new ArrayList<>();
        List<ConsumptionAnalysisData> dataList = new ArrayList<>();

        ProductEnergyAnalysisVO productEnergyAnalysisVO = new ProductEnergyAnalysisVO();

        final String nodeId = dto.getNodeId();
        final String energyType = dto.getEnergyType();
        final Date queryTime = dto.getDataTime();

        /**
         * 查询点位与用能单元信息
         */
        List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.getModelNodeIndexIdByNodeId(nodeId,energyType);

        final List<String> eneryIdList = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getEnergyId).distinct().collect(Collectors.toList());
        final LambdaQueryWrapper<SysEnergy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(CollectionUtils.isNotEmpty(eneryIdList),SysEnergy::getEnersno,eneryIdList);
        final List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(queryWrapper);
        final Map<String, BigDecimal> energyCoefficientMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, SysEnergy::getCoefficient));
        final Map<String, String> indexIdEnergyIdMap = new HashMap<>();
        nodeIndexInforList.forEach(n->{
            final String indexId = n.getIndexId();
            final String energyId = n.getEnergyId();
            if(!indexIdEnergyIdMap.containsKey(indexId)){
                indexIdEnergyIdMap.put(indexId,energyId);
            }
        });
        List<String> indexIds = nodeIndexInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
        Date beginTime;
        Date endTime;
        String queryTimeType = dto.getTimeType();
        String shixuTimeType;
        String timeFormat;
        if (TimeType.DAY.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            shixuTimeType = TimeType.HOUR.name();
            timeFormat = "yyyy-MM-dd HH";
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            shixuTimeType = TimeType.DAY.name();
            timeFormat = "yyyy-MM-dd";
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(queryTime);
            shixuTimeType = TimeType.MONTH.name();
            timeFormat = "yyyy-MM";
        }
        
        

        // 根据indexId查询dataItem
        List<DataItem> dataItemList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(indexIds)) {
            dataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, shixuTimeType, indexIds);
        }
        Map<String, List<DataItem>> dataItemMap = dataItemList.stream().collect(Collectors.groupingBy(li -> DateUtil.format(li.getDataTime(), timeFormat)));
        Map<String, List<ProductOutput>> prodCountMap = new HashMap<>();
        while (!beginTime.after(endTime)) {
            //查询开始时间和结束时间之间的产量
            LambdaQueryWrapper<ProductOutput> indicatorsWrapper = new LambdaQueryWrapper<>();
            indicatorsWrapper.eq(ProductOutput::getTimeType,shixuTimeType);
            indicatorsWrapper.eq(ProductOutput::getNodeId,dto.getNodeId());
            indicatorsWrapper.eq(ProductOutput::getDataTime,DateUtil.format(beginTime,timeFormat));
            List<ProductOutput> energyIndicators = productOutputMapper.selectList(indicatorsWrapper);
            prodCountMap.put(DateUtil.format(beginTime,timeFormat),energyIndicators);
            
            ConsumptionAnalysisData data = new ConsumptionAnalysisData();
            final String currentTime = DateUtil.format(beginTime, timeFormat);

            final List<DataItem> dataItems = dataItemMap.get(currentTime);
            BigDecimal sum = new BigDecimal(0);
            if (CollectionUtils.isNotEmpty(dataItems)) {
                // 求和
                for (int i=0;i<dataItems.size(); i++){
                    final DataItem dataItem = dataItems.get(i);
                    final String indexId = dataItem.getIndexId();
                    final String energyId = indexIdEnergyIdMap.get(indexId);

                    final BigDecimal coefficient = energyCoefficientMap.get(energyId);
                    if(coefficient == null){
                        throw new RuntimeException("能源类型" + energyId + "没有配置折标系数，无法计算");
                    }

                    sum = sum.add(new BigDecimal(dataItem.getValue()).multiply(coefficient)).setScale(2, RoundingMode.HALF_UP);;
                }

            }

            data.setCurrentValue(sum.doubleValue());
            data.setCurrentTime(currentTime);

            dataList.add(data);

            switch (TimeType.valueOf(queryTimeType)) {
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
        dataList.forEach(d->{
            ProductEnergyAnalysisData productEnergyAnalysisData = new ProductEnergyAnalysisData();
            final String currentTime = d.getCurrentTime();
            
            productEnergyAnalysisData.setDateTime(currentTime);
            
            final List<ProductOutput> productOutputs = prodCountMap.get(currentTime);
            if(StringUtils.isEmpty(dto.getProdType())){
                productEnergyAnalysisData.setEnergyCount(format2Double(d.getCurrentValue()));
                final double sum = productOutputs.stream().map(ProductOutput::getNumber).mapToDouble(BigDecimal::doubleValue).sum();
                productEnergyAnalysisData.setProductCount(format2Double(sum));
                if(sum != 0) {
                    final double averageEnergy = productEnergyAnalysisData.getEnergyCount() / productEnergyAnalysisData.getProductCount();
                    // 创建DecimalFormat对象，设置保留两位小数
                    DecimalFormat df = new DecimalFormat("#.00");

                    // 格式化结果
                    String formattedResult = df.format(averageEnergy);
                    productEnergyAnalysisData.setAverage(Double.valueOf(formattedResult));
                }else {
                    productEnergyAnalysisData.setAverage(0);
                }
            }else {
                if(CollectionUtils.isNotEmpty(productOutputs)) {
                    final Map<String, List<ProductOutput>> productTypeMap = productOutputs.stream().collect(Collectors.groupingBy(ProductOutput::getProductType));
                    final double sum = productOutputs.stream().map(ProductOutput::getNumber).mapToDouble(BigDecimal::doubleValue).sum();
                    final List<ProductOutput> outputList = productTypeMap.get(dto.getProdType());
                    if(CollectionUtils.isNotEmpty(outputList)) {
                        final double enengyProd = outputList.stream().map(ProductOutput::getNumber).mapToDouble(BigDecimal::doubleValue).sum();
                        productEnergyAnalysisData.setProductCount(format2Double(enengyProd));
                        productEnergyAnalysisData.setEnergyCount(format2Double(d.getCurrentValue() * enengyProd / sum));
                        if (enengyProd != 0) {
                            final double averageEnergy = productEnergyAnalysisData.getEnergyCount() / productEnergyAnalysisData.getProductCount();
                            double formattedResult = format2Double(averageEnergy);
                            productEnergyAnalysisData.setAverage(formattedResult);
                        } else {
                            productEnergyAnalysisData.setAverage(0);
                        }
                    }else {
                        productEnergyAnalysisData.setProductCount(0);
                        productEnergyAnalysisData.setEnergyCount(d.getCurrentValue());
                        productEnergyAnalysisData.setAverage(0);
                    }
                }else {
                    productEnergyAnalysisData.setProductCount(0);
                    productEnergyAnalysisData.setEnergyCount(d.getCurrentValue());
                    productEnergyAnalysisData.setAverage(0);
                }
            }
            
           
            chart.add(productEnergyAnalysisData);
        });

        productEnergyAnalysisVO.setChart(chart);
        final double totalEnergy = chart.stream().map(ProductEnergyAnalysisData::getEnergyCount).mapToDouble(Double::doubleValue).sum();
        final double totalProd = chart.stream().map(ProductEnergyAnalysisData::getProductCount).mapToDouble(Double::doubleValue).sum();
        productEnergyAnalysisVO.setTotalEnergy(format2Double(totalEnergy));
        productEnergyAnalysisVO.setTotalProduct(format2Double(totalProd));
        if(totalProd != 0) {
            productEnergyAnalysisVO.setAverageEnergy(format2Double(totalEnergy / totalProd));
        }else {
            productEnergyAnalysisVO.setAverageEnergy(0D);
        }
        return productEnergyAnalysisVO;
    }

    private double format2Double(double averageEnergy) {
        // 创建DecimalFormat对象，设置保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");

        // 格式化结果
        String formattedResult = df.format(averageEnergy);
        return Double.valueOf(formattedResult);
    }
}
