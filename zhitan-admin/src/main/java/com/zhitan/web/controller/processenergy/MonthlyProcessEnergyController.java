package com.zhitan.web.controller.processenergy;

import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.model.domain.EnergyIndex;
import com.zhitan.model.domain.ModelNode;
import com.zhitan.model.service.IModelNodeService;
import com.zhitan.processenergy.domain.MonthlyProcessEnergy;
import com.zhitan.processenergy.service.IMonthlyProcessEnergyService;
import com.zhitan.realtimedata.domain.DataItem;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
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
 * 工序能耗 月
 */
@RestController
@RequestMapping("/processEnergy/monthlyProcessEnergy")
@Api(value = "工序能耗统计（月）", tags = {"工序能耗统计"})
public class MonthlyProcessEnergyController extends BaseController {

    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private IMonthlyProcessEnergyService monthlyProcessEnergyService;

    /**
     *
     * @param dataItem
     * @return
     * @throws ParseException
     */
    @GetMapping("/list")
    @ApiOperation(value = "工序能耗统计（月）")
    public AjaxResult list(DataItem dataItem) throws ParseException {
        List<MonthlyProcessEnergy> dataList = new ArrayList<>();

        Map tableColumn = new HashMap<>();//表数据
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa = df.format(dataItem.getDataTime());
        String bb = "";
        int i = 1;
        String beginTime = aa + "-01 00:00:00";
        dataItem.setBeginTime(sf.parse(beginTime));
        String endTime = aa + "-" + Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length() - 2)) + " 00:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        while (i <= Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length() - 2))) {
            if (i > 9) {
                bb = aa + "-" + i + " 00:00:00";
            } else {
                bb = aa + "-0" + i + " 00:00:00";
            }
            MonthlyProcessEnergy report = new MonthlyProcessEnergy();
            report.setDataTime(sf.parse(bb));
            report.setValue("value" + i);
            dataList.add(report);
            tableColumn.put("value" + i, String.valueOf(i) + "日");
            i++;
        }
        List<Map> table = new ArrayList<>();
        MonthlyProcessEnergy reportList = new MonthlyProcessEnergy();
        table.add(tableColumn);
        reportList.setTablehead(table);
        List<ModelNode> nodeId = modelNodeService.getModelNodeByModelCode(dataItem.getIndexCode());
        if (CollectionUtils.isEmpty(nodeId)) {
            return success(new ArrayList<>());
        }
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(nodeId.get(0).getNodeId());
        if (CollectionUtils.isEmpty(energyList)) {
            return success(new ArrayList<>());
        }
        List<String> indexIds = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());

        List<MonthlyProcessEnergy> list = monthlyProcessEnergyService.getMonthlyProcessEnergy(indexIds, dataList, dataItem.getBeginTime(), dataItem.getEndTime(), dataItem.getTimeType(), dataItem.getEnergyType());

        return success(list);
    }

    /**
     *
     * @param dataItem
     * @return
     * @throws ParseException
     */
    @GetMapping("/listChart")
    @ApiOperation(value = "重点设备能耗统计（月）图表")
    public AjaxResult listChart(DataItem dataItem) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String aa = df.format(dataItem.getDataTime());
        String beginTime = aa + "-01 00:00:00";
        dataItem.setBeginTime(sf.parse(beginTime));
        String endTime = aa + "-" + Integer.valueOf(getLastDayOfMonth(aa).substring(getLastDayOfMonth(aa).length() - 2)) + " 00:00:00";
        dataItem.setEndTime(sf.parse(endTime));
        List<MonthlyProcessEnergy> list = monthlyProcessEnergyService.getListChart(dataItem.getIndexId(), dataItem.getBeginTime(), dataItem.getEndTime(), dataItem.getTimeType(), dataItem.getEnergyType());
        return AjaxResult.success(list);
    }

    /**
     *
     * @param yearMonth
     * @return
     */
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
