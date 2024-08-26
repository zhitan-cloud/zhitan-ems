package com.dingzhuo.energy.basic.data.facility.service;

import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;

import java.util.List;

/**
 * 设备档案Service接口
 *
 * @author zhaowei
 * @date 2020-02-24
 */
public interface IFacilityArchivesService
{
    /**
     * 查询设备档案
     *
     * @param id 设备档案ID
     * @return 设备档案
     */
    public FacilityArchives selectFacilityArchivesById(String id);

    /**
     * 查询设备档案code
     *
     * @param facilityArchives 设备档案
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
     * 查询设备档案列表导出列表
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
     * 批量删除设备档案
     *
     * @param ids 需要删除的设备档案ID
     * @return 结果
     */
    public int deleteFacilityArchivesByIds(String[] ids);

    /**
     * 删除设备档案信息
     *
     * @param id 设备档案ID
     * @return 结果
     */
    public int deleteFacilityArchivesById(String id);


    /**
     * 批量检定恢复设备档案
     *
     * @param ids 需要删除的设备档案ID
     * @return 结果
     */
    public int resetFacilityArchivesByIds(String[] ids);

   /* *//**
     * 检定恢复设备档案信息
     *
     * @param id 设备档案ID
     * @return 结果
     *//*
    public int resetFacilityArchivesById(String[] id);*/

    /**
     * Excel导入 设备档案维护信息
     *
     * @param facilityArchivesList  要导入的设备档案集合
     * @param loginUser 登录用户对象
     * @return 结果
     */
    public String excelImpSave(List<FacilityArchives> facilityArchivesList, LoginUser loginUser);
}
