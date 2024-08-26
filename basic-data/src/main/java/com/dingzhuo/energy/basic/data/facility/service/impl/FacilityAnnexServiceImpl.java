package com.dingzhuo.energy.basic.data.facility.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.facility.mapper.FacilityAnnexMapper;
import com.dingzhuo.energy.basic.data.facility.domain.FacilityAnnex;
import com.dingzhuo.energy.basic.data.facility.service.IFacilityAnnexService;

/**
 * 设备档案附件Service业务层处理
 *
 * @author sys
 * @date 2020-02-24
 */
@Service
public class FacilityAnnexServiceImpl implements IFacilityAnnexService
{
    @Autowired
    private FacilityAnnexMapper facilityAnnexMapper;

    /**
     * 查询设备档案附件
     *
     * @param id 设备档案附件ID
     * @return 设备档案附件
     */
    @Override
    public FacilityAnnex selectFacilityAnnexById(String id)
    {
        return facilityAnnexMapper.selectFacilityAnnexById(id);
    }

    /**
     * 查询设备档案附件列表
     *
     * @param facilityAnnex 设备档案附件
     * @return 设备档案附件
     */
    @Override
    public List<FacilityAnnex> selectFacilityAnnexList(FacilityAnnex facilityAnnex)
    {
        return facilityAnnexMapper.selectFacilityAnnexList(facilityAnnex);
    }

    /**
     * 新增设备档案附件
     *
     * @param facilityAnnex 设备档案附件
     * @return 结果
     */
    @Override
    public int insertFacilityAnnex(FacilityAnnex facilityAnnex)
    {
        facilityAnnex.setCreateTime(DateUtils.getNowDate());
        return facilityAnnexMapper.insertFacilityAnnex(facilityAnnex);
    }

    /**
     * 修改设备档案附件
     *
     * @param facilityAnnex 设备档案附件
     * @return 结果
     */
    @Override
    public int updateFacilityAnnex(FacilityAnnex facilityAnnex)
    {
        facilityAnnex.setUpdateTime(DateUtils.getNowDate());
        return facilityAnnexMapper.updateFacilityAnnex(facilityAnnex);
    }

    /**
     * 批量删除设备档案附件
     *
     * @param ids 需要删除的设备档案附件ID
     * @return 结果
     */
    @Override
    public int deleteFacilityAnnexByIds(String[] ids)
    {
        return facilityAnnexMapper.deleteFacilityAnnexByIds(ids);
    }

    /**
     * 删除设备档案附件信息
     *
     * @param id 设备档案附件ID
     * @return 结果
     */
    @Override
    public int deleteFacilityAnnexById(String id)
    {
        return facilityAnnexMapper.deleteFacilityAnnexById(id);
    }
}
