package com.zhitan.carbonemission.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.carbonemission.domain.CarbonEmission;
import com.zhitan.carbonemission.domain.dto.CarbonEmissionDTO;
import com.zhitan.carbonemission.domain.vo.CarbonEmissionRankVO;
import com.zhitan.carbonemission.domain.vo.carbonEmissionYQVO;
import com.zhitan.carbonemission.service.ICarbonEmissionService;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.dataitem.mapper.DataItemMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 【碳排放核算】Service业务层处理
 *
 * @author lsk
 * @date 2024-10-29
 */
@Service
public class CarbonEmissionServiceImpl implements ICarbonEmissionService {

    @Resource
    private DataItemMapper dataItemMapper;

    @Resource
    private SysEnergyMapper sysEnergyMapper;

    private final static String[] YEAR = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
    private final static String[] DAY = {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

    //获取页面上部分的碳排放
    @Override
    public Map<String, Object> getUpCarbonEmission(CarbonEmissionDTO carbonEmissionDTO) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        validate(carbonEmissionDTO, "N");

        // 获取查询时间的开始和结束时间
        Date queryTime = carbonEmissionDTO.getDataTime();
        Date beginTime;
        Date endTime;
        switch (carbonEmissionDTO.getTimeType()) {
            case "DAY":
                beginTime = DateUtil.beginOfDay(queryTime);
                endTime = DateUtil.endOfDay(queryTime);
                break;
            case "MONTH":
                beginTime = DateUtil.beginOfMonth(queryTime);
                endTime = DateUtil.endOfMonth(queryTime);
                break;
            case "YEAR":
                beginTime = DateUtil.beginOfYear(queryTime);
                endTime = DateUtil.endOfYear(queryTime);
                break;
            default:
                throw new RuntimeException("时间类型错误");
        }
        Date lastTime = DateUtil.offsetMonth(beginTime, -12);
        Date lastEndTime = DateUtil.offsetMonth(endTime, -12);
        // 获取当前年份和上一年份的碳排放数据
        List<CarbonEmission> upCarbonEmission = dataItemMapper.getUpCarbonEmission(beginTime, endTime, carbonEmissionDTO.getTimeType(), carbonEmissionDTO.getNodeId());

        List<CarbonEmission> upLastCarbonEmission = dataItemMapper.getUpCarbonEmission(lastTime, lastEndTime, carbonEmissionDTO.getTimeType(), carbonEmissionDTO.getNodeId());
        List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(null);
        if (ObjectUtil.isEmpty(sysEnergies)) {
            throw new RuntimeException("获取碳排放转换率时出错: 系统能源数据为空");
        }

        // 构建能源映射和碳排放数据映射
        Map<String, SysEnergy> energyMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, Function.identity()));
        final List<String> energyType = sysEnergies.stream().map(SysEnergy::getEnersno).collect(Collectors.toList());

        double allValue = 0.0;
        double allLastValue = 0.0;

        List<CarbonEmissionRankVO> upData = new ArrayList<>();

        // 计算每种能源的当前值和去年的值
        for (String s : energyType) {
            final double value = getValues(energyMap, upCarbonEmission,energyType);
            final double lastYearValueNum = getValues(energyMap, upLastCarbonEmission,energyType);
            allValue += value;
            allLastValue += lastYearValueNum;
            final CarbonEmissionRankVO carbonEmissionRankVO = new CarbonEmissionRankVO();
            carbonEmissionRankVO.setAllEneryType(energyMap.get(s).getEnername());
            carbonEmissionRankVO.setYoyEnery(String.format("%.2f", calculateYOY(value, lastYearValueNum)));
            carbonEmissionRankVO.setTotalEnery(value);
            upData.add(carbonEmissionRankVO);
        }
        CarbonEmissionRankVO carbonEmissionRankVOAll = new CarbonEmissionRankVO();
        carbonEmissionRankVOAll.setAllEneryType("总碳排放量");
        carbonEmissionRankVOAll.setYoyEnery(String.format("%.2f", calculateYOY(allValue, allLastValue)));
        carbonEmissionRankVOAll.setTotalEnery(allValue);
        upData.add(carbonEmissionRankVOAll);

        // 计算下方的碳排放并排序
        List<CarbonEmissionRankVO> carbonEmissionRankVOS = calculateDownCarbonEmission(beginTime, endTime, carbonEmissionDTO, energyMap,sysEnergies);
        carbonEmissionRankVOS.sort(Comparator.comparing(CarbonEmissionRankVO::getAllValue).reversed());
        stringObjectMap.put("down", carbonEmissionRankVOS);
        stringObjectMap.put("upData", upData);

        return stringObjectMap;
    }

    // 计算同比增长率
    private double calculateYOY(double current, double last) {
        if(last !=0.0) {
            return (current - last) / last * 100;
        }
        return 0;
//        return (current - last) / (last == 0.0 ? 1 : last) * 100;
    }

    // 计算下方的碳排放
    private List<CarbonEmissionRankVO> calculateDownCarbonEmission(Date beginTime, Date endTime, CarbonEmissionDTO carbonEmissionDTO, Map<String, SysEnergy> energyMap,List<SysEnergy> sysEnergies) {
        List<CarbonEmission> downCarbonEmission = dataItemMapper.getDownCarbonEmission(beginTime, endTime, carbonEmissionDTO.getTimeType(), carbonEmissionDTO.getNodeId());
        List<CarbonEmissionRankVO> carbonEmissionRankVOS = new ArrayList<>();

        final List<String> energyType = sysEnergies.stream().map(SysEnergy::getEnersno).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(downCarbonEmission)) {
            DecimalFormat df = new DecimalFormat("#.00");
            Map<String, List<CarbonEmission>> nodeNameMap = downCarbonEmission.stream().collect(Collectors.groupingBy(CarbonEmission::getNodeName));

            nodeNameMap.forEach((k, v) -> {
                CarbonEmissionRankVO rankVO = new CarbonEmissionRankVO();
                rankVO.setNodeName(k);
                double totalValues = 0.0;

                // 计算每种能源的值
                for (String s : energyType) {
                    SysEnergy sysEnergy = energyMap.get(s);
                    if (sysEnergy == null || ObjectUtil.isEmpty(sysEnergy.getCoefficient())) {
                        throw new RuntimeException("获取碳排放转换率时出错: 系统能源数据中不存在" + s + "能源类型");
                    }
                    double value = v.stream().filter(ec -> ec.getEnergyId().equals(s))
                        .mapToDouble(CarbonEmission::getValue)
                        .sum();
                    double calculatedValue = Double.parseDouble(df.format(sysEnergy.getCoefficient().doubleValue() * value));

                    switch (s) {
                        case "water":
                            rankVO.setWater(calculatedValue);
                            break;
                        case "steam":
                            rankVO.setSteam(calculatedValue);
                            break;
                        case "electric":
                            rankVO.setElectric(calculatedValue);
                            break;
                    }
                    totalValues += calculatedValue;
                }
                rankVO.setAllValue(totalValues);
                carbonEmissionRankVOS.add(rankVO);
            });
        }

        return carbonEmissionRankVOS;
    }


    @Override
    public List<carbonEmissionYQVO> getMiddleCarbonEmission(CarbonEmissionDTO carbonEmissionDTO) {
        validate(carbonEmissionDTO, "Y");
        // 该方法用于计算碳排放总值并返回结果
        Map<String, Object> stringObjectMap = new HashMap<>();
        Date beginTime;
        Date endTime;
        final String timeType = carbonEmissionDTO.getTimeType();
        final Date dataTime = carbonEmissionDTO.getDataTime();
        String timeTypeStr;
        String[] XAxis;
        switch (timeType) {
            case "DAY":
                timeTypeStr = "HOUR";
                XAxis = DAY;
                beginTime = DateUtil.beginOfDay(dataTime);
                endTime = DateUtil.endOfDay(dataTime);
                break;
            case "MONTH":
                timeTypeStr = "DAY";
                XAxis = DateUtils.getDays(dataTime);
                beginTime = DateUtil.beginOfMonth(dataTime);
                endTime = DateUtil.endOfMonth(dataTime);
                break;
            case "YEAR":
                timeTypeStr = "MONTH";
                XAxis = YEAR;
                beginTime = DateUtil.beginOfYear(dataTime);
                endTime = DateUtil.endOfYear(dataTime);
                break;
            default:
                throw new RuntimeException("时间类型错误");
        }
        Date lastTime = DateUtil.offsetMonth(beginTime, -12);
        Date lastEndTime = DateUtil.offsetMonth(endTime, -12);

        List<SysEnergy> sysEnergies = sysEnergyMapper.selectList(null);
        if (ObjectUtil.isEmpty(sysEnergies)) {
            throw new RuntimeException("获取碳排放转换率时出错: 系统能源数据为空");
        }

        // 构建能源映射和碳排放数据映射
        Map<String, SysEnergy> energyMap = sysEnergies.stream().collect(Collectors.toMap(SysEnergy::getEnersno, Function.identity()));
        final List<String> energyType = sysEnergies.stream().map(SysEnergy::getEnersno).collect(Collectors.toList());
        final List<CarbonEmission> middleCarbonEmission = dataItemMapper.getMiddleCarbonEmission(beginTime, endTime, timeTypeStr, carbonEmissionDTO.getNodeId(), carbonEmissionDTO.getEmissionType());
        final List<CarbonEmission> lastMiddleCarbonEmission = dataItemMapper.getMiddleCarbonEmission(lastTime, lastEndTime, timeTypeStr, carbonEmissionDTO.getNodeId(), carbonEmissionDTO.getEmissionType());
        final Map<String, List<CarbonEmission>> collect = middleCarbonEmission.stream().collect(Collectors.groupingBy(CarbonEmission::getDataTimeStr));
        final Map<String, List<CarbonEmission>> lastCollect = lastMiddleCarbonEmission.stream().collect(Collectors.groupingBy(CarbonEmission::getDataTimeStr));
        List<carbonEmissionYQVO> carbonEmissionYQVOS = new ArrayList<>();
        for (int i = 0; i < XAxis.length; i++) {
            // 当前时间段的碳排放数据
            final List<CarbonEmission> carbonEmissions = collect.get(XAxis[i]);
            // 上一时间段的碳排放数据
            final List<CarbonEmission> lastCarbonEmissions = lastCollect.get(XAxis[i]);
            // 总值
            double totalValue = getValues(energyMap, carbonEmissions,energyType);
            // 上一时间段的总值
            double totalLastValue = getValues(energyMap, lastCarbonEmissions,energyType);
            // 前一次的总值
            double totalFrontValue = (i == 0) ? 0.0 : getValues(energyMap, collect.get(XAxis[i - 1]),energyType);
            final carbonEmissionYQVO carbonEmissionYQVO = new carbonEmissionYQVO(XAxis[i] ,  String.format("%.2f", totalValue) ,String.format("%.2f", calculateYOY(totalValue, totalLastValue)) , String.format("%.2f", calculateYOY(totalValue, totalFrontValue)));
            carbonEmissionYQVOS.add(carbonEmissionYQVO);
        }
        return carbonEmissionYQVOS;
    }

    // 验证 CarbonEmissionDTO 对象的各项属性是否有效
    public static void validate(CarbonEmissionDTO carbonEmissionDTO, String type) {
        if (ObjectUtil.isNull(carbonEmissionDTO.getDataTime())) {
            throw new RuntimeException("请选择时间");
        }
        if (StringUtils.isEmpty(carbonEmissionDTO.getTimeType())) {
            throw new RuntimeException("请选择时间类型");
        }
        if (ObjectUtil.isEmpty(carbonEmissionDTO.getNodeId())) {
            throw new RuntimeException("请选择节点");
        }
        if ("Y".equals(type) && ObjectUtil.isEmpty(carbonEmissionDTO.getEmissionType())) {
            throw new RuntimeException("请选择排放类型");
        }
    }


    public static double getValues(Map<String, SysEnergy> energyMap, List<CarbonEmission> carbonEmissions,List<String> energyType) {
        if (CollectionUtils.isEmpty(carbonEmissions)) {
            return 0.0;
        }

        // 初始化总值
        double allValue = 0.0;

        // 按能源ID分组
        final Map<String, List<CarbonEmission>> energyValueMap = carbonEmissions.stream()
                .collect(Collectors.groupingBy(CarbonEmission::getEnergyId));
//        final List<String> energyType = sysEnergies.stream().map(SysEnergy::getEnersno).collect(Collectors.toList());
// 计算每种能源的当前值
        for (String s : energyType) {
            SysEnergy sysEnergy = energyMap.get(s);

            // 确保能源存在且系数不为空
            if (sysEnergy == null || sysEnergy.getCoefficient() == null) {
                throw new RuntimeException("获取碳排放转换率时出错: 系统能源数据中不存在或系数为空" + s + "能源类型");
            }

            // 获取对应能源的值
            double value = energyValueMap.getOrDefault(s, Collections.emptyList()).stream()
                    .mapToDouble(CarbonEmission::getValue)
                    .sum();

            // 直接计算总值，避免冗余的转换
            allValue += sysEnergy.getCoefficient().doubleValue() * value;
        }

        // 格式化最终结果
        return Double.parseDouble(new DecimalFormat("#.00").format(allValue));
    }

}
