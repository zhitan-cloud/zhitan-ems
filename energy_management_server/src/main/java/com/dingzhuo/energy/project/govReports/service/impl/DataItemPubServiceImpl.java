package com.dingzhuo.energy.project.govReports.service.impl;

import java.util.List;

import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.project.govReports.domain.DataItemPubVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.govReports.mapper.DataItemPubMapper;
import com.dingzhuo.energy.project.govReports.domain.DataItemPub;
import com.dingzhuo.energy.project.govReports.service.IDataItemPubService;

/**
 * GovReportsService业务层处理
 *
 * @author zy
 * @date 2022-04-06
 */
@Service
public class DataItemPubServiceImpl implements IDataItemPubService {

    @Autowired
    private DataItemPubMapper dataItemPubMapper;

    /**
     * 查询GovReports
     *
     * @param indexId GovReportsID
     * @return GovReports
     */
    @Override
    public DataItemPub selectDataItemPubById(String indexId) {
        return dataItemPubMapper.selectDataItemPubById(indexId);
    }

    /**
     * 查询GovReports列表
     *
     * @param dataItemPub GovReports
     * @return GovReports
     */
    @Override
    public List<DataItemPub> selectDataItemPubList(DataItemPub dataItemPub) {
        return dataItemPubMapper.selectDataItemPubList(dataItemPub);
    }

    /**
     * 新增GovReports
     *
     * @param dataItemPub GovReports
     * @return 结果
     */
    @Override
    public int insertDataItemPub(DataItemPub dataItemPub) {
        dataItemPub.setCreateTime(DateUtils.getNowDate());
        return dataItemPubMapper.insertDataItemPub(dataItemPub);
    }

    /**
     * 修改GovReports
     *
     * @param dataItemPub GovReports
     * @return 结果
     */
    @Override
    public int updateDataItemPub(DataItemPub dataItemPub) {
        dataItemPub.setUpdateTime(DateUtils.getNowDate());
        return dataItemPubMapper.updateDataItemPub(dataItemPub);
    }

    /**
     * 批量删除GovReports
     *
     * @param indexIds 需要删除的GovReportsID
     * @return 结果
     */
    @Override
    public int deleteDataItemPubByIds(String[] indexIds) {
        return dataItemPubMapper.deleteDataItemPubByIds(indexIds);
    }

    /**
     * 删除GovReports信息
     *
     * @param indexId GovReportsID
     * @return 结果
     */
    @Override
    public int deleteDataItemPubById(String indexId) {
        return dataItemPubMapper.deleteDataItemPubById(indexId);
    }

    @Override
    public List<DataItemPub> getReportData(String timeCode, String dateType) {
        return dataItemPubMapper.getReportData(timeCode, dateType);
    }

    /**
     * 插入或者更新数据
     **/
    @Override
    public int insertOrUpdateDateItemPub(List<DataItemPub> list) {
        return dataItemPubMapper.insertOrUpdateDateItemPub(list);
    }

    /**
     * 查询GovReports的显示信息列表
     *
     * @param dataItemPub GovReports
     * @return GovReports
     */
    @Override
    public List<DataItemPubVo> selectDataItemPubInfoList(DataItemPub dataItemPub) {
        return dataItemPubMapper.selectDataItemPubInfoList(dataItemPub);
    }
}
