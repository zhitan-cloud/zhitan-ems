package com.dingzhuo.energy.project.govReports;

import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.project.govReports.domain.DataItemPub;
import com.dingzhuo.energy.project.govReports.service.IDataItemPubService;
import com.dingzhuo.energy.project.reportForm.domain.reportSet;
import com.dingzhuo.energy.project.reportForm.mapper.reportSetMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Configuration
@Component
public class GovReportsTask {

    private static final String DATE_TYPE_DAY = "DAY";
    private static final String DATE_TYPE_MONTH = "MONTH";

    /**
     * 被处理的表的数据
     **/
    final IDataItemPubService dataItemPubService;

    final reportSetMapper reportSetMapper;

    public GovReportsTask(IDataItemPubService dataItemPubService, reportSetMapper reportSetMapper) {
        this.dataItemPubService = dataItemPubService;
        this.reportSetMapper = reportSetMapper;
    }

    @Scheduled(cron = "${govReport.cronDay}")
    private void handleDayDataTask() {
        Date yesterday = DateUtil.yesterday();
        String timeCode = MessageFormat.format("D{0}", DateUtil.format(yesterday, "yyyyMMdd"));
        List<reportSet> reportSetList = reportSetMapper.getAllEnableSetByDateType(DATE_TYPE_DAY);
        List<DataItemPub> data = dataItemPubService.getReportData(timeCode, DATE_TYPE_DAY);
        compareData(data, reportSetList);
        // 插入或者更新数据
        dataItemPubService.insertOrUpdateDateItemPub(data);
    }

    @Scheduled(cron = "${govReport.cronMonth}")
    private void handleMonthDataTask() {
        Date lastMonth = DateUtil.lastMonth();
        String timeCode = MessageFormat.format("M{0}", DateUtil.format(lastMonth, "yyyyMM"));
        List<reportSet> reportSetList = reportSetMapper.getAllEnableSetByDateType(DATE_TYPE_MONTH);
        List<DataItemPub> data = dataItemPubService.getReportData(timeCode, DATE_TYPE_MONTH);
        compareData(data, reportSetList);
        // 插入或者更新数据
        dataItemPubService.insertOrUpdateDateItemPub(data);
    }

    /**
     * 比较并且处理数据
     *
     * @param itemPubList 原始数据
     **/
    private List<DataItemPub> compareData(List<DataItemPub> itemPubList, List<reportSet> mReportSet) {

        // 取出来的是空，则直接返回数据
        if (mReportSet.isEmpty()) {
            return itemPubList;
        }

        itemPubList.forEach(m -> {
            m.setOriginValue(m.getValue());
            // 获取此点位的设置
            reportSet current = mReportSet.stream().filter(n -> n.getIndexId().equals(m.getIndexId())).findFirst().orElse(null);
            if (current != null) {
                // id
                m.setReportSetId(current.getId());
                // 下限值
                m.setReportLowerLimit(Double.valueOf(current.getLimitValDown()));
                // 下限替换
                m.setReportLowerReplace(Double.valueOf(current.getLimitReplaceValDown()));
                // 上限值
                m.setReportUpperLimit(Double.valueOf(current.getLimitValUp()));
                // 上限替换
                m.setReportLowerLimit(Double.valueOf(current.getLimitReplaceValUp()));
                if (m.getOriginValue() > m.getReportUpperLimit()) {
                    m.setValue(m.getReportUpperReplace());
                }
                if (m.getOriginValue() < m.getReportLowerLimit()) {
                    m.setValue(m.getReportLowerReplace());
                }
            }
        });

        return itemPubList;
    }
}
