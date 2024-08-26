package com.dingzhuo.energy.project.comprehensiveStatistics.service.impl;

import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.common.CommonConst;
import com.dingzhuo.energy.project.common.DateTimeUtil;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.yearComperhensive;
import com.dingzhuo.energy.project.comprehensiveStatistics.mapper.yearComprehensiveMapper;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.IyearComprehensive;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务层处理
 * 
 * @author sys
 * @date 2020-03-25
 */
@Service
public class yearComprehensiveServiceImpl implements IyearComprehensive {
    @Autowired
    private yearComprehensiveMapper yearMapper;

    @Override
    public List<yearComperhensive> getYearComprehensiveList(String nodeId, List<yearComperhensive> dataList,
                                                            Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        if (StringUtils.isNotEmpty(nodeId)) {
            return yearMapper.getYearComprehensiveList(nodeId, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<yearComperhensive> getListChart(String indexId, Date beginTime, Date endTime, TimeType timeType, String indexStorageId){
        List<yearComperhensive> dataList = new ArrayList<>();
        if (StringUtils.isNotEmpty(indexId)) {
            List<yearComperhensive> listChart = yearMapper.getListChart(indexId, beginTime, endTime, timeType, indexStorageId);
            if (CollectionUtils.isNotEmpty(listChart)) {
                Date date = new Date();
                yearComperhensive first = listChart.get(CommonConst.DIGIT_0);
                Map<String, yearComperhensive> listChartMap = yearMapper.getListChart(indexId, beginTime, endTime, timeType, indexStorageId)
                        .stream().collect(Collectors.toMap(yearComperhensive::getTimeCode, prot -> prot));
                while (beginTime.before(date)) {
                    yearComperhensive yearComperhensive = new yearComperhensive();
                    String format = CommonConst.WORD_M + DateUtil.format(beginTime, DateTimeUtil.COMMON_PATTERN_MONTH);
                    yearComperhensive item = listChartMap.get(format);
                    if (ObjectUtils.isNotEmpty(item)) {
                        yearComperhensive = item;
                    } else {
                        yearComperhensive.setTimeCode(format);
                        yearComperhensive.setIndexId(indexId);
                        yearComperhensive.setTimeType(timeType.name());
                        yearComperhensive.setUnitId(first.getUnitId());
                        yearComperhensive.setIndexName(first.getIndexName());
                    }
                    dataList.add(yearComperhensive);
                    beginTime = DateUtil.offsetMonth(beginTime, CommonConst.DIGIT_1);
                }
            }
        }
        return dataList;
    }
}
