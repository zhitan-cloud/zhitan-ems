package com.dingzhuo.energy.project.reportForm.service.impl;

import com.dingzhuo.energy.project.common.CommonConst;
import com.dingzhuo.energy.project.common.StringUtil;
import com.dingzhuo.energy.project.reportForm.domain.reportSet;
import com.dingzhuo.energy.project.reportForm.domain.reportSetDataModel;
import com.dingzhuo.energy.project.reportForm.domain.reportSetRequestModel;
import com.dingzhuo.energy.project.reportForm.mapper.reportSetMapper;
import com.dingzhuo.energy.project.reportForm.service.IreportSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * stagseDataEntryService业务层处理
 *
 * @author sys
 * @date 2020-03-25
 */
@Service
public class reportSetServiceImpl implements IreportSetService {
    @Autowired
    private reportSetMapper reportSetMapper;

    /**
     * 获取上报设置列表
     *
     * @param requestModel 参数
     * @return
     */
    @Override
    public List<reportSet> listReportSet(reportSetRequestModel requestModel) {
        List<reportSet> models = reportSetMapper.listReportSet(requestModel.getNodeId(), requestModel.getIndexId());
        if (models.isEmpty()) {
            reportSet daySet = new reportSet();
            daySet.setDateType("DAY");
            daySet.setIndexId(requestModel.getIndexId());
            daySet.setNodeId(requestModel.getNodeId());
            models.add(daySet);
            reportSet monthSet = new reportSet();
            monthSet.setDateType("MONTH");
            monthSet.setIndexId(requestModel.getIndexId());
            monthSet.setNodeId(requestModel.getNodeId());
            models.add(monthSet);
        }
        for (reportSet model : models) {
            if ("DAY".equals(model.getDateType())) {
                model.setDateTypeShow("日");
            } else {
                model.setDateTypeShow("月");
            }
        }
        return models;
    }

    /**
     * 保存上报配置
     *
     * @param listMap
     */
    @Override
    public int saveReportSet(List<reportSet> listMap, String username) {
        if (!listMap.isEmpty()) {
            for (reportSet reportSet : listMap) {
                reportSet.setCreateBy(username);
                reportSet.setUpdateBy(username);
                reportSet.setId(UUID.randomUUID().toString());
            }
            reportSet rs = listMap.get(0);
            int delRes = reportSetMapper.deleteReportSetByNodeId(rs.getNodeId(), rs.getIndexId(), username);
            int res = reportSetMapper.saveReportSet(listMap);
        }

        return 1;
    }

    /**
     * 获取节点下的点位列表
     *
     * @param nodeId
     * @param indexType
     * @return
     */
    @Override
    public List<reportSetDataModel> listNodeIndex(String nodeId, String indexType) {
        List<reportSetDataModel> models = reportSetMapper.listNodeIndex(nodeId, indexType);
        List<reportSet> reportSetList = reportSetMapper.listNodeReportSet(nodeId);
        for (reportSetDataModel model : models) {
            reportSet rs = reportSetList.stream().filter(x -> StringUtil.ifEmptyOrNullReturnValue(x.getIndexId()).equals(model.getIndexId())).findAny().orElse(null);
            if (rs != null) {
                if (rs.getEnableLimitValue() != null) {
                    model.setEnableLimitValue(rs.getEnableLimitValue() == CommonConst.DIGIT_1);
                }
            }
        }
        return models;
    }

    /**
     * 设置该点位的限值启用状态
     *
     * @param nodeId
     * @param indexId
     * @param enableStatus 要设置的状态
     * @param username
     * @return
     */
    @Override
    public int updateEnableStatus(String nodeId, String indexId, boolean enableStatus, String username) {
        List<reportSet> models = reportSetMapper.listReportSet(nodeId, indexId);
        if(models.isEmpty()){
            return -1;
        }
        int intEnableStatus = enableStatus ? CommonConst.DIGIT_1 : CommonConst.DIGIT_0;
        return reportSetMapper.updateEnableStatus(nodeId, indexId, intEnableStatus, username);
    }
}
