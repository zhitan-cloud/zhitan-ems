package com.zhitan.branchanalysis.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.zhitan.branchanalysis.domain.BranchAnalysisVO;
import com.zhitan.common.constant.TimeTypeConst;
import com.zhitan.common.exception.ServiceException;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.branchanalysis.service.IBranchAnalysisService;
import com.zhitan.common.utils.PropUtils;
import com.zhitan.dataitem.mapper.DataItemMapper;
import com.zhitan.model.domain.vo.ModelNodeIndexInfo;
import com.zhitan.model.domain.vo.ModelNodeIndexInfor;
import com.zhitan.model.mapper.ModelNodeMapper;
import com.zhitan.realtimedata.domain.DataItem;
import com.zhitan.realtimedata.domain.dto.BranchAnalysisDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * c
 *
 * @author sys
 * @date 2021-01-11
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

        ModelNodeIndexInfo info = nodeIndexInfo.stream().findFirst().get();

        //根据时间类型调整时间范围
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

        List<DataItem> dataItemlist = dataItemMapper.getDataItemTimeRangeInforByIndexIds(beginTime, endTime, timeType, indexlist);

        BranchAnalysisVO vo = new BranchAnalysisVO();
        double sum = dataItemlist.stream().mapToDouble(DataItem::getValue).sum();
        vo.setTotal(sum);
        if (ObjectUtil.isNotEmpty(info.getIndexId())) {
            vo.setUntil(info.getUnitId());
        }

        vo.setNodeId(info.getNodeId());
        vo.setNodeName(info.getName());
        for (int i = 0; i < dataItemlist.size(); i++) {
            PropUtils.setValue(vo, "value" + i, dataItemlist.get(i).getValue());
        }

        return vo;


    }
}
