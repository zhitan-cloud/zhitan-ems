package com.zhitan.Itemizedenergyanalysis.service;

import com.zhitan.Itemizedenergyanalysis.domain.VO.ItemizedEnergyAnalysisVO;
import com.zhitan.Itemizedenergyanalysis.dto.ItemizedEnergyAnalysisDTO;

public interface IItemizedEnergyAnalysisService {
    /**
     * 分项用能分析
     * @param dto   请求参数
     * @return 结果
     */
    ItemizedEnergyAnalysisVO getItemizedEnergyAnalysisService(ItemizedEnergyAnalysisDTO dto);
}
