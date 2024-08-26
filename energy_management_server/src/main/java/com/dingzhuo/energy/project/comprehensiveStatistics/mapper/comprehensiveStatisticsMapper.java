package com.dingzhuo.energy.project.comprehensiveStatistics.mapper;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.comprehensiveStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface comprehensiveStatisticsMapper {
    List<FacilityArchives> getFacilityArchives();
    List<comprehensiveStatistics> getDatasByList(@Param("indexIds") List<String> indexIds, @Param("beginTime") Date beginTime,
                                                 @Param("endTime") Date endTime,
                                                 @Param("timeType") TimeType timeType);
    List<comprehensiveStatistics> getDatasByIndex(@Param("indexIds") List<String> indexIds, @Param("timeCode") String timeCode);
    List<comprehensiveStatistics> getDatasIndex(@Param("indexIds") List<String> indexIds,@Param("timeCode") String timeCode);
    List<comprehensiveStatistics> getEnergyByIndex(@Param("indexType") String indexType);

}
