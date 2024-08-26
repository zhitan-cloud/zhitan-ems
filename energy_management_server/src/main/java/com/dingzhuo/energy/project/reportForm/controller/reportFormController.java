package com.dingzhuo.energy.project.reportForm.controller;

import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.reportForm.domain.reportForm;
import com.dingzhuo.energy.project.reportForm.service.IreportFormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 报表
 * @author sys
 * @date 2020-03-25
 */
@RestController
@RequestMapping("/reportForm")
class reportFormController extends BaseController {
    @Autowired
    private IreportFormService reportFormService;

    /**
     * 报表查询
     */
    @PreAuthorize("@ss.hasPermi('dailyReport:list')")
    @GetMapping("/list")
    public AjaxResult list(reportForm report) {
        List<reportForm> list= reportFormService.selectReportFormList(report.getCode());//根据父级查询二级数据
        List<reportForm> resultList= reportFormService.selectReportFormListp(report.getCode());//根据二级查询三级数据
        List<reportForm> result= reportFormService.selectList(report.getTimeDate(),report.getCode(),report.getTimeYear(),report.getTimeType());//查询三级

        List<Map> table = new ArrayList<Map>();
        List<reportForm> tables = new ArrayList<reportForm>();
        resultList.forEach(value->{
            list.forEach(item->{
                if(item.getParentId().equals(value.getNodeId())){
                    Map map=new HashMap();
                    map.put("parentName",value.getName());
                    map.put("name",item.getName());
                    map.put("nodeId",item.getNodeId());
                    table.add(map);
                }
            });
        });
        Map<String, List<reportForm>> groupBy = result.stream().filter(item-> StringUtils.isNotBlank(item.getNodeId())).collect(Collectors.groupingBy(reportForm::getNodeId));
        groupBy.forEach((indexName,value)->{
            for (Map item : table) {
                //Map map=new HashMap();
                reportForm map=new reportForm();
                if(item.get("nodeId").equals(indexName)){
                    for(reportForm resultValue:value){
                        if(report.getTimeYear().equals(resultValue.getTimeYear())){
                            //map.put("value13",resultValue.getValue());
                            map.setValue13(resultValue.getValue());
                        }else  if(resultValue.getTimeDate().equals("01")){
                            //map.put("value1",resultValue.getValue());
                            map.setValue1(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("02")){
                            //map.put("value2",resultValue.getValue());
                            map.setValue2(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("03")){
                            //map.put("value3",resultValue.getValue());
                            map.setValue3(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("04")){
                            //map.put("value4",resultValue.getValue());
                            map.setValue4(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("05")){
                            //map.put("value5",resultValue.getValue());
                            map.setValue5(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("06")){
                            //map.put("value6",resultValue.getValue());
                            map.setValue6(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("07")){
                            //map.put("value7",resultValue.getValue());
                            map.setValue7(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("08")){
                            //map.put("value8",resultValue.getValue());
                            map.setValue8(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("09")){
                            //map.put("value9",resultValue.getValue());
                            map.setValue9(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("10")){
                            //map.put("value10",resultValue.getValue());
                            map.setValue10(resultValue.getValue());
                        }else if(resultValue.getTimeDate().equals("11")){
                            //map.put("value11",resultValue.getValue());
                            map.setValue11(resultValue.getValue());
                        }else {
                            //map.put("value12",resultValue.getValue());
                            map.setValue12(resultValue.getValue());
                        }
                    }
                    //map.put("parentName",item.get("parentName"));
                    //map.put("name",item.get("name"));
                    map.setParentName((String) item.get("parentName"));
                    map.setName((String) item.get("name"));
                    tables.add(map);
                }
            }
        });
        return AjaxResult.success(tables);
    }
}
