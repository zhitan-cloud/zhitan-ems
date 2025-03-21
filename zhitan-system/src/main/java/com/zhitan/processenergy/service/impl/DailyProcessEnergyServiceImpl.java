package com.zhitan.processenergy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.processenergy.domain.DailyProcessEnergy;
import com.zhitan.processenergy.mapper.DailyProcessEnergyMapper;
import com.zhitan.processenergy.service.IDailyProcessEnergyService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 *重点设备能耗统计 日
 *
 * @author sys
 * @date 2021-01-11
 */
@Service
public class DailyProcessEnergyServiceImpl extends ServiceImpl<DailyProcessEnergyMapper,DailyProcessEnergy> implements IDailyProcessEnergyService {


    @Override
    public List<DailyProcessEnergy> getDailyProcessEnergyList(List<String> indexIds, List<DailyProcessEnergy> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()){
            return baseMapper.getDailyProcessEnergyList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<DailyProcessEnergy> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (indexId != null && !indexId.isEmpty()) {
            return baseMapper.getListChart(indexId,beginTime,endTime,timeType,indexStorageId);
        }
        return Collections.emptyList();
    }
}
