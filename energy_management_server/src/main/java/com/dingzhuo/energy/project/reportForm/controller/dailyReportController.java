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
import com.dingzhuo.energy.project.reportForm.domain.dailyReport;
import com.dingzhuo.energy.project.reportForm.service.IdailyReportService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
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
@RequestMapping("/report/dailyReport")
class dailyReportController extends BaseController {
    @Autowired
    private IdailyReportService dailyReportService;
    @Autowired
    private IModelNodeService modelNodeService;
    /**
     * 报表查询
     */
    @GetMapping("/list")
    public AjaxResult list(DataItem dataItem) throws ParseException {
        List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
        List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        dailyReport reportList =new  dailyReport();
        List<dailyReport> dataList=new ArrayList<>();
        /*List<Map> table=new ArrayList<>();
        Map tableColumn =new HashMap<>();//表数据*/
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        String bb="";
        int i = 0;
        dataItem.setBeginTime(dataItem.getDataTime());
        String endTime=aa+" 24:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        while (i < 24) {
            if(i>9){
                bb=aa+" "+i+":00:00";
            }else{
                bb=aa+" 0"+i+":00:00";
            }
            dailyReport report=new dailyReport();
            report.setDataTime(sf.parse(bb));
            report.setValue("value"+i);
            dataList.add(report);
            i++;
        }
       List<dailyReport> list = dailyReportService.getDailyReportList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        reportList.setTabledata(list);
        /*table.add(tableColumn);
        reportList.setTablehead(table);*/
        return AjaxResult.success(list);
        /*//表格数据
        List<DataItem> list = reportFormService.getDailyReportList(indexCode, dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType());
        Map<Date, List<DataItem>> groupBy = list.stream().collect(Collectors.groupingBy(DataItem::getDataTime));
        List<Map<String,Object>> tableRows= new ArrayList<>();
        AtomicInteger colcount= new AtomicInteger(0);//日期列的分组记录数
        groupBy.forEach((time,indexDatas)->{
            //time 是每一列的 日期
            //indexdatas是每一列的行数据
            //遍历每一列的行集合
            for(int rowCounts=0;rowCounts<indexDatas.size();rowCounts++)
            {
                DataItem indexData = indexDatas.get(rowCounts);//取列的行单元格
                if(rowCounts==0 && tableRows.size()==0)
                {
                    Map<String,Object> indexDataRow = new HashMap<>();
                    indexDataRow.put("indexname",indexData.getIndexName());
                    indexDataRow.put(time.getHours()+"",indexData.getValue());
                    tableRows.add(indexDataRow);
                }else
                {
                    if((tableRows.size()-1)==rowCounts)
                    {
                        Map<String,Object> t = tableRows.get(rowCounts);
                        t.put("indexname",indexData.getIndexName());
                        t.put(time.getHours()+"",indexData.getValue());
                        tableRows.add(t);
                    }else if((tableRows.size()-1)<rowCounts){
                        Map<String,Object> indexDataRow = new HashMap<>();
                        indexDataRow.put("indexname",indexData.getIndexName());
                        indexDataRow.put(time.getHours()+"",indexData.getValue());
                        tableRows.add(indexDataRow);
                    }
                }
            }
            colcount.getAndIncrement();
        });
        System.out.print("tableRows="+tableRows.size());
        System.out.print("=========================");*/
    }
    /**
     * 导出工序单耗统计指标设置功能列表
     */
    @Log(title = "综合报表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(DataItem dataItem) throws ParseException {
        ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dataItem.getIndexCode());
        if (ObjectUtils.isEmpty(modelNode)) {
            return AjaxResult.success("暂无数据");
        }
        List<dailyReport> dataList=new ArrayList<>();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        String bb;
        int i = 0;
        dataItem.setBeginTime(dataItem.getDataTime());
        String endTime=aa+" 24:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        while (i < 24) {
            if(i>9){
                bb=aa+" "+i+":00:00";
            }else{
                bb=aa+" 0"+i+":00:00";
            }
            dailyReport report=new dailyReport();
            report.setDataTime(sf.parse(bb));
            report.setValue("value"+i);
            dataList.add(report);
            i++;
        }
        List<dailyReport> list = dailyReportService.listDailyReportList(modelNode.getNodeId(), dataList,dataItem.getBeginTime(),
                dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        if(CollectionUtils.isNotEmpty(list)){
            list.forEach(this::valueRep);
        }
        ExcelUtil<dailyReport> util = new ExcelUtil<>(dailyReport.class);
        return util.exportExcel(list, "综合指标分析日");
    }

    public void valueRep(dailyReport dr){
        Field[] fields = dr.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            Object obj = field.getType();
            if(field.getType().getName().equals("java.lang.Double")){
                String name = field.getName();
                try {
                    if(ObjectUtils.isEmpty(field.get(dr)))
                    {
                        field.set(dr,0.00);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
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
