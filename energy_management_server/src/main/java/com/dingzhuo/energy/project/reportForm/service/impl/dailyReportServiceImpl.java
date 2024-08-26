package com.dingzhuo.energy.project.reportForm.service.impl;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.mapper.dailyReportMapper;
import com.dingzhuo.energy.project.reportForm.service.IdailyReportService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * stagseDataEntryService业务层处理
 *
 * @author sys
 * @date 2020-03-25
 */
@Service
public class dailyReportServiceImpl implements IdailyReportService {
    @Autowired
    private dailyReportMapper dailyReportMapper;

    @Override
    public List<dailyReport> getDailyReportList(List<String> indexIds, List<dailyReport> dataList, Date beginTime, Date endTime,
                                                TimeType timeType, String indexStorageId) {
        if (CollectionUtils.isNotEmpty(indexIds)) {
            return dailyReportMapper.getDailyReportList(indexIds, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }

    @Override
    public List<dailyReport> listDailyReportList(String nodeId, List<dailyReport> dataList, Date beginTime,
                                                 Date endTime, TimeType timeType, String indexStorageId) {

        if (StringUtils.isNotEmpty(nodeId)) {
            return dailyReportMapper.listDailyReportList(nodeId, dataList, beginTime, endTime, timeType, indexStorageId);
        }
        return Collections.emptyList();
    }
}
