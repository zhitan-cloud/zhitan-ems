package com.dingzhuo.energy.project.comprehensiveStatistics.controller;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.project.comprehensiveStatistics.domain.yearComperhensive;
import com.dingzhuo.energy.project.comprehensiveStatistics.service.IyearComprehensive;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * electricityPriceController
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/comprehensive/yearComprehensive")
@Api(value = "综合指标分析（年）controller",tags = {"综合指标分析"})
public class yearComprehensiveController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IyearComprehensive yearComprehensive;

    /*全厂能耗统计*/
    @GetMapping("/list")
    @ApiOperation(value = "获取综合指标分析（年）列表")
    public AjaxResult list(DataItem dataItem) {
        try {
            ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dataItem.getIndexCode());
            if (ObjectUtils.isEmpty(modelNode)) {
                return AjaxResult.success("暂无数据");
            }
            List<yearComperhensive> dataList = new ArrayList<>();
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
                yearComperhensive report=new yearComperhensive();
                report.setDataTime(sf.parse(bb));
                report.setValue("value"+i);
                dataList.add(report);
                i++;
            }
            List<yearComperhensive> list = yearComprehensive.getYearComprehensiveList(modelNode.getNodeId(),
                    dataList,dataItem.getBeginTime(), dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    /**
     * 全厂综合能耗统计图
     */
    @GetMapping("/listChart")
    @ApiOperation(value = "获取综合指标分析图表（年）数据")
    public AjaxResult listChart(DataItem dataItem){
        List<yearComperhensive> list = yearComprehensive.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return AjaxResult.success(list);
    }

    /**
     * 导出工序单耗统计指标设置功能列表
     */
    @Log(title = "综合报表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation(value = "综合年报表导出")
    public AjaxResult export(DataItem dataItem) {
        try {
            ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dataItem.getIndexCode());
            if (ObjectUtils.isEmpty(modelNode)) {
                return AjaxResult.success("暂无数据");
            }
            List<yearComperhensive> dataList = new ArrayList<>();
            DateFormat df = new SimpleDateFormat("yyyy");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String aa = df.format(dataItem.getBeginTime());
            String bb = "";
            int i = 1;
            while (i <= 12) {
                if(i>9){
                    bb=aa+"-"+i+"-01 00:00:00";
                }else{
                    bb=aa+"-0"+i+"-01 00:00:00";
                }
                yearComperhensive report=new yearComperhensive();
                report.setDataTime(sf.parse(bb));
                report.setValue("value"+i);
                dataList.add(report);
                i++;
            }
            List<yearComperhensive> list = yearComprehensive.getYearComprehensiveList(modelNode.getNodeId(),
                    dataList,dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
            if(CollectionUtils.isNotEmpty(list)){
                list.forEach(this::valueRep);
            }
            ExcelUtil<yearComperhensive> util = new ExcelUtil<>(yearComperhensive.class);
            return util.exportExcel(list, "综合指标分析年");
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    public void valueRep(Object dr){
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

}
