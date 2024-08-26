package com.dingzhuo.energy.project.energyShareAnalysis.service;

import com.dingzhuo.energy.project.energyShareAnalysis.domain.dto.EnergyAnalysisDTO;
import com.dingzhuo.energy.project.energyShareAnalysis.domain.vo.EnergyAnalysisVO;

import java.util.List;

/**
 * 能耗占比分析  接口层
 */
public interface IEnergyShareAnalysisService {

    /**
     * 获取电占比统计信息
     *
     * @param dto 查询参数
     * @return 结果
     */
    List<EnergyAnalysisVO> listEnergyShareAnalysis(EnergyAnalysisDTO dto);
}