package com.dingzhuo.energy.project.energyBalance.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;
import com.dingzhuo.energy.project.energyBalance.service.IEnergyBalanceService;
import com.dingzhuo.energy.project.energyBalance.service.IEnergyPicService;
import com.dingzhuo.energy.project.reportForm.domain.reportForm;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;

/**
 * 对标Controller
 * 
 * @author sys
 * @date 2020-12-22
 */
@RestController
@RequestMapping("/balance/energyBalance")
public class energyBalanceController extends BaseController
{
    @Autowired
    private IModelNodeService modelNodeService;
    @Autowired
    private PeriodDataService periodDataService;
    @Autowired
    private IEnergyPicService energyPicService;
    @Autowired
    private IEnergyBalanceService balanceService;
    /**
     * 能源平衡分析
     */
    @PreAuthorize("@ss.hasPermi('balance:energyBalance:list')")
    @GetMapping("/list")
    public AjaxResult list(DataItem dataItem)
    {
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(dataItem.getIndexCode());
        List<String> indexCode = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<DataItem> list = balanceService.getBalanceList(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
        return AjaxResult.success(list);
    }
    @PreAuthorize("@ss.hasPermi('balance:energyBalance:tableList')")
    @GetMapping("/tableList")
    public AjaxResult tableList(DataItem dataItem)
    {
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(dataItem.getIndexCode());
        List<String> indexCode = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<DataItem> list = periodDataService.getDatasByIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
        List<Map> energyHeadList = new ArrayList<Map>();
        Map tableColumn =new HashMap<>();//表数据
        list.forEach(result->{
            if(result.getIndexCode().lastIndexOf("_CC")==result.getIndexCode().length()-3){
                tableColumn.put("value_CC",result.getValue());
            }else if(result.getIndexCode().lastIndexOf("_XH")==result.getIndexCode().length()-3){
                tableColumn.put("value_XH",result.getValue());
            }else if(result.getIndexCode().lastIndexOf("_WG")==result.getIndexCode().length()-3){
                tableColumn.put("value_WG",result.getValue());
            }else if(result.getIndexCode().lastIndexOf("_SH")==result.getIndexCode().length()-3){
                tableColumn.put("value_SH",result.getValue());
            }
        });
        energyHeadList.add(tableColumn);
        return AjaxResult.success(energyHeadList);
    }
    /**
     * 能源损失分析
     */
    @PreAuthorize("@ss.hasPermi('balance:energyBalance:lossAnalysisList')")
    @GetMapping("/lossAnalysisList")
    public AjaxResult lossAnalysisList(DataItem dataItem)
    {
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(dataItem.getIndexCode());
        List<String> indexCode = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<DataItem> list = periodDataService.getDatasByIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
        return AjaxResult.success(list);
    }
    @PreAuthorize("@ss.hasPermi('balance:energyBalance:lossTableList')")
    @GetMapping("/lossTableList")
    public AjaxResult lossTableList(DataItem dataItem)
    {
        List<EnergyIndex> energyList = modelNodeService.getSettingIndex(dataItem.getIndexCode());
        List<String> indexCode = energyList.stream().map(EnergyIndex::getIndexId).collect(Collectors.toList());
        List<DataItem> list = periodDataService.getDatasByIndex(indexCode, dataItem.getDataTime(), dataItem.getTimeType());
        List<Map> energyHeadList = new ArrayList<Map>();
        Map tableColumn =new HashMap<>();//表数据
        list.forEach(result->{
            if(result.getIndexCode().lastIndexOf("_CC")==result.getIndexCode().length()-3){
                tableColumn.put("value_CC",result.getValue());
            }else if(result.getIndexCode().lastIndexOf("_XH")==result.getIndexCode().length()-3){
                tableColumn.put("value_XH",result.getValue());
            }else if(result.getIndexCode().lastIndexOf("_WG")==result.getIndexCode().length()-3){
                tableColumn.put("value_WG",result.getValue());
            }else if(result.getIndexCode().lastIndexOf("_SH")==result.getIndexCode().length()-3){
                tableColumn.put("value_SH",result.getValue());
            }else if(result.getIndexCode().lastIndexOf("_total")==result.getIndexCode().length()-6){
                tableColumn.put("value_total",result.getValue());
            }
        });
        energyHeadList.add(tableColumn);
        return AjaxResult.success(energyHeadList);
    }
    /**
     * 查询  能流分析   功能报表  列表
     */
    @GetMapping("/listEnergyPicIndex")
    public TableDataInfo list(EnergyPic energyPic){
        List<EnergyPic> list = energyPicService.selectEnergyPicList(energyPic);
        return getDataTable(list);
    }
}
