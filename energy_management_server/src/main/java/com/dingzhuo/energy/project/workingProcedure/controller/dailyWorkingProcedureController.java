package com.dingzhuo.energy.project.workingProcedure.controller;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.workingProcedure.domain.dailyWorkingProcedure;
import com.dingzhuo.energy.project.workingProcedure.service.IdailyWorkingProcedure;
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
 *工序能耗统计
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/workingProcedure/dailyWorkingProcedure")
public class dailyWorkingProcedureController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IdailyWorkingProcedure dailyWorkingProcedure;

    @GetMapping("/list")
    public AjaxResult list(DataItem dataItem) {
        try {
            List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
            List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
            List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
            List<dailyWorkingProcedure> dataList=new ArrayList<>();
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
                dailyWorkingProcedure report=new dailyWorkingProcedure();
                report.setDataTime(sf.parse(bb));
                report.setValue("value"+i);
                dataList.add(report);
                i++;
            };
            List<dailyWorkingProcedure> list = dailyWorkingProcedure.getdailyWorkingProcedureList(indexIds, dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    @GetMapping("/listChart")
    public AjaxResult listChart(DataItem dataItem) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        dataItem.setBeginTime(dataItem.getDataTime());
        String endTime=aa+" 24:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        List<dailyWorkingProcedure> list = dailyWorkingProcedure.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return AjaxResult.success(list);
    }
}