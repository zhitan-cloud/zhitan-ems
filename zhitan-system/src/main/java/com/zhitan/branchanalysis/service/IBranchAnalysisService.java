package com.zhitan.branchanalysis.service;


import com.zhitan.branchanalysis.domain.BranchAnalysisVO;
import com.zhitan.realtimedata.domain.dto.BranchAnalysisDTO;


public interface IBranchAnalysisService {
    /**
     * 支路用能分析
     *
     * @author ZhiTan
     * @date 2021-01-11
     */
    BranchAnalysisVO getBranchAnalysisService(BranchAnalysisDTO dataItem);
}
