package com.zhitan.keyequipment.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.common.utils.TypeTime;
import com.zhitan.keyequipment.domain.YearKeyEquipment;
import com.zhitan.keyequipment.mapper.YearKeyEquipmentMapper;
import com.zhitan.keyequipment.service.IYearKeyEquipmentService;
import com.zhitan.realtimedata.domain.dto.DataItemQueryDTO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 *重点设备能耗统计 年
 *
 * @author sys
 * @date 2021-01-11
 */
@Service
public class YearKeyEquipmentServiceImpl implements IYearKeyEquipmentService {
    @Autowired
    private YearKeyEquipmentMapper yearKeyEquipmentMapper;

    public List<YearKeyEquipment> getYearKeyEquipmentList(List<String> indexIds, List<TypeTime> dataList, Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (indexIds != null && !indexIds.isEmpty()) {
            return yearKeyEquipmentMapper.getYearKeyEquipmentList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<YearKeyEquipment> getListChart(DataItemQueryDTO queryDto){
        if(ObjectUtils.isEmpty(queryDto.getIndexId())){
            return Collections.emptyList();}
        Date convertTime = DateTimeUtil.getTime(queryDto.getTimeType(), queryDto.getDataTime());
        DateTime beginTime = DateUtil.beginOfYear(convertTime);
        DateTime endTime = DateUtil.endOfYear(convertTime);
        return yearKeyEquipmentMapper.getListChart(queryDto.getIndexId(),beginTime,endTime,queryDto.getTimeType());
        }
}
