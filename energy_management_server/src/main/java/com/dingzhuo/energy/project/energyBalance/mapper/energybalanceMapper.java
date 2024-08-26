package com.dingzhuo.energy.project.energyBalance.mapper;


import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 能流分析  Mapper接口
 * 
 * @author zhaow
 * @date 2020-12-26
 */
public interface energybalanceMapper
{
    /**
     * 查询  能源平衡分析
     *
     */
    public List<DataItem> getBalanceList(@Param("indexIds") List<String> indexIds,
                                   @Param("timeCode") String timeCode);
}
