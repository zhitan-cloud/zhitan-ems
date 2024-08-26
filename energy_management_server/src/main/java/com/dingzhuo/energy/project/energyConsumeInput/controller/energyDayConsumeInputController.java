package com.dingzhuo.energy.project.energyConsumeInput.controller;

import com.alibaba.fastjson.JSON;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.common.*;
import com.dingzhuo.energy.project.energyConsumeInput.domain.*;
import com.dingzhuo.energy.project.energyConsumeInput.service.IEnergyConsumeInputService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对标Controller
 *
 * @author sys
 * @date 2020-12-22
 */
@RestController
@RequestMapping("/energyAssistInput/energyDayConsumeInput")
@Api(value = "日数据录入",tags = {"日数据录入"})
public class energyDayConsumeInputController extends BaseController {
    @Autowired
    private IEnergyConsumeInputService energyConsumeInputService;

    /**
     * 查询  日度能耗手动录入值 列表
     */
    @GetMapping("/listEnergyConsumeInputIndex")
    @ApiOperation(value = "日数据录入列表")
    public TableDataInfo list(String nodeId, String date, String timeType) {
        String tempDate = date;
        date = DateTimeUtil.toDateTimeStr(date, DateTimeUtil.COMMON_PATTERN_TO_DAY, DateTimeUtil.COMMON_PATTERN_DAY);
        String timeCode = CommonConst.WORD_D + date;
        List<EnergyConsumeInput> list = energyConsumeInputService.selectEnergyConsumeInputList(nodeId, timeCode, timeType);
        for (EnergyConsumeInput input : list) {
            input.setDataTimeStr(DateTimeUtil.getDateTime(input.getDataTime(), DateTimeUtil.COMMON_PATTERN_TO_DAY));
            if (StringUtil.isEmptyOrNull(input.getDataTimeStr())) {
                input.setDataTimeStr(tempDate);
            }
            input.setCreateTimeStr(DateTimeUtil.toString(input.getCreateTime()));
        }
        return getDataTable(list);
    }

    /**
     * 保存能耗手动输入信息
     *
     * @return
     */
    @PostMapping("/saveEnergyConsumeInput")
    @ApiOperation(value = "日数据录入保存")
    public TableDataInfo save(@RequestHeader("Authorization") String authorization, @RequestBody SaveEnergyConsumeInputModel model) {
        if (model != null && !model.getModels().isEmpty()) {
            EnergyConsumeInput temp = model.getModels().stream().findFirst().orElse(null);
            String timeCode = CommonConst.EMPTY;
            Date dataTime = null;
            if (temp != null) {
                String dt = temp.getDataTimeStr();
                dataTime = DateTimeUtil.toDateTime(dt, DateTimeUtil.COMMON_PATTERN_TO_DAY);
                timeCode = CommonConst.WORD_D + DateTimeUtil.getDay(dataTime);
            }
            List<String> indexIds = model.getModels().stream().map(EnergyConsumeInput::getIndexId).collect(Collectors.toList());

            int res = energyConsumeInputService.deleteEnergyConsumeInputByIds(indexIds, timeCode);

            List<SaveEnergyConsumeInputEntity> dataItemList = new ArrayList<>();
            Date endTime = DateTimeUtil.addSeconds(DateTimeUtil.addDays(dataTime, CommonConst.DIGIT_1), CommonConst.DIGIT_MINUS_1);
            String userName = SecurityUtils.getUsername();
            for (EnergyConsumeInput input : model.getModels()) {
                if (input.getValue() != null) {
                    SaveEnergyConsumeInputEntity data = new SaveEnergyConsumeInputEntity();
                    data.setValue(input.getValue());
                    data.setCreateTime(DateUtils.getNowDate());
                    data.setIndexId(input.getIndexId());
                    data.setIndexCode(input.getCode());
                    data.setTimeType(TimeTypeConst.TIME_TYPE_DAY);
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
                boolean mark = energyConsumeInputService.reCalcIndexIdByIds(indexIds, TimeTypeConst.TIME_TYPE_DAY, authorization, dataTime);
            }
        }
        List<String> res = new ArrayList<>();
        return getDataTable(res);
    }
}
