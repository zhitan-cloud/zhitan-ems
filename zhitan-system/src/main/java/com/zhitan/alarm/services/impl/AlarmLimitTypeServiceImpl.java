package com.zhitan.alarm.services.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.LimitType;
import com.zhitan.alarm.mapper.AlarmLimitTypeMapper;
import com.zhitan.alarm.services.IAlarmLimitTypeService;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @Description
 * @Author zhoubg
 * @Date 2024/10/11
 */
@Service
public class AlarmLimitTypeServiceImpl implements IAlarmLimitTypeService {

    @Autowired
    private AlarmLimitTypeMapper alarmLimitTypeMapper;

    @Override
    public int insertAlarmLimitType(LimitType alarmLimitType) {
        alarmLimitType.setId(UUID.randomUUID().toString());
        alarmLimitType.setCreateTime(DateUtils.getNowDate());
        LambdaQueryWrapper<LimitType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LimitType::getLimitCode,alarmLimitType.getLimitCode());
        final Long aLong = alarmLimitTypeMapper.selectCount(queryWrapper);
        if(aLong>0){
            throw new RuntimeException("限制值类型编号不能重复");
        }
        return alarmLimitTypeMapper.insertLimitType(alarmLimitType);
    }

    @Override
    public List<LimitType> selectAlarmLimitTypeList(LimitType alarmLimitType) {
        return alarmLimitTypeMapper.selectLimitTypeList(alarmLimitType);
    }

    @Override
    public int updateAlarmLimitType(LimitType alarmLimitType) {
        alarmLimitType.setUpdateTime(DateUtils.getNowDate());
        LambdaQueryWrapper<LimitType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(LimitType::getLimitCode,alarmLimitType.getLimitCode());
        final List<LimitType> alarmLimitTypes = alarmLimitTypeMapper.selectList(queryWrapper);
        if(CollectionUtils.isNotEmpty(alarmLimitTypes)){
            final String id = alarmLimitTypes.get(0).getId();
            if(!id.equals(alarmLimitType.getId())) {
                throw new RuntimeException("限制值类型编号不能重复");
            }
        }
        return alarmLimitTypeMapper.updateLimitType(alarmLimitType);
    }

    @Override
    public int deleteLimitTypeByIds(String[] ids) {
        return alarmLimitTypeMapper.deleteLimitTypeByIds(ids);
    }

    @Override
    public LimitType selectAlarmLimitTypeById(String id) {
        return alarmLimitTypeMapper.selectLimitTypeById(id);
    }

    @Override
    public Page<LimitType> selectAlarmLimitTypePage(LimitType alarmLimitType, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<LimitType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(alarmLimitType.getLimitName()),LimitType::getLimitName,alarmLimitType.getLimitName());
        queryWrapper.orderByDesc(LimitType::getCreateTime);
        return alarmLimitTypeMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
    }
}
