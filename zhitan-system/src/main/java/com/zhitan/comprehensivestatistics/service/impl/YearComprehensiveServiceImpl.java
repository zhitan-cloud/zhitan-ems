package com.zhitan.comprehensivestatistics.service.impl;

import cn.hutool.core.date.DateUtil;
import com.zhitan.common.constant.CommonConst;
import com.zhitan.common.utils.DateTimeUtil;
import com.zhitan.comprehensivestatistics.domain.YearComperhensive;
import com.zhitan.comprehensivestatistics.mapper.YearComprehensiveMapper;
import com.zhitan.comprehensivestatistics.service.IyearComprehensive;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务层处理
 * 
 * @author sys
 * @date 2020-03-25
 */
@Service
public class YearComprehensiveServiceImpl implements IyearComprehensive {
    @Resource
    private YearComprehensiveMapper yearMapper;

    @Override
    public List<YearComperhensive> getYearComprehensiveList(String nodeId, List<YearComperhensive> dataList,
                                                            Date beginTime, Date endTime, String timeType, String indexStorageId){
        if (StringUtils.isNotEmpty(nodeId)) {
            return yearMapper.getYearComprehensiveList(nodeId, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
    @Override
    public List<YearComperhensive> getListChart(String indexId, Date beginTime, Date endTime, String timeType, String indexStorageId){
        List<YearComperhensive> dataList = new ArrayList<>();
        if (StringUtils.isNotEmpty(indexId)) {
            List<YearComperhensive> listChart = yearMapper.getListChart(indexId, beginTime, endTime, timeType, indexStorageId);
            if (CollectionUtils.isNotEmpty(listChart)) {
                Date date = new Date();
                YearComperhensive first = listChart.get(CommonConst.DIGIT_0);
                Map<String, YearComperhensive> listChartMap = yearMapper.getListChart(indexId, beginTime, endTime, timeType, indexStorageId)
                        .stream().collect(Collectors.toMap(YearComperhensive::getTimeCode, prot -> prot));
                while (beginTime.before(date)) {
                    YearComperhensive yearComperhensive = new YearComperhensive();
                    String format = CommonConst.WORD_M + DateUtil.format(beginTime, DateTimeUtil.COMMON_PATTERN_MONTH);
                    YearComperhensive item = listChartMap.get(format);
                    if (ObjectUtils.isNotEmpty(item)) {
                        yearComperhensive = item;
                    } else {
                        yearComperhensive.setTimeCode(format);
                        yearComperhensive.setIndexId(indexId);
                        yearComperhensive.setTimeType(timeType);
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
