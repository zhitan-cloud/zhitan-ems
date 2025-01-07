package com.zhitan.realtimedata.service;

import com.zhitan.realtimedata.domain.dto.EnergyIndexMonitorDTO;
import com.zhitan.realtimedata.domain.vo.EquipmentPointParametersExcel;
import com.zhitan.realtimedata.domain.vo.ExportrealtimeTrendVO;
import com.zhitan.realtimedata.domain.vo.RealTimeDataVO;
import com.zhitan.realtimedata.domain.vo.RealTimeHistoryDataVO;

import java.util.List;

/**
 * @Description
 * @Author zhoubg
 * @date 2024-10-15
 **/
public interface RealtimeTrendService {

    List<RealTimeDataVO> list(EnergyIndexMonitorDTO energyIndexMonitorDTO);

    /**
     * 根据code获取指定日期的历史点位数据
     * @param tagCode 点位code
     * @param dataTime 时间
     * @return
     */
    List<RealTimeHistoryDataVO> chartByDay(String tagCode, String dataTime);


    /**
     * 导出实时监测Excel信息
     * @return
     */
    List<EquipmentPointParametersExcel> export(ExportrealtimeTrendVO exportrealtimeTrendVO);

}
