package com.dingzhuo.energy.project.energyConsumeInput.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingzhuo.energy.project.common.*;
import com.dingzhuo.energy.project.energyBalance.domain.EnergyPic;
import com.dingzhuo.energy.project.energyConsumeInput.domain.EnergyConsumeInput;
import com.dingzhuo.energy.project.energyConsumeInput.domain.ReCalcParamsModel;
import com.dingzhuo.energy.project.energyConsumeInput.domain.ReCalcRespModel;
import com.dingzhuo.energy.project.energyConsumeInput.domain.SaveEnergyConsumeInputEntity;
import com.dingzhuo.energy.project.energyConsumeInput.mapper.EnergyConsumeInputMapper;
import com.dingzhuo.energy.project.energyConsumeInput.service.IEnergyConsumeInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工序单耗统计功能Service业务层处理
 *
 * @author zhaow
 * @date 2020-12-26
 */
@Service
public class EnergyConsumeInputServiceImpl implements IEnergyConsumeInputService {
    @Autowired
    private EnergyConsumeInputMapper energyConsumeInputMapper;

    /**
     * 查询  能耗手动录入 数据列表
     *
     * @return 能耗手动录入    功能集合
     */
    @Override
    public List<EnergyConsumeInput> selectEnergyConsumeInputList(String nodeId, String timeCode, String timeType) {
        List<EnergyConsumeInput> resList = energyConsumeInputMapper.selectEnergyConsumeInputList(nodeId, timeCode, timeType);
        return resList;
    }

    /**
     * 根据日期和指标id删除数据
     *
     * @param indexIds 指标id列表
     * @param timeCode 月份值
     * @return
     */
    @Override
    public int deleteEnergyConsumeInputByIds(List<String> indexIds, String timeCode) {
        return energyConsumeInputMapper.deleteEnergyConsumeInputByIds(indexIds, timeCode);
    }

    /**
     * 保存能耗手动输入数据
     *
     * @param models
     * @return
     */
    @Override
    public int saveEnergyConsumeInputList(List<SaveEnergyConsumeInputEntity> models) {
        return energyConsumeInputMapper.saveEnergyConsumeInputList(models);
    }

    /**
     * 重算相关指标
     *
     * @param indexIds
     * @param timeType
     * @param authorization
     * @param dataTime
     * @return
     */
    @Override
    public boolean reCalcIndexIdByIds(List<String> indexIds, String timeType, String authorization, Date dataTime) {
        boolean mark = false;
        List<EnergyConsumeInput> energyConsumeInputs = energyConsumeInputMapper.queryReCalcIndexIdByIds(indexIds);
        List<String> indexIdList = energyConsumeInputs.stream().map(EnergyConsumeInput::getIndexId).collect(Collectors.toList());
        if (!indexIdList.isEmpty()) {
            List<ReCalcParamsModel> reCalcParamsModels = new ArrayList<>();
            for (String indexId : indexIdList) {
                ReCalcParamsModel t1 = new ReCalcParamsModel();
                t1.setIndexId(indexId);
                t1.setDataTime(DateTimeUtil.toString(dataTime));
                t1.setTimeType(timeType);
                reCalcParamsModels.add(t1);
            }
            String json = StringUtil.toJson(reCalcParamsModels);
            String result = HttpClientUtil.doGet(CommonConst.RE_CALC_INTERFACE_ADDRESS, json, authorization);
            if (!StringUtil.isEmptyOrNull(result)) {
                ReCalcRespModel respModel = JSON.parseObject(result, ReCalcRespModel.class);
                if (respModel != null && respModel.getCode() == 200) {
                    mark = true;
                }
            }
        }

        return mark;
    }
}
