package com.zhitan.peakvalley.service.impl;


import cn.hutool.core.date.DateUtil;
import com.zhitan.common.enums.ElectricityTypeEnum;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.costmanagement.domain.vo.CostPriceRelevancyVo;
import com.zhitan.costmanagement.mapper.CostPriceRelevancyMapper;
import com.zhitan.model.domain.vo.ModelNodeIndexInfor;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.peakvalley.domain.ElectricityDataItem;
import com.zhitan.peakvalley.domain.dto.ElectricityDataItemListDTO;
import com.zhitan.peakvalley.domain.dto.PeakValleyDTO;
import com.zhitan.peakvalley.domain.vo.peakvalley.*;
import com.zhitan.peakvalley.mapper.PeakValleyMapper;
import com.zhitan.peakvalley.service.IPeakValleyService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 尖峰平谷数据Service业务层处理
 *
 * @author sys
 * @date 2024-08-27
 */
@Service
public class PeakValleyServiceImpl implements IPeakValleyService {
    @Resource
    private ModelNodeMapper modelNodeMapper;
    @Resource
    private PeakValleyMapper electricityDataItemMapper;
    @Resource
    CostPriceRelevancyMapper costPriceRelevancyMapper;

    /**
     * 查询统计数据
     *
     * @param dto 请求参数
     * @return 结果
     */
    @Override
    public List<PeakValleyHourDataVO> getDataStatistics(ElectricityDataItemListDTO dto) {
        List<PeakValleyHourDataVO> reportVOList = new ArrayList<>();
        // 查询时间范围
        Date startTime = DateUtil.beginOfMonth(dto.getQueryTime());
        Date endTime = DateUtil.endOfMonth(startTime);
        String timeType = dto.getTimeType();

        Map<String, List<ElectricityDataItem>> electricityDataMap = new HashMap<>();
        // 查询点位信息
        List<ModelNodeIndexInfor> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), dto.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, startTime, endTime, timeType);

            electricityDataMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
        }
        while (!startTime.after(endTime)) {
            String mapKey = DateUtil.formatDateTime(startTime);
            List<ElectricityDataItem> dataItemList = electricityDataMap.get(mapKey);

            BigDecimal sharpFee = BigDecimal.ZERO;
            BigDecimal sharpPower = BigDecimal.ZERO;
            BigDecimal peakFee = BigDecimal.ZERO;
            BigDecimal peakPower = BigDecimal.ZERO;
            BigDecimal flatFee = BigDecimal.ZERO;
            BigDecimal flatPower = BigDecimal.ZERO;
            BigDecimal valleyFee = BigDecimal.ZERO;
            BigDecimal valleyPower = BigDecimal.ZERO;

            if (CollectionUtils.isNotEmpty(dataItemList)) {
                for (ElectricityDataItem electricityDataItem : dataItemList) {
                    String electricityType = electricityDataItem.getElectricityType();

                    if (ElectricityTypeEnum.SHARP.name().equals(electricityType)) {
                        sharpFee = sharpFee.add(electricityDataItem.getCost());
                        sharpPower = sharpPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.PEAK.name().equals(electricityType)) {
                        peakFee = peakFee.add(electricityDataItem.getCost());
                        peakPower = peakPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.FLAT.name().equals(electricityType)) {
                        flatFee = flatFee.add(electricityDataItem.getCost());
                        flatPower = flatPower.add(electricityDataItem.getElectricity());
                    } else {
                        valleyFee = valleyFee.add(electricityDataItem.getCost());
                        valleyPower = valleyPower.add(electricityDataItem.getElectricity());
                    }
                }
            }
            PeakValleyHourDataVO peakAndValleyReportVO = new PeakValleyHourDataVO(startTime, sharpFee, sharpPower,
                    peakFee, peakPower, flatFee, flatPower, valleyFee, valleyPower);

            reportVOList.add(peakAndValleyReportVO);
            switch (TimeType.valueOf(timeType)) {
                case HOUR:
                    startTime = DateUtil.offsetHour(startTime, 1);
                    break;
                case DAY:
                    startTime = DateUtil.offsetDay(startTime, 1);
                    break;
                case MONTH:
                    startTime = DateUtil.offsetMonth(startTime, 1);
                    break;
                default:
                    startTime = DateUtil.offsetMonth(startTime, 12);
                    break;
            }
        }

        return reportVOList;
    }


    @Override
    public PeakValleyDayVO segmentAnalysisDay(PeakValleyDTO dto) {
        PeakValleyDayVO peakValleyVO = new PeakValleyDayVO();
        List<PeakValleyDayDataVO> reportVOList = new ArrayList<>();
        // 查询时间范围
        Date startTime = DateUtil.beginOfMonth(dto.getQueryTime());
        Date endTime = DateUtil.endOfMonth(startTime);
        String timeType = dto.getTimeType();

        Map<String, List<ElectricityDataItem>> electricityDataMap = new HashMap<>();
        // 查询点位信息
        List<ModelNodeIndexInfor> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), dto.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, startTime, endTime, timeType);

            electricityDataMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
        }
        while (!startTime.after(endTime)) {
            String mapKey = DateUtil.formatDateTime(startTime);
            List<ElectricityDataItem> dataItemList = electricityDataMap.get(mapKey);

            BigDecimal sharpFee = BigDecimal.ZERO;
            BigDecimal sharpPower = BigDecimal.ZERO;
            BigDecimal peakFee = BigDecimal.ZERO;
            BigDecimal peakPower = BigDecimal.ZERO;
            BigDecimal flatFee = BigDecimal.ZERO;
            BigDecimal flatPower = BigDecimal.ZERO;
            BigDecimal valleyFee = BigDecimal.ZERO;
            BigDecimal valleyPower = BigDecimal.ZERO;

            if (CollectionUtils.isNotEmpty(dataItemList)) {
                for (ElectricityDataItem electricityDataItem : dataItemList) {
                    String electricityType = electricityDataItem.getElectricityType();

                    if (ElectricityTypeEnum.SHARP.name().equals(electricityType)) {
//                        sharpFee = sharpFee.add(electricityDataItem.getCost());

                        sharpPower = sharpPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.PEAK.name().equals(electricityType)) {
//                        peakFee = peakFee.add(electricityDataItem.getCost());
                        peakPower = peakPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.FLAT.name().equals(electricityType)) {
//                        flatFee = flatFee.add(electricityDataItem.getCost());
                        flatPower = flatPower.add(electricityDataItem.getElectricity());
                    } else {
//                        valleyFee = valleyFee.add(electricityDataItem.getCost());
                        valleyPower = valleyPower.add(electricityDataItem.getElectricity());
                    }
                }
                //2024-11-12新增
                CostPriceRelevancyVo voS =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(),ElectricityTypeEnum.SHARP.name());
                if(voS!=null){
                    sharpFee = voS.getPrice().multiply(sharpPower);
                }
                CostPriceRelevancyVo voP =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(),ElectricityTypeEnum.PEAK.name());
                if(voP!=null){
                    peakFee = voP.getPrice().multiply(peakPower);
                }
                CostPriceRelevancyVo voF =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(),ElectricityTypeEnum.FLAT.name());
                if(voF!=null){
                    flatFee = voF.getPrice().multiply(flatPower);
                }
                CostPriceRelevancyVo voV =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(), ElectricityTypeEnum.VALLEY.name());
                if(voV!=null){
                    valleyFee = voV.getPrice().multiply(valleyPower);
                }
//                CostPriceRelevancyVo voD =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(), ElectricityTypeEnum.DEEP.name());
//                if(voV!=null){
//                    DeepFee = voV.getPrice().multiply(valleyPower);
//                }




            }
            PeakValleyDayDataVO peakAndValleyReportVO = new PeakValleyDayDataVO(startTime, sharpFee, sharpPower,
                    peakFee, peakPower, flatFee, flatPower, valleyFee, valleyPower);

            reportVOList.add(peakAndValleyReportVO);
            switch (TimeType.valueOf(timeType)) {
                case HOUR:
                    startTime = DateUtil.offsetHour(startTime, 1);
                    break;
                case DAY:
                    startTime = DateUtil.offsetDay(startTime, 1);
                    break;
                case MONTH:
                    startTime = DateUtil.offsetMonth(startTime, 1);
                    break;
                default:
                    startTime = DateUtil.offsetMonth(startTime, 12);
                    break;
            }
        }
        List<PeakValleyLineChatVO> costList = new ArrayList<>();
        List<PeakValleyLineChatVO> powerConsumptionList = new ArrayList<>();
        PeakValleyDayTotalVO peakValleyDayTotalVO = new PeakValleyDayTotalVO();

        AtomicReference<BigDecimal> flatFreeCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> tipFreeCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> troughFreeCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> peakFreeCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> flatCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> tipCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> troughCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> peakCount = new AtomicReference<>(BigDecimal.ZERO);

        reportVOList.stream().forEach(r->{
            PeakValleyLineChatVO costVO = new PeakValleyLineChatVO();
            PeakValleyLineChatVO powerConsumptionVO = new PeakValleyLineChatVO();

            /**
             * 用电量
             */
            final BigDecimal peakPower = r.getPeakPower();
            final BigDecimal valleyPower = r.getValleyPower();
            final BigDecimal sharpPower = r.getSharpPower();
            final BigDecimal flatPower = r.getFlatPower();
            powerConsumptionVO.setXdata(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,r.getTime()));
            powerConsumptionVO.setYtip(sharpPower);
            powerConsumptionVO.setYpeak(peakPower);
            powerConsumptionVO.setYtrough(valleyPower);
            powerConsumptionVO.setYflat(flatPower);
            powerConsumptionList.add(powerConsumptionVO);

            /**
             * 用电费用
             */
            final BigDecimal peakFee = r.getPeakFee();
            final BigDecimal valleyFee = r.getValleyFee();
            final BigDecimal sharpFee = r.getSharpFee();
            final BigDecimal flatFee = r.getFlatFee();
            costVO.setXdata(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,r.getTime()));
            costVO.setYtip(sharpFee);
            costVO.setYpeak(peakFee);
            costVO.setYtrough(valleyFee);
            costVO.setYflat(flatFee);
            costList.add(costVO);

            flatCount.set(flatCount.get().add(r.getFlatPower()));
            tipCount.set(tipCount.get().add(r.getSharpPower()));
            troughCount.set(troughCount.get().add(r.getValleyPower()));
            peakCount.set(peakCount.get().add(r.getPeakPower()));

            flatFreeCount.set(flatFreeCount.get().add(r.getFlatFee()));
            tipFreeCount.set(tipFreeCount.get().add(r.getSharpFee()));
            troughFreeCount.set(troughFreeCount.get().add(r.getValleyFee()));
            peakFreeCount.set(peakFreeCount.get().add(r.getPeakFee()));
        });

        peakValleyDayTotalVO.setPeakPowerCost(peakFreeCount.get().doubleValue());
        peakValleyDayTotalVO.setPeakPowerConsumption(peakCount.get().doubleValue());
        peakValleyDayTotalVO.setFlatPowerCost(flatFreeCount.get().doubleValue());
        peakValleyDayTotalVO.setFlatPowerConsumption(flatCount.get().doubleValue());
        peakValleyDayTotalVO.setTipPowerCost(tipFreeCount.get().doubleValue());
        peakValleyDayTotalVO.setTipPowerConsumption(tipCount.get().doubleValue());
        peakValleyDayTotalVO.setTroughPowerCost(troughFreeCount.get().doubleValue());
        peakValleyDayTotalVO.setTroughPowerConsumption(troughCount.get().doubleValue());

        BigDecimal powerTotal = peakCount.get().add(tipCount.get()).add(troughCount.get()).add(flatCount.get());
        if(powerTotal.compareTo(BigDecimal.ZERO) > 0) {
            peakValleyDayTotalVO.setPeakPowerProportion(peakCount.get().divide(powerTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
            peakValleyDayTotalVO.setFlatPowerProportion(flatCount.get().divide(powerTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
            peakValleyDayTotalVO.setTipPowerProportion(tipCount.get().divide(powerTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
            peakValleyDayTotalVO.setTroughPowerProportion(troughCount.get().divide(powerTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
        }else {
            peakValleyDayTotalVO.setPeakPowerProportion(0);
            peakValleyDayTotalVO.setFlatPowerProportion(0);
            peakValleyDayTotalVO.setTipPowerProportion(0);
            peakValleyDayTotalVO.setTroughPowerProportion(0);
        }

        BigDecimal freeTotal = peakFreeCount.get().add(tipFreeCount.get()).add(troughFreeCount.get()).add(flatFreeCount.get());
        if(freeTotal.compareTo(BigDecimal.ZERO) > 0) {
            peakValleyDayTotalVO.setPeakPowerCostProportion(peakFreeCount.get().divide(freeTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
            peakValleyDayTotalVO.setFlatPowerCostProportion(flatFreeCount.get().divide(freeTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
            peakValleyDayTotalVO.setTipPowerCostProportion(tipFreeCount.get().divide(freeTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
            peakValleyDayTotalVO.setTroughPowerCostProportion(troughFreeCount.get().divide(freeTotal.multiply(new BigDecimal(0.01)), 2, RoundingMode.HALF_UP).doubleValue());
        }else {
            peakValleyDayTotalVO.setPeakPowerCostProportion(0);
            peakValleyDayTotalVO.setFlatPowerCostProportion(0);
            peakValleyDayTotalVO.setTipPowerCostProportion(0);
            peakValleyDayTotalVO.setTroughPowerCostProportion(0);
        }

//        peakValleyDayTotalVO.setTotalCost(powerTotal.doubleValue());
//        peakValleyDayTotalVO.setTotalPowerConsumption(freeTotal.doubleValue());
        peakValleyDayTotalVO.setTotalCost(freeTotal.doubleValue());
        peakValleyDayTotalVO.setTotalPowerConsumption(powerTotal.doubleValue());
        peakValleyVO.setTotalVO(peakValleyDayTotalVO);
        peakValleyVO.setCostList(costList);
        peakValleyVO.setPowerConsumptionList(powerConsumptionList);

        return peakValleyVO;
    }

    @Override
    public PeakValleyHourVO segmentAnalysisHour(PeakValleyDTO dto) {
        PeakValleyHourVO peakValleyVO = new PeakValleyHourVO();
        List<PeakValleyHourDataVO> reportVOList = new ArrayList<>();
        // 查询时间范围
        Date startTime = DateUtil.beginOfDay(dto.getQueryTime());
        Date endTime = DateUtil.endOfDay(startTime);
        String timeType = dto.getTimeType();

        Map<String, List<ElectricityDataItem>> electricityDataMap = new HashMap<>();
        // 查询点位信息
        List<ModelNodeIndexInfor> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), dto.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, startTime, endTime, timeType);

            electricityDataMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
        }
        while (!startTime.after(endTime)) {
            String mapKey = DateUtil.formatDateTime(startTime);
            List<ElectricityDataItem> dataItemList = electricityDataMap.get(mapKey);

            BigDecimal sharpFee = BigDecimal.ZERO;
            BigDecimal sharpPower = BigDecimal.ZERO;
            BigDecimal peakFee = BigDecimal.ZERO;
            BigDecimal peakPower = BigDecimal.ZERO;
            BigDecimal flatFee = BigDecimal.ZERO;
            BigDecimal flatPower = BigDecimal.ZERO;
            BigDecimal valleyFee = BigDecimal.ZERO;
            BigDecimal valleyPower = BigDecimal.ZERO;

            if (CollectionUtils.isNotEmpty(dataItemList)) {
                for (ElectricityDataItem electricityDataItem : dataItemList) {
                    String electricityType = electricityDataItem.getElectricityType();

                    if (ElectricityTypeEnum.SHARP.name().equals(electricityType)) {
//                        sharpFee = sharpFee.add(electricityDataItem.getCost());
                        sharpPower = sharpPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.PEAK.name().equals(electricityType)) {
//                        peakFee = peakFee.add(electricityDataItem.getCost());
                        peakPower = peakPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.FLAT.name().equals(electricityType)) {
//                        flatFee = flatFee.add(electricityDataItem.getCost());
                        flatPower = flatPower.add(electricityDataItem.getElectricity());
                    } else {
//                        valleyFee = valleyFee.add(electricityDataItem.getCost());
                        valleyPower = valleyPower.add(electricityDataItem.getElectricity());
                    }
                }
                //2024-11-12新增
                CostPriceRelevancyVo voS =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(),ElectricityTypeEnum.SHARP.name());
                if(voS!=null){
                    sharpFee = voS.getPrice().multiply(sharpPower);
                }
                CostPriceRelevancyVo voP =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(),ElectricityTypeEnum.PEAK.name());
                if(voP!=null){
                    peakFee = voP.getPrice().multiply(peakPower);
                }
                CostPriceRelevancyVo voF =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(),ElectricityTypeEnum.FLAT.name());
                if(voF!=null){
                    flatFee = voF.getPrice().multiply(flatPower);
                }
                CostPriceRelevancyVo voV =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(dto.getNodeId(), ElectricityTypeEnum.VALLEY.name());
                if(voV!=null){
                    valleyFee = voV.getPrice().multiply(valleyPower);
                }



            }
            PeakValleyHourDataVO peakAndValleyReportVO = new PeakValleyHourDataVO(startTime, sharpFee, sharpPower,
                    peakFee, peakPower, flatFee, flatPower, valleyFee, valleyPower);

            reportVOList.add(peakAndValleyReportVO);
            switch (TimeType.valueOf(timeType)) {
                case HOUR:
                    startTime = DateUtil.offsetHour(startTime, 1);
                    break;
                case DAY:
                    startTime = DateUtil.offsetDay(startTime, 1);
                    break;
                case MONTH:
                    startTime = DateUtil.offsetMonth(startTime, 1);
                    break;
                default:
                    startTime = DateUtil.offsetMonth(startTime, 12);
                    break;
            }
        }

        peakValleyVO.setDataList(reportVOList);
        List<PeakValleyLineChatVO> peakValleyLineChatVOS = new ArrayList<>();
        AtomicReference<BigDecimal> flatCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> tipCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> troughCount = new AtomicReference<>(BigDecimal.ZERO);
        AtomicReference<BigDecimal> peakCount = new AtomicReference<>(BigDecimal.ZERO);
        reportVOList.stream().forEach(r->{
            PeakValleyLineChatVO lineChatVO = new PeakValleyLineChatVO();
            lineChatVO.setXdata(DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,r.getTime()));
            lineChatVO.setYtip(r.getSharpPower());
            lineChatVO.setYflat(r.getFlatPower());
            lineChatVO.setYtrough(r.getValleyPower());
            lineChatVO.setYpeak(r.getPeakPower());
            peakValleyLineChatVOS.add(lineChatVO);
            BigDecimal flat  = flatCount.get().add(r.getFlatPower());
            flatCount.set(flat);
            BigDecimal tip  = tipCount.get().add(r.getSharpPower());
            tipCount.set(tip);
            BigDecimal trough  = troughCount.get().add(r.getValleyPower());
            troughCount.set(trough);
            BigDecimal peak  = peakCount.get().add(r.getPeakPower());
            peakCount.set(peak);

        });
        PeakValleyPieChatVO peakValleyPieChatVO = new PeakValleyPieChatVO();
        BigDecimal total = peakCount.get().add(tipCount.get()).add(troughCount.get()).add(flatCount.get()).multiply(new BigDecimal(0.01));
        if(total.compareTo(BigDecimal.ZERO) > 0) {
            peakValleyPieChatVO.setPeak(peakCount.get().divide(total, 2, RoundingMode.HALF_UP).toString());
            peakValleyPieChatVO.setFlat(flatCount.get().divide(total, 2, RoundingMode.HALF_UP).toString());
            peakValleyPieChatVO.setTip(tipCount.get().divide(total, 2, RoundingMode.HALF_UP).toString());
            peakValleyPieChatVO.setTrough(troughCount.get().divide(total, 2, RoundingMode.HALF_UP).toString());
        }else {
            peakValleyPieChatVO.setPeak("0");
            peakValleyPieChatVO.setFlat("0");
            peakValleyPieChatVO.setTip("0");
            peakValleyPieChatVO.setTrough("0");
        }
        peakValleyVO.setPieChat(peakValleyPieChatVO);
        peakValleyVO.setLineChat(peakValleyLineChatVOS);
        return peakValleyVO;
    }

    @Override
    public List<PeakValleyHourDataVO> segmentAnalysisHourExport(PeakValleyDTO dto) {
        List<PeakValleyHourDataVO> reportVOList = new ArrayList<>();
        // 查询时间范围
        Date startTime = DateUtil.beginOfDay(dto.getQueryTime());
        Date endTime = DateUtil.endOfDay(startTime);
        String timeType = dto.getTimeType();

        Map<String, List<ElectricityDataItem>> electricityDataMap = new HashMap<>();
        // 查询点位信息
        List<ModelNodeIndexInfor> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), dto.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, startTime, endTime, timeType);

            electricityDataMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
        }
        while (!startTime.after(endTime)) {
            String mapKey = DateUtil.formatDateTime(startTime);
            List<ElectricityDataItem> dataItemList = electricityDataMap.get(mapKey);

            BigDecimal sharpFee = BigDecimal.ZERO;
            BigDecimal sharpPower = BigDecimal.ZERO;
            BigDecimal peakFee = BigDecimal.ZERO;
            BigDecimal peakPower = BigDecimal.ZERO;
            BigDecimal flatFee = BigDecimal.ZERO;
            BigDecimal flatPower = BigDecimal.ZERO;
            BigDecimal valleyFee = BigDecimal.ZERO;
            BigDecimal valleyPower = BigDecimal.ZERO;

            if (CollectionUtils.isNotEmpty(dataItemList)) {
                for (ElectricityDataItem electricityDataItem : dataItemList) {
                    String electricityType = electricityDataItem.getElectricityType();

                    if (ElectricityTypeEnum.SHARP.name().equals(electricityType)) {
                        sharpFee = sharpFee.add(electricityDataItem.getCost());
                        sharpPower = sharpPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.PEAK.name().equals(electricityType)) {
                        peakFee = peakFee.add(electricityDataItem.getCost());
                        peakPower = peakPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.FLAT.name().equals(electricityType)) {
                        flatFee = flatFee.add(electricityDataItem.getCost());
                        flatPower = flatPower.add(electricityDataItem.getElectricity());
                    } else {
                        valleyFee = valleyFee.add(electricityDataItem.getCost());
                        valleyPower = valleyPower.add(electricityDataItem.getElectricity());
                    }
                }
            }
            PeakValleyHourDataVO peakAndValleyReportVO = new PeakValleyHourDataVO(startTime, sharpFee, sharpPower,
                    peakFee, peakPower, flatFee, flatPower, valleyFee, valleyPower);

            reportVOList.add(peakAndValleyReportVO);
            switch (TimeType.valueOf(timeType)) {
                case HOUR:
                    startTime = DateUtil.offsetHour(startTime, 1);
                    break;
                case DAY:
                    startTime = DateUtil.offsetDay(startTime, 1);
                    break;
                case MONTH:
                    startTime = DateUtil.offsetMonth(startTime, 1);
                    break;
                default:
                    startTime = DateUtil.offsetMonth(startTime, 12);
                    break;
            }
        }

       return reportVOList;
    }

    @Override
    public PeakValleyDayVO segmentAnalysisDayCustomize(PeakValleyDTO dto) {
        return null;
    }
}
