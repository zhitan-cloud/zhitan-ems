package com.zhitan.powerDistribution.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.powerDistribution.domain.PowerDistribution;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PowerDistributionMapper extends BaseMapper<PowerDistribution> {

    int deletePowerDistributionByIds(@Param("ids") List<String> ids);
}
