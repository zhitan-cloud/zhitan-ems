package com.dingzhuo.energy.data.monitoring.alarm.controller;


import com.dingzhuo.energy.data.monitoring.alarm.domain.JkRealTimeAlarmList;
import com.dingzhuo.energy.dataservice.domain.RetrievalModes;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.framework.security.service.TokenService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.data.monitoring.alarm.service.IRealtimeAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报警监测 下 实时报警监测 Controller
 *
 * @author zhaowei
 * @date 2020-02-12
 */
@Api("报警检测——实时报警检测")
@RestController
@RequestMapping("/energyAlarm/realTimeAlarm")
public class RealTimeAlarmController extends BaseController
{
    @Autowired
    private IRealtimeAlarmService iRealtimeAlarmService;

    @Autowired
    private RealtimeDatabaseService realtimeDatabaseService;

    @Autowired
    private TokenService tokenService;


    /**
     * 实时报警 页面 根据 节点目录和 条件查询
     */
//    @ApiOperation("实时报警查询")
    @PreAuthorize("@ss.hasPermi('energyAlarm:realTimeAlarm:list')")
    @GetMapping("/list")
    public AjaxResult list(JkRealTimeAlarmList jkRealTimeAlarmList)
    {
        //startPage();
        List<JkRealTimeAlarmList> list = iRealtimeAlarmService.selectRealtimeAlarmJkList(jkRealTimeAlarmList);
        //return getDataTable(list);
        return AjaxResult.success(list);
    }

    /**
     * 实时报警 组件 实时数据获取
     */
//    @ApiOperation("实时报警查询")
    @PreAuthorize("@ss.hasPermi('energyAlarm:realTimeAlarm:list')")
    @GetMapping("/liveHistoryData/{code}/{minute}/{pointCount}")
    public AjaxResult liveList(@PathVariable("code") String code,@PathVariable("minute") String minute,@PathVariable("pointCount") String pointCount)
    {
        int time = Integer.parseInt(minute);
        int count = Integer.parseInt(pointCount);
        Calendar c = Calendar.getInstance();
        Date  endTime= c.getTime();
        c.add(Calendar.MINUTE, -time);
        Date startTime = c.getTime();
        List<TagValue> TagValuelist = realtimeDatabaseService.retrieve(code,startTime,endTime,RetrievalModes.BestFit,count);
        for(TagValue tagVal:TagValuelist)
        {
            if(ObjectUtils.isEmpty(tagVal)){
                tagVal = new TagValue();
            }
            tagVal.setShowDataTime("HH:mm:ss");
            if(ObjectUtils.isEmpty(tagVal.getValue())){
                tagVal.setValue(0.00);
            }else{
                BigDecimal b   =   new   BigDecimal(tagVal.getValue());
                tagVal.setValue( b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
            }

        }
//        Collections.reverse(TagValuelist);
        return AjaxResult.success(TagValuelist);
    }
}
