package com.dingzhuo.energy.project.energyStatistics.controller;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.project.energyStatistics.domain.dataTimeSVG;
import com.dingzhuo.energy.project.energyStatistics.domain.energyTable;
import com.dingzhuo.energy.project.energyStatistics.domain.EnergyConsumption;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.domain.StatisticResult;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.project.energyStatistics.service.IEnergyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * 能源统计分析Controller
 *
 * @author ruoyi
 * @date 2020-02-07
 */
@RestController
@RequestMapping("/energyStatistics")
public class energyStatisticsController extends BaseController {
  @Autowired
  private PeriodDataService periodDataService;
  @Autowired
  private IEnergyStatisticsService IEnergyStatisticsService;
  @Autowired
  private IModelNodeService modelNodeService;
  /**
   * 导出能源指标趋势分析列表
   */
  @PreAuthorize("@ss.hasPermi('energyStatistics:energyStatisticsTrend:export')")
  @Log(title = "能耗指标趋势分析", businessType = BusinessType.EXPORT)
  @GetMapping("/export")
  public AjaxResult export(DataItem dataItem) {
    List<StatisticResult> list = periodDataService.statisticById(dataItem.getIndexId(), dataItem.getTimeType(), dataItem.getBeginTime(),dataItem.getEndTime());
    ExcelUtil<StatisticResult> util = new ExcelUtil<StatisticResult>(StatisticResult.class);
    return util.exportExcel(list, "set");
  }
  /**
   * 能源指标趋势分析
   */
  @GetMapping("/getEnergyStatisticsTrend")
  public TableDataInfo getEnergyStatisticsTrend(DataItem dataItem) {
    startPage();
    List<StatisticResult> list = periodDataService.statisticById(dataItem.getIndexId(), dataItem.getTimeType(), dataItem.getBeginTime(),dataItem.getEndTime());
    return getDataTable(list);
  }
  /**
   * 能源消费成本构成
   */
  @GetMapping("/getEnergyConstitute")
  public AjaxResult getEnergyConstitute(DataItem dataItem) {
    try {
      /*List<String> indexCodes = new ArrayList<String>();
      String[] indexCode =dataItem.getIndexCode().split(",");
      for(int i =0; i <indexCode.length ; i++){
        indexCodes.add(indexCode[i]);
      }*/
      List<String> indexCodeXFL = new ArrayList<String>();
      List<String> indexCodeXFCB = new ArrayList<String>();
      List<String> indexCodeZBL = new ArrayList<String>();
      List<EnergyIndex> energyIndexList = modelNodeService.getSettingIndex(dataItem.getIndexCode());
      energyIndexList.forEach(param ->{
        if (param.getCode().contains("XFL_")){
          indexCodeXFL.add(param.getCode());
        }else if(param.getCode().contains("XFCB_")){
          indexCodeXFCB.add(param.getCode());
        }else{
          indexCodeZBL.add(param.getCode());
        }
      });
      energyTable tableData =new energyTable();
      //消费量
      List<DataItem> listXFL= IEnergyStatisticsService.statisticByCode(indexCodeXFL,dataItem.getTimeType(),dataItem.getBeginTime(),dataItem.getEndTime());
      //折标
      List<DataItem> listZBL= IEnergyStatisticsService.statisticByCode(indexCodeZBL,dataItem.getTimeType(),dataItem.getBeginTime(),dataItem.getEndTime());
      //消费成本
      List<DataItem> listXFCB= IEnergyStatisticsService.statisticByCode(indexCodeXFCB,dataItem.getTimeType(),dataItem.getBeginTime(),dataItem.getEndTime());

      tableData.setListXFL(listXFL);
      tableData.setListZBL(listZBL);
      tableData.setListXFCB(listXFCB);
      return AjaxResult.success(tableData);
    } catch (Exception ex) {
      logger.error("获取出错！", ex);
      return AjaxResult.error("获取出错!");
    }
  }
  /**
   * 能源消费成本分析
   */
  @GetMapping("/getEnergyConsumption")
  public AjaxResult getEnergyConsumption(DataItem dataItem) {
    try {
      /*TimeType timeType=TimeType.MONTH;
      if( dataItem.getTimeType().name().equals("YEAR")){
        timeType=TimeType.YEAR;
      }else if(dataItem.getTimeType().name().equals("MONTH")){
        timeType=TimeType.MONTH;
      }if(dataItem.getTimeType().name().equals("DAY")){
        timeType=TimeType.DAY;
      }*/
      String[] indexCode =dataItem.getIndexName().split(",");
      List<String> indexCodeXFCB = new ArrayList<String>();
      List<String> indexCodeZBL = new ArrayList<String>();
      for(int i =0; i <indexCode.length ; i++){
        if (indexCode[i].contains("ZBL_")){
          indexCodeZBL.add(indexCode[i]);
        }else if(indexCode[i].contains("XFCB_")){
          indexCodeXFCB.add(indexCode[i]);
        }
      }
      energyTable tableData =new energyTable();
      //折标对比
      List<StatisticResult> resultListZBL= periodDataService.statisticByCode(indexCodeZBL,dataItem.getTimeType(),dataItem.getBeginTime(),dataItem.getEndTime());
      resultListZBL.forEach(statisticResult -> statisticResult.setFormatdate("yyyy-MM-dd"));
      Map<String, List<StatisticResult>> groupByZBL = resultListZBL.stream().collect(Collectors.groupingBy(StatisticResult::getIndexName));
      List<Map> tableZBL = new ArrayList<Map>();
      List<Map> mListZBL = new ArrayList<Map>();
      groupByZBL.forEach((indexName,value)->{
        Map tableColumn =new HashMap<>();
        tableColumn.put("indexName",indexName);
        tableZBL.add(tableColumn);
        tableData.setResultZBL(tableZBL);
        Map table =new HashMap<>();
        List currentList = new ArrayList<>();
        List lastYearList = new ArrayList<>();
        for(StatisticResult result:value){
          currentList.add(new BigDecimal(result.getCurrentValue()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
          lastYearList.add(new BigDecimal(result.getLastYearValue()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        tableColumn.put("currentValue",currentList);
        tableColumn.put("lastYearValue",lastYearList);
        mListZBL.add(tableColumn);
      });
      //消费成本对比
      List<StatisticResult> resultListXFCB= periodDataService.statisticByCode(indexCodeXFCB,dataItem.getTimeType(),dataItem.getBeginTime(),dataItem.getEndTime());
      resultListXFCB.forEach(statisticResult -> statisticResult.setFormatdate("yyyy-MM-dd"));
      Map<String, List<StatisticResult>> groupByXFCB = resultListXFCB.stream().collect(Collectors.groupingBy(StatisticResult::getIndexName));
      List<Map> tableXFCB = new ArrayList<Map>();
      List<Map> mListXFCB = new ArrayList<Map>();
      groupByXFCB.forEach((indexName,value)->{
        Map tableColumn =new HashMap<>();
        tableColumn.put("indexName",indexName);
        tableXFCB.add(tableColumn);
        tableData.setResultXFCB(tableXFCB);
        Map table =new HashMap<>();
        List currentList = new ArrayList<>();
        List lastYearList = new ArrayList<>();
        for(StatisticResult result:value){
          currentList.add(new BigDecimal(result.getCurrentValue()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
          lastYearList.add(new BigDecimal(result.getLastYearValue()).setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue());
        }
        tableColumn.put("currentValue",currentList);
        tableColumn.put("lastYearValue",lastYearList);
        mListXFCB.add(tableColumn);
      });
      tableData.setResultListZBL(mListZBL);
      tableData.setResultListXFCB(mListXFCB);
      return AjaxResult.success(tableData);
    } catch (Exception ex) {
      logger.error("获取出错！", ex);
      return AjaxResult.error("获取出错!");
    }
  }
  /**
   * 能源消费成本分时分析报表
   */
  @GetMapping("/listEnergyConsumption")
  public AjaxResult listEnergyConsumption(DataItem dataItem){
    List<String> indexCodes = new ArrayList<String>();
    String[] indexCode =dataItem.getIndexCode().split(",");
    for(int i =0; i <indexCode.length ; i++){
      indexCodes.add(indexCode[i]);
    }
    List<StatisticResult> list= periodDataService.statisticByCode(indexCodes,dataItem.getTimeType(),dataItem.getBeginTime(),dataItem.getEndTime());
    list.forEach(statisticResult -> statisticResult.setFormatdate("yyyy-MM-dd"));
    List<EnergyConsumption> energyHeadList = new ArrayList<>();
    energyTable tableData =new energyTable();
    List<Map> tableDataMaps = new ArrayList<Map>();
    Map<String, List<StatisticResult>> groupBy = list.stream().collect(Collectors.groupingBy(StatisticResult::getFormatdate));
    groupBy.forEach((time,value)->{
      int propcount=0;//表格prop的数量
      int colcount=0;//表格列的数量
      Map tableColumn =new HashMap<>();//表数据
      for(StatisticResult result:value){
        EnergyConsumption energyHead = new EnergyConsumption();//一级表头
        energyHead.setLable(result.getIndexName());
        EnergyConsumption energyChildNode1 = new EnergyConsumption();//二级表头的第一列
        energyChildNode1.setProp("col"+colcount);
        energyChildNode1.setLable("耗量("+result.getUnitId()+")");
        tableColumn.put("col"+colcount,result.getMaxValue());
        EnergyConsumption energyChildNode2 = new EnergyConsumption();//二级表头的第一列
        colcount++;
        energyChildNode2.setProp("col"+colcount);
        energyChildNode2.setLable("成本(万元)");
        tableColumn.put("col"+colcount,result.getMinValue());
        colcount++;
        energyHead.addChildNode(energyChildNode1);
        energyHead.addChildNode(energyChildNode2);
        energyHeadList.add(energyHead);
        propcount++;
      }
      tableColumn.put("dateTims",time);
      tableDataMaps.add(tableColumn);
      tableData.setTabledataMap(tableDataMaps);
      tableData.setTablehead(energyHeadList);
    });
    return AjaxResult.success(tableData);
  }
  @Log(title = "能源消费成本分时分析报表", businessType = BusinessType.EXPORT)
  @GetMapping("/energyConsumptionExport")
  public AjaxResult energyConsumptionExport(DataItem dataItem){
    List<String> indexCodes = new ArrayList<String>();
    String[] indexCode =dataItem.getIndexCode().split(",");
    for(int i =0; i <indexCode.length ; i++){
      indexCodes.add(indexCode[i]);
    }
    List<StatisticResult> list= periodDataService.statisticByCode(indexCodes,dataItem.getTimeType(),dataItem.getBeginTime(),dataItem.getEndTime());
    ExcelUtil<StatisticResult> util = new ExcelUtil<StatisticResult>(StatisticResult.class);
    return util.exportExcel(list, "set");
  }
  /*
  用能单元能耗分析
   */
  @GetMapping("/getEnergyUnit")
  public AjaxResult getEnergyUnit(DataItem dataItem) {
    try {
      List<String> indexCode =  Collections.singletonList(dataItem.getIndexCode());
      // List<StatisticResult> list= periodDataService.statisticByCode(dataItem.getIndexCode(),timeType, dataItem.getBeginTime(), dataItem.getEndTime());
      List<StatisticResult> list= periodDataService.statisticByCode(indexCode,dataItem.getTimeType(),dataItem.getDataTime());
      list.forEach(statisticResult -> statisticResult.setFormatdate("yyyy-MM-dd"));
      return AjaxResult.success(list);
    } catch (Exception ex) {
      logger.error("获取出错！", ex);
      return AjaxResult.error("获取出错!");
    }
  }
  /*
     实时检测多sheet页展示组态图报表
   */
  @GetMapping("/reportFormsvg")
  public AjaxResult reportFormsvg(dataTimeSVG dataItem) {
    List<dataTimeSVG> list= IEnergyStatisticsService.reportFormsvg(dataItem);
    list.forEach(dataTimeSVG -> dataTimeSVG.setFormatdate("yyyy-MM-dd HH:mm:ss"));
    Map<String, List<dataTimeSVG>> groupBy = list.stream().collect(Collectors.groupingBy(dataTimeSVG::getIndexName));
    energyTable tableData =new energyTable();//表格
    List<Map> tablehead = new ArrayList<Map>();
    groupBy.forEach((indexName,value)->{
      tableData.setTabledata(value);
      Map tableColumn =new HashMap<>();
      tableColumn.put("indexName",indexName);
      tablehead.add(tableColumn);
      tableData.setTabledataMap(tablehead);
    });
    return AjaxResult.success(tableData);
  }
  /*
    实时检测多sheet页展示组态图报表导出
  */
  @GetMapping("/reportFormsvgExport")
  public AjaxResult reportFormsvgExport(dataTimeSVG dataItem) {
    List<dataTimeSVG> list = IEnergyStatisticsService.reportFormsvg(dataItem);
    ExcelUtil<dataTimeSVG> util = new ExcelUtil<dataTimeSVG>(dataTimeSVG.class);
    return util.exportExcel(list, "set");
  }
}
