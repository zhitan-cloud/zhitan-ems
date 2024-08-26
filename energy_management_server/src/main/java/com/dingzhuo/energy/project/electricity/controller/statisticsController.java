package com.dingzhuo.energy.project.electricity.controller;

import com.dingzhuo.energy.common.utils.Arith;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.electricity.domain.*;
import com.dingzhuo.energy.project.electricity.service.IstatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * electricityPriceController
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/electricityPrice/statistics")
public class statisticsController extends BaseController {
    @Autowired
    private IstatisticsService statisticsService;

    @Autowired
    private PeriodDataService periodDataService;
    @GetMapping("/getStatisticsList")
    public AjaxResult getStatisticsList(statistics statistics) {
        List<EnergyIndex> list = statisticsService.getStatisticsList(statistics);//查询指标list
        List<String> indexIds= list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<electricityDataItem> result = statisticsService.getPeriodDatasByIndex(indexIds, statistics.getBeginTime(),statistics.getEndTime(),statistics.getTimeType());//峰平谷指标计算数据
        List<TimePeriodPrice> priceList = statisticsService.getElectricityPriceList(statistics);//峰平谷价格
        electricitTable tableData =new electricitTable();//表格
        if(result.size()>0){
            Map<String, List<electricityDataItem>> groupBy = result.stream().collect(Collectors.groupingBy(electricityDataItem::getFormatdate));//根据时间进行分组
            List<electricity> energyHeadList = new ArrayList<>();//表头
            List<Map> tableDataMaps = new ArrayList<Map>();//表格数据
            List<Map> table = new ArrayList<Map>();
            //拼接表头
            /*groupBy.forEach((indexName,value)->{*/
                electricity energyHead = new electricity();
                energyHead.setLable(statistics.getNodeName());//一级标题
                AtomicInteger colcount= new AtomicInteger(2);//表格列的数量
                priceList.forEach((item)->{
                    Map map =new HashMap<>();//表数据
                    electricity energyChildNode1 = new electricity();
                    energyChildNode1.setLable(item.getElectricityName());//二级标题
                    energyChildNode1.setValue("用量");//三级标题
                    energyChildNode1.setPrice("费用");//三级标题
                    energyChildNode1.setValueProp("col"+colcount);
                    map.put("用量","col"+colcount);
                    colcount.getAndIncrement();
                    energyChildNode1.setPriceProp("col"+colcount);
                    map.put("费用","col"+colcount);
                    map.put("col"+colcount,item.getPrices());
                    map.put("code",item.getTimePeriod());
                    colcount.getAndIncrement();
                    energyHead.addChildNode(energyChildNode1);
                    table.add(map);
                });
                energyHeadList.add(energyHead);
            /*});*/
            tableData.setTablehead(energyHeadList);
            //表格数据
           groupBy.forEach((indexName,value)->{
               Map tableColumn =new HashMap<>();//表数据
                value.forEach((data)->{
                    tableColumn.put("name",indexName);
                    table.forEach((item)->{
                        String bb =item.get("code").toString().substring(0, 2);
                        String b =data.getIndexCode().substring(0, 2);
                        if(bb.equals("F_") && b.equals("F_")){
                            Double outlay=new BigDecimal(data.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            Double usage=new BigDecimal(item.get(item.get("费用")).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            tableColumn.put(item.get("用量"),outlay);
                            tableColumn.put(item.get("费用"),Arith.mul(outlay, usage));
                        }
                        if(bb.equals("P_") && b.equals("P_")){
                            Double outlay=new BigDecimal(data.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            Double usage=new BigDecimal(item.get(item.get("费用")).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            tableColumn.put(item.get("用量"),outlay);
                            tableColumn.put(item.get("费用"),Arith.mul(outlay, usage));
                        }
                        if(bb.equals("G_") && b.equals("G_")){
                            Double outlay=new BigDecimal(data.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            Double usage=new BigDecimal(item.get(item.get("费用")).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            tableColumn.put(item.get("用量"),outlay);
                            tableColumn.put(item.get("费用"),Arith.mul(outlay, usage));
                        }
                        if(bb.equals("J_") && b.equals("J_")){
                            Double outlay=new BigDecimal(data.getValue()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            Double usage=new BigDecimal(item.get(item.get("费用")).toString()).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            tableColumn.put(item.get("用量"),outlay);
                            tableColumn.put(item.get("费用"),Arith.mul(outlay, usage));
                        }
                    });
                });
               tableDataMaps.add(tableColumn);
            });
            tableData.setTabledataMap(tableDataMaps);
        }
        return AjaxResult.success(tableData);
    }

    @GetMapping("/getDataStatistics")
    public AjaxResult getDataStatistics(statistics statistics) {
        List<EnergyIndex> list = statisticsService.getStatisticsList(statistics);//查询指标list
        List<String> indexIds= list.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        //List<electricityDataItem> result = statisticsService.getDatasByIndex(indexIds, statistics.getTimeType());//查询峰平谷指标数据
        List<electricityDataItem> result = statisticsService.getDatasByIndex(indexIds, statistics.getBeginTime(),statistics.getEndTime(),statistics.getTimeType());//峰平谷指标计算数据
        List<TimePeriodPrice> priceList = statisticsService.getElectricityPriceList(statistics);//峰平谷价格
        electricitTable tableData =new electricitTable();
        List<Map> tableDataMaps = new ArrayList<Map>();
        //峰平谷占比
        priceList.forEach(item->{
            String bb =item.getTimePeriod().substring(0, 2);
            result.forEach(data->{
                String dataItem =data.getIndexCode().substring(0, 2);
                Map map =new HashMap<>();
                if(bb.equals(dataItem)){
                    map.put("value", Arith.mul(data.getValue(), item.getPrices()));
                    map.put("name",data.getIndexName());
                }
                tableDataMaps.add(map) ;
            });
        });
        tableData.setTabledataMap(tableDataMaps);
        return AjaxResult.success(tableData);
    }
}