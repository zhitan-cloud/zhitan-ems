package com.zhitan.web.controller.alarm;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.JkRealTimeAlarmList;
import com.zhitan.alarm.services.IRealtimeAlarmService;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.realtimedata.domain.TagValue;
import com.zhitan.realtimedata.service.RealtimeDatabaseService;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    /**
     * 实时报警 页面 根据 节点目录和 条件查询
     */
//    @ApiOperation("实时报警查询")
    @PreAuthorize("@ss.hasPermi('energyAlarm:realTimeAlarm:list')")
    @GetMapping("/list")
    public TableDataInfo list(JkRealTimeAlarmList jkRealTimeAlarmList, Long pageNum, Long pageSize)
    {
        Page<JkRealTimeAlarmList> list = iRealtimeAlarmService.selectRealtimeAlarmJkPage(jkRealTimeAlarmList,pageNum,pageSize);
        return getDataTable(list);
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
        List<TagValue> TagValuelist = realtimeDatabaseService.retrieve(code, startTime, endTime, count);
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
