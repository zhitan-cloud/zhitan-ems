package com.zhitan.energyMonitor.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.mapper.MeterImplementMapper;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.common.utils.*;
import com.zhitan.energyMonitor.domain.vo.ElectricThreePhaseDetail;
import com.zhitan.energyMonitor.domain.vo.ElectricThreePhaseItem;
import com.zhitan.energyMonitor.domain.vo.ElectricThreePhaseTempModel;
import com.zhitan.energyMonitor.domain.vo.ElectricThreePhaseVO;
import com.zhitan.energyMonitor.service.IElectricThreePhaseService;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.service.RealtimeDatabaseService;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: sensor_alarm_item
 * @Author: jeecg-boot
 * @Date: 2022-04-19
 * @Version: V1.0
 */
@Service
public class ElectricThreePhaseServiceImpl implements IElectricThreePhaseService {

    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;
    @Resource
    private MeterImplementMapper meterImplementMapper;

    /**
     * 获取三相不平衡数据
     *
     * @param timeType        时间类型
     * @param timeCode        时间编码
     * @param energyIndexList 点位集合
     * @param requestType     类型
     * @return ElectricThreePhaseVo
     * @Date 14:27 2022/5/30
     **/
    @Override
    public ElectricThreePhaseVO list(String timeType, String timeCode, List<EnergyIndex> energyIndexList, String requestType, String meterId) {
        ElectricThreePhaseVO vo = new ElectricThreePhaseVO();
        if (ObjectUtil.isEmpty(energyIndexList)) {
            return vo;
        }
        // 获取电压不平衡数据
        if (CommonConst.STR_NUMBER_0.equals(requestType)) {
            energyIndexList = energyIndexList.stream()
                    .filter(x -> StringUtil.ifEmptyOrNullReturnValue(x.getCode()).trim().endsWith(CommonConst.TAG_CODE_VOLTAGE_A)
                            || StringUtil.ifEmptyOrNullReturnValue(x.getCode()).trim().endsWith(CommonConst.TAG_CODE_VOLTAGE_B)
                            || StringUtil.ifEmptyOrNullReturnValue(x.getCode()).trim().endsWith(CommonConst.TAG_CODE_VOLTAGE_C)).collect(Collectors.toList());
        } else {
            energyIndexList = energyIndexList.stream()
                    .filter(x -> StringUtil.ifEmptyOrNullReturnValue(x.getCode()).trim().endsWith(CommonConst.TAG_CODE_CURRENT_A)
                            || StringUtil.ifEmptyOrNullReturnValue(x.getCode()).trim().endsWith(CommonConst.TAG_CODE_CURRENT_B)
                            || StringUtil.ifEmptyOrNullReturnValue(x.getCode()).trim().endsWith(CommonConst.TAG_CODE_CURRENT_C))
                    .collect(Collectors.toList());
        }
        List<String> tagCodeList = energyIndexList.stream().map(EnergyIndex::getCode).collect(Collectors.toList());
        tagCodeList.add(CommonConst.STR_NUMBER_MINUS_ONE);
        String tagCodes = String.join(StrUtil.COMMA, tagCodeList);

        Date start = ChartUtils.getDateTime(timeType, timeCode);
        Date end = getEndTime(timeType, start);

        DateTime begin = new DateTime(start);
        DateTime finish = new DateTime(end);
        long millis = new Duration(begin, finish).getMillis();
        int pointCount = IntegerUtil.toInt(millis / CommonConst.DIGIT_3600 / CommonConst.DIGIT_1000);

        List<TagValue> tagValueList = realtimeDatabaseService.retrieve(tagCodes, start, end, pointCount);
        List<ElectricThreePhaseItem> itemList = new ArrayList<>();
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(timeType, timeCode, dateList);
        ElectricThreePhaseTempModel tempModel = new ElectricThreePhaseTempModel();

        Date now = new Date();
        for (Date date : dateList) {
            Date tempDate = date;
            Date temNow = now;
            switch (timeType) {
                case TimeTypeConst.TIME_TYPE_DAY:
                    tempDate = DateTimeUtil.addHours(date, CommonConst.DIGIT_1);
                    //当前时间转成整时整点
                    temNow = DateTimeUtil.getHourTime(TimeTypeConst.TIME_TYPE_HOUR, now);
                    break;
                case TimeTypeConst.TIME_TYPE_MONTH:
                    tempDate = date;
                    //当前时间转成整时整点
                    temNow = DateTimeUtil.getHourTime(TimeTypeConst.TIME_TYPE_DAY, now);
                    break;
                case TimeTypeConst.TIME_TYPE_YEAR:
                    tempDate = date;
                    temNow = DateTimeUtil.getHourTime(TimeTypeConst.TIME_TYPE_MONTH, now);
                    break;
                default:
                    break;
            }
            MeterImplement meterImplement = meterImplementMapper.selectById(meterId);

            ElectricThreePhaseItem temp = new ElectricThreePhaseItem();
            if (ObjectUtil.isNotEmpty(meterImplement)) {
                temp.setName(meterImplement.getMeterName());
            }
            temp.setTimeCode(ChartUtils.getTimeCode(timeType, date));
            temp.setTimeCodeChart(ChartUtils.getTimeCodeChart(timeType, date));
            temp.setValueA(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setValueB(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setValueC(CommonConst.DOUBLE_MINUS_SIGN);
            itemList.add(temp);
            // 如果大于当前时间默认--
            if (DateTimeUtil.compareDateDiff(date, temNow) > CommonConst.DIGIT_0) {
                continue;
            }
            switch (timeType) {
                case TimeTypeConst.TIME_TYPE_DAY:
                    // 构造当天每个小时的数据
                    listDayData(tempDate, tagValueList, temp, tempModel);
                    break;
                case TimeTypeConst.TIME_TYPE_MONTH:
                    // 构造当月每天的数据
                    listMonthData(tempDate, tagValueList, temp, tempModel);
                    break;
                case TimeTypeConst.TIME_TYPE_YEAR:
                    // 构造当年每月的数据
                    listYearData(tempDate, tagValueList, temp, tempModel);
                    break;
                default:
                    break;
            }
        }

        if (ObjectUtil.isEmpty(tempModel.getMin()) || CommonConst.MIN_INIT_VALUE == tempModel.getMin()) {
            tempModel.setMin(null);
        }
        //设置值
        ElectricThreePhaseDetail detail = new ElectricThreePhaseDetail();
        BeanUtils.copyProperties(tempModel, detail);
        detail.setMax(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMaxTime(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setValueMaxA(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setValueMaxB(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setValueMaxC(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMin(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMinTime(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setValueMinA(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setValueMinB(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setValueMinC(CommonConst.DOUBLE_MINUS_SIGN);
        if (ObjectUtil.isNotNull(tempModel.getMax())) {
            detail.setMax(DoubleUtil.formatDoubleToStr(tempModel.getMax()) + CommonConst.SYMBOL_PERCENT);
            detail.setMaxTime(DateTimeUtil.getDateTime(tempModel.getMaxTime()));
            detail.setValueMaxA(DoubleUtil.formatDoubleToStr(tempModel.getValueMaxA()));
            detail.setValueMaxB(DoubleUtil.formatDoubleToStr(tempModel.getValueMaxB()));
            detail.setValueMaxC(DoubleUtil.formatDoubleToStr(tempModel.getValueMaxC()));
        }
        if (ObjectUtil.isNotNull(tempModel.getMin())) {
            detail.setMin(DoubleUtil.formatDoubleToStr(tempModel.getMin()) + CommonConst.SYMBOL_PERCENT);
            detail.setMinTime(DateTimeUtil.getDateTime(tempModel.getMinTime()));
            detail.setValueMinA(DoubleUtil.formatDoubleToStr(tempModel.getValueMinA()));
            detail.setValueMinB(DoubleUtil.formatDoubleToStr(tempModel.getValueMinB()));
            detail.setValueMinC(DoubleUtil.formatDoubleToStr(tempModel.getValueMinC()));
        }
        vo.setDetail(detail);
        vo.setItemList(itemList);
        return vo;
    }

    /**
     * 构造当月的数据列表
     *
     * @param date
     * @param tagValueList
     * @param temp
     * @param tempModel
     */
    private void listYearData(Date date, List<TagValue> tagValueList, ElectricThreePhaseItem temp, ElectricThreePhaseTempModel tempModel) {
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(TimeTypeConst.TIME_TYPE_MONTH, DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_MONTH), dateList);
        ElectricThreePhaseTempModel tempModelMonth = new ElectricThreePhaseTempModel();
        for (Date date1 : dateList) {
            listMonthData(date1, tagValueList, temp, tempModelMonth);
            temp.setMax(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setMin(CommonConst.DOUBLE_MINUS_SIGN);
            if (ObjectUtil.isNotNull(tempModelMonth.getMax())) {
                temp.setMax(DoubleUtil.formatDoubleToStr(tempModelMonth.getMax()));
            }
            if (ObjectUtil.isNotNull(tempModelMonth.getMin())) {
                temp.setMin(DoubleUtil.formatDoubleToStr(tempModelMonth.getMin()));
            }
            if (ObjectUtil.isEmpty(tempModel.getMin()) || CommonConst.MIN_INIT_VALUE == tempModel.getMin()) {
                tempModel.setMin(null);
            }
            if (ObjectUtil.isNotNull(tempModelMonth.getMax())) {
                if (ObjectUtil.isNull(tempModel.getMax()) || (ObjectUtil.isNotNull(tempModel.getMax()) && tempModelMonth.getMax() > tempModel.getMax())) {
                    tempModel.setMax(DoubleUtil.formatDouble(tempModelMonth.getMax()));
                    tempModel.setMaxTime(tempModelMonth.getMaxTime());
                    tempModel.setValueMaxA(DoubleUtil.formatDouble(tempModelMonth.getValueMaxA()));
                    tempModel.setValueMaxB(DoubleUtil.formatDouble(tempModelMonth.getValueMaxB()));
                    tempModel.setValueMaxC(DoubleUtil.formatDouble(tempModelMonth.getValueMaxC()));
                }
            }
            if (ObjectUtil.isNotNull(tempModelMonth.getMin())) {
                if (ObjectUtil.isNull(tempModel.getMin()) || (ObjectUtil.isNotNull(tempModel.getMin()) && tempModelMonth.getMin() <= tempModel.getMin())) {
                    tempModel.setMin(DoubleUtil.formatDouble(tempModelMonth.getMin()));
                    tempModel.setMinTime(tempModelMonth.getMinTime());
                    tempModel.setValueMinA(DoubleUtil.formatDouble(tempModelMonth.getValueMinA()));
                    tempModel.setValueMinB(DoubleUtil.formatDouble(tempModelMonth.getValueMinB()));
                    tempModel.setValueMinC(DoubleUtil.formatDouble(tempModelMonth.getValueMinC()));
                }
            }
        }
    }

    /**
     * 构造当月的数据列表
     *
     * @param date
     * @param tagValueList
     * @param temp
     * @param tempModel
     */
    private void listMonthData(Date date, List<TagValue> tagValueList, ElectricThreePhaseItem temp, ElectricThreePhaseTempModel tempModel) {
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(TimeTypeConst.TIME_TYPE_DAY, DateTimeUtil.getDateTime(date, DateTimeUtil.COMMON_PATTERN_TO_DAY), dateList);
        ElectricThreePhaseTempModel tempModelDay = new ElectricThreePhaseTempModel();
        for (Date date1 : dateList) {
            Date tempDate = DateTimeUtil.addHours(date1, CommonConst.DIGIT_1);
            listDayData(tempDate, tagValueList, temp, tempModelDay);
            temp.setMax(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setMin(CommonConst.DOUBLE_MINUS_SIGN);
            if (ObjectUtil.isNotNull(tempModelDay.getMax())) {
                temp.setMax(DoubleUtil.formatDoubleToStr(tempModelDay.getMax()));
            }
            if (ObjectUtil.isNotNull(tempModelDay.getMin())) {
                temp.setMin(DoubleUtil.formatDoubleToStr(tempModelDay.getMin()));
            }
            if (ObjectUtil.isEmpty(tempModel.getMin()) || CommonConst.MIN_INIT_VALUE == tempModel.getMin()) {
                tempModel.setMin(null);
            }
            if (ObjectUtil.isNotNull(tempModelDay.getMax())) {
                if (ObjectUtil.isNull(tempModel.getMax()) || (ObjectUtil.isNotNull(tempModel.getMax()) && tempModelDay.getMax() > tempModel.getMax())) {
                    tempModel.setMax(DoubleUtil.formatDouble(tempModelDay.getMax()));
                    tempModel.setMaxTime(tempModelDay.getMaxTime());
                    tempModel.setValueMaxA(DoubleUtil.formatDouble(tempModelDay.getValueMaxA()));
                    tempModel.setValueMaxB(DoubleUtil.formatDouble(tempModelDay.getValueMaxB()));
                    tempModel.setValueMaxC(DoubleUtil.formatDouble(tempModelDay.getValueMaxC()));
                }
            }
            if (ObjectUtil.isNotNull(tempModelDay.getMin())) {
                if (ObjectUtil.isNull(tempModel.getMin()) || (ObjectUtil.isNotNull(tempModel.getMin()) && tempModelDay.getMin() <= tempModel.getMin())) {
                    tempModel.setMin(DoubleUtil.formatDouble(tempModelDay.getMin()));
                    tempModel.setMinTime(tempModelDay.getMinTime());
                    tempModel.setValueMinA(DoubleUtil.formatDouble(tempModelDay.getValueMinA()));
                    tempModel.setValueMinB(DoubleUtil.formatDouble(tempModelDay.getValueMinB()));
                    tempModel.setValueMinC(DoubleUtil.formatDouble(tempModelDay.getValueMinC()));
                }
            }
        }
    }

    /**
     * 获取当天的数据列表
     *
     * @param date
     * @param tagValueList
     * @param tempModel
     */
    private void listDayData(Date date, List<TagValue> tagValueList, ElectricThreePhaseItem temp, ElectricThreePhaseTempModel tempModel) {
        Date endTime = DateTimeUtil.addHours(date, CommonConst.DIGIT_1);
        List<TagValue> currentTagValueList = tagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) <= 0 && DateTimeUtil.compareDateDiff(endTime, x.getDataTime()) > 0).collect(Collectors.toList());
        List<TagValue> currentATagValueList = currentTagValueList.stream().filter(x -> StringUtil.ifEmptyOrNullReturnValue(x.getTagCode()).trim().endsWith("_A")).collect(Collectors.toList());
        List<TagValue> currentBTagValueList = currentTagValueList.stream().filter(x -> StringUtil.ifEmptyOrNullReturnValue(x.getTagCode()).trim().endsWith("_B")).collect(Collectors.toList());
        List<TagValue> currentCTagValueList = currentTagValueList.stream().filter(x -> StringUtil.ifEmptyOrNullReturnValue(x.getTagCode()).trim().endsWith("_C")).collect(Collectors.toList());
        TagValue tagValueA = currentATagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        TagValue tagValueB = currentBTagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        TagValue tagValueC = currentCTagValueList.stream().filter(x -> DateTimeUtil.compareDateDiff(date, x.getDataTime()) == 0).findAny().orElse(null);
        Double valueA = null, valueB = null, valueC = null;
        if (ObjectUtil.isNotEmpty(tagValueA)) {
            valueA = tagValueA.getValue();
            temp.setValueA(String.valueOf(DoubleUtil.formatDouble(tagValueA.getValue())));
        } else {
            temp.setValueA(CommonConst.DOUBLE_MINUS_SIGN);
        }
        if (!ObjectUtil.isEmpty(tagValueB)) {
            valueB = tagValueB.getValue();
            temp.setValueB(String.valueOf(DoubleUtil.formatDouble(tagValueB.getValue())));
        } else {
            temp.setValueB(CommonConst.DOUBLE_MINUS_SIGN);
        }
        if (!ObjectUtil.isEmpty(tagValueC)) {
            valueC = tagValueC.getValue();
            temp.setValueC(String.valueOf(DoubleUtil.formatDouble(tagValueC.getValue())));
        } else {
            temp.setValueC(CommonConst.DOUBLE_MINUS_SIGN);
        }
        Double value = calcUnbalanceValue(valueA, valueB, valueC);
        if (ObjectUtil.isEmpty(value)) {
            return;
        }
        if (ObjectUtil.isEmpty(tempModel.getMin()) || CommonConst.MIN_INIT_VALUE == tempModel.getMin()) {
            tempModel.setMin(value);
        }
        if (ObjectUtil.isEmpty(tempModel.getMax()) || value > tempModel.getMax()) {
            tempModel.setMax(DoubleUtil.formatDouble(value));
            if (!ObjectUtil.isEmpty(tagValueA)) {
                tempModel.setMaxTime(tagValueA.getDataTime());
            }
            tempModel.setValueMaxA(DoubleUtil.formatDouble(valueA));
            tempModel.setValueMaxB(DoubleUtil.formatDouble(valueB));
            tempModel.setValueMaxC(DoubleUtil.formatDouble(valueC));
        }
        if (ObjectUtil.isEmpty(tempModel.getMin()) || value <= tempModel.getMin()) {
            tempModel.setMin(DoubleUtil.formatDouble(value));
            tempModel.setValueMinA(DoubleUtil.formatDouble(valueA));
            tempModel.setValueMinB(DoubleUtil.formatDouble(valueB));
            tempModel.setValueMinC(DoubleUtil.formatDouble(valueC));
            if (!ObjectUtil.isEmpty(tagValueA)) {
                tempModel.setMinTime(tagValueA.getDataTime());
            }
        }
    }

    /**
     * 计算三相不平衡极值
     *
     * @param valueA
     * @param valueB
     * @param valueC
     * @return
     */
    private Double calcUnbalanceValue(Double valueA, Double valueB, Double valueC) {
        /**
         * 1、计算三相平均电流，A/B/C三相电流相加除以3
         * 2、 MAX（相电流-三相平均电流）/三相平均电流；
         * 比如三相电流分别为IA=9A IB=8A IC=4A，则三相平均电流为7A，相电流-三相平均电流分别为2A 1A 3A，取差值最大那个，故MAX（相电流-三相平均电流）=3A，所以三相电流不平衡度=3/7。
         */
        Double result = null;
        Double sum = null;
        if (ObjectUtil.isNotNull(valueA)) {
            sum = valueA;
        }
        if (ObjectUtil.isNotNull(valueB)) {
            sum += valueB;
        }
        if (ObjectUtil.isNotNull(valueC)) {
            sum += valueC;
        }
        if (ObjectUtil.isNotNull(sum)) {
            double avg = sum / CommonConst.DIGIT_3;
            double diff1 = 0, diff2 = 0, diff3 = 0;
            if (ObjectUtil.isNotNull(valueA)) {
                diff1 = Math.abs(valueA - avg);
            }
            if (ObjectUtil.isNotNull(valueB)) {
                diff2 = Math.abs(valueB - avg);
            }
            if (ObjectUtil.isNotNull(valueC)) {
                diff3 = Math.abs(valueC - avg);
            }
            double max = diff1;
            if (diff2 > max) {
                max = diff2;
            }
            if (diff3 > max) {
                max = diff3;
            }
            if (avg != CommonConst.DIGIT_DOUBLE_0) {
                result = max * CommonConst.DIGIT_DOUBLE_100 / avg;
            }
        }

        return result;
    }

    /**
     * 获取对应的结束时间
     *
     * @param timeType
     * @param date
     * @return
     */
    public static Date getEndTime(String timeType, Date date) {
        Date d1 = null;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                d1 = DateTimeUtil.addDays(date, CommonConst.DIGIT_1);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                d1 = DateTimeUtil.addMonths(date, CommonConst.DIGIT_1);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                d1 = DateTimeUtil.addYears(date, CommonConst.DIGIT_1);
                break;
            default:
                break;
        }
        return d1;
    }
}
