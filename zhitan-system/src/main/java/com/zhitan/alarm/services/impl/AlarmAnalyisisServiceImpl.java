package com.zhitan.alarm.services.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zhitan.alarm.domain.JkHistoryAlarm;
import com.zhitan.alarm.domain.dto.AlarmAnalysisDTO;
import com.zhitan.alarm.domain.vo.AlarmAnalysisVO;
import com.zhitan.alarm.mapper.HistoryAlarmMapper;
import com.zhitan.alarm.services.IAlarmAnalysisService;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.domain.SysEnergy;
import com.zhitan.basicdata.mapper.MeterImplementMapper;
import com.zhitan.basicdata.mapper.SysEnergyMapper;
import com.zhitan.common.enums.IndexType;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.consumptionanalysis.domain.vo.ChartData;
import com.zhitan.consumptionanalysis.domain.vo.EnergyProportion;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.EnergyIndexMapper;
import com.zhitan.model.mapper.ModelNodeMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
     * 根据节点id获取报警分析信息(废弃)
     *
     * @param alarmAnalysisDTO
     * @return
     */
    @Override
    public AlarmAnalysisVO getByNodeId(AlarmAnalysisDTO alarmAnalysisDTO) {
        AlarmAnalysisVO alarmAnalysisVO = new AlarmAnalysisVO();
        List<EnergyProportion> alarmProportionList = new ArrayList<>();
        for(IndexType indexType : IndexType.values()){
            EnergyProportion proportion = new EnergyProportion();
            proportion.setEnergyName(indexType.name());
            proportion.setCount(0D);
            proportion.setPercentage(0D);
            alarmProportionList.add(proportion);
        }
                
                
        List<EnergyProportion> energyProportionList = new ArrayList<>();
        final List<SysEnergy> sysEnergies = sysEnergyMapper.selectSysEnergyList(new SysEnergy());
        sysEnergies.forEach(sysEnergy -> {
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
            jkHistoryAlarms.forEach(alarm -> {
                final String indexType = alarm.getIndexType();
                final String indexId = alarm.getIndexId();
                final String alarmNodeId = alarm.getNodeId();
                if ("COLLECT".equals(indexType) && StringUtils.isEmpty(alarm.getEnergyId())) {
                    //根据nodeId和indexId 去查询计量器具
                    EnergyIndex energyIndex = energyIndexMapper.selectEnergyIndexById(indexId);
                    final MeterImplement meterImplement = meterImplementMapper.selectMeterImplementById(energyIndex.getMeterId());
                    alarm.setEnergyId(meterImplement.getEnergyType());
                }
            });
            final Map<String, List<JkHistoryAlarm>> alarmTypeMap = jkHistoryAlarms.stream().collect(Collectors.groupingBy(JkHistoryAlarm::getIndexType));
            alarmTypeMap.forEach((key, value) -> {
                alarmProportionList.forEach(alarm->{
                    if(alarm.getEnergyName().equals(key)){
                        alarm.setEnergyName(key);
                        alarm.setCount(format2Double(value.size()));
                        double percentage = value.size() / jkHistoryAlarms.size() * 100;
                        alarm.setPercentage(format2Double(percentage));
                    }
                });
            });
            final Map<String, List<JkHistoryAlarm>> energyTypeMap = jkHistoryAlarms.stream().collect(Collectors.groupingBy(JkHistoryAlarm::getEnergyId));
            energyTypeMap.forEach((key, value) -> {
                energyProportionList.forEach(en->{
                    if(en.getEnergyName().equals(key)){
                        en.setEnergyName(key);
                        en.setCount(format2Double(value.size()));
                        double percentage = value.size() / jkHistoryAlarms.size() * 100;
                        en.setPercentage(format2Double(percentage));
                    }
                });
                
            });


            jkHistoryAlarms.forEach(jkHistoryAlarm -> {
                final String alarmBeginTime = DateUtil.format(jkHistoryAlarm.getAlarmBeginTime(), timeFormat);
                jkHistoryAlarm.setAlarmTime(alarmBeginTime);
            });
            final Map<String, List<JkHistoryAlarm>> timeMap = jkHistoryAlarms.stream().collect(Collectors.groupingBy(JkHistoryAlarm::getAlarmTime));
            while (!beginTime.after(endTime)) {
                final String currentTime = DateUtil.format(beginTime, timeFormat);
                final List<JkHistoryAlarm> value = timeMap.get(currentTime);
                ChartData chartData = new ChartData();
                chartData.setXData(currentTime);
                chartData.setYValue(null == value?0:(double)value.size());
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
        alarmAnalysisVO.setAlarmProportion(alarmProportionList);
        alarmAnalysisVO.setEnergyProportion(energyProportionList);
        alarmAnalysisVO.setChartDataList(chartDataList);
        return alarmAnalysisVO;
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

        // 查询模型下的点位数据
        ModelNode modelNode = modelNodeMapper.selectModelNodeById(alarmAnalysisDTO.getNodeId());
        List<ModelNodeIndexInfo> nodeIndexInfoList = modelNodeMapper.getAllModelNodeIndexByAddress(modelNode.getModelCode(), modelNode.getAddress());
        alarmAnalysisVO.setIndexCount(nodeIndexInfoList.size());
        if (CollectionUtils.isEmpty(nodeIndexInfoList)) {
            return alarmAnalysisVO;
        }

        // 获取月报警数、年报警数
        List<String> nodeIdList = nodeIndexInfoList.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toList());

        DateTime beginOfMonth = DateUtil.beginOfMonth(new Date());
        DateTime endOfMonth = DateUtil.endOfMonth(new Date());
        DateTime beginOfYear = DateUtil.beginOfYear(new Date());
        DateTime endOfYear = DateUtil.endOfYear(new Date());
        Integer monthCount = historyAlarmMapper.selectCountByTime(beginOfMonth,endOfMonth, nodeIdList);
        Integer yearCount = historyAlarmMapper.selectCountByTime(beginOfYear,endOfYear, nodeIdList);

        alarmAnalysisVO.setMonthCount(monthCount);
        alarmAnalysisVO.setYearCount(yearCount);
        return alarmAnalysisVO;
    }
}
