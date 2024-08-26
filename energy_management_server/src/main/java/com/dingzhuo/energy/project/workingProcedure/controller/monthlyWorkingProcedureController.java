package com.dingzhuo.energy.project.workingProcedure.controller;

import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.workingProcedure.domain.monthlyWorkingProcedure;
import com.dingzhuo.energy.project.workingProcedure.service.ImonthlyWorkingProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 工序能耗统计
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/workingProcedure/monthlyWorkingProcedure")
public class monthlyWorkingProcedureController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private ImonthlyWorkingProcedure imonthlyWorkingProcedure;

    @GetMapping("/list")
    public AjaxResult list(DataItem dataItem) {
        try {
            List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
            List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
            List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<monthlyWorkingProcedure> dataList=new ArrayList<>();
            Map tableColumn =new HashMap<>();//表数据
            monthlyWorkingProcedure report1=new monthlyWorkingProcedure();
            DateFormat df = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String aa= df.format(dataItem.getDataTime());
            String bb="";
            int i = 1;
            String beginTime=aa+"-01 00:00:00";
            dataItem.setBeginTime(sf.parse(beginTime));
            String endTime=aa+"-"+Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length()-2))+" 00:00:00";
            dataItem.setEndTime(sf.parse(endTime));
            while (i <= Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length()-2))) {
                if(i>9){
                    bb=aa+"-"+i+" 00:00:00";
                }else{
                    bb=aa+"-0"+i+" 00:00:00";
                }
                monthlyWorkingProcedure report=new monthlyWorkingProcedure();
                report.setDataTime(sf.parse(bb));
                report.setValue("value"+i);
                dataList.add(report);
                tableColumn.put("value"+i,String.valueOf(i)+"日");
                i++;
            }
            monthlyWorkingProcedure reportList =new  monthlyWorkingProcedure();
            List<Map> table=new ArrayList<>();
            List<monthlyWorkingProcedure> list = imonthlyWorkingProcedure.getMonthlyWorkingProcedureList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
            int count=Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length()-2));
            list.forEach(monthlyReport -> monthlyReport.setCount(count));
            reportList.setTabledata(list);
            table.add(tableColumn);
            reportList.setTablehead(table);
            return AjaxResult.success(reportList);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    @GetMapping("/listChart")
    public AjaxResult listChart(DataItem dataItem) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        String beginTime=aa+"-01 00:00:00";
        dataItem.setBeginTime(sf.parse(beginTime));
        String endTime=aa+"-"+Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length()-2))+" 00:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        List<monthlyWorkingProcedure> list = imonthlyWorkingProcedure.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return AjaxResult.success(list);
    }
    public static String getLastDayOfMonth(String yearMonth) {
        int year = Integer.parseInt(yearMonth.split("-")[0]);  //年
        int month = Integer.parseInt(yearMonth.split("-")[1]); //月
        Calendar cal = Calendar.getInstance();
        // 设置年份
        cal.set(Calendar.YEAR, year);
        // 设置月份
        // cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.MONTH, month); //设置当前月的上一个月
        // 获取某月最大天数
        //int lastDay = cal.getActualMaximum(Calendar.DATE);
        int lastDay = cal.getMinimum(Calendar.DATE); //获取月份中的最小值，即第一天
        // 设置日历中月份的最大天数
        //cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.DAY_OF_MONTH, lastDay - 1); //上月的第一天减去1就是当月的最后一天
        // 格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(cal.getTime());
    }
}