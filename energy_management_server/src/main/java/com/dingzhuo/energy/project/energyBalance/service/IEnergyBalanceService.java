package com.dingzhuo.energy.project.energyBalance.service;


import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;

import java.util.Date;
import java.util.List;

/**
 * 能员平衡Service接口
 * 
 * @author zhaow
 * @date 2020-12-26
 */
public interface IEnergyBalanceService
{

    /**
     * 查询能源平衡
     *
     */
    public List<DataItem> getBalanceList(List<String> indexIds,Date dataTime,TimeType timeType);
}
