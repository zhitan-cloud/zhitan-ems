package com.dingzhuo.energy.project.reportForm.mapper;

import java.util.List;

import com.dingzhuo.energy.project.reportForm.domain.reportSet;
import com.dingzhuo.energy.project.reportForm.domain.reportSetDataModel;
import com.dingzhuo.energy.project.reportForm.domain.reportSetRequestModel;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: report_set
 * @Author: jeecg-boot
 * @Date: 2022-04-01
 * @Version: V1.0
 */
public interface reportSetMapper {
    /**
     * 获取上报设置列表
     *
     * @param nodeId  节点id
     * @param indexId 指标id
     * @return
     */
    List<reportSet> listReportSet(String nodeId, String indexId);
    /**
     * 获取节点下所有的上报设置列表
     *
     * @param nodeId  节点id
     * @return
     */
    List<reportSet> listNodeReportSet(String nodeId);

    /**
     * 保存上报配置
     *
     * @param listMap
     * @return
     */
    int saveReportSet(List<reportSet> listMap);

    /**
     * 删除该点位的上报配置
     *
     * @param nodeId
     * @param indexId
     * @param username
     * @return
     */
    int deleteReportSetByNodeId(String nodeId, String indexId, String username);

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
    int updateEnableStatus(String nodeId,String indexId, int enableStatus, String username);

    /**
     * 根据日期类型获取上报设置
     **/
    List<reportSet> getAllEnableSetByDateType(@Param("dateType") String dateType);
}
