package com.dingzhuo.energy.project.electricityTypeSetting.service.impl;


import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.vo.ModelNodeIndexInfor;
import com.dingzhuo.energy.data.model.mapper.ModelNodeMapper;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.dto.ElectricityDataItemListDTO;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.entity.ElectricityDataItem;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.enums.ElectricityTypeEnum;
import com.dingzhuo.energy.project.electricityTypeSetting.domain.vo.PeakAndValleyReportVO;
import com.dingzhuo.energy.project.electricityTypeSetting.mapper.ElectricityDataItemMapper;
import com.dingzhuo.energy.project.electricityTypeSetting.service.IElectricityDataItemService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 尖峰平谷数据Service业务层处理
 *
 * @author sys
 * @date 2024-08-27
 */
@Service
public class ElectricityDataItemServiceImpl implements IElectricityDataItemService {
    @Resource
    private ModelNodeMapper modelNodeMapper;
    @Resource
    private ElectricityDataItemMapper electricityDataItemMapper;


    /**
     * 查询统计数据
     *
     * @param dto 请求参数
     * @return 结果
     */
    @Override
    public List<PeakAndValleyReportVO> getDataStatistics(ElectricityDataItemListDTO dto) {
        List<PeakAndValleyReportVO> reportVOList = new ArrayList<>();
        // 查询时间范围
        Date startTime = DateUtil.beginOfMonth(dto.getQueryTime());
        Date endTime = DateUtil.endOfMonth(startTime);
        String timeType = dto.getTimeType();

        Map<String, List<ElectricityDataItem>> electricityDataMap = new HashMap<>();
        // 查询点位信息
        List<ModelNodeIndexInfor> nodeIndexInfoList = modelNodeMapper.selectIndexByModelCodeAndNodeId(dto.getModelCode(), dto.getNodeId());
        if (CollectionUtils.isNotEmpty(nodeIndexInfoList)) {
            Set<String> indexSet = nodeIndexInfoList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toSet());
            List<ElectricityDataItem> dataItemList = electricityDataItemMapper.getDataStatistics(indexSet, startTime, endTime, timeType);

            electricityDataMap = dataItemList.stream()
                    .collect(Collectors.groupingBy(li -> DateUtil.formatDateTime(li.getDataTime())));
        }
        while (!startTime.after(endTime)) {
            String mapKey = DateUtil.formatDateTime(startTime);
            List<ElectricityDataItem> dataItemList = electricityDataMap.get(mapKey);

            BigDecimal sharpFee = BigDecimal.ZERO;
            BigDecimal sharpPower = BigDecimal.ZERO;
            BigDecimal peakFee = BigDecimal.ZERO;
            BigDecimal peakPower = BigDecimal.ZERO;
            BigDecimal flatFee = BigDecimal.ZERO;
            BigDecimal flatPower = BigDecimal.ZERO;
            BigDecimal valleyFee = BigDecimal.ZERO;
            BigDecimal valleyPower = BigDecimal.ZERO;

            if (CollectionUtils.isNotEmpty(dataItemList)) {
                for (ElectricityDataItem electricityDataItem : dataItemList) {
                    String electricityType = electricityDataItem.getElectricityType();

                    if (ElectricityTypeEnum.SHARP.name().equals(electricityType)) {
                        sharpFee = sharpFee.add(electricityDataItem.getCost());
                        sharpPower = sharpPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.PEAK.name().equals(electricityType)) {
                        peakFee = peakFee.add(electricityDataItem.getCost());
                        peakPower = peakPower.add(electricityDataItem.getElectricity());
                    } else if (ElectricityTypeEnum.FLAT.name().equals(electricityType)) {
                        flatFee = flatFee.add(electricityDataItem.getCost());
                        flatPower = flatPower.add(electricityDataItem.getElectricity());
                    } else {
                        valleyFee = valleyFee.add(electricityDataItem.getCost());
                        valleyPower = valleyPower.add(electricityDataItem.getElectricity());
                    }
                }
            }
            PeakAndValleyReportVO peakAndValleyReportVO = new PeakAndValleyReportVO(startTime, sharpFee, sharpPower,
                    peakFee, peakPower, flatFee, flatPower, valleyFee, valleyPower);

            reportVOList.add(peakAndValleyReportVO);
            switch (TimeType.valueOf(timeType)) {
                case HOUR:
                    startTime = DateUtil.offsetHour(startTime, 1);
                    break;
                case DAY:
                    startTime = DateUtil.offsetDay(startTime, 1);
                    break;
                case MONTH:
                    startTime = DateUtil.offsetMonth(startTime, 1);
                    break;
                default:
                    startTime = DateUtil.offsetMonth(startTime, 12);
                    break;
            }
        }

        return reportVOList;
    }
}
