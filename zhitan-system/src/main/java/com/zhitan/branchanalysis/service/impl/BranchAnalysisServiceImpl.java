package com.zhitan.branchanalysis.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zhitan.branchanalysis.domain.BranchAnalysisVO;
import com.zhitan.branchanalysis.service.IBranchAnalysisService;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.common.exception.ServiceException;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.PropUtils;
import com.zhitan.common.utils.TypeTime;
import com.zhitan.dataitem.mapper.DataItemMapper;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.realtimedata.domain.DataItem;
import com.zhitan.realtimedata.domain.dto.BranchAnalysisDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 支路用能分析
 *
 * @author ZhiTan
 * @date 2025-03-27
 */
@Service
@AllArgsConstructor
public class BranchAnalysisServiceImpl implements IBranchAnalysisService {

    private ModelNodeMapper modelNodeMapper;
    private DataItemMapper dataItemMapper;

    @Override
    public BranchAnalysisVO getBranchAnalysisService(BranchAnalysisDTO dto) {
        String timeType = dto.getTimeType();
        String dataTime = dto.getDataTime();
        Date beginTime = DateTimeUtil.getTime(timeType, dataTime);
        DateTime endTime = null;

        List<ModelNodeIndexInfo> nodeIndexInfo = modelNodeMapper.getModelNodeIndexIdByNodeId(dto.getNodeId(), dto.getEnergyType());
        List<String> indexlist = nodeIndexInfo.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toList());

        if (ObjectUtil.isEmpty(indexlist)) {
            return new BranchAnalysisVO();
        }

        List<TypeTime> dateTimeList;
        //根据时间类型调整时间范围
        switch (dto.getTimeType()) {
            case TimeTypeConst.TIME_TYPE_DAY:
                timeType = TimeTypeConst.TIME_TYPE_HOUR;
                endTime = DateUtil.endOfDay(beginTime);
                dateTimeList = DateTimeUtil.getDateTimeListSame(TimeTypeConst.TIME_TYPE_DAY, beginTime);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                timeType = TimeTypeConst.TIME_TYPE_DAY;
                endTime = DateUtil.endOfMonth(beginTime);
                dateTimeList = DateTimeUtil.getDateTimeListSame(TimeTypeConst.TIME_TYPE_MONTH, beginTime);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                timeType = TimeTypeConst.TIME_TYPE_MONTH;
                endTime = DateUtil.endOfYear(beginTime);
                dateTimeList = DateTimeUtil.getDateTimeListSame(TimeTypeConst.TIME_TYPE_YEAR, beginTime);
                break;
            default:
                throw new ServiceException("时间格式错误");
        }
        BranchAnalysisVO vo = new BranchAnalysisVO();
        if (ObjectUtil.isEmpty(indexlist)) {
            return vo;
        }
        List<DataItem> dataItemlist = dataItemMapper.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, timeType, indexlist);

        double sum = dataItemlist.stream().mapToDouble(DataItem::getValue).sum();
        vo.setTotal(sum);
        vo.setNodeId(dto.getNodeId());
        vo.setNodeName(nodeIndexInfo.get(0).getName());
        Map<Date, List<DataItem>> dateListMap = dataItemlist.stream().collect(Collectors.groupingBy(DataItem::getDataTime));

        List<DataItem> results = new ArrayList<>();
        dateListMap.forEach((key, value) -> {
            DataItem dataItem = new DataItem();
            dataItem.setDataTime(key);
            //保留四位小数
            double totalValue = value.stream().map(data -> BigDecimal.valueOf(data.getValue()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(4, RoundingMode.HALF_UP).doubleValue();
            dataItem.setValue(totalValue);
            results.add(dataItem);
        });
        //根据时间排序
        results.sort(Comparator.comparing(DataItem::getDataTime));
        for (int i = 0; i < dateTimeList.size(); i++) {
            TypeTime typeTime = dateTimeList.get(i);
            Optional<DataItem> dataItem = results.stream().filter(result -> result.getDataTime().equals(typeTime.getDateTime())).findFirst();
            if (dataItem.isPresent()) {
                DataItem item = dataItem.get();
                PropUtils.setValue(vo, "value" + i, item.getValue());
            } else {
                PropUtils.setValue(vo, "value" + i, null);
            }
        }
        return vo;
    }
}
