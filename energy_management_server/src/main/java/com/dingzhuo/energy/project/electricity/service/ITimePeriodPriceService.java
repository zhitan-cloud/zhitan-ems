package com.dingzhuo.energy.project.electricity.service;

import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
import com.dingzhuo.energy.project.electricity.domain.TimePeriodPrice;
import java.util.List;

/**
 * electricityService接口
 * 
 * @author sys
 * @date 2020-02-19
 */
public interface ITimePeriodPriceService 
{
    /**
     * 查询electricity
     * 
     * @param id electricityID
     * @return electricity
     */
    public TimePeriodPrice selectTimePeriodPriceById(String id);

    /**
     * 查询electricity列表
     * 
     * @param timePeriodPrice electricity
     * @return electricity集合
     */
    public List<TimePeriodPrice> selectTimePeriodPriceList(TimePeriodPrice timePeriodPrice);

    /**
     * 新增electricity
     * 
     * @param timePeriodPrice electricity
     * @return 结果
     */
    public int insertTimePeriodPrice(TimePeriodPrice timePeriodPrice);
    /**
     * 修改electricity
     * 
     * @param timePeriodPrice electricity
     * @return 结果
     */
    public int updateTimePeriodPrice(TimePeriodPrice timePeriodPrice);

    /**
     * 批量删除electricity
     * 
     * @param ids 需要删除的electricityID
     * @return 结果
     */
    public int deleteTimePeriodPriceByIds(String[] ids);

    /**
     * 删除electricity信息
     * 
     * @param id electricityID
     * @return 结果
     */
    public int deleteTimePeriodPriceById(String id);

    public List<TimePeriodPrice> selectDictType(TimePeriodPrice timePeriodPrice);

    public List<TimePeriodPrice> dictTypeList(TimePeriodPrice timePeriodPrice);
}
