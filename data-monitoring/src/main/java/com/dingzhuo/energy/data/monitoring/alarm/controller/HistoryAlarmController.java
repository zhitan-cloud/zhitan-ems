package com.dingzhuo.energy.data.monitoring.alarm.controller;


import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.DaqTemplate;
import com.dingzhuo.energy.data.monitoring.alarm.domain.HistoryTable;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkHistoryAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.domain.JkRealTimeAlarmList;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.security.service.TokenService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.data.monitoring.alarm.domain.HistoryAlarm;
import com.dingzhuo.energy.data.monitoring.alarm.service.IHistoryAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 报警监测 下  历史报警监测 Controller
 *
 * @author zhaowei
 * @date 2020-02-12
 */
@Api(value = "报警检测—历史报警检测",tags = {"报警检测"})
@RestController
@RequestMapping("/energyAlarm/historicalAlarm")
public class HistoryAlarmController extends BaseController
{
    @Autowired
    private IHistoryAlarmService iHistoryAlarmService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;

    /**
     * 历史报警 页面 根据 节点目录和 条件查询
     */
    @ApiOperation("历史报警查询")
//    @PreAuthorize("@ss.hasPermi('energyAlarm:historicalAlarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(JkHistoryAlarm jkHistoryAlarm)
    {
        startPage();
        List<JkHistoryAlarm> list = iHistoryAlarmService.selectJkHistoryAlarmList(jkHistoryAlarm);
        return getDataTable(list);
    }
    /**
     * 导出历史报警监控列表
     */
    @PreAuthorize("@ss.hasPermi('energyAlarm:historicalAlarm:export')")
    @Log(title = "历史报警", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(JkHistoryAlarm jkHistoryAlarm)
    {
        List<JkHistoryAlarm> list = iHistoryAlarmService.selectJkHistoryAlarmListExcel(jkHistoryAlarm);
        ExcelUtil<JkHistoryAlarm> util = new ExcelUtil<JkHistoryAlarm>(JkHistoryAlarm.class);
        return util.exportExcel(list, "alarm");
    }

    /**
     * 历史报警 组件 历史数据获取
     */
    @ApiOperation("历史报警数据查询")
    @PreAuthorize("@ss.hasPermi('energyAlarm:realTimeAlarm:list')")
    @GetMapping("/historyData/{code}/{start}/{end}/{pointCount}")
    public AjaxResult historyDataList(@PathVariable("code") String code, @PathVariable("start") String start, @PathVariable("end") String end, @PathVariable("pointCount") String pointCount)
    {
        int count = Integer.parseInt(pointCount);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sdate = null;
        Date edate = null;
        try {
            sdate = formatter.parse(start);
            edate = formatter.parse(end);
            List<TagValue> TagValuelist = realtimeDatabaseService.retrieve(code,sdate,edate, RetrievalModes.BestFit,count);
            System.out.println("TagValuelist>>>>>>>>>"+TagValuelist);
            System.out.println("TagValuelist size>>>>>>>>>"+TagValuelist.size());
            for(TagValue tagVal:TagValuelist){
                System.out.println("TagValue>>>>>>>>>"+tagVal);
                if(ObjectUtils.isEmpty(tagVal)){
                    tagVal = new TagValue();
                }
                tagVal.setShowDataTime("yyyy-MM-dd HH:mm:ss");
                if(ObjectUtils.isEmpty(tagVal.getValue())){
                    tagVal.setValue(0.00);
                }else{
                    BigDecimal b   =   new   BigDecimal(tagVal.getValue());
                    tagVal.setValue( b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
                }

            }
            return AjaxResult.success(TagValuelist);
        } catch (ParseException e) {
            e.printStackTrace();
            return AjaxResult.success("数据查询失败");
        }
//        Collections.reverse(TagValuelist);

    }

    /**
     * 历史报警 组件 历史报警表格导出
     */
    @ApiOperation("历史报警表格导出")
    @PreAuthorize("@ss.hasPermi('energyAlarm:realTimeAlarm:list')")
    @GetMapping("/historyDataExcel/{code}/{start}/{end}/{pointCount}/{indexName}/{indexUnit}")
    public AjaxResult historyDataExcel(@PathVariable("code") String code,
                                       @PathVariable("start") String start,
                                       @PathVariable("end") String end,
                                       @PathVariable("pointCount") String pointCount,
                                       @PathVariable("indexName") String indexName,
                                       @PathVariable("indexUnit") String indexUnit)
    {
        int count = Integer.parseInt(pointCount);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sdate = null;
        Date edate = null;
        try {
            sdate = formatter.parse(start);
            edate = formatter.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<TagValue> tagValuelist = realtimeDatabaseService.retrieve(code,sdate,edate, RetrievalModes.BestFit,count);
        List<HistoryTable> historyTableList = new ArrayList<>();
        for(TagValue tagVal:tagValuelist)
        {
            tagVal.setShowDataTime("yyyy-MM-dd HH:mm:ss");
            HistoryTable hh = new HistoryTable();
            hh.setCode(tagVal.getTagCode());
            hh.setAlarmTime(tagVal.getShowDataTime());
            BigDecimal b   =   new   BigDecimal(tagVal.getValue());
            hh.setEarlyWarningValue(b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
            hh.setIndexName(indexName);
            hh.setUnitName(indexUnit);
            historyTableList.add(hh);
        }
        ExcelUtil<HistoryTable> util = new ExcelUtil<HistoryTable>(HistoryTable.class);
        return util.exportExcel(historyTableList, "template");
    }

    /**
     * 实时检测 功能 的多 sheet页  展示 组态图  测点 报警信息
     */
    @ApiOperation("实时检测报警信息查询")
    @GetMapping("/listNote")
    public TableDataInfo listNote(JkHistoryAlarm jkHistoryAlarm)
    {
        startPage();
        List<JkHistoryAlarm> list = iHistoryAlarmService.selectHistoryAlarmNoteList(jkHistoryAlarm);
        return getDataTable(list);
    }
}
