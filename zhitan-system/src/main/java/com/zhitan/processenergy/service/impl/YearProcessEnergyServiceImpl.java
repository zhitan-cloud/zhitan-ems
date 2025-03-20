package com.zhitan.processenergy.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.TypeTime;
import com.zhitan.processenergy.domain.YearProcessEnergy;
import com.zhitan.processenergy.mapper.YearProcessEnergyMapper;
import com.zhitan.processenergy.service.IYearProcessEnergyService;
import com.zhitan.realtimedata.domain.dto.DataItemQueryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.annotation.Resources;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 *重点设备能耗统计 年
 *
 * @author sys
 * @date 2021-01-11
 */
@Service
public class YearProcessEnergyServiceImpl implements IYearProcessEnergyService {

    @Resource
    private YearProcessEnergyMapper yearProcessEnergyMapper;

    @Override
    public List<YearProcessEnergy> getYearProcessEnergy(List<String> indexIds, List<TypeTime> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return yearProcessEnergyMapper.getYearProcessEnergy(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }

    /**
     *
     * @param queryDto
     * @return
     */
    @Override
    public List<YearProcessEnergy> getListChart(DataItemQueryDTO queryDto) {

        if(ObjectUtil.isEmpty(queryDto.getIndexId())) {
            return Collections.emptyList();
        }

        Date convertTime = DateTimeUtil.getTypeTime(queryDto.getTimeType(), queryDto.getDataTime());
        DateTime beginTime = DateUtil.beginOfYear(convertTime);
        DateTime endTime = DateUtil.endOfYear(convertTime);
        return yearProcessEnergyMapper.getListChart(queryDto.getIndexId(),beginTime,endTime,queryDto.getTimeType());
    }
}
