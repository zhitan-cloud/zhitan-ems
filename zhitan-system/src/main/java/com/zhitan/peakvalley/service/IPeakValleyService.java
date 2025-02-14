package com.zhitan.peakvalley.service;


import com.zhitan.peakvalley.domain.dto.ElectricityDataItemListDTO;
import com.zhitan.peakvalley.domain.dto.PeakValleyDTO;
import com.zhitan.peakvalley.domain.vo.peakvalley.PeakValleyDayVO;
import com.zhitan.peakvalley.domain.vo.peakvalley.PeakValleyHourDataVO;
import com.zhitan.peakvalley.domain.vo.peakvalley.PeakValleyHourVO;

import java.util.List;

/**
 * 尖峰平谷数据Service接口
 *
 * @author sys
 * @date 2024-08-27
 */
public interface IPeakValleyService {

    /**
     * 查询统计数据
     *
     * @param dto 请求参数
     * @return 结果
     */
    List<PeakValleyHourDataVO> getDataStatistics(ElectricityDataItemListDTO dto);

    /**
     * 尖峰平谷分时统计
     *
     * @param dto 请求参数
     * @return 结果
     */
    PeakValleyDayVO segmentAnalysisDay(PeakValleyDTO dto);

    /**
     * 尖峰平谷分时统计
     *
     * @param dto 请求参数
     * @return 结果
     */
    PeakValleyHourVO segmentAnalysisHour(PeakValleyDTO dto);

    /**
     * 尖峰平谷分时统计导出
     * @param dto
     * @return
     */
    List<PeakValleyHourDataVO> segmentAnalysisHourExport(PeakValleyDTO dto);

    PeakValleyDayVO segmentAnalysisDayCustomize(PeakValleyDTO dto);
}
