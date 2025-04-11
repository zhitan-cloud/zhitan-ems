package com.zhitan.Itemizedenergyanalysis.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zhitan.Itemizedenergyanalysis.domain.VO.ItemizedEnergyAnalysisItemVO;
import com.zhitan.Itemizedenergyanalysis.domain.VO.ItemizedEnergyAnalysisVO;
import com.zhitan.Itemizedenergyanalysis.dto.ItemizedEnergyAnalysisDTO;
import com.zhitan.Itemizedenergyanalysis.service.IItemizedEnergyAnalysisService;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.common.exception.ServiceException;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.PropUtils;
import com.zhitan.common.utils.TypeTime;
import com.zhitan.dataitem.service.IDataItemService;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.realtimedata.domain.DataItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 分项用能分析
 *
 * @author ZhiTan
 * @date 2025-03-25
 */
@Service
@AllArgsConstructor
public class ItemizedEnergyAnalysisServiceImpl implements IItemizedEnergyAnalysisService {

    @Resource
    private final ModelNodeMapper modelNodeMapper;
    @Resource
    private final IDataItemService dataItemService;

    /**
     * @param dto 请求参数
     * @return 结果
     */
    @Override
    public ItemizedEnergyAnalysisVO getItemizedEnergyAnalysisService(ItemizedEnergyAnalysisDTO dto) {

        String timeType = dto.getTimeType();
        String dataTime = dto.getDataTime();
        Date beginTime = DateTimeUtil.getTime(timeType, dataTime);
        DateTime endTime = null;

        // 获取节点信息
        List<ModelNodeIndexInfo> nodeIndexInfo = modelNodeMapper.getModelNodeIndexIdByNodeId(dto.getNodeId(), dto.getEnergyType());
        List<String> indexList = nodeIndexInfo.stream().map(ModelNodeIndexInfo::getIndexId).collect(Collectors.toList());

        if(ObjectUtil.isEmpty(indexList)){
            return new ItemizedEnergyAnalysisVO();
        }

        ModelNodeIndexInfo info = nodeIndexInfo.stream().findFirst().get();

        List<TypeTime> dateTimeList;
        // 根据时间类型调整时间范围
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
        // 获取数据项列表
        List<DataItem> dataItemList = dataItemService.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, timeType, indexList);

        // 获取最大值、最小值、合计、平均值
        ItemizedEnergyAnalysisVO vo = new ItemizedEnergyAnalysisVO();
        double sum = dataItemList.stream().mapToDouble(DataItem::getValue).sum();
        double max = dataItemList.stream().mapToDouble(DataItem::getValue).max().getAsDouble();
        double min = dataItemList.stream().mapToDouble(DataItem::getValue).min().getAsDouble();
        double avg = dataItemList.stream().mapToDouble(DataItem::getValue).average().getAsDouble();

        vo.setTotal(BigDecimal.valueOf(sum).setScale(2, RoundingMode.HALF_UP).toString());
        vo.setMax(BigDecimal.valueOf(max).setScale(2, RoundingMode.HALF_UP).toString());
        vo.setMin(BigDecimal.valueOf(min).setScale(2, RoundingMode.HALF_UP).toString());
        vo.setAvg(BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP).toString());
        if(ObjectUtil.isNotEmpty(info.getUnitId())){
            vo.setUnit(info.getUnitId());
        }

        List<ItemizedEnergyAnalysisItemVO> voList = new ArrayList<>();
        ItemizedEnergyAnalysisItemVO itemVO = new ItemizedEnergyAnalysisItemVO();
        itemVO.setNodeId(info.getNodeId());
        itemVO.setNodeName(info.getName());
        itemVO.setTotal(sum);

        Map<Date, List<DataItem>> dateListMap = dataItemList.stream().collect(Collectors.groupingBy(DataItem::getDataTime));
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
                PropUtils.setValue(itemVO, "value" + i, item.getValue());
            } else {
                PropUtils.setValue(itemVO, "value" + i, null);
            }
        }

        voList.add(itemVO);
        vo.setDataList(voList);

        return vo;
    }
}
