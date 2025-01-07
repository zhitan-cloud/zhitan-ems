package com.zhitan.web.controller.comprehensivestatistics;

import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.comprehensivestatistics.domain.DailyComprehensive;
import com.zhitan.comprehensivestatistics.service.IDailyComprehensiveService;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.realtimedata.domain.DataItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/comprehensive/dailyComprehensive")
@Api(value = "综合指标分析（日）controller",tags = {"综合指标分析"})
public class DailyComprehensiveController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IDailyComprehensiveService dailyComprehensiveService;

    /*全厂能耗统计*/
    @ApiOperation(value = "获取综合指标分析（日）列表")
    @GetMapping("/list")
    public AjaxResult list(DataItem dataItem) {
        try {
            ModelNode modelNode = modelNodeService.getModelNodeByModelCodeByIndexCode(dataItem.getIndexCode());
            if (ObjectUtils.isEmpty(modelNode)) {
                return AjaxResult.success("暂无数据");
            }
            List<DailyComprehensive> dataList = new ArrayList<>();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String aa = df.format(dataItem.getDataTime());
            String bb = "";
            int i = 0;
            dataItem.setBeginTime(dataItem.getDataTime());
            String endTime=aa+" 24:00:00";
            dataItem.setEndTime(sf.parse(endTime));
            while (i < 24) {
                if(i>9){
                    bb = aa + " " + i + ":00:00";
                } else {
                    bb = aa + " 0" + i + ":00:00";
                }
                DailyComprehensive report = new DailyComprehensive();
                report.setDataTime(sf.parse(bb));
                report.setValue("value" + i);
                dataList.add(report);
                i++;
            }
            List<DailyComprehensive> list = dailyComprehensiveService.getDailyComprehensiveList(modelNode.getNodeId(),
                    dataList, dataItem.getBeginTime(), dataItem.getEndTime(), dataItem.getTimeType(), dataItem.getIndexStorageId());
            return AjaxResult.success(list);
        } catch (Exception ex) {
            logger.error("获取出错！", ex);
            return AjaxResult.error("获取出错!");
        }
    }

    /**
     * 全厂综合能耗统计图
     */
    @ApiOperation(value = "获取综合指标分析图表（日）数据")
    @GetMapping("/listChart")
    public AjaxResult listChart(DataItem dataItem) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa= df.format(dataItem.getDataTime());
        dataItem.setBeginTime(dataItem.getDataTime());
        String endTime=aa+" 24:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        List<DailyComprehensive> list = dailyComprehensiveService.getListChart(dataItem.getIndexId(),dataItem.getBeginTime(),dataItem.getEndTime(), dataItem.getTimeType(),dataItem.getIndexStorageId());
        return AjaxResult.success(list);
    }

}
