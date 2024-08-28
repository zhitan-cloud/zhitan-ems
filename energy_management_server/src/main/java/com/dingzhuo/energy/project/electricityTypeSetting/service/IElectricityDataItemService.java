package com.dingzhuo.energy.project.electricityTypeSetting.service;


import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityDataItemListDTO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.PeakAndValleyReportVO;

import java.util.List;

/**
 * 尖峰平谷数据Service接口
 *
 * @author sys
 * @date 2024-08-27
 */
public interface IElectricityDataItemService {

    /**
     * 查询统计数据
     *
     * @param dto 请求参数
     * @return 结果
     */
    List<PeakAndValleyReportVO> getDataStatistics(ElectricityDataItemListDTO dto);
}
