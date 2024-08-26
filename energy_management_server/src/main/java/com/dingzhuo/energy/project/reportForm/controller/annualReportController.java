package com.dingzhuo.energy.project.reportForm.controller;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.reportForm.domain.annualReport;
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.service.IannualReportService;
import com.dingzhuo.energy.project.reportForm.service.IreportFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 报表
 * @author sys
 * @date 2020-03-25
 */
@RestController
@RequestMapping("/report/annualReport")
class annualReportController extends BaseController {
    @Autowired
    private IannualReportService annualReportService;
    @Autowired
    private IModelNodeService modelNodeService;
    /**
     * 报表查询
     */
    @PreAuthorize("@ss.hasPermi('report:annualReport:list')")
    @GetMapping("/list")
    public AjaxResult list(DataItem dataItem) throws ParseException {
        List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
        List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<annualReport> dataList=new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getBeginTime());
        String bb="";
        int i = 1;
        while (i <= 12) {
            if(i>9){
                bb=aa+"-"+i+"-01 00:00:00";
            }else{
                bb=aa+"-0"+i+"-01 00:00:00";
            }
            annualReport report=new annualReport();
            report.setDataTime(sf.parse(bb));
            report.setValue("value"+i);
            dataList.add(report);
            i++;
        }
        List<annualReport> list = annualReportService.getannualReportList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return AjaxResult.success(list);
    }
    /**
     * 导出报表设置功能列表
     */
    @PreAuthorize("@ss.hasPermi('report:annualReport::export')")
    @Log(title = "综合报表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DataItem dataItem) throws ParseException {
        List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
        List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<annualReport> dataList=new ArrayList<>();
        if(dataItem.getTimeType().name().equals("HOUR")){
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String aa= df.format(dataItem.getBeginTime());
            String bb="";
            int i = 1;
            while (i <= 24) {
                if(i>9){
                    bb=aa+" "+i+":00:00";
                }else{
                    bb=aa+" 0"+i+":00:00";
                }
                annualReport report=new annualReport();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                report.setDataTime(sf.parse(bb));
                report.setValue("value"+i);
                dataList.add(report);
                i++;
            }
        }else if(dataItem.getTimeType().name().equals("DAY")){
            DateFormat df = new SimpleDateFormat("yyyy-MM");
            String aa= df.format(dataItem.getDataTime());
            String bb="";
            int i = 1;
            annualReport report1=new annualReport();
            while (i <= Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length()-2))) {
                if(i>9){
                    bb=aa+"-"+i+" 00:00:00";
                }else{
                    bb=aa+"-0"+i+" 00:00:00";
                }
                annualReport report=new annualReport();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                report.setDataTime(sf.parse(bb));
                report.setValue("value"+i);
                i++;
            }
            //table.add(report1);
        }else if(dataItem.getTimeType().name().equals("MONTH")){
            DateFormat df = new SimpleDateFormat("yyyy");
            String aa= df.format(dataItem.getDataTime());
            String bb="";
            int i = 1;
            while (i <= 12) {
                if(i>9){
                    bb=aa+"-"+i+"-01 00:00:00";
                }else{
                    bb=aa+"-0"+i+"-01 00:00:00";
                }
                annualReport report=new annualReport();
                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                report.setDataTime(sf.parse(bb));
                report.setValue("value"+i);
                dataList.add(report);
                i++;
            }
        }
        List<annualReport> list = annualReportService.getannualReportList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());;
        ExcelUtil<annualReport> util = new ExcelUtil<annualReport>(annualReport.class);
        return util.exportExcel(list, "综合报表");
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
    private static Map<String, DataItem> change(List<DataItem> list) {
        Map<String, DataItem> resultMap = new HashMap<String, DataItem>();
        Set setTmp = new HashSet();
        for (DataItem tmp : list) {
            setTmp.add(tmp.getIndexId());
        }
        Iterator it = setTmp.iterator();
        while (it.hasNext()) {
            String oneSetTmpStr = (String) it.next();
            List<DataItem> oneSetTmpList = new ArrayList<DataItem>();
            for (DataItem tmp : list) {
                String oneMapValueStr = tmp.getIndexId();
                if (oneMapValueStr.equals(oneSetTmpStr)) {
                    oneSetTmpList.add(tmp);
                }
            }
            resultMap.put(oneSetTmpStr, oneSetTmpList.get(0));
        }
        return  resultMap;
    }
}
