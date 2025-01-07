package com.zhitan.energydata.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.MeterImplementMapper;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.enums.ElectricityTypeEnum;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.bean.BeanUtils;
import com.zhitan.consumptionanalysis.domain.vo.RankingEnergyData;
import com.zhitan.costmanagement.domain.CostElectricityInput;
import com.zhitan.costmanagement.domain.vo.CostPriceRelevancyVo;
import com.zhitan.costmanagement.mapper.CostElectricityInputMapper;
import com.zhitan.costmanagement.mapper.CostPriceRelevancyMapper;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.energydata.mapper.EnergyDataStatisticMapper;
import com.zhitan.energydata.service.IEnergyDataStatisticService;
import com.zhitan.energydata.vo.EnergyChainYoyVO;
import com.zhitan.energydata.vo.FactoryEnergyConsumptionItemVo;
import com.zhitan.energydata.vo.FactoryEnergyConsumptionVo;
import com.zhitan.energydata.vo.PurchaseConsumptionVo;
import com.zhitan.home.domain.vo.HomeEnergyStatisticsVO;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.vo.ModelNodeIndexInfor;
import com.zhitan.model.mapper.EnergyIndexMapper;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.model.service.IEnergyIndexService;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.peakvalley.domain.ElectricityDataItem;
import com.zhitan.peakvalley.domain.dto.PeakValleyDTO;
import com.zhitan.peakvalley.domain.vo.peakvalley.*;
import com.zhitan.peakvalley.mapper.PeakValleyMapper;
import com.zhitan.realtimedata.domain.DataItem;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 恒邦综合治理大屏接口实现类
 */
@Service
public class EnergyDataStatisticServiceImpl implements IEnergyDataStatisticService {

  @Autowired
  private ModelNodeMapper modelNodeMapper;
  @Autowired
  private PeakValleyMapper electricityDataItemMapper;
  @Autowired
  CostPriceRelevancyMapper costPriceRelevancyMapper;

  @Autowired
  CostElectricityInputMapper electricityInputMapper;
  @Autowired
  private  IDataItemService dataItemService;

  @Autowired
  private SysEnergyMapper sysEnergyMapper;
  @Autowired
  private  IModelNodeService modelNodeService;
  @Autowired
  private  IEnergyIndexService energyIndexService;

  @Autowired
  private EnergyDataStatisticMapper statisticMapper;

  @Autowired
  private EnergyIndexMapper energyIndexMapper;

  @Autowired
  private MeterImplementMapper meterImplementMapper;

  @Override
  public List<EnergyChainYoyVO> energyConsumptionSummation(String timeType, String modelcode) {
    Date currentTime = new Date();
//        Date currentTime = DateUtil.parseDateTime("2023-04-11 00:00:00");

    DateTime tongbiTime = DateUtil.offsetMonth(currentTime, -12);
    DateTime huanbiTime = DateUtil.offsetMonth(currentTime, -1);
    final List<EnergyChainYoyVO> resultData = new ArrayList<>();
    final List<HomeEnergyStatisticsVO> current = getEnergyTotalByTime(timeType, modelcode, currentTime);
    final List<HomeEnergyStatisticsVO> tongbi = getEnergyTotalByTime(timeType, modelcode, tongbiTime);
    final List<HomeEnergyStatisticsVO> huanbi = getEnergyTotalByTime(timeType, modelcode, huanbiTime);

    final Map<String, List<HomeEnergyStatisticsVO>> tongbiMap = tongbi.stream().collect(Collectors.groupingBy(HomeEnergyStatisticsVO::getEnergyNo));
    final Map<String, List<HomeEnergyStatisticsVO>> huanbiMap = huanbi.stream().collect(Collectors.groupingBy(HomeEnergyStatisticsVO::getEnergyNo));

    current.stream().forEach(vo->{
      EnergyChainYoyVO chainYoyVO = new EnergyChainYoyVO();

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

      BeanUtils.copyProperties(vo, chainYoyVO);
      chainYoyVO.setSamePeriodCount(BigDecimal.valueOf(tongbiCount));
      chainYoyVO.setCount(BigDecimal.valueOf(vo.getCount()));
      chainYoyVO.setTongbi(BigDecimal.valueOf(vo.getTongbi()));
      chainYoyVO.setHuanbi(BigDecimal.valueOf(vo.getHuanbi()));
      chainYoyVO.setTonCount(BigDecimal.valueOf(vo.getTonCount()));
      resultData.add(chainYoyVO);
    });
    return resultData;
  }
  /**
   * 用电趋势分析--选择年
   * @param dto
   * @return
   */
  @Override
  public PeakValleyDayVO segmentAnalysisMonth(PeakValleyDTO dto) {
    PeakValleyDayVO peakValleyVO = new PeakValleyDayVO();
    List<PeakValleyDayDataVO> reportVOList = new ArrayList<>();
    //获取nodeId
    ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dto.getModelCode());
    dto.setNodeId(modelNode.getNodeId());
    // 查询时间范围
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DATE, 1);
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date endTime =calendar.getTime();
    calendar.add(Calendar.MONTH, -12);
    Date startTime = calendar.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
      PeakValleyDayDataVO peakAndValleyReportVO = new PeakValleyDayDataVO(startTime, sharpFee, sharpPower,
              peakFee, peakPower, flatFee, flatPower, valleyFee, valleyPower);

      reportVOList.add(peakAndValleyReportVO);

      startTime = DateUtil.offsetMonth(startTime, 1);
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

    peakValleyDayTotalVO.setTotalCost(powerTotal.doubleValue());
    peakValleyDayTotalVO.setTotalPowerConsumption(freeTotal.doubleValue());
    peakValleyVO.setTotalVO(peakValleyDayTotalVO);
    peakValleyVO.setCostList(costList);
    peakValleyVO.setPowerConsumptionList(powerConsumptionList);

    return peakValleyVO;
  }

  @Override
  public PeakValleyDayVO segmentAnalysisDay(PeakValleyDTO dto) {
    PeakValleyDayVO peakValleyVO = new PeakValleyDayVO();
    List<PeakValleyDayDataVO> reportVOList = new ArrayList<>();
    //获取nodeId
    ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dto.getModelCode());
    dto.setNodeId(modelNode.getNodeId());
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
    //获取nodeId
    ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dto.getModelCode());
    dto.setNodeId(modelNode.getNodeId());

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

  /**
   * 能耗科室排名前10
   * @param modelCode
   * @param timeType
   * @return
   */
  @Override
  public List<RankingEnergyData> energyConsumptionRanking(String modelCode, String timeType) {
    List<RankingEnergyData> energyDataList = new ArrayList<>();
    String nodeCategory = "2";
    LambdaQueryWrapper<ModelNode> modelNodeLambdaQueryWrapper = new LambdaQueryWrapper<>();
    modelNodeLambdaQueryWrapper.eq(ModelNode::getModelCode,modelCode);
    modelNodeLambdaQueryWrapper.eq(ModelNode::getNodeCategory,nodeCategory);
    List<ModelNode> modelNodeList = modelNodeMapper.selectList(modelNodeLambdaQueryWrapper);
    if(CollectionUtils.isEmpty(modelNodeList)){
      return energyDataList;
    }
    final List<String> nodeIds = modelNodeList.stream().map(ModelNode::getNodeId).collect(Collectors.toList());
    List<ModelNodeIndexInfor> nodeIndexInforList = modelNodeMapper.selectIndexByNodeIds(modelCode ,nodeIds);

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
      List<String> indexIdList = value.stream().map(ModelNodeIndexInfor::getIndexId).filter(Objects::nonNull).collect(Collectors.toList());
      if (null!=indexIdList &&!indexIdList.isEmpty()){
        indexIdList.forEach(indexId -> {
          final List<DataItem> dataItems = dataItemMap.get(indexId);
          final String energyId = indexIdEnergyIdMap.get(indexId);
          BigDecimal coefficient = (BigDecimal) energyCoefficientMap.get(energyId);

        if (coefficient == null && StringUtils.isBlank(energyId)) {
          EnergyIndex energyIndex = energyIndexMapper.selectEnergyIndexById(indexId);
          MeterImplement meterImplement = meterImplementMapper.selectMeterImplementById(energyIndex.getMeterId());
          coefficient = (BigDecimal) energyCoefficientMap.get(meterImplement.getEnergyType());
        }

          if (CollectionUtils.isNotEmpty(dataItems)) {
            BigDecimal sum = BigDecimal.valueOf(dataItems.stream()
                    .mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP).multiply(coefficient);

            if (resultMap.containsKey(key)) {
              resultMap.put(key, resultMap.get(key).add(sum));
            } else {
              resultMap.put(key, sum);
            }
          }
        });
      }
    });

    resultMap.forEach((key,value)->{
      RankingEnergyData rankingEnergyData = new RankingEnergyData();
      rankingEnergyData.setNodeId(key);
      rankingEnergyData.setNodeName(nodeNameMap.get(key));
      rankingEnergyData.setEnergyConsumption(value.doubleValue());
      energyDataList.add(rankingEnergyData);
    });
    Collections.sort(energyDataList, Comparator.comparingDouble((RankingEnergyData item) -> item.getEnergyConsumption()).reversed());
    // 获取前10条记录
    List<RankingEnergyData> top5Items = energyDataList.subList(0, Math.min(10, energyDataList.size()));

    return top5Items;
  }

  /**
   * 各个车间能耗分析
   * @param timeType
   * @param modelCode
   * @return
   */
  @Override
  public FactoryEnergyConsumptionVo factoryEnergyConsumption(String timeType, String modelCode) {
    ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(modelCode);
    //顶部本月综合能耗
    FactoryEnergyConsumptionVo energyConsumptionVo=new FactoryEnergyConsumptionVo();
    final List<EnergyChainYoyVO> current = energyConsumptionSummation(timeType, modelCode);
    //本月能耗费用
    BigDecimal monthEnergyAmount=getTotalConsumption(modelCode,modelNode.getNodeId(),timeType);
    energyConsumptionVo.setMonthEnergyAmount(monthEnergyAmount);
    //各个车间能耗
    final List<FactoryEnergyConsumptionItemVo> factoryCurrent = factoryEnergyConData(timeType, modelCode);
    EnergyChainYoyVO homeEnergyStatisticsVO = current.get(0);
    energyConsumptionVo.setItemVoList(factoryCurrent);//
    energyConsumptionVo.setMonthConsumption(homeEnergyStatisticsVO.getCount());
    energyConsumptionVo.setCEmission(BigDecimal.ZERO);
    energyConsumptionVo.setUseEnergyIndex(BigDecimal.ZERO);
    return energyConsumptionVo;

  }

  /**
   * 购入消耗对比
   * @param modelCode
   * @param timeType
   * @return
   */
  @Override
  public List<PurchaseConsumptionVo> purchaseConsumption(String modelCode, String timeType) {
    ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(modelCode);
    List<PurchaseConsumptionVo> voList=new ArrayList<>();

    String queryTime=null;
    if (TimeType.DAY.name().equals(timeType)) {
      queryTime=DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD,new Date());
      // 月
    } else if (TimeType.MONTH.name().equals(timeType)) {
      queryTime=DateUtils.parseDateToStr(DateUtils.YYYY_MM,new Date());
      // 年
    } else {
      queryTime=DateUtils.parseDateToStr(DateUtils.YYYY,new Date());
    }

    //获取购入量
    LambdaQueryWrapper<CostElectricityInput> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.like(CostElectricityInput::getType,timeType);
    queryWrapper.like(CostElectricityInput::getTime,queryTime);
    List<CostElectricityInput> list=electricityInputMapper.selectList(queryWrapper);

    //获取消耗量
    BigDecimal totalConsumption=getTotalConsumption(modelCode,modelNode.getNodeId(),timeType);
    //写入实体类
    PurchaseConsumptionVo vo=new PurchaseConsumptionVo();
    vo.setEnergyNo("electric");
    vo.setEnergyName("电");
    vo.setConsumptionSum(totalConsumption.divide(BigDecimal.valueOf(1000),RoundingMode.HALF_UP));
    if (!list.isEmpty()){
      vo.setPurchaseSum(list.get(0).getElectricityFee().divide(BigDecimal.valueOf(1000),RoundingMode.HALF_UP));
    }else{
      vo.setPurchaseSum(BigDecimal.ZERO);
    }
    voList.add(vo);

    return voList;
  }

  /**
   * 能源成本占比
   * @param modelCode
   * @param timeType
   * @return
   */
  @Override
  public List<PurchaseConsumptionVo> costProp(String modelCode, String timeType) {
    List<PurchaseConsumptionVo> list=new ArrayList<>();
    ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(modelCode);
    BigDecimal totalConsumption=getTotalConsumption(modelCode, modelNode.getNodeId(), timeType);
    PurchaseConsumptionVo vo=new PurchaseConsumptionVo();
    vo.setEnergyNo("electric");
    vo.setEnergyName("电");
    vo.setConsumptionSum(totalConsumption.divide(BigDecimal.valueOf(10000),RoundingMode.HALF_UP));
    list.add(vo);
    return list;
  }

  /**
   * 根据尖峰平谷获取消耗电量金额
   * @param modelCode
   * @param nodeId
   * @param timeType
   * @return
   */
  public BigDecimal getTotalConsumption(String modelCode,String nodeId,String timeType){
    BigDecimal totalConsumption = BigDecimal.ZERO;
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date queryTime =calendar.getTime();
    Date startTime =new Date();
    Date endTime =new Date();

    if (TimeType.DAY.name().equals(timeType)) {
      startTime = DateUtil.beginOfDay(queryTime);
      endTime = DateUtil.endOfDay(queryTime);
      // 月
    } else if (TimeType.MONTH.name().equals(timeType)) {
      calendar.set(Calendar.DATE, 1);
      startTime = DateUtil.beginOfMonth(queryTime);
      endTime = DateUtil.endOfMonth(queryTime);
      // 年
    } else {
      calendar.set(Calendar.DATE, 1);
      startTime = DateUtil.beginOfYear(queryTime);
      endTime = DateUtil.endOfYear(queryTime);
    }



    Map<String, List<ElectricityDataItem>> electricityDataMap = new HashMap<>();
// 查询点位信息
    List<ModelNodeIndexInfor> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(modelCode, nodeId);
    if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
      Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toSet());
      List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, startTime,endTime , timeType);

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
            sharpPower = sharpPower.add(electricityDataItem.getElectricity());
          } else if (ElectricityTypeEnum.PEAK.name().equals(electricityType)) {
            peakPower = peakPower.add(electricityDataItem.getElectricity());
          } else if (ElectricityTypeEnum.FLAT.name().equals(electricityType)) {
            flatPower = flatPower.add(electricityDataItem.getElectricity());
          } else {
            valleyPower = valleyPower.add(electricityDataItem.getElectricity());
          }
        }
        //2024-11-12新增
        CostPriceRelevancyVo voS =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(nodeId,ElectricityTypeEnum.SHARP.name());
        if(voS!=null){
          sharpFee = voS.getPrice().multiply(sharpPower);
        }
        CostPriceRelevancyVo voP =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(nodeId,ElectricityTypeEnum.PEAK.name());
        if(voP!=null){
          peakFee = voP.getPrice().multiply(peakPower);
        }
        CostPriceRelevancyVo voF =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(nodeId,ElectricityTypeEnum.FLAT.name());
        if(voF!=null){
          flatFee = voF.getPrice().multiply(flatPower);
        }
        CostPriceRelevancyVo voV =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(nodeId, ElectricityTypeEnum.VALLEY.name());
        if(voV!=null){
          valleyFee = voV.getPrice().multiply(valleyPower);
        }
      }
      totalConsumption = totalConsumption.add(sharpFee).add(peakFee).add(flatFee).add(valleyFee);

      if (TimeType.DAY.name().equals(timeType)) {
        startTime = DateUtil.offsetDay(startTime, 1);
        // 月
      } else if (TimeType.MONTH.name().equals(timeType)) {
        startTime = DateUtil.offsetMonth(startTime, 1);
        // 年
      } else {
        startTime = DateUtil.offsetMonth(startTime, 12);
      }

    }
    return totalConsumption;
  }

  /**
   * 根据厂区、能源类型分组获取每个厂区、每种能源类型的消耗量
   * @param timeType
   * @param modelCode
   * @return
   */
  public List<FactoryEnergyConsumptionItemVo> factoryEnergyConData(String timeType, String modelCode){
    List<FactoryEnergyConsumptionItemVo> energyDataList=new ArrayList<>();

    //厂区固定为当前6个
    List<String> fixedNodeIds=new ArrayList<>();
    fixedNodeIds.add("d9a155d2-b2dd-4a70-a00b-f8ab5be91f00");//冶一
    fixedNodeIds.add("58dcb139-c943-41fe-a764-7cf79f987c9f");//冶二
    fixedNodeIds.add("dca20897-5977-40ae-a28a-88a6acdee335");//冶三
    fixedNodeIds.add("4912c751-0611-4f4b-afce-0f58629512b7");//精炼
    fixedNodeIds.add("8eef471f-880c-4636-a428-620a4d2ccd5d");//矿业
    fixedNodeIds.add("6d6a0412-7c6d-4654-9cb3-0079655e23e5");//恒邦新材料

    LambdaQueryWrapper<ModelNode> nodeLambdaQueryWrapper = new LambdaQueryWrapper<>();
    nodeLambdaQueryWrapper.eq(ModelNode::getModelCode,modelCode);
    nodeLambdaQueryWrapper.in(ModelNode::getNodeId,fixedNodeIds);

    List<ModelNode> nodeList=modelNodeMapper.selectList(nodeLambdaQueryWrapper);

    if  (nodeList.isEmpty()){
      throw new RuntimeException("未查询到6个厂区信息！");
    }

    Map<String,ModelNode> nodeMap=nodeList.stream().collect(Collectors.toMap(ModelNode::getNodeId,x->x));

    List<ModelNodeIndexInfor> indexList=statisticMapper.getModelNodeIndexIdByFixedNodeIds(modelCode,fixedNodeIds);


    // 根据厂区分组
    Map<String, List<ModelNodeIndexInfor>> nodeIndexMap = indexList.stream().collect(
            Collectors.groupingBy(ModelNodeIndexInfor::getNodeId));
    //查询所有能源类型
    final List<String> eneryIdList = indexList.stream().map(ModelNodeIndexInfor::getEnergyId).distinct().collect(Collectors.toList());
    final LambdaQueryWrapper<SysEnergy> queryWrapper = new LambdaQueryWrapper<>();
    queryWrapper.in(CollectionUtils.isNotEmpty(eneryIdList),SysEnergy::getEnersno,eneryIdList);

    //index和能源
    final Map<String, String> indexIdEnergyIdMap = new HashMap<>();
    indexList.forEach(n->{
      final String indexId = n.getIndexId();
      final String energyId = n.getEnergyId();
      if(!indexIdEnergyIdMap.containsKey(indexId)){
        indexIdEnergyIdMap.put(indexId,energyId);
      }
    });
    List<String> indexIds = indexList.stream().filter(l -> StringUtils.isNotEmpty(l.getIndexId())).map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
//        Date queryTime = new Date();
    Date queryTime = new Date();
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

    final List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(queryWrapper);
    //能源编号和能源折标系数
    final Map<String, SysEnergy> energyCoefficientMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno,x->x));

    //获取数据:nodeMap
//    nodeIndexMap.forEach((key, value) -> {
    nodeMap.forEach((key,nodeItem)->{

      if  (!nodeIndexMap.containsKey(key)){
        FactoryEnergyConsumptionItemVo voData = new FactoryEnergyConsumptionItemVo();
        voData.setNodeId(key);
        voData.setNodeName(nodeItem.getName());
        BigDecimal kgceCount=BigDecimal.ZERO;
        BigDecimal kgceTonCount=BigDecimal.ZERO;
        voData.setKgceCount(kgceCount);
        voData.setKgceTonCount(kgceTonCount);
        voData.setEnergyItemList(new ArrayList<>());
        energyDataList.add(voData);
        return;
      }

      //node和index关联集合
      List<ModelNodeIndexInfor> value=nodeIndexMap.get(key);

      List<HomeEnergyStatisticsVO> itemVo=new ArrayList<>();
      //根据能源类型分组
      Map<String, List<ModelNodeIndexInfor>> sysEnergyIds = value.stream().collect(
              Collectors.groupingBy(ModelNodeIndexInfor::getEnergyId));

      if (!sysEnergyIds.isEmpty()) {
        //遍历
        for (Map.Entry<String, List<ModelNodeIndexInfor>> entry : sysEnergyIds.entrySet()) {
          String energyIndex = entry.getKey();
          List<ModelNodeIndexInfor> energyValue = entry.getValue();
          if (energyCoefficientMap.containsKey(energyIndex)) {
            SysEnergy sysEnergy = energyCoefficientMap.get(energyIndex);
            HomeEnergyStatisticsVO item = new HomeEnergyStatisticsVO();
            item.setEnergyNo(sysEnergy.getEnersno());
            item.setEnergyName(sysEnergy.getEnername());
            item.setEnergyUnit(sysEnergy.getMuid());
            item.setCoefficient(sysEnergy.getCoefficient().toString());

            BigDecimal totalConsumption = BigDecimal.ZERO;
            BigDecimal totalCount = BigDecimal.ZERO;
            //合计值
            for (ModelNodeIndexInfor valueItem:entry.getValue()){
              List<DataItem> dataItems = dataItemMap.get(valueItem.getIndexId());
              if (CollectionUtils.isNotEmpty(dataItems)) {
                BigDecimal sum = BigDecimal.valueOf(dataItems.stream()
                        .mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP).multiply(sysEnergy.getCoefficient());
                totalConsumption = totalConsumption.add(sum);
                totalCount = totalCount.add(BigDecimal.valueOf(dataItems.stream()
                        .mapToDouble(DataItem::getValue).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP));
              }
              ;
            }

            item.setTonCount(totalConsumption.doubleValue());
            item.setCount(totalCount.doubleValue());
            itemVo.add(item);
          }
        }
      };

      FactoryEnergyConsumptionItemVo voData = new FactoryEnergyConsumptionItemVo();
      voData.setNodeId(key);
      voData.setNodeName(nodeItem.getName());
      BigDecimal kgceCount=BigDecimal.ZERO;
      BigDecimal kgceTonCount=BigDecimal.ZERO;
      if (!itemVo.isEmpty()){
        kgceCount=BigDecimal.valueOf(itemVo.stream().mapToDouble(HomeEnergyStatisticsVO::getCount).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
        kgceTonCount=BigDecimal.valueOf(itemVo.stream().mapToDouble(HomeEnergyStatisticsVO::getTonCount).sum()).setScale(CommonConst.DIGIT_2, RoundingMode.HALF_UP);
      }
      voData.setKgceCount(kgceCount);
      voData.setKgceTonCount(kgceTonCount);
      voData.setEnergyItemList(itemVo);
      energyDataList.add(voData);
    });
    return energyDataList;
  }


  private List<HomeEnergyStatisticsVO> getEnergyTotalByTime(String timeType, String modelCode, Date queryTime) {
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
    ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(modelCode);
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

}
