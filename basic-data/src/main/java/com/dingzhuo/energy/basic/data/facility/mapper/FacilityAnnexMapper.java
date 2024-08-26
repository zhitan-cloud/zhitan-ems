package com.dingzhuo.energy.basic.data.facility.mapper;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityAnnex;
import java.util.List;

/**
 * 设备档案附件Mapper接口
 *
 * @author sys
 * @date 2020-02-24
 */
public interface FacilityAnnexMapper
{
    /**
     * 查询设备档案附件
     *
     * @param id 设备档案附件ID
     * @return 设备档案附件
     */
    public FacilityAnnex selectFacilityAnnexById(String  id);

    /**
     * 查询设备档案附件列表
     *
     * @param facilityAnnex 设备档案附件
     * @return 设备档案附件集合
     */
    public List<FacilityAnnex> selectFacilityAnnexList(FacilityAnnex facilityAnnex);

    /**
     * 新增设备档案附件
     *
     * @param facilityAnnex 设备档案附件
     * @return 结果
     */
    public int insertFacilityAnnex(FacilityAnnex facilityAnnex);

    /**
     * 修改设备档案附件
     *
     * @param facilityAnnex 设备档案附件
     * @return 结果
     */
    public int updateFacilityAnnex(FacilityAnnex facilityAnnex);

    /**
     * 删除设备档案附件
     *
     * @param id 设备档案附件ID
     * @return 结果
     */
    public int deleteFacilityAnnexById(String id);

    /**
     * 批量删除设备档案附件
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFacilityAnnexByIds(String[] ids);
}
