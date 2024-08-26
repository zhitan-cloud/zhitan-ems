package com.dingzhuo.compute.engine.function;

import com.dingzhuo.compute.engine.message.alarm.AlarmStatus;
import com.dingzhuo.compute.engine.utils.ServiceProvicer;
import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.data.model.domain.LimitType;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmItem;
import com.dingzhuo.energy.data.monitoring.alarm.domain.AlarmJudgeDirection;
import com.dingzhuo.energy.dataservice.domain.CollectionModes;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.greenpineyu.fel.context.FelContext;
import com.greenpineyu.fel.function.CommonFunction;
import com.greenpineyu.fel.function.Function;

import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.Instant;

/**
 * 自定义函数
 *
 * @author fanxinfu
 */
public class CustomFunction {

    static Function accumulate = new CommonFunction() {
        @Override
        public Object call(Object[] arguments, FelContext felContext) {
            if (arguments == null || arguments.length == 0) {
                return 0;
            }

            String indexCode = String.valueOf(arguments[0]);
            String timeCode = String.valueOf(felContext.get("timeCode"));
            Date beginTime = TimeManager.getBeginTime(timeCode);
            Date endTime = TimeManager.getEndTime(timeCode);
            if (timeCode.startsWith("M") && endTime.after(Instant.now().toDate())) {
                endTime = Instant.now().toDate();
            }
            RealtimeDatabaseService service = ServiceProvicer.getRealtimeDatabaseService();
            TagValue beginValue = service.retrieve(indexCode, beginTime, timeCode);
            TagValue endValue = service.retrieve(indexCode, endTime, timeCode);
            if (beginValue == null || beginValue.getValue() == null) {
                return 0;
            }
            if (endValue == null || endValue.getValue() == null) {
                return 0;
            }

            if (beginValue.getValue() > endValue.getValue()) {
                TagValue maxValue = service
                        .statistics(indexCode, beginTime, endTime, CollectionModes.Maximum);
                TagValue minValue = service
                        .statistics(indexCode, beginTime, endTime, CollectionModes.Minimum);
                if (maxValue == null || maxValue.getValue() == null) {
                    return 0;
                }
                if (minValue == null || minValue.getValue() == null) {
                    return 0;
                }

                BigDecimal value = BigDecimal.valueOf(maxValue.getValue())
                        .subtract(BigDecimal.valueOf(beginValue.getValue()))
                        .add(BigDecimal.valueOf(endValue.getValue()))
                        .subtract(BigDecimal.valueOf(minValue.getValue()));
                return value.doubleValue();
            }

            BigDecimal value = BigDecimal.valueOf(endValue.getValue())
                    .subtract(BigDecimal.valueOf(beginValue.getValue()));
            return value.doubleValue();
        }

        @Override
        public String getName() {
            return "accumulate";
        }
    };

    static Function get = new CommonFunction() {
        @Override
        public Object call(Object[] arguments, FelContext felContext) {
            if (arguments == null || arguments.length == 0) {
                return 0;
            }

            String indexCode = String.valueOf(arguments[0]);
            String timeCode = String.valueOf(felContext.get("timeCode"));
            PeriodDataService service = ServiceProvicer.getPeriodDataService();
            DataItem item = service.getDataByIndexCode(indexCode, timeCode);
            return item == null ? 0 : item.getValue();
        }

        @Override
        public String getName() {
            return "get";
        }
    };

    static Function limitRealtimeAlarm = new CommonFunction() {
        @Override
        public Object call(Object[] arguments, FelContext felContext) {
            if (arguments == null || arguments.length == 0) {
                return 0;
            }

            String actorId = String.valueOf(arguments[0]);
            AlarmItem item = ServiceProvicer.getCacheService().getAlarmItem(actorId);
            if (item == null) {
                return false;
            }

            boolean isAlarm = false;
            TagValue tagValue = ServiceProvicer.getCacheService().getTagValue(item.getIndexCode());
            if (tagValue == null) {
                tagValue = ServiceProvicer.getRealtimeDatabaseService().retrieve(item.getIndexCode());
            }

            if (tagValue == null || tagValue.getValue() == null) {
                // 取不到数时保持上次状态
                AlarmStatus lastStatus = ServiceProvicer.getCacheService()
                        .getAlarmStatus(item.getDwid(), item.getTimeSlot(), item.getLimitType());
                isAlarm = lastStatus != null && lastStatus.isAlarm();
            } else {
                LimitType limitType = ServiceProvicer.getCacheService().getLimitType(item.getLimitType());
                AlarmJudgeDirection judge = AlarmJudgeDirection.value(limitType.getComparatorOperator());
                Double limitValue = Double.parseDouble(item.getLimitVal());
                if (judge == AlarmJudgeDirection.G) {
                    isAlarm = tagValue.getValue() > limitValue;
                } else if (judge == AlarmJudgeDirection.GE) {
                    isAlarm = tagValue.getValue() >= limitValue;
                } else if (judge == AlarmJudgeDirection.L) {
                    isAlarm = tagValue.getValue() < limitValue;
                } else if (judge == AlarmJudgeDirection.LE) {
                    isAlarm = tagValue.getValue() <= limitValue;
                } else if (judge == AlarmJudgeDirection.E) {
                    isAlarm = Math.abs(tagValue.getValue() - limitValue) < 0.000000001;
                }
            }

            ServiceProvicer.getCacheService().cacheTagValue(tagValue);
            return isAlarm;
        }

        @Override
        public String getName() {
            return "limitRealtimeAlarm";
        }
    };

    static Function limitPeriodAlarm = new CommonFunction() {
        @Override
        public Object call(Object[] arguments, FelContext felContext) {
            if (arguments == null || arguments.length == 0) {
                return 0;
            }

            String actorId = String.valueOf(arguments[0]);
            AlarmItem item = ServiceProvicer.getCacheService().getAlarmItem(actorId);
            boolean isAlarm = false;
            String timeCode = felContext.get("timeCode").toString();
            DataItem dataItem = ServiceProvicer.getPeriodDataService()
                    .getDataByIndex(item.getDwid(), timeCode);

            if (dataItem == null || dataItem.getValue() == null) {
                // 取不到数时保持上次状态
                AlarmStatus lastStatus = ServiceProvicer.getCacheService()
                        .getAlarmStatus(item.getDwid(), item.getTimeSlot(), item.getLimitType());
                isAlarm = lastStatus != null && lastStatus.isAlarm();
            } else {
                LimitType limitType = ServiceProvicer.getCacheService().getLimitType(item.getLimitType());
                AlarmJudgeDirection judge = AlarmJudgeDirection.value(limitType.getComparatorOperator());
                Double limitValue = Double.parseDouble(item.getLimitVal());
                if (judge == AlarmJudgeDirection.G) {
                    isAlarm = dataItem.getValue() > limitValue;
                } else if (judge == AlarmJudgeDirection.GE) {
                    isAlarm = dataItem.getValue() >= limitValue;
                } else if (judge == AlarmJudgeDirection.L) {
                    isAlarm = dataItem.getValue() < limitValue;
                } else if (judge == AlarmJudgeDirection.LE) {
                    isAlarm = dataItem.getValue() <= limitValue;
                } else if (judge == AlarmJudgeDirection.E) {
                    isAlarm = Math.abs(dataItem.getValue() - limitValue) < 0.000000001;
                }
            }

            return isAlarm;
        }

        @Override
        public String getName() {
            return "limitPeriodAlarm";
        }
    };

}
