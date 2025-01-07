package com.zhitan.alarm.services;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.LimitType;

import java.util.List;

/**
 * @Description
 * @Author zhoubg
 * @Date 2024/10/11
 */
public interface IAlarmLimitTypeService {

    int insertAlarmLimitType(LimitType alarmLimitType);

    List<LimitType> selectAlarmLimitTypeList(LimitType alarmLimitType);

    int updateAlarmLimitType(LimitType alarmLimitType);

    int deleteLimitTypeByIds(String[] ids);

    LimitType selectAlarmLimitTypeById(String id);


    Page<LimitType> selectAlarmLimitTypePage(LimitType alarmLimitType, Long pageNum, Long pageSize);
}
