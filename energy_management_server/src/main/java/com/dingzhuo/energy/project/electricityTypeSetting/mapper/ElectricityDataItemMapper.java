package com.dingzhuo.energy.project.electricityTypeSetting.mapper;


import com.dingzhuo.energy.project.electricityTypeSetting.domain.entity.ElectricityDataItem;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 尖峰平谷数据对象 Mapper接口
 *
 * @author sys
 * @date 2024-08-27
 */
public interface ElectricityDataItemMapper {

    /**
     * 查询尖峰平谷统计数据
     *
     * @param indexIdSet 点位id集合
     * @param startTime  开始时间
     * @param endTime    截止时间
     * @param timeType   时间类型
     * @return 结果
     */
    List<ElectricityDataItem> getDataStatistics(@Param("indexIdSet") Set<String> indexIdSet, @Param("startTime") Date startTime,
                                                @Param("endTime") Date endTime, @Param("timeType") String timeType);
}
