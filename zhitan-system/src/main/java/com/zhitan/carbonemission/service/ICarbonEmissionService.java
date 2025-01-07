package com.zhitan.carbonemission.service;

import com.zhitan.carbonemission.domain.dto.CarbonEmissionDTO;
import com.zhitan.carbonemission.domain.vo.carbonEmissionYQVO;

import java.util.List;
import java.util.Map;

/**
 * 碳排放核算
 *
 * @author lsk
 * @date 2024-10-29
 */
public interface ICarbonEmissionService {

    /**
     * 计算碳排放量 - 顶部与底部
     *
     * @param carbonEmissionDTO 输入参数
     * @return 碳排放量
     */
    Map<String,Object> getUpCarbonEmission(CarbonEmissionDTO carbonEmissionDTO);


    /**
     * 计算碳排放量 - 中间层
     *
     * @param carbonEmissionDTO 输入参数
     * @return 碳排放量
     */
    List<carbonEmissionYQVO> getMiddleCarbonEmission(CarbonEmissionDTO carbonEmissionDTO);


}
