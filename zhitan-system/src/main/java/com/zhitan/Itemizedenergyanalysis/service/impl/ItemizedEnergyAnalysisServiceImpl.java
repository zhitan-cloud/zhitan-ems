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
 * @author sys
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

        // 根据时间类型调整时间范围
        switch (dto.getTimeType()) {
            case TimeTypeConst.TIME_TYPE_DAY:
                timeType = TimeTypeConst.TIME_TYPE_HOUR;
                endTime = DateUtil.endOfDay(beginTime);
                break;
            case TimeTypeConst.TIME_TYPE_MONTH:
                timeType = TimeTypeConst.TIME_TYPE_DAY;
                endTime = DateUtil.endOfMonth(beginTime);
                break;
            case TimeTypeConst.TIME_TYPE_YEAR:
                timeType = TimeTypeConst.TIME_TYPE_MONTH;
                endTime = DateUtil.endOfYear(beginTime);
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
        for (int i = 0; i < dataItemList.size(); i++) {
            PropUtils.setValue(itemVO,"value"+ i, dataItemList.get(i).getValue());
        }
        voList.add(itemVO);
        vo.setDataList(voList);

        return vo;
    }
}
