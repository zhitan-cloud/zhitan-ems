package com.dingzhuo.energy.project.electricity.service;

import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
import java.util.List;

/**
 * electricityPriceService接口
 *
 * @author sys
 * @date 2020-02-18
 */
public interface IElectricityPriceService
{
    /**
     * 查询electricityPrice
     *
     * @param id electricityPriceID
     * @return electricityPrice
     */
    public ElectricityPrice selectElectricityPriceById(String id);

    /**
     * 查询electricityPrice列表
     *
     * @param electricityPrice electricityPrice
     * @return electricityPrice集合
     */
    public List<ElectricityPrice> selectElectricityPriceList(ElectricityPrice electricityPrice);
    /**
     * 查询生效日期列表
     *
     * @param electricityPrice electricityPrice
     * @return electricityPrice集合
     */
    public List<ElectricityPrice> selectList(ElectricityPrice electricityPrice);
    /**
     * 查询历史列表
     *
     * @param electricityPrice electricityPrice
     * @return electricityPrice集合
     */
    public List<ElectricityPrice> listHistory(ElectricityPrice electricityPrice);
    /**
     * 新增electricityPrice
     *
     * @param electricityPrice electricityPrice
     * @return 结果
     */
    public int insertElectricityPrice(ElectricityPrice electricityPrice);

    /**
     * 修改electricityPrice
     *
     * @param electricityPrice electricityPrice
     * @return 结果
     */
    public int updateElectricityPrice(ElectricityPrice electricityPrice);

    /**
     * 批量删除electricityPrice
     *
     * @param ids 需要删除的electricityPriceID
     * @return 结果
     */
    public int deleteElectricityPriceByIds(String[] ids);

    /**
     * 删除electricityPrice信息
     *
     * @param id electricityPriceID
     * @return 结果
     */
    public int deleteElectricityPriceById(String id);

}