package com.dingzhuo.energy.project.energyBalance.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.energyBalance.mapper.energybalanceMapper;
import com.dingzhuo.energy.project.energyBalance.service.IEnergyBalanceService;
import com.dingzhuo.energy.project.energyBalance.service.IEnergyPicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 工序单耗统计功能Service业务层处理
 * 
 * @author zhaow
 * @date 2020-12-26
 */
@Service
public class energyBalanceServiceImpl implements IEnergyBalanceService
{
    @Autowired
    private energybalanceMapper energybalanceMapper;

    @Override
    public List<DataItem> getBalanceList(List<String> indexIds, Date dataTime, TimeType timeType) {
        if (indexIds != null && !indexIds.isEmpty()) {
            String timeCode = TimeManager.getTimeCode(dataTime, timeType);
            return energybalanceMapper.getBalanceList(indexIds, timeCode);
        }

        return Collections.emptyList();
    }
}
