package com.dingzhuo.energy.data.monitoring.trend.realtime.controller;

import cn.hutool.core.date.DateUtil;
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplement;
import com.dingzhuo.energy.basic.data.meter.service.IMeterImplementService;
import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.dto.EnergyIndexMonitorDTO;
import com.dingzhuo.energy.data.model.domain.vo.ModelNodeIndexInfor;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.data.model.service.IStateTypeService;
import com.dingzhuo.energy.data.monitoring.device.domain.EquipmentMeasuringPointParameters;
import com.dingzhuo.energy.data.monitoring.device.domain.vo.EquipmentPointParametersExcel;
import com.dingzhuo.energy.data.monitoring.device.domain.vo.EquipmentPointParametersVO;
import com.dingzhuo.energy.data.monitoring.device.service.IDeviceStatusLiveService;
import com.dingzhuo.energy.data.monitoring.trend.realtime.domain.CollectHistory;
import com.dingzhuo.energy.data.monitoring.trend.svg.service.ISvgTrendService;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 设备启停实时监测Controller
 *
 * @author sys
 * @date 2020-03-30
 */
@RestController
@RequestMapping("/dataMonitoring/realTimeTrend")
@Api(value = "实时检测", tags = {"实时监测"})
public class RealTimeTrend extends BaseController {

    @Autowired
    private ISvgTrendService iSvgTrendService;

    @Autowired
    private IStateTypeService iStateTypeService;

    @Autowired
    private final IModelNodeService modelNodeService;

    @Autowired
    private final IEnergyIndexService energyIndexService;

    @Autowired
    private IMeterImplementService meterImplementService;

    @Autowired
    private IDeviceStatusLiveService deviceStatusLiveService;

    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;


    @Autowired
    public RealTimeTrend(
            IModelNodeService modelNodeService, IEnergyIndexService energyIndexService) {
        this.modelNodeService = modelNodeService;
        this.energyIndexService = energyIndexService;
    }

    @Log(title = "获取模型节点关联采集指标", businessType = BusinessType.UPDATE)
    @GetMapping("/energyIndex/list")
    @ApiOperation(value = "获取模型节点关联采集指标")
    public AjaxResult getSettingIndex(@Validated EnergyIndexMonitorDTO dto) {
        try {
            List<EquipmentPointParametersVO> resultList = new ArrayList<>();
            // 模型id
            String nodeId = dto.getNodeId();
            // 查询传入的nodeId
            List<ModelNodeIndexInfor> nodeInforList = modelNodeService.listModelNodeIndexIdRelationInforByParentId(nodeId);
            // 如果是空存在两种情况，1：id有问题，2：最底层
            if (CollectionUtils.isEmpty(nodeInforList)) {
                List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(nodeId);
                if (CollectionUtils.isNotEmpty(inforList)) {
                    nodeInforList.addAll(inforList);
                }
            }
            if (CollectionUtils.isEmpty(nodeInforList)) {
                return AjaxResult.success(resultList);
            }
            // 找出所有点位id信息
            List<String> indexIds = nodeInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
            // 排序
            List<EnergyIndex> infoList = energyIndexService.getEnergyIndexByIds(indexIds).stream()
                    .sorted(Comparator.comparing(EnergyIndex::getCode)).collect(Collectors.toList());
            // 获取所有表id
            List<String> codeList = new ArrayList<>();
            List<String> meterIdList = new ArrayList<>();
            infoList.forEach(li -> {
                codeList.add(li.getCode());
                meterIdList.add(li.getMeterId());
            });
            // 当前时间
            String timeString = DateUtil.formatDateTime(DateUtil.date());
            Map<String, List<MeterImplement>> meterImplementMap = meterImplementService.listMeterImplementByIds(meterIdList).stream()
                    .collect(Collectors.groupingBy(MeterImplement::getId));
            List<TagValue> valList = realtimeDatabaseService.retrieve(codeList);
            for (EnergyIndex energyIndex : infoList) {
                // 获取点位名称
                String modelNodeName = "";
                List<MeterImplement> meterImplementList = meterImplementMap.get(energyIndex.getMeterId());
                if (CollectionUtils.isNotEmpty(meterImplementList)) {
                    MeterImplement infor = meterImplementList.get(0);
                    modelNodeName = infor.getInstallactionLocation() + "_" + infor.getMeterName();
                }
                String indexName = modelNodeName + "_" + energyIndex.getName();
                EquipmentPointParametersVO item = new EquipmentPointParametersVO();
                item.setCode(energyIndex.getCode());
                item.setIndexName(indexName);
                item.setIndexUnit(energyIndex.getUnitId());
                item.setTimeString(timeString);
                String value = "--";
                if (CollectionUtils.isNotEmpty(valList)) {
                    double sum = valList.stream().filter(x -> item.getCode().equals(x.getTagCode())).mapToDouble(TagValue::getValue).sum();
                    if ("SWWSDJ_SD".equals(energyIndex.getCode()) || "SWWSDJ_WD".equals(energyIndex.getCode())) {
                        sum = sum * 0.1;
                    }
                    value = String.valueOf(BigDecimal.valueOf(sum).setScale(2, RoundingMode.HALF_UP));
                }
                item.setValue(value);
                resultList.add(item);
            }
            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            logger.error("获取关联采集指标出错！", ex);
            return AjaxResult.error("获取关联指标出错!");
        }
    }

    //    @Log(title = "获取折线图数据", businessType = BusinessType.UPDATE)
    @GetMapping("/energyIndex/lineList/{codes}/{minute}/{count}")
    @ApiOperation(value = "获取折线图数据")
    public AjaxResult getSettingIndexLine(@PathVariable String[] codes, @PathVariable Integer minute, @PathVariable Integer count) {
        try {
            List<String> codesList = new ArrayList<>(Arrays.asList(codes));
            Date endDate = DateUtil.date();
            long time = minute * 60 * 1000;//分钟
            Date startDate = new Date(endDate.getTime() - time);
            List<List<TagValue>> resultList = new ArrayList<>();
            for (int i = 0; i < codesList.size(); i++) {
                List<TagValue> valList = realtimeDatabaseService.retrieve(codesList.get(i), startDate, endDate, RetrievalModes.Full, count);
                for (TagValue tagVal : valList) {
                    if (tagVal.getValue() != null) {
                        double value = BigDecimal.valueOf(tagVal.getValue()).setScale(2, RoundingMode.HALF_UP).doubleValue();

                        if ("SWWSDJ_SD".equals(tagVal.getTagCode()) || "SWWSDJ_WD".equals(tagVal.getTagCode())) {
                            value = value * 0.1;
                        }

                        tagVal.setValue(BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue());
                    }
                    tagVal.setDataTime(tagVal.getDataTime());
                    tagVal.setShowDataTime("yyyy-MM-dd HH:mm:ss");
                }
                resultList.add(valList);
            }
            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            ex.printStackTrace();
            return AjaxResult.error("获取关联指标出错!");
        }
    }

    @Log(title = "获取模型节点关联采集指标", businessType = BusinessType.UPDATE)
    @GetMapping("/svgTrendView/energyIndex/list")
    @ApiOperation(value = "获取模型节点关联采集指标")
    public AjaxResult getSvgTrendViewSettingIndex(EnergyIndex energyIndex) {
        try {
            List<EnergyIndex> infoList = iSvgTrendService.selectSvgList(energyIndex);
            List<String> codeList = infoList.stream().map(EnergyIndex::getCode).collect(Collectors.toList());
            List<TagValue> valList = realtimeDatabaseService.retrieve(codeList);
            List resultList = new ArrayList();
            if (valList != null && valList.size() > 0) {
                for (int i = 0; i < infoList.size(); i++) {
                    EquipmentMeasuringPointParameters item = new EquipmentMeasuringPointParameters();
                    item.setCode(infoList.get(i).getCode());
                    item.setIndexName(infoList.get(i).getName());
                    item.setIndexUnit(infoList.get(i).getUnitId());
                    item.setMeteName(infoList.get(i).getMeterName());
                    valList.forEach(x -> {
                        if (x.getTagCode().equals(item.getCode())) {
                            item.setValue(new BigDecimal(x.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        }
                    });
                    item.setyValue("y0");
                    resultList.add(item);
                }
            }
            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            logger.error("获取关联采集指标出错！", ex);
            return AjaxResult.error("获取关联指标出错!");
        }
    }

    @Log(title = "获取指定采集点位的小时实时数据", businessType = BusinessType.UPDATE)
    @GetMapping("/svgTrendView/energyIndex/history/{code}/{dataTime}")
    @ApiOperation(value = "获取指定采集点位的小时实时数据")
    public AjaxResult getIndexHistoryValueList(@PathVariable("code") String code, @PathVariable("dataTime") String dataTime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date sdate = null;
            Date edate = null;
            try {
                if (StringUtils.isEmpty(dataTime)) {
                    dataTime = formatter.format(new Date());
                }
                if (dataTime.length() < 18) {
                    dataTime = dataTime + ":00:00";
                }
                sdate = formatter.parse(dataTime);
                edate = formatter.parse(dataTime);
                //这里要加一小时
                edate = DateUtils.addHours(edate, 1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            List<String> codes = new ArrayList<>();
            codes.add(code);
            List<EnergyIndex> infoList = energyIndexService.getEnergyIndexByCodes(codes);
            EnergyIndex ei = infoList.get(0);
            if (CollectionUtils.isEmpty(infoList)) {
                ei = new EnergyIndex();
            }

            List<TagValue> tagValuelist = realtimeDatabaseService.retrieve(code, sdate, edate, RetrievalModes.Full, 0);
            List resultList = new ArrayList();
            for (TagValue tagVal : tagValuelist) {
                tagVal.setShowDataTime("yyyy-MM-dd HH:mm:ss");
                CollectHistory hh = new CollectHistory();
                hh.setTagCode(tagVal.getTagCode());
                hh.setDataTime(tagVal.getShowDataTime());
                BigDecimal b = new BigDecimal(tagVal.getValue());
                hh.setValue(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                hh.setUnitId(ei.getUnitId());
                resultList.add(hh);
            }

            return AjaxResult.success(resultList);
        } catch (Exception ex) {
            logger.error("获取采集点历史小时错误！", ex);
            return AjaxResult.error("获取采集点历史小时错误!");
        }
    }

    @Log(title = "导出实时监测Excel信息", businessType = BusinessType.UPDATE)
    @GetMapping("/export")
    @ApiOperation(value = "导出实时监测Excel信息")
    public AjaxResult export(@Validated EnergyIndexMonitorDTO dto) {
        try {
            List<EquipmentPointParametersExcel> excelList = new ArrayList<>();
            // 模型id
            String nodeId = dto.getNodeId();
            // 查询传入的nodeId
            List<ModelNodeIndexInfor> nodeInforList = modelNodeService.listModelNodeIndexIdRelationInforByParentId(nodeId);
            // 如果是空存在两种情况，1：id有问题，2：最底层
            if (CollectionUtils.isEmpty(nodeInforList)) {
                List<ModelNodeIndexInfor> inforList = modelNodeService.getModelNodeIndexIdRelationInforByNodeId(nodeId);
                if (CollectionUtils.isNotEmpty(inforList)) {
                    nodeInforList.addAll(inforList);
                }
            }
            if (CollectionUtils.isEmpty(nodeInforList)) {
                return AjaxResult.success("暂无数据");
            }
            // 找出所有点位id信息
            List<String> indexIds = nodeInforList.stream().map(ModelNodeIndexInfor::getIndexId).collect(Collectors.toList());
            List<EnergyIndex> infoList = energyIndexService.getEnergyIndexByIds(indexIds);
            // 获取所有表id
            List<String> codeList = new ArrayList<>();
            List<String> meterIdList = new ArrayList<>();
            infoList.forEach(li -> {
                codeList.add(li.getCode());
                meterIdList.add(li.getMeterId());
            });
            // 当前时间
            String timeString = DateUtil.formatDateTime(DateUtil.date());
            Map<String, List<MeterImplement>> meterImplementMap = meterImplementService.listMeterImplementByIds(meterIdList)
                    .stream().collect(Collectors.groupingBy(MeterImplement::getId));
            List<TagValue> valList = realtimeDatabaseService.retrieve(codeList);
            for (EnergyIndex energyIndex : infoList) {
                // 获取点位名称
                String modelNodeName = "";
                List<MeterImplement> meterImplementList = meterImplementMap.get(energyIndex.getMeterId());
                if (CollectionUtils.isNotEmpty(meterImplementList)) {
                    MeterImplement infor = meterImplementList.get(0);
                    modelNodeName = infor.getInstallactionLocation() + "_" + infor.getMeterName();
                }
                String indexName = modelNodeName + "_" + energyIndex.getName();
                EquipmentPointParametersExcel item = new EquipmentPointParametersExcel();
                item.setIndexName(indexName);
                item.setIndexUnit(energyIndex.getUnitId());
                item.setTimeString(timeString);
                String value = "--";
                if (CollectionUtils.isNotEmpty(valList)) {
                    double sum = valList.stream().filter(x -> energyIndex.getCode().equals(x.getTagCode())).mapToDouble(TagValue::getValue).sum();
                    value = String.valueOf(BigDecimal.valueOf(sum).setScale(2, RoundingMode.HALF_UP));
                }
                item.setValue(value);
                excelList.add(item);
            }

            ExcelUtil<EquipmentPointParametersExcel> util = new ExcelUtil<>(EquipmentPointParametersExcel.class);
            return util.exportExcel(excelList, "实时监测");
        } catch (Exception ex) {
            logger.error("导出数据出错！", ex);
            return AjaxResult.error("导出数据出错!");
        }
    }

}
