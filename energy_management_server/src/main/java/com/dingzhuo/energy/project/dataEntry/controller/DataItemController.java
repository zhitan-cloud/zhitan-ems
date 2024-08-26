package com.dingzhuo.energy.project.dataEntry.controller;

import java.util.*;
import java.util.stream.Collectors;

import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.dataservice.domain.DataItem;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.project.common.CommonConst;
import com.dingzhuo.energy.project.common.DateTimeUtil;
import com.dingzhuo.energy.project.common.StringUtil;
import com.dingzhuo.energy.project.common.TimeTypeConst;
import com.dingzhuo.energy.project.energyConsumeInput.domain.EnergyConsumeInput;
import com.dingzhuo.energy.project.energyConsumeInput.domain.SaveEnergyConsumeInputEntity;
import com.dingzhuo.energy.project.energyConsumeInput.service.IEnergyConsumeInputService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.dataEntry.service.*;
import com.dingzhuo.energy.project.dataEntry.domain.*;

/**
 * 阶段数据录入
 *
 * @author sys
 * @date 2020-03-25
 */
@RestController
@Api(value = "阶段录入",tags = {"阶段录入"})
@RequestMapping("/dataEntry/stagseDataEntry")
public class DataItemController extends BaseController {
    @Autowired
    private IDataItemService dataItemService;
    @Autowired
    private PeriodDataService periodDataService;
    @Autowired
    private IEnergyConsumeInputService energyConsumeInputService;

    /**
     * 阶段数据录入查询
     */
    @PreAuthorize("@ss.hasPermi('dataEntry:stagseDataEntry:list')")
    @Log(title = "阶段数据录入查询", businessType = BusinessType.UPDATE)
    @GetMapping("/list")
    @ApiOperation(value = "阶段数据录入列表")
    public TableDataInfo list(stagseDataEntry stagseDataEntry) {
        stagseDataEntry.setDataTime(DateTimeUtil.getDateTimeWithTimeType(stagseDataEntry.getDataTimeStr(), stagseDataEntry.getTimeType().toString()));
        String timeCode = TimeManager.getTimeCode(stagseDataEntry.getDataTime(), stagseDataEntry.getTimeType());
        stagseDataEntry.setBeginTime(TimeManager.getBeginTime(timeCode));
        stagseDataEntry.setEndTime(TimeManager.getEndTime(timeCode));
        List<EnergyConsumeInput> list = energyConsumeInputService.selectEnergyConsumeInputList(stagseDataEntry.getNodeId(), timeCode, stagseDataEntry.getTimeType().toString());
        for (EnergyConsumeInput input : list) {
            input.setDataTimeStr(DateTimeUtil.getDateTimeStrWithTimeType(input.getDataTime(), stagseDataEntry.getTimeType().toString()));
            if (StringUtil.isEmptyOrNull(input.getDataTimeStr())) {
                input.setDataTimeStr(DateTimeUtil.getDateTimeStrWithTimeType(stagseDataEntry.getDataTime(), stagseDataEntry.getTimeType().toString()));
            }
            input.setCreateTimeStr(DateTimeUtil.toString(input.getCreateTime()));
            input.setTimeType(stagseDataEntry.getTimeType());
        }
        return getDataTable(list);
    }

    /**
     * 阶段数据录入查询
     */
    @PreAuthorize("@ss.hasPermi('dataEntry:stagseDataEntry:edit')")
    @Log(title = "阶段数据录入修改", businessType = BusinessType.UPDATE)
    @GetMapping("/edit")
    @ApiOperation(value = "阶段数据录入修改")
    public TableDataInfo edit(stagseDataEntry stagseDataEntry) {
        List<stagseDataEntry> list = dataItemService.getSettingEdit(stagseDataEntry);
        list.forEach(s -> {
            if (s.getDataTime() == null) {
                s.setDataTime(stagseDataEntry.getDataTime());
            }
        });
        return getDataTable(list);
    }

    /**
     * 新增阶段数据
     */
    @PreAuthorize("@ss.hasPermi('dataEntry:stagseDataEntry:add')")
    @PostMapping
    @ApiOperation(value = "新增阶段数据")
    public AjaxResult add(@RequestHeader("Authorization") String authorization, @RequestBody List<EnergyConsumeInput> listMap) {
        if (listMap != null && !listMap.isEmpty()) {
            EnergyConsumeInput temp = listMap.stream().findFirst().orElse(null);

            List<String> indexIds = listMap.stream().map(EnergyConsumeInput::getIndexId).collect(Collectors.toList());
            Date dataTime = DateTimeUtil.getDateTimeWithTimeType(temp.getDataTimeStr(), temp.getTimeType().toString());
            String timeCode = TimeManager.getTimeCode(dataTime, temp.getTimeType());
            int res = energyConsumeInputService.deleteEnergyConsumeInputByIds(indexIds, timeCode);

            List<SaveEnergyConsumeInputEntity> dataItemList = new ArrayList<>();
            Date endTime = DateTimeUtil.getEndTimeWithTimeType(dataTime, temp.getTimeType().toString());
            String userName = SecurityUtils.getUsername();
            for (EnergyConsumeInput input : listMap) {
                if (input.getValue() != null) {
                    SaveEnergyConsumeInputEntity data = new SaveEnergyConsumeInputEntity();
                    data.setValue(input.getValue());
                    data.setCreateTime(DateUtils.getNowDate());
                    data.setIndexId(input.getIndexId());
                    data.setIndexCode(input.getCode());
                    data.setTimeType(temp.getTimeType().toString());
                    data.setTimeCode(timeCode);
                    data.setDataTime(dataTime);
                    data.setBeginTime(dataTime);
                    data.setEndTime(endTime);
                    data.setCreateBy(userName);
                    dataItemList.add(data);
                }
            }
            int saveRes = energyConsumeInputService.saveEnergyConsumeInputList(dataItemList);
            if (saveRes > 0) {
                boolean mark = energyConsumeInputService.reCalcIndexIdByIds(indexIds, temp.getTimeType().toString(), authorization, dataTime);
            }
        }
        return AjaxResult.success();
    }

    /**
     * 阶段数据录入查询
     */
    @PreAuthorize("@ss.hasPermi('dataEntry:stagseDataEntry:listEdit')")
    @Log(title = "阶段数据录入查询", businessType = BusinessType.UPDATE)
    @GetMapping("/listEdit")
    @ApiOperation(value = "阶段数据录入查询")
    public AjaxResult c(stagseDataEntry stagseDataEntry) {
        List<String> indexCode = Arrays.asList(stagseDataEntry.getIndexCode().split(","));
        List<stagseDataEntry> list = dataItemService.stagseDataEntry(stagseDataEntry.getNodeId(), indexCode, stagseDataEntry.getTimeType(), stagseDataEntry.getBeginTime(), stagseDataEntry.getEndTime(), stagseDataEntry.getCalcType());
        return AjaxResult.success(list);
    }
}
