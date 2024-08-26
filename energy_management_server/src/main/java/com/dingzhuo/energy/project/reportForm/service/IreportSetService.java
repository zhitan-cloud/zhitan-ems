package com.dingzhuo.energy.project.reportForm.service;

import com.dingzhuo.energy.project.reportForm.domain.reportSet;
import com.dingzhuo.energy.project.reportForm.domain.reportSetDataModel;
import com.dingzhuo.energy.project.reportForm.domain.reportSetRequestModel;

import java.util.List;

/**
 * 阶段数据录入接口
 *
 * @author sys
 * @date 2020-03-25
 */
public interface IreportSetService {
    /**
     * 获取上报设置列表
     *
     * @param requestModel 参数
     * @return
     */
    List<reportSet> listReportSet(reportSetRequestModel requestModel);

    /**
     * 保存上报配置
     *
     * @param listMap
     */
    int saveReportSet(List<reportSet> listMap, String username);

    /**
     * 获取节点下的点位列表
     * @param nodeId
     * @param indexType
     * @return
     */
    List<reportSetDataModel> listNodeIndex(String nodeId, String indexType);

    /**
     * 设置该点位的限值启用状态
     * @param nodeId
     * @param indexId
     * @param enableStatus 要设置的状态
     * @param username
     * @return
     */
    int updateEnableStatus(String nodeId,String indexId, boolean enableStatus, String username);
}
