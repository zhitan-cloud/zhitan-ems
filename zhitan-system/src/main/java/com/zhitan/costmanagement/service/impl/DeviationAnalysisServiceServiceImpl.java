package com.zhitan.costmanagement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhitan.common.enums.ElectricityTypeEnum;
import com.zhitan.common.enums.TimeType;
import com.zhitan.costmanagement.domain.CostElectricityInput;
import com.zhitan.costmanagement.domain.vo.*;
import com.zhitan.costmanagement.mapper.CostElectricityInputMapper;
import com.zhitan.costmanagement.mapper.CostPriceRelevancyMapper;
import com.zhitan.costmanagement.service.DeviationAnalysisService;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.ModelInfoMapper;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.peakvalley.domain.ElectricityDataItem;
import com.zhitan.peakvalley.mapper.PeakValleyMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.zhitan.common.utils.DateUtils.YYYY;
import static com.zhitan.common.utils.DateUtils.YYYY_MM;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@Service
public class DeviationAnalysisServiceServiceImpl implements DeviationAnalysisService {
    @Resource
    private CostElectricityInputMapper costElectricityInputMapper;
    @Resource
    private ModelNodeMapper modelNodeMapper;
    @Resource
    private ModelInfoMapper modelInfoMapper;
    @Resource
    private PeakValleyMapper electricityDataItemMapper;
    @Resource
    CostPriceRelevancyMapper costPriceRelevancyMapper;


    @Override
    public ElectricityDataOutItem getElectricityDataItem(DeviationAnalysisDTO dto) throws ParseException {
        ElectricityDataOutItem ed = new ElectricityDataOutItem();
        CostElectricityInput search = costElectricityInputMapper.selectOne(new QueryWrapper<CostElectricityInput>().
                eq("type",dto.getTimeType()).eq("time",dto.getDate()));
        if(search!=null){
            ed.setElectricityFee(search.getElectricityFee());
            ed.setElectricityNum(search.getElectricityNum());
            ed.setPowerFactor(search.getPowerFactor());
            ed.setSharpElectricity(search.getSharpElectricity());
            ed.setPeakElectricity(search.getPeakElectricity());
            ed.setFlatElectricity(search.getFlatElectricity());
            ed.setValleyElectricity(search.getValleyElectricity());
            //如果统计的是年，先获取上一年

            DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM);
            String lastDate = "";

            if(TimeType.MONTH.name().equals(dto.getTimeType())){
                YearMonth yearMonth = YearMonth.parse(dto.getDate(), df);

                //如果统计的是月
                String lastMonth = yearMonth.minusMonths(1).format(df);
                CostElectricityInput searchMonth = costElectricityInputMapper.selectOne(new QueryWrapper<CostElectricityInput>().
                        eq("type",dto.getTimeType()).eq("time",lastMonth));

                ed.setElectricityFeeQoq(percentage(search.getElectricityFee(),searchMonth.getElectricityFee()));
                ed.setElectricityNumQoq(percentage(search.getElectricityNum(),searchMonth.getElectricityNum()));
                ed.setPowerFactorQoq(percentage(search.getPowerFactor(),searchMonth.getPowerFactor()));
                lastDate = yearMonth.minusYears(1).format(df);
            }else{
                df = DateTimeFormatter.ofPattern(YYYY);
                Year year = Year.parse(dto.getDate(), df);
                lastDate = year.minusYears(1).format(df);
            }

            CostElectricityInput searchLastYear = costElectricityInputMapper.selectOne(new QueryWrapper<CostElectricityInput>().
                    eq("type",dto.getTimeType()).eq("time",lastDate));
            if(searchLastYear!=null){
                ed.setElectricityFeeYoy(percentage(search.getElectricityFee(),searchLastYear.getElectricityFee()));
                ed.setElectricityNumYoy(percentage(search.getElectricityNum(),searchLastYear.getElectricityNum()));
                ed.setPowerFactorYoy(percentage(search.getPowerFactor(),searchLastYear.getPowerFactor()));
            }else{
                ed.setElectricityFeeYoy(new BigDecimal(100));
                ed.setElectricityNumYoy(new BigDecimal(100));
                ed.setPowerFactorYoy(new BigDecimal(100));
            }

        }

        return ed;

    }

    @Override
    public StatisticsInfoListOut getStatisticsInfoList(DeviationAnalysisDTO dto) throws ParseException {
        StatisticsInfoListOut out = new StatisticsInfoListOut();
        List<ModelNode>modelNodeList = modelNodeMapper.getModelNodeByModelCode(dto.getModelCode());

        List<ModelNode>modelNodeList2 = modelNodeList.stream().filter(s->s.getParentId()==null).collect(Collectors.toList());
        List<ModelNode>modelNodeList3 = modelNodeList.stream().filter(s->s.getParentId()!=null).collect(Collectors.toList());
        String nodeId = modelNodeList2.get(0).getNodeId();

        List<ModelNode>childList = modelNodeList3.stream().filter(t->nodeId.equals(t.getParentId())).collect(Collectors.toList());

        List<String>nodes = childList.stream().map(s->s.getNodeId()).collect(Collectors.toList());
        if(nodes!=null&& nodes.size()>0){
            out = computeInfoList(dto,nodes,modelNodeList,nodeId);
        }else{
            out.setNodeId(nodeId);
            List<StatisticsInfoListOut> child = new ArrayList<>();
            List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), nodeId);
            if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
                Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());
                List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet, dto.getTimeType());
                StatisticsDataOutItem nowData = compute(dto.getTimeType(),dto.getDate(),nodeId,dataItemList);
                BeanUtil.copyProperties(nowData,out);
                out.setElectricityNumRadio(new BigDecimal(100));

                DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM);
                String lastDate = "";
                if(TimeType.MONTH.name().equals(dto.getTimeType())){
                    //如果统计的是月
                    YearMonth yearMonth = YearMonth.parse(dto.getDate(), df);
                    lastDate = yearMonth.minusYears(1).format(df);
                    //如果统计的是月
                    String lastMonth = yearMonth.minusMonths(1).format(df);

                    StatisticsDataOutItem lastMonthData = compute(dto.getTimeType(),lastMonth,nodeId,dataItemList);
                    out.setElectricityNumQoq(percentage(out.getElectricityNum(),lastMonthData.getElectricityNum()));
                }else{
                    df = DateTimeFormatter.ofPattern(YYYY);
                    Year year = Year.parse(dto.getDate(), df);
                    lastDate = year.minusYears(1).format(df);

                }
                StatisticsDataOutItem lastYearData = compute(dto.getTimeType(),lastDate,nodeId,dataItemList);
                out.setElectricityNumYoy(percentage(out.getElectricityNum(),lastYearData.getElectricityNum()));
            }
            out.setChildren(child);
        }


        return out;
    }

    @Override
    public List<StatisticsInfoListOut> getStatisticsInfoNewList(DeviationAnalysisDTO dto) throws ParseException {
        List<StatisticsInfoListOut>result=new ArrayList<>();
        List<ModelNode>modelNodeList = modelNodeMapper.getModelNodeByModelCode(dto.getModelCode());
        List<ModelNode>modelNodeList2 = modelNodeList.stream().filter(s->s.getParentId()==null).collect(Collectors.toList());
        BigDecimal totalNum = new BigDecimal(0);
        String nodeIdP = modelNodeList2.get(0).getNodeId();

        for(ModelNode modelNode:modelNodeList){
            StatisticsInfoListOut out = new StatisticsInfoListOut();
            String nodeId = modelNode.getNodeId();
            List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), nodeId);
            if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
                Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());
                List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet, dto.getTimeType());
                StatisticsDataOutItem nowData = compute(dto.getTimeType(),dto.getDate(),nodeId,dataItemList);
                BeanUtil.copyProperties(nowData,out);
                if (nodeIdP.equals(nodeId)) {
                    totalNum = nowData.getElectricityNum();
                }
                out.setElectricityNumRadio(nowData.getElectricityNum().divide(totalNum));
                DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM);
                String lastDate = "";
                if(TimeType.MONTH.name().equals(dto.getTimeType())){
                    //如果统计的是月
                    YearMonth yearMonth = YearMonth.parse(dto.getDate(), df);
                    lastDate = yearMonth.minusYears(1).format(df);
                    //如果统计的是月
                    String lastMonth = yearMonth.minusMonths(1).format(df);

                    StatisticsDataOutItem lastMonthData = compute(dto.getTimeType(),lastMonth,nodeId,dataItemList);
                    out.setElectricityNumQoq(percentage(out.getElectricityNum(),lastMonthData.getElectricityNum()));
                }else{
                    df = DateTimeFormatter.ofPattern(YYYY);
                    Year year = Year.parse(dto.getDate(), df);
                    lastDate = year.minusYears(1).format(df);

                }
                StatisticsDataOutItem lastYearData = compute(dto.getTimeType(),lastDate,nodeId,dataItemList);
                out.setElectricityNumYoy(percentage(out.getElectricityNum(),lastYearData.getElectricityNum()));
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> getEcharst(DeviationAnalysisDTO dto) throws ParseException {
        List<String>xData = new ArrayList<>();
        List<String>xData2 = new ArrayList<>();
        List<BigDecimal>yData = new ArrayList<>();
        Map<String, Object>result = new HashMap<>();
        List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), dto.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {

            String dateStr = dto.getDate();
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());

            List<ElectricityDataItem> dataItemList = new ArrayList<>();

            if(TimeType.MONTH.name().equals(dto.getTimeType())){
                dateStr = "D"+dateStr.replace("-","");
                dataItemList = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet, TimeType.DAY.name());
            }else{
                dateStr = "M"+dateStr;
                dataItemList = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet, TimeType.MONTH.name());
            }
            String finalDateStr1 = dateStr;
            List<ElectricityDataItem>dataNow = new ArrayList<>();

            dataNow = dataItemList.stream().filter(s->s.getTimeCode().substring(0,7).equals(finalDateStr1)).collect(Collectors.toList());

            Map<String, List<ElectricityDataItem>> electricityDataMap = new HashMap<>();
            electricityDataMap = dataNow.stream()
                    .collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
//            xData = dataNow.stream().map(s->DateUtil.formatDateTime(s.getDataTime())).collect(Collectors.toList());
            for(String date : electricityDataMap.keySet()){
                List<ElectricityDataItem> ylist = electricityDataMap.get(date);
                BigDecimal allNum = BigDecimal.ZERO;
                for(ElectricityDataItem i : ylist){
                    allNum = allNum.add(i.getElectricity());
                }
                yData.add(allNum);
                String mapKey = DateUtil.formatDate(DateUtil.parseDate(date));
                xData2.add(mapKey);
            }

        }
        result.put("xData",xData2);
        result.put("yData",yData);




        return result;
    }

    @Override
    public StatisticsDataOutItem getStatisticsDataItem(DeviationAnalysisDTO dto) throws ParseException {
        ElectricityDataOutItem electricityDataOutItem = this.getElectricityDataItem(dto);
        StatisticsDataOutItem result = new StatisticsDataOutItem();

//        // 查询点位信息
        List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), dto.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet, dto.getTimeType());
            StatisticsDataOutItem nowData = compute(dto.getTimeType(),dto.getDate(),dto.getNodeId(),dataItemList);
            BeanUtil.copyProperties(nowData,result);
            result.setPowerFactor(new BigDecimal(0.9).setScale(2, RoundingMode.HALF_UP));
            result.setPowerFactorQoq(new BigDecimal(0).setScale(2, RoundingMode.HALF_UP));
            result.setPowerFactorYoy(new BigDecimal(0.6).setScale(2, RoundingMode.HALF_UP));
            result.setPowerFactorDiff(result.getPowerFactor().subtract(electricityDataOutItem.getPowerFactor()));
            result.setElectricityNumDiff(result.getElectricityNum().subtract(electricityDataOutItem.getElectricityNum()));
            result.setElectricityFeeDiff(result.getElectricityFee().subtract(electricityDataOutItem.getElectricityFee()));
            result.setSharpElectricityDiff(result.getSharpElectricity().subtract(electricityDataOutItem.getSharpElectricity()));
            result.setPeakElectricityDiff(result.getPeakElectricity().subtract(electricityDataOutItem.getPeakElectricity()));
            result.setFlatElectricityDiff(result.getFlatElectricity().subtract(electricityDataOutItem.getFlatElectricity()));
            result.setValleyElectricityDiff(result.getValleyElectricity().subtract(electricityDataOutItem.getValleyElectricity()));
            DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM);
            String lastDate = "";
            if(TimeType.MONTH.name().equals(dto.getTimeType())){
                //如果统计的是月
                YearMonth yearMonth = YearMonth.parse(dto.getDate(), df);

                //如果统计的是月
                String lastMonth = yearMonth.minusMonths(1).format(df);

                StatisticsDataOutItem lastMonthData = compute(dto.getTimeType(),lastMonth,dto.getNodeId(),dataItemList);
                result.setElectricityFeeQoq(percentage(result.getElectricityFee(),lastMonthData.getElectricityFee()));
                result.setElectricityNumQoq(percentage(result.getElectricityNum(),lastMonthData.getElectricityNum()));
                lastDate = yearMonth.minusYears(1).format(df);
//                result.setPowerFactorQoq(percentage(result.getPowerFactor(),lastMonthData.getPowerFactor()));
            }else{
                df = DateTimeFormatter.ofPattern(YYYY);
                Year year = Year.parse(dto.getDate(), df);
                lastDate = year.minusYears(1).format(df);
//                result.setPowerFactorQoq(percentage(result.getPowerFactor(),lastYearData.getPowerFactor()));
            }
            StatisticsDataOutItem lastYearData = compute(dto.getTimeType(),lastDate,dto.getNodeId(),dataItemList);
            result.setElectricityFeeYoy(percentage(result.getElectricityFee(),lastYearData.getElectricityFee()));
            result.setElectricityNumYoy(percentage(result.getElectricityNum(),lastYearData.getElectricityNum()));


        }
        return result;
    }
    //计算电费、耗电量
    private StatisticsDataOutItem compute(String timeType,String dateStr,String nodeId,List<ElectricityDataItem>dataItemList){
        StatisticsDataOutItem sd = new StatisticsDataOutItem();
        if(TimeType.MONTH.name().equals(timeType)){
            dateStr = "M"+dateStr.replace("-","");
        }else{
            dateStr = "Y"+dateStr;
        }

        String finalDateStr = dateStr;
        List<ElectricityDataItem>dataNow = new ArrayList<>();
        BigDecimal sharpFee = BigDecimal.ZERO;
        BigDecimal sharpPower = BigDecimal.ZERO;
        BigDecimal peakFee = BigDecimal.ZERO;
        BigDecimal peakPower = BigDecimal.ZERO;
        BigDecimal flatFee = BigDecimal.ZERO;
        BigDecimal flatPower = BigDecimal.ZERO;
        BigDecimal valleyFee = BigDecimal.ZERO;
        BigDecimal valleyPower = BigDecimal.ZERO;
        BigDecimal DeepFee = BigDecimal.ZERO;
        BigDecimal DeepPower = BigDecimal.ZERO;

        dataNow = dataItemList.stream().filter(s->s.getTimeCode().equals(finalDateStr)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(dataNow)) {
            for (ElectricityDataItem electricityDataItem : dataNow) {
                String electricityType = electricityDataItem.getElectricityType();
                if (ElectricityTypeEnum.SHARP.name().equals(electricityType)) {
                    sharpPower = sharpPower.add(electricityDataItem.getElectricity());

                } else if (ElectricityTypeEnum.PEAK.name().equals(electricityType)) {

                    peakPower = peakPower.add(electricityDataItem.getElectricity());
                } else if (ElectricityTypeEnum.FLAT.name().equals(electricityType)) {

                    flatPower = flatPower.add(electricityDataItem.getElectricity());
                } else if (ElectricityTypeEnum.VALLEY.name().equals(electricityType)){

                    valleyPower = valleyPower.add(electricityDataItem.getElectricity());
                }else if (ElectricityTypeEnum.DEEP.name().equals(electricityType)){

                    DeepPower = DeepPower.add(electricityDataItem.getElectricity());
                }
            }
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
            CostPriceRelevancyVo voD =  costPriceRelevancyMapper.selectCostPriceRelevancyByNodeId(nodeId, ElectricityTypeEnum.DEEP.name());
            if(voV!=null){
                DeepFee = voV.getPrice().multiply(valleyPower);
            }
            BigDecimal allNum = sharpPower.add(peakPower).add(flatPower).add(valleyPower);
            BigDecimal allFee = sharpFee.add(peakFee).add(flatFee).add(valleyFee);
            sd.setSharpElectricity(sharpPower);
            sd.setPeakElectricity(peakPower);
            sd.setFlatElectricity(flatPower);
            sd.setValleyElectricity(valleyPower);
            sd.setElectricityFee(allFee);
            sd.setElectricityNum(allNum);

        }
        return sd;
    }
    /**
     * 同环比计算
     * @param thisData
     * @param lastData
     * @return
     */
    private BigDecimal percentage(BigDecimal thisData, BigDecimal lastData) {
        BigDecimal rate = new BigDecimal(100);
        if(lastData.compareTo(BigDecimal.ZERO)==1){
            BigDecimal differ = thisData.subtract(lastData);
            rate = differ.multiply(new BigDecimal(100)).divide(lastData,BigDecimal.ROUND_CEILING).setScale(2, RoundingMode.HALF_UP);

        }

        return rate;
    }

    private StatisticsInfoListOut computeInfoList(DeviationAnalysisDTO dto,List<String>nodes,List<ModelNode>modelNodeList,String nodeId){
        StatisticsInfoListOut out = new StatisticsInfoListOut();
        List<StatisticsInfoListOut> child = new ArrayList<>();
        List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), nodeId);
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet, dto.getTimeType());
            StatisticsDataOutItem nowData = compute(dto.getTimeType(),dto.getDate(),nodeId,dataItemList);
            BeanUtil.copyProperties(nowData,out);
            out.setElectricityNumRadio(new BigDecimal(100));
            out.setNodeId(nodeId);
            DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM);
            String lastDate = "";
            if(TimeType.MONTH.name().equals(dto.getTimeType())){
                //如果统计的是月
                YearMonth yearMonth = YearMonth.parse(dto.getDate(), df);

                //如果统计的是月
                String lastMonth = yearMonth.minusMonths(1).format(df);

                StatisticsDataOutItem lastMonthData = compute(dto.getTimeType(),lastMonth,nodeId,dataItemList);
                out.setElectricityNumQoq(percentage(out.getElectricityNum(),lastMonthData.getElectricityNum()));
                lastDate = yearMonth.minusYears(1).format(df);
            }else{
                df = DateTimeFormatter.ofPattern(YYYY);
                Year year = Year.parse(dto.getDate(), df);
                lastDate = year.minusYears(1).format(df);

            }
            StatisticsDataOutItem lastYearData = compute(dto.getTimeType(),lastDate,nodeId,dataItemList);
            out.setElectricityNumYoy(percentage(out.getElectricityNum(),lastYearData.getElectricityNum()));
            if(nodes!=null&&nodes.size()>0){
                for(String node : nodes){

                    StatisticsInfoListOut out2 = new StatisticsInfoListOut();
                    out2.setNodeId(node);
                    List<ModelNodeIndexInfo> nodeIndexInfoList2 = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), node);
                    if (CollectionUtils.isNotEmpty(nodeIndexInfoList2)) {
                        Set<String> indexSet2 = nodeIndexInfoList2.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());
                        List<ElectricityDataItem> dataItemList2 = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet2, dto.getTimeType());
                        StatisticsDataOutItem nowData2 = compute(dto.getTimeType(),dto.getDate(),node,dataItemList2);
                        BeanUtil.copyProperties(nowData2,out2);
                        out2.setElectricityNumRadio(new BigDecimal(100));
                        if(TimeType.MONTH.name().equals(dto.getTimeType())){
                            //如果统计的是月
                            YearMonth yearMonth = YearMonth.parse(dto.getDate(), df);
                            lastDate = yearMonth.minusYears(1).format(df);
                            //如果统计的是月
                            String lastMonth = yearMonth.minusMonths(1).format(df);

                            StatisticsDataOutItem lastMonthData = compute(dto.getTimeType(),lastMonth,node,dataItemList2);

                            out2.setElectricityNumQoq(percentage(out2.getElectricityNum(),lastMonthData.getElectricityNum()));


                        }else{
                            df = DateTimeFormatter.ofPattern(YYYY);
                            Year year = Year.parse(dto.getDate(), df);
                            lastDate = year.minusYears(1).format(df);

                        }
                        StatisticsDataOutItem lastYearData2 = compute(dto.getTimeType(),lastDate,dto.getNodeId(),dataItemList2);
                        out2.setElectricityNumYoy(percentage(out2.getElectricityNum(),lastYearData2.getElectricityNum()));

                    }
                    child.add(out2);
//                    List<ModelNode>childList = modelNodeList.stream().filter(s->s.getParentId().equals(node)).collect(Collectors.toList());
//                    List<String>node2s = childList.stream().map(s->s.getNodeId()).collect(Collectors.toList());
//                    if(){
//
//                    }


                }
            }
        }
        out.setChildren(child);
        return out;
    }


    @Override
    public StatisticsInfoListOut getStatisticsInfoList2(DeviationAnalysisDTO dto) throws ParseException {






















        StatisticsInfoListOut out = new StatisticsInfoListOut();
        List<ModelNode>modelNodeList = modelNodeMapper.getModelNodeByModelCode(dto.getModelCode());

        List<ModelNode>modelNodeList2 = modelNodeList.stream().filter(s->s.getParentId()==null).collect(Collectors.toList());
        List<ModelNode>modelNodeList3 = modelNodeList.stream().filter(s->s.getParentId()!=null).collect(Collectors.toList());
        String nodeId = modelNodeList2.get(0).getNodeId();

        List<ModelNode>childList = modelNodeList3.stream().filter(t->nodeId.equals(t.getParentId())).collect(Collectors.toList());

        List<String>nodes = childList.stream().map(s->s.getNodeId()).collect(Collectors.toList());
        if(nodes!=null&& nodes.size()>0){
            out = computeInfoList(dto,nodes,modelNodeList,nodeId);
        }else{
            out.setNodeId(nodeId);
            List<StatisticsInfoListOut> child = new ArrayList<>();
            List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), nodeId);
            if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
                Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toSet());
                List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatisticsDeviationAnalysis(indexSet, dto.getTimeType());
                StatisticsDataOutItem nowData = compute(dto.getTimeType(),dto.getDate(),nodeId,dataItemList);
                BeanUtil.copyProperties(nowData,out);
                out.setElectricityNumRadio(new BigDecimal(100));

                DateTimeFormatter df = DateTimeFormatter.ofPattern(YYYY_MM);
                String lastDate = "";
                if(TimeType.MONTH.name().equals(dto.getTimeType())){
                    //如果统计的是月
                    YearMonth yearMonth = YearMonth.parse(dto.getDate(), df);
                    lastDate = yearMonth.minusYears(1).format(df);
                    //如果统计的是月
                    String lastMonth = yearMonth.minusMonths(1).format(df);

                    StatisticsDataOutItem lastMonthData = compute(dto.getTimeType(),lastMonth,nodeId,dataItemList);
                    out.setElectricityNumQoq(percentage(out.getElectricityNum(),lastMonthData.getElectricityNum()));
                }else{
                    df = DateTimeFormatter.ofPattern(YYYY);
                    Year year = Year.parse(dto.getDate(), df);
                    lastDate = year.minusYears(1).format(df);

                }
                StatisticsDataOutItem lastYearData = compute(dto.getTimeType(),lastDate,nodeId,dataItemList);
                out.setElectricityNumYoy(percentage(out.getElectricityNum(),lastYearData.getElectricityNum()));
            }
            out.setChildren(child);
        }


        return out;
    }


    /**
     * 递归建立树形结构 （算法）
     * @param lists
     * @param pid
     * @return
     */
//    private List<StatisticsInfoListOut> createTree(List<StatisticsInfoListOut> lists, int pid) {
//        List<StatisticsInfoListOut> tree = new ArrayList<>();
//        for (StatisticsInfoListOut catelog : lists) {
//            if (catelog.get() == pid) {
//                catelog.setChirldren(createTree(lists,catelog.getId()));
//                tree.add(catelog);
//            }
//        }
//        return tree;
//    }

}
