package com.dingzhuo.energy.basic.data.meter.service;

import com.dingzhuo.energy.basic.data.meter.domain.MeterImplement;
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplementExcel;
import com.dingzhuo.energy.framework.security.LoginUser;

import java.util.List;

/**
 * 计量器具档案维护Service接口
 * 
 * @author zhaowei
 * @date 2020-02-12
 */
public interface IMeterImplementService 
{
    /**
     * 查询计量器具档案维护
     * 
     * @param id 计量器具档案维护ID
     * @return 计量器具档案维护
     */
    public MeterImplement selectMeterImplementById(String id);
    /**
     * 查询计量器具档案维护
     *
     * @param meterImplement 计量器具档案维护编号
     * @return 计量器具档案维护
     */
    public MeterImplement selectMeterImplementByCode(MeterImplement meterImplement);
    /**
     * 查询计量器具档案维护列表
     * 
     * @param meterImplement 计量器具档案维护
     * @return 计量器具档案维护集合
     */
    public List<MeterImplement> selectMeterImplementList(MeterImplement meterImplement);

    /**
     * 导出计量器具档案维护列表
     *
     * @param meterImplement 计量器具档案维护
     * @return 计量器具档案维护集合
     */
    public List<MeterImplementExcel> exectMeterImplementList(MeterImplement meterImplement);

    /**
     * 新增计量器具档案维护
     * 
     * @param meterImplement 计量器具档案维护
     * @return 结果
     */
    public int insertMeterImplement(MeterImplement meterImplement);

    /**
     * 修改计量器具档案维护
     * 
     * @param meterImplement 计量器具档案维护
     * @return 结果
     */
    public int updateMeterImplement(MeterImplement meterImplement);

    /**
     * 批量删除计量器具档案维护
     * 
     * @param ids 需要删除的计量器具档案维护ID
     * @return 结果
     */
    public int deleteMeterImplementByIds(String[] ids);

    /**
     * 删除计量器具档案维护信息
     * 
     * @param id 计量器具档案维护ID
     * @return 结果
     */
    public int deleteMeterImplementById(String id);

    /**
     * Excel导入 计量器具档案维护信息
     *
     * @param meterImplementList 要导入的计量器具档案集合
     * @param loginUser          登录用户对象
     * @return 结果
     */
    public String excelImpSave(List<MeterImplement> meterImplementList, LoginUser loginUser);

    /**
     * 根据id集合查询计量器具信息
     *
     * @param meterIdList
     * @return
     */
    List<MeterImplement> listMeterImplementByIds(List<String> meterIdList);
}
