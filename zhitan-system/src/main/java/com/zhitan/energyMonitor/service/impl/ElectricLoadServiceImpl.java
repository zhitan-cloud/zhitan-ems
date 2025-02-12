package com.zhitan.energyMonitor.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.zhitan.basicdata.domain.MeterImplement;
import com.zhitan.basicdata.mapper.MeterImplementMapper;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.common.enums.CollectionModes;
import com.zhitan.common.enums.GroupTimeType;
import com.zhitan.common.utils.ChartUtils;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.DoubleUtil;
import com.zhitan.common.utils.StringUtil;
import com.zhitan.energyMonitor.domain.vo.ListElectricLoadDetail;
import com.zhitan.energyMonitor.domain.vo.ListElectricLoadItem;
import com.zhitan.energyMonitor.domain.vo.ListElectricLoadVO;
import com.zhitan.energyMonitor.domain.vo.ListElectricityMeterVO;
import com.zhitan.energyMonitor.service.IElectricLoadService;
import com.zhitan.knowledgeBase.domain.enums.EnergyTypeEnum;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.service.RealtimeDatabaseService;
import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
public class ElectricLoadServiceImpl implements IElectricLoadService {
    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;
    @Resource
    private MeterImplementMapper meterImplementMapper;

    @Override
    public ListElectricLoadVO list(String timeType, String timeCode, EnergyIndex energyIndex) {
        ListElectricLoadVO vo = new ListElectricLoadVO();
        List<ListElectricLoadItem> itemList = new ArrayList<>();
        vo.setItemList(itemList);
        ListElectricLoadDetail detail = new ListElectricLoadDetail();
        detail.setMaxTime(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMax(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMin(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMinTime(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setAvg(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setRate(CommonConst.DOUBLE_MINUS_SIGN);
        vo.setDetail(detail);

        MeterImplement meterImplement = meterImplementMapper.selectById(energyIndex.getMeterId());

        if (ObjectUtil.isEmpty(meterImplement)) {
            return vo;
        }
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(timeType, timeCode, dateList);
        // 因为influxdb没有按照月分组取数据，只能按照日期循环取数据
        if (TimeTypeConst.TIME_TYPE_YEAR.equals(timeType)) {
            getYearData(timeType, dateList, energyIndex, meterImplement.getMeterName(), itemList);
        } else {
            getDayAndMonthData(timeType, timeCode, energyIndex, meterImplement.getMeterName(), itemList);
        }
        if (!StringUtil.isEmptyOrNull(energyIndex.getCode())) {
            Date start = ChartUtils.getDateTime(timeType, timeCode);
            Date end = ChartUtils.getEndTime(timeType, start);

            String code = energyIndex.getCode();
            TagValue maxTagValueModel = realtimeDatabaseService.statistics(code, start, end, CollectionModes.max);
            TagValue minTagValueModel = realtimeDatabaseService.statistics(code, start, end, CollectionModes.min);
            TagValue avgTagValueModel = realtimeDatabaseService.statistics(code, start, end, CollectionModes.mean);

            double avg = CommonConst.DIGIT_DOUBLE_0;
            if (!ObjectUtil.isEmpty(avgTagValueModel) && !ObjectUtil.isEmpty(avgTagValueModel.getValue())) {
                detail.setAvg(DoubleUtil.formatDoubleToStr(avgTagValueModel.getValue()) + CommonConst.ELECTRIC_LOAD_UNIT);
                avg = avgTagValueModel.getValue();
            }

            if (!ObjectUtil.isEmpty(maxTagValueModel) && !ObjectUtil.isEmpty(maxTagValueModel.getValue())) {
                detail.setMax(DoubleUtil.formatDoubleToStr(maxTagValueModel.getValue()));
                detail.setMaxTime(DateTimeUtil.getDateTime(maxTagValueModel.getDataTime(), DateTimeUtil.COMMON_PATTERN));

                if (maxTagValueModel.getValue() != CommonConst.DIGIT_DOUBLE_0) {
                    double rate = avg * CommonConst.DIGIT_DOUBLE_100 / maxTagValueModel.getValue();
                    detail.setRate(DoubleUtil.formatDoubleToStr(rate) + CommonConst.SYMBOL_PERCENT);
                }
            }

            if (!ObjectUtil.isEmpty(minTagValueModel) && !ObjectUtil.isEmpty(minTagValueModel.getValue())) {
                detail.setMin(DoubleUtil.formatDoubleToStr(minTagValueModel.getValue()));
                detail.setMinTime(DateTimeUtil.getDateTime(minTagValueModel.getDataTime(), DateTimeUtil.COMMON_PATTERN));
            }

            if (!CommonConst.DOUBLE_MINUS_SIGN.equals(detail.getMax())) {
                String m = detail.getMax();
                detail.setMax(m + CommonConst.ELECTRIC_LOAD_UNIT);
            }
            if (!CommonConst.DOUBLE_MINUS_SIGN.equals(detail.getMin())) {
                String min = detail.getMin();
                detail.setMin(min + CommonConst.ELECTRIC_LOAD_UNIT);
            }
            if (CommonConst.DOUBLE_MINUS_SIGN.equals(detail.getMax())) {
                detail.setAvg(CommonConst.DOUBLE_MINUS_SIGN);
            }
        }

        return vo;
    }

    /**
     * 获取月和天数据,因为influxdb可以按照分。时。天分组取数，不可以按照月分组取数，所以分成两个方法来写
     */
    private void getDayAndMonthData(String timeType, String timeCode, EnergyIndex energyIndex, String meterName, List<ListElectricLoadItem> itemList) {
        String tagCodes = energyIndex.getCode();
        List<TagValue> maxList = new ArrayList<>();
        List<TagValue> minList = new ArrayList<>();
        List<TagValue> avgList = new ArrayList<>();
        if (TimeTypeConst.TIME_TYPE_DAY.equals(timeType)) {
        } else {
            String tempTimeCode = StringUtil.ifEmptyOrNullReturnValue(timeCode).replace(CommonConst.SINGLE_MINUS_SIGN, CommonConst.EMPTY);
            Date start = DateTimeUtil.toDateTime(tempTimeCode, DateTimeUtil.COMMON_PATTERN_MONTH);
            Date end = DateTimeUtil.addMonths(start, CommonConst.DIGIT_1);
            if (!StringUtil.isEmptyOrNull(tagCodes)) {
                maxList = realtimeDatabaseService.statistics(tagCodes, start, end, CollectionModes.max, GroupTimeType.d);
                minList = realtimeDatabaseService.statistics(tagCodes, start, end, CollectionModes.min, GroupTimeType.d);
                avgList = realtimeDatabaseService.statistics(tagCodes, start, end, CollectionModes.mean, GroupTimeType.d);
            }
        }
        if (CollectionUtils.isEmpty(maxList)) {
            maxList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(minList)) {
            minList = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(avgList)) {
            avgList = new ArrayList<>();
        }
        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(timeType, timeCode, dateList);
        Date now = new Date();
        Date temNow = now;
        switch (timeType) {
            case TimeTypeConst.TIME_TYPE_DAY:
                //当前时间转成整时整点
                temNow = DateTimeUtil.getHourTime(TimeTypeConst.TIME_TYPE_HOUR, now);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                //当前时间转成整时整点
                temNow = DateTimeUtil.getHourTime(TimeTypeConst.TIME_TYPE_DAY, now);
                break;
            default:
                break;
        }
        for (Date date : dateList) {
            ListElectricLoadItem temp = new ListElectricLoadItem();
            temp.setTimeCode(ChartUtils.getTimeCode(timeType, date));
            temp.setName(ObjectUtils.isNotEmpty(meterName) ? meterName : "");
            temp.setMax(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setMin(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setAvg(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setTimeCodeChart(ChartUtils.getTimeCodeChart(timeType, date));
            temp.setRate(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setValue(CommonConst.DOUBLE_MINUS_SIGN);
            itemList.add(temp);
            // 如果大于当前时间默认--
            if (DateTimeUtil.compareDateDiff(date, temNow) > CommonConst.DIGIT_0) {
                continue;
            }
            //天的判断
            if (TimeTypeConst.TIME_TYPE_DAY.equals(timeType)) {
                // 由于实时库返回的时间对应值代表的是前一个周期的值
                Date nextHour = DateTimeUtil.addHours(date, CommonConst.DIGIT_1);
                if (!StringUtil.isEmptyOrNull(energyIndex.getCode())) {
                    TagValue tagValue = realtimeDatabaseService.retrieve(energyIndex.getCode(), nextHour);
                    if (!ObjectUtil.isEmpty(tagValue)) {
                        if (ObjectUtils.isEmpty(tagValue) || ObjectUtils.isEmpty(tagValue.getValue())) {
                            temp.setValue(CommonConst.DOUBLE_MINUS_SIGN);
                        } else {
                            temp.setValue(String.valueOf(DoubleUtil.formatDouble(tagValue.getValue())));
                        }
                    }
                }
            } else {
                //月的判断，由于实时库返回的时间对应值代表的是前一个周期的值
                Date nextDay = DateTimeUtil.addDays(date, CommonConst.DIGIT_1);
                TagValue tagValueMonth = maxList.stream().filter(x -> (new DateTime(x.getDataTime())).withTimeAtStartOfDay().toDate().equals(nextDay)).findAny().orElse(null);
                if (!ObjectUtil.isEmpty(tagValueMonth)) {
                    //月的找天
                    TagValue minModel = minList.stream().filter(x -> x.getDataTime().equals(tagValueMonth.getDataTime())).findAny().orElse(null);
                    if (!ObjectUtil.isEmpty(minModel)) {
                        temp.setMin(String.valueOf(DoubleUtil.formatDouble(minModel.getValue())));
                    }
                    TagValue avgModel = avgList.stream().filter(x -> x.getDataTime().equals(tagValueMonth.getDataTime())).findAny().orElse(null);
                    if (!ObjectUtil.isEmpty(avgModel)) {
                        temp.setAvg(String.valueOf(DoubleUtil.formatDouble(avgModel.getValue())));
                    }
                    temp.setMax(String.valueOf(DoubleUtil.formatDouble(tagValueMonth.getValue())));

                    temp.setRate(CommonConst.DOUBLE_MINUS_SIGN);
                    if (Double.parseDouble(temp.getMax()) != CommonConst.DIGIT_DOUBLE_0) {
                        double rate = Double.parseDouble(temp.getAvg()) * 100 / Double.parseDouble(temp.getMax());
                        temp.setRate(DoubleUtil.formatDoubleToStr(rate) + CommonConst.SYMBOL_PERCENT);
                    }
                }
            }
        }
    }

    /**
     * 获取年数据
     */
    private void getYearData(String timeType, List<Date> dateList, EnergyIndex energyIndex, String meterName, List<ListElectricLoadItem> itemList) {
        String tagCode = StringUtil.ifEmptyOrNullReturnValue(energyIndex.getCode());
        for (Date date : dateList) {
            ListElectricLoadItem temp = new ListElectricLoadItem();
            Date endTime = DateTimeUtil.addMonths(date, CommonConst.DIGIT_1);
            temp.setAvg(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setMax(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setMin(CommonConst.DOUBLE_MINUS_SIGN);
            if (!StringUtil.isEmptyOrNull(tagCode)) {
                String code = energyIndex.getCode();
                TagValue rt3 = realtimeDatabaseService.statistics(code, date, endTime, CollectionModes.max);
                TagValue rt4 = realtimeDatabaseService.statistics(code, date, endTime, CollectionModes.min);
                TagValue rt2 = realtimeDatabaseService.statistics(code, date, endTime, CollectionModes.mean);
                if (ObjectUtils.isNotEmpty(rt2.getValue())) {
                    temp.setAvg(String.valueOf(DoubleUtil.formatDouble(rt2.getValue())));
                }
                if (ObjectUtils.isNotEmpty(rt3.getValue())) {
                    temp.setMax(String.valueOf(DoubleUtil.formatDouble(rt3.getValue())));
                }
                if (ObjectUtils.isNotEmpty(rt4.getValue())) {
                    temp.setMin(String.valueOf(DoubleUtil.formatDouble(rt4.getValue())));
                }
            }

            temp.setTimeCode(ChartUtils.getTimeCode(timeType, date));
            temp.setTimeCodeChart(ChartUtils.getTimeCodeChart(timeType, date));
            temp.setName(StringUtil.ifEmptyOrNullReturnValue(meterName));
            temp.setRate(CommonConst.DOUBLE_MINUS_SIGN);
            temp.setValue(CommonConst.DOUBLE_MINUS_SIGN);
            if (!temp.getMax().equals(CommonConst.DOUBLE_MINUS_SIGN) && Double.parseDouble(temp.getMax()) != CommonConst.DIGIT_DOUBLE_0) {
                double rate = Double.parseDouble(temp.getAvg()) * 100 / Double.parseDouble(temp.getMax());
                temp.setRate(DoubleUtil.formatDoubleToStr(rate) + CommonConst.SYMBOL_PERCENT);
            }
            itemList.add(temp);
        }
    }


    /**
     * 获取电表列表
     */
    @Override
    public List<ListElectricityMeterVO> listElectricMeter(String nodeId) {
        List<MeterImplement> meterImplements = meterImplementMapper.selectByNodeId(nodeId);
        meterImplements = meterImplements.stream().filter(x -> "electric".equals(x.getEnergyType())).collect(Collectors.toList());
        List<ListElectricityMeterVO> list = new ArrayList<>();
        for (MeterImplement meterImplement : meterImplements) {
            ListElectricityMeterVO vo = new ListElectricityMeterVO();
            vo.setCode(meterImplement.getId());
            vo.setLabel(meterImplement.getMeterName());
            list.add(vo);
        }
        return list;
    }
}
