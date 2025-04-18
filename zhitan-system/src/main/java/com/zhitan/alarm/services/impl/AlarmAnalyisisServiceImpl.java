package com.zhitan.alarm.services.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zhitan.alarm.domain.JkHistoryAlarm;
import com.zhitan.alarm.domain.dto.AlarmAnalysisDTO;
import com.zhitan.alarm.domain.vo.AlarmAnalysisVO;
import com.zhitan.alarm.mapper.HistoryAlarmMapper;
import com.zhitan.alarm.services.IAlarmAnalysisService;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.domain.vo.MeterImplementModel;
import com.zhitan.basicdata.mapper.MeterImplementMapper;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.common.enums.IndexType;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.consumptionanalysis.domain.vo.ChartData;
import com.zhitan.consumptionanalysis.domain.vo.EnergyProportion;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.EnergyIndexMapper;
import com.zhitan.model.mapper.ModelNodeMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报警分析实现
 *
 * @author
 * @date
 */
@Service
@AllArgsConstructor
public class AlarmAnalyisisServiceImpl implements IAlarmAnalysisService {

    private final ModelNodeMapper modelNodeMapper;
    
    private final HistoryAlarmMapper historyAlarmMapper;

    private final MeterImplementMapper meterImplementMapper;
    
    private final EnergyIndexMapper energyIndexMapper;
    
    private final SysEnergyMapper sysEnergyMapper;

    /**
     * 根据节点id获取报警分析信息
     *
     * @param alarmAnalysisDTO
     * @return
     */
    @Override
    public AlarmAnalysisVO getByNodeId(AlarmAnalysisDTO alarmAnalysisDTO) {
        AlarmAnalysisVO alarmAnalysisVO = new AlarmAnalysisVO();
        // 初始化报警比例列表
        List<EnergyProportion> alarmProportionList = initializeProportionList(IndexType.values());
        // 初始化能源比例列表
        List<EnergyProportion> energyProportionList = new ArrayList<>();

        sysEnergyMapper.selectSysEnergyList(new SysEnergy()).forEach(sysEnergy -> {
            EnergyProportion proportion = new EnergyProportion();
            proportion.setEnergyName(sysEnergy.getEnersno());
            proportion.setCount(0D);
            proportion.setPercentage(0D);
            energyProportionList.add(proportion);
        });

        List<ChartData> chartDataList = new ArrayList<>();
        final String nodeId = alarmAnalysisDTO.getNodeId();
        final Date queryTime = alarmAnalysisDTO.getDataTime();
       
        Date beginTime;
        Date endTime;
        String timeFormat;
        String queryTimeType = alarmAnalysisDTO.getTimeType();
        if (TimeType.DAY.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfDay(queryTime);
            endTime = DateUtil.endOfDay(queryTime);
            timeFormat = "yyyy-MM-dd HH";
            // 月
        } else if (TimeType.MONTH.name().equals(queryTimeType)) {
            beginTime = DateUtil.beginOfMonth(queryTime);
            endTime = DateUtil.endOfMonth(queryTime);
            timeFormat = "yyyy-MM-dd";
            // 年
        } else {
            beginTime = DateUtil.beginOfYear(queryTime);
            endTime = DateUtil.endOfYear(queryTime);
            timeFormat = "yyyy-MM";
        }
        JkHistoryAlarm query = new JkHistoryAlarm();
        query.setEierarchyFlag("ALL");
        query.setBeginTime(DateUtil.format(beginTime,"yyyy-MM-dd HH:mm:ss"));
        query.setEndTime(DateUtil.format(endTime,"yyyy-MM-dd HH:mm:ss"));
        query.setNodeId(nodeId);
        final List<JkHistoryAlarm> jkHistoryAlarms = historyAlarmMapper.selectJkHistoryAlarmList(query);
        if(CollectionUtils.isNotEmpty(jkHistoryAlarms)) {

            // 设置能源类型
            processEnergyType(jkHistoryAlarms);

            // 计算报警类型比例
            calculateProportions(jkHistoryAlarms, alarmProportionList, JkHistoryAlarm::getIndexType);
            // 计算能源类型比例
            calculateProportions(jkHistoryAlarms, energyProportionList, JkHistoryAlarm::getEnergyId);

            // 处理报警时间
            processAlarmTimes(jkHistoryAlarms, timeFormat);

            // 生成图表数据
            generateChartData(chartDataList, jkHistoryAlarms, beginTime, endTime, timeFormat, queryTimeType);

        }
        alarmAnalysisVO.setAlarmProportion(alarmProportionList);
        alarmAnalysisVO.setEnergyProportion(energyProportionList);
        alarmAnalysisVO.setChartDataList(chartDataList);
        return alarmAnalysisVO;
    }

    /**
     * 设置能源类型
     */
    private void processEnergyType(List<JkHistoryAlarm> jkHistoryAlarms) {
        List<String> indexIds = jkHistoryAlarms.stream().map(JkHistoryAlarm::getIndexId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(indexIds)) {
            List<MeterImplementModel> modelList = energyIndexMapper.selectEnergyTypeByIndex(indexIds);
            if (CollectionUtils.isNotEmpty(modelList)){
                Map<String, String> energyMap = modelList.stream().collect(Collectors.toMap(MeterImplementModel::getIndexId, MeterImplementModel::getEnergyType));

                jkHistoryAlarms.forEach(alarm -> {
                    if (IndexType.COLLECT.getDescription().equals(alarm.getIndexType()) && StringUtils.isEmpty(alarm.getEnergyId())){
                        alarm.setEnergyId(energyMap.get(alarm.getIndexId()));
                    }
                });
            }
        }
    }

    /**
     * 计算占比
     */
    private void calculateProportions(List<JkHistoryAlarm> jkHistoryAlarms, List<EnergyProportion> proportionList, java.util.function.Function<JkHistoryAlarm, String> keyExtractor) {
        Map<String, List<JkHistoryAlarm>> typeMap = jkHistoryAlarms.stream()
                .collect(Collectors.groupingBy(keyExtractor));
        typeMap.forEach((key, value) -> {
            proportionList.forEach(alarm -> {
                if (alarm.getEnergyName().equals(key)) {
                    alarm.setCount(format2Double(value.size()));
                    double percentage = (double) value.size() / jkHistoryAlarms.size() * 100;
                    alarm.setPercentage(format2Double(percentage));
                }
            });
        });
    }

    /**
     * 处理报警时间
     */
    private void processAlarmTimes(List<JkHistoryAlarm> jkHistoryAlarms, String timeFormat) {
        jkHistoryAlarms.forEach(jkHistoryAlarm -> {
            String alarmBeginTime = DateUtil.format(jkHistoryAlarm.getAlarmBeginTime(), timeFormat);
            jkHistoryAlarm.setAlarmTime(alarmBeginTime);
        });
    }

    /**
     * 生成图表数据
     */
    private void generateChartData(List<ChartData> chartDataList, List<JkHistoryAlarm> jkHistoryAlarms, Date beginTime, Date endTime, String timeFormat, String queryTimeType) {
        Map<String, List<JkHistoryAlarm>> timeMap = jkHistoryAlarms.stream()
                .collect(Collectors.groupingBy(JkHistoryAlarm::getAlarmTime));
        while (!beginTime.after(endTime)) {
            String currentTime = DateUtil.format(beginTime, timeFormat);
            List<JkHistoryAlarm> value = timeMap.get(currentTime);
            ChartData chartData = new ChartData();
            chartData.setXData(currentTime);
            chartData.setYValue(value == null ? 0 : (double) value.size());
            chartDataList.add(chartData);

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
    }

    /**
     * 初始化占比列表
     */
    private List<EnergyProportion> initializeProportionList(Object[] items) {
        List<EnergyProportion> proportionList = new ArrayList<>();
        for (Object item : items) {
            EnergyProportion proportion = new EnergyProportion();
            proportion.setEnergyName(item.toString());
            proportion.setCount(0D);
            proportion.setPercentage(0D);
            proportionList.add(proportion);
        }
        return proportionList;
    }

    private double format2Double(double averageEnergy) {
        // 创建DecimalFormat对象，设置保留两位小数
        DecimalFormat df = new DecimalFormat("#.00");

        // 格式化结果
        String formattedResult = df.format(averageEnergy);
        return Double.valueOf(formattedResult);
    }

    /**
     * 获取报警分析统计信息
     * @param alarmAnalysisDTO
     * @return
     */
    @Override
    public AlarmAnalysisVO getCountInfo(AlarmAnalysisDTO alarmAnalysisDTO) {

        AlarmAnalysisVO alarmAnalysisVO = new AlarmAnalysisVO();

        ModelNode parentNode = modelNodeMapper.selectModelNodeById(alarmAnalysisDTO.getNodeId());
        if(ObjectUtils.isEmpty(parentNode)){
            return alarmAnalysisVO;
        }

        // 查询模型下的点位数据
        List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.getAllModelNodeIndexByAddress(parentNode.getModelCode(), parentNode.getAddress());
        alarmAnalysisVO.setIndexCount(nodeIndexInfoList.size());
        if (CollectionUtils.isEmpty(nodeIndexInfoList)) {
            return alarmAnalysisVO;
        }

        // 获取月报警数、年报警数
        List<String> indexIdList = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toList());

        DateTime beginOfMonth = DateUtil.beginOfMonth(new Date());
        DateTime endOfMonth = DateUtil.endOfMonth(new Date());
        DateTime beginOfYear = DateUtil.beginOfYear(new Date());
        DateTime endOfYear = DateUtil.endOfYear(new Date());
        Integer monthCount = historyAlarmMapper.selectCountByTime(beginOfMonth,endOfMonth, indexIdList);
        Integer yearCount = historyAlarmMapper.selectCountByTime(beginOfYear,endOfYear, indexIdList);

        alarmAnalysisVO.setMonthCount(monthCount);
        alarmAnalysisVO.setYearCount(yearCount);
        return alarmAnalysisVO;
    }
}
