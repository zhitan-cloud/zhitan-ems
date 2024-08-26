package com.dingzhuo.energy.data.monitoring.trend.history.controller;

import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplement;
import com.dingzhuo.energy.basic.data.meter.service.IMeterImplementService;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IStateTypeService;
import com.dingzhuo.energy.data.monitoring.trend.history.domain.dto.HistoricalDataDTO;
import com.dingzhuo.energy.data.monitoring.trend.history.domain.vo.HistoricalDataExcel;
import com.dingzhuo.energy.data.monitoring.trend.history.domain.vo.HistoricalDataVO;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备启停实时监测Controller
 *
 * @author sys
 * @date 2020-03-30
 */
@RestController
@RequestMapping("/dataMonitoring/historyDataTrend")
public class HistoryDataTrend extends BaseController {
    @Autowired
    private final IEnergyIndexService energyIndexService;

    @Autowired
    private final IMeterImplementService meterImplementService;

    @Autowired
    private final RealtimeDatabaseService realtimeDatabaseService;


    public HistoryDataTrend(IEnergyIndexService energyIndexService,
                            IMeterImplementService meterImplementService,
                            RealtimeDatabaseService realtimeDatabaseService,
                            IStateTypeService iStateTypeService) {
        this.energyIndexService = energyIndexService;
        this.meterImplementService = meterImplementService;
        this.realtimeDatabaseService = realtimeDatabaseService;
    }


    @Log(title = "获取模型节点关联采集指标", businessType = BusinessType.UPDATE)
    @GetMapping("/energyIndex/list")
    public AjaxResult getSettingIndex(EnergyIndex energyIndex) {
        try {
            List<EnergyIndex> infoList = energyIndexService.selectEnergyIndexList(energyIndex);
//            List<String> codeList= infoList.stream().map(EnergyIndex::getCode).collect(Collectors.toList());
//            List<TagValue> valList = realtimeDatabaseService.retrieve(codeList);
//            List resultList = new ArrayList();
            return AjaxResult.success(infoList);
        } catch (Exception ex) {
            logger.error("获取关联采集指标出错！", ex);
            return AjaxResult.error("获取关联指标出错!");
        }
    }

    @Log(title = "根据时间与点位查询历史监测数据", businessType = BusinessType.UPDATE)
    @GetMapping("/getHistoricalDataByIndexId")
    public AjaxResult getHistoricalDataByIndexId(HistoricalDataDTO dto) {
        try {
            // 获取点位信息
            EnergyIndex energyIndex = energyIndexService.selectEnergyIndexById(dto.getIndexId());
            if (ObjectUtils.isEmpty(energyIndex)) {
                return AjaxResult.error("未找到点位信息");
            }
            Date beginTime = dto.getDataTime();
            Date endTime;
            // 查询条数
            int count = 23;
            if ("DAY".equals(dto.getTimeType())) {
                endTime = DateUtil.endOfDay(beginTime);
            } else {
                count = 19;
                endTime = DateUtil.offsetSecond(DateUtil.offsetHour(beginTime, 1), -1);
            }
            // 查询计量器具
            MeterImplement infor = meterImplementService.selectMeterImplementById(energyIndex.getMeterId());
            List<TagValue> tagValueList = realtimeDatabaseService.retrieve(energyIndex.getCode(), beginTime, endTime,
                    RetrievalModes.BestFit, count);
            List<HistoricalDataVO> voList = new ArrayList<>();
            Date date = DateUtil.date();
            for (int i = 0; i < count + 1; i++) {
                HistoricalDataVO vo = new HistoricalDataVO();
                vo.setIndexId(energyIndex.getIndexId());
                String indexName = energyIndex.getName();
                if (ObjectUtils.isNotEmpty(infor)) {
                    indexName = infor.getInstallactionLocation() + "_" + infor.getMeterName() + "_" + indexName;
                }
                vo.setIndexName(indexName);
                // 取值
                String value = "--";
                String usedValue = "--";
                if (beginTime.getTime() <= date.getTime()) {
                    try {
                        TagValue tagValue = tagValueList.get(i);
                        BigDecimal cumulative = BigDecimal.valueOf(tagValue.getValue());

                        if ("SWWSDJ_SD".equals(energyIndex.getCode()) || "SWWSDJ_WD".equals(energyIndex.getCode())) {
                            cumulative = cumulative.multiply(BigDecimal.valueOf(0.1));
                        }
                        if (i > 0) {
                            TagValue previousTagValue = tagValueList.get(i - 1);
                            BigDecimal previousValue = BigDecimal.ZERO;
                            if (ObjectUtils.isNotEmpty(previousTagValue.getValue())) {
                                previousValue = BigDecimal.valueOf(previousTagValue.getValue());
                            }
                            if ("SWWSDJ_SD".equals(energyIndex.getCode()) || "SWWSDJ_WD".equals(energyIndex.getCode())) {
                                previousValue = previousValue.multiply(BigDecimal.valueOf(0.1));

                            }
                            usedValue = String.valueOf(cumulative.subtract(previousValue).setScale(2, RoundingMode.HALF_UP));
                        }

                        value = String.valueOf(cumulative.setScale(2, RoundingMode.HALF_UP));
                    } catch (Exception ignored) {
                    }
                }
                // 时间
                String timeName = DateUtil.formatDateTime(beginTime);
                vo.setDataTime(timeName);
                if ("DAY".equals(dto.getTimeType())) {
                    beginTime = DateUtil.offsetHour(beginTime, 1);
                } else {
                    beginTime = DateUtil.offsetMinute(beginTime, 3);
                }
                vo.setUsedValue(String.valueOf(usedValue));
                vo.setValue(String.valueOf(value));
                voList.add(vo);
            }
            return AjaxResult.success(voList);
        } catch (Exception ex) {
            logger.error("查询历史监测数据出错！", ex);
            return AjaxResult.error("查询历史监测数据出错!");
        }
    }

    @Log(title = "导出Excel", businessType = BusinessType.UPDATE)
    @GetMapping("/export")
    public AjaxResult export(HistoricalDataDTO dto) {
        try {
            // 获取点位信息
            EnergyIndex energyIndex = energyIndexService.selectEnergyIndexById(dto.getIndexId());
            if (ObjectUtils.isEmpty(energyIndex)) {
                return AjaxResult.success("未找到点位信息");
            }
            Date beginTime = dto.getDataTime();
            Date endTime;
            // 查询条数
            int count = 23;
            if ("DAY".equals(dto.getTimeType())) {
                endTime = DateUtil.endOfDay(beginTime);
            } else {
                count = 19;
                endTime = DateUtil.offsetSecond(DateUtil.offsetHour(beginTime, 1), -1);
            }
            // 查询计量器具
            MeterImplement infor = meterImplementService.selectMeterImplementById(energyIndex.getMeterId());
            List<TagValue> tagValueList = realtimeDatabaseService.retrieve(energyIndex.getCode(), beginTime, endTime,
                    RetrievalModes.BestFit, count);
            List<HistoricalDataExcel> excelList = new ArrayList<>();
            Date date = DateUtil.date();
            for (int i = 0; i < count + 1; i++) {
                HistoricalDataExcel vo = new HistoricalDataExcel();
                String indexName = energyIndex.getName();
                if (ObjectUtils.isNotEmpty(infor)) {
                    indexName = infor.getInstallactionLocation() + "_" + infor.getMeterName() + "_" + indexName;
                }
                vo.setIndexName(indexName);
                // 取值
                String value = "--";
                String usedValue = "--";
                if (beginTime.getTime() <= date.getTime()) {
                    try {
                        TagValue tagValue = tagValueList.get(i);
                        BigDecimal cumulative = BigDecimal.valueOf(tagValue.getValue());
                        if (i > 0) {
                            TagValue previousTagValue = tagValueList.get(i - 1);
                            BigDecimal previousValue = BigDecimal.valueOf(previousTagValue.getValue());
                            usedValue = String.valueOf(cumulative.subtract(previousValue).setScale(2, RoundingMode.HALF_UP));
                        }
                        value = String.valueOf(cumulative.setScale(2, RoundingMode.HALF_UP));
                    } catch (Exception ignored) {
                    }
                }
                // 时间
                String timeName = DateUtil.formatDateTime(beginTime);
                vo.setDataTime(timeName);
                if ("DAY".equals(dto.getTimeType())) {
                    beginTime = DateUtil.offsetHour(beginTime, 1);
                } else {
                    beginTime = DateUtil.offsetMinute(beginTime, 3);
                }
                vo.setValue(String.valueOf(value));
                vo.setUsedValue(String.valueOf(usedValue));
                excelList.add(vo);
            }
            ExcelUtil<HistoricalDataExcel> util = new ExcelUtil<>(HistoricalDataExcel.class);
            String sheetName = "历史数据统计" + DateUtil.formatDate(dto.getDataTime());
            return util.exportRealTimeDataExcel(excelList, sheetName);
        } catch (Exception ex) {
            logger.error("导出Excel数据出错！", ex);
            return AjaxResult.error("导出Excel数据出错!");
        }
    }

}
