package com.zhitan.energydata.mapper;

import com.zhitan.model.domain.CalcFunction;
import com.zhitan.model.domain.vo.ModelNodeIndexInfor;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface EnergyDataStatisticMapper
{
    List<ModelNodeIndexInfor> getModelNodeIndexIdByFixedNodeIds(@Param("modelCode")String modelCode,@Param("fixedNodeIds") List<String> fixedNodeIds);
}
