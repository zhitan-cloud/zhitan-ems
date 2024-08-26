package com.dingzhuo.energy.project.govReports.service;

import com.dingzhuo.energy.project.govReports.domain.DataItemPub;
import com.dingzhuo.energy.project.govReports.domain.DataItemPubVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * GovReportsService接口
 *
 * @author zy
 * @date 2022-04-06
 */
public interface IDataItemPubService {
    /**
     * 查询GovReports
     *
     * @param indexId GovReportsID
     * @return GovReports
     */
    DataItemPub selectDataItemPubById(String indexId);

    /**
     * 查询GovReports列表
     *
     * @param dataItemPub GovReports
     * @return GovReports集合
     */
    List<DataItemPub> selectDataItemPubList(DataItemPub dataItemPub);

    /**
     * 新增GovReports
     *
     * @param dataItemPub GovReports
     * @return 结果
     */
    int insertDataItemPub(DataItemPub dataItemPub);

    /**
     * 修改GovReports
     *
     * @param dataItemPub GovReports
     * @return 结果
     */
    int updateDataItemPub(DataItemPub dataItemPub);

    /**
     * 批量删除GovReports
     *
     * @param indexIds 需要删除的GovReportsID
     * @return 结果
     */
    int deleteDataItemPubByIds(String[] indexIds);

    /**
     * 删除GovReports信息
     *
     * @param indexId GovReportsID
     * @return 结果
     */
    int deleteDataItemPubById(String indexId);

    /**
     * 获取需要处理的数据
     *
     * @param dateType 日期类型 DAY 天  MONTH 月
     * @param timeCode 时间编码
     **/
    List<DataItemPub> getReportData(String timeCode, String dateType);

    /**
     * 插入或者更新数据
     **/
    int insertOrUpdateDateItemPub(List<DataItemPub> list);

    /**
     * 查询GovReports的显示信息列表
     *
     * @param dataItemPub GovReports
     * @return GovReports集合
     */
    List<DataItemPubVo> selectDataItemPubInfoList(DataItemPub dataItemPub);
}
