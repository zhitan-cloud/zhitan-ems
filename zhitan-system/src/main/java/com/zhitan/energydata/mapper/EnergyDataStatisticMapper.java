package com.zhitan.energydata.mapper;

import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EnergyDataStatisticMapper
{
    List<ModelNodeIndexInfo> getModelNodeIndexIdByFixedNodeIds(@Param("modelCode")String modelCode, @Param("fixedNodeIds") List<String> fixedNodeIds);
}
