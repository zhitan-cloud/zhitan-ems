package com.zhitan.energyMonitor.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.common.utils.ChartUtils;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.DoubleUtil;
import com.zhitan.energyMonitor.domain.vo.ElectricPowerFactorDetail;
import com.zhitan.energyMonitor.domain.vo.ElectricPowerFactorItem;
import com.zhitan.energyMonitor.domain.vo.ElectricPowerFactorVO;
import com.zhitan.energyMonitor.service.IElectricPowerFactorService;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.service.RealtimeDatabaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: sensor_alarm_item
 * @Author: jeecg-boot
 * @Date: 2022-04-19
 * @Version: V1.0
 */
@Slf4j
@Service
public class ElectricPowerFactorServiceImpl implements IElectricPowerFactorService {
    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;

    @Override
    public ElectricPowerFactorVO list(String timeCode, EnergyIndex energyIndex) {
        ElectricPowerFactorVO vo = new ElectricPowerFactorVO();
        List<ElectricPowerFactorItem> itemList = new ArrayList<>();
        ElectricPowerFactorDetail detail = new ElectricPowerFactorDetail();
        detail.setMaxTime(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMax(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMin(CommonConst.DOUBLE_MINUS_SIGN);
        detail.setMinTime(CommonConst.DOUBLE_MINUS_SIGN);
        vo.setItemList(itemList);
        vo.setDetail(detail);

        List<Date> dateList = new ArrayList<>();
        ChartUtils.generateDateList(TimeTypeConst.TIME_TYPE_DAY, timeCode, dateList);
        double max = CommonConst.DIGIT_0, min = CommonConst.MIN_INIT_VALUE;
        //dateList计数
        Date now = new Date();
        //当前时间转成整时整点
        now = DateTimeUtil.getHourTime(TimeTypeConst.TIME_TYPE_HOUR, now);
        int i = CommonConst.DIGIT_0;
        for (Date date : dateList) {
            ElectricPowerFactorItem temp = new ElectricPowerFactorItem();
            temp.setTimeCode(ChartUtils.getTimeCodeChart(TimeTypeConst.TIME_TYPE_DAY, date));
            temp.setValue(CommonConst.DOUBLE_MINUS_SIGN);
            itemList.add(temp);
            // 如果大于当前时间默认--
            if (DateTimeUtil.compareDateDiff(date, now) > CommonConst.DIGIT_0) {
                i++;
                continue;
            }
            TagValue tagValue = new TagValue();
            try {
                TagValue retrieve = realtimeDatabaseService.retrieve(energyIndex.getCode(), date);
                if (ObjectUtil.isNotEmpty(retrieve)) {
                    tagValue = retrieve;
                }
            } catch (Exception e) {
                log.error("获取功率因数异常：" + e.getMessage());
            }
            if (ObjectUtil.isNotEmpty(tagValue.getValue())) {
                double value = DoubleUtil.formatDouble(tagValue.getValue());

                temp.setValue(String.valueOf(value));
                //给最小值赋值第一条
                if (i == CommonConst.DIGIT_0) {
                    min = Double.parseDouble(temp.getValue());
                }
                if (value > max) {
                    max = value;
                    detail.setMax(max + CommonConst.EMPTY);
                    detail.setMaxTime(DateTimeUtil.getDateTime(date));
                }
                if (value <= min) {
                    min = value;
                    detail.setMin(min + CommonConst.EMPTY);
                    detail.setMinTime(DateTimeUtil.getDateTime(date));
                }
            }
            i++;
        }

        detail.setAvg(CommonConst.DOUBLE_MINUS_SIGN);
        if (ObjectUtil.isNotEmpty(itemList)) {
            double avg = CommonConst.DIGIT_DOUBLE_0;
            for (ElectricPowerFactorItem li : itemList) {
                try {
                    if (!li.getValue().equals(CommonConst.DOUBLE_MINUS_SIGN)) {
                        avg += DoubleUtil.toDouble(li.getValue());
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
            detail.setAvg(DoubleUtil.formatDoubleToStr(avg / itemList.size()));
        }

        return vo;
    }

}
