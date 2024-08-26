package com.dingzhuo.energy.basic.data.facility.mapper;

import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import java.util.List;

/**
 * 设备档案Mapper接口
 *
 * @author zhaowei
 * @date 2020-02-24
 */
public interface FacilityArchivesMapper
{
    /**
     * 查询设备档案
     *
     * @param id 设备档案ID
     * @return 设备档案
     */
    public FacilityArchives selectFacilityArchivesById(String id);

    /**
     * 查询设备档案的code是否存在
     *
     * @param facilityArchives 设备档案facilityArchives
     * @return 设备档案
     */
    public FacilityArchives selectFacilityArchivesByCode(FacilityArchives facilityArchives);
    /**
     * 查询设备档案列表
     *
     * @param facilityArchives 设备档案
     * @return 设备档案集合
     */
    public List<FacilityArchives> selectFacilityArchivesList(FacilityArchives facilityArchives);
    /**
     * 查询设备档案导出列表
     *
     * @param facilityArchives 设备档案
     * @return 设备档案集合
     */
    public List<FacilityArchives> excelFacilityArchivesList(FacilityArchives facilityArchives);

    /**
     * 新增设备档案
     *
     * @param facilityArchives 设备档案
     * @return 结果
     */
    public int insertFacilityArchives(FacilityArchives facilityArchives);

    /**
     * 修改设备档案
     *
     * @param facilityArchives 设备档案
     * @return 结果
     */
    public int updateFacilityArchives(FacilityArchives facilityArchives);

    /**
     * 删除设备档案
     *
     * @param id 设备档案ID
     * @return 结果
     */
    public int deleteFacilityArchivesById(String id);

    /**
     * 批量删除设备档案
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteFacilityArchivesByIds(String[] ids);



/*
    *
     * 检定恢复设备档案
     *
     * @param id 设备档案ID
     * @return 结果
    public int resetFacilityArchivesById(String[] id);
*/

    /**
     * 批量检定恢复设备档案
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int resetFacilityArchivesByIds(String[] ids);
}
