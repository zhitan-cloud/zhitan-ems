package com.zhitan.peakvalley.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.peakvalley.domain.ElectricityPriceDate;

import java.util.List;

/**
 * 尖峰平谷电价时间段Service接口
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
public interface IElectricityPriceDateService extends IService<ElectricityPriceDate> {
    /**
     * 查询尖峰平谷电价时间段
     *
     * @param id 尖峰平谷电价时间段主键
     * @return 尖峰平谷电价时间段
     */
    public ElectricityPriceDate selectElectricityPriceDateById(String id);

    /**
     * 查询尖峰平谷电价时间段列表
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 尖峰平谷电价时间段集合
     */
    public List<ElectricityPriceDate> selectElectricityPriceDateList(ElectricityPriceDate electricityPriceDate);

    /**
     * 新增尖峰平谷电价时间段
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 结果
     */
    public int insertElectricityPriceDate(ElectricityPriceDate electricityPriceDate);

    /**
     * 修改尖峰平谷电价时间段
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 结果
     */
    public int updateElectricityPriceDate(ElectricityPriceDate electricityPriceDate);

    /**
     * 批量删除尖峰平谷电价时间段
     *
     * @param ids 需要删除的尖峰平谷电价时间段主键集合
     * @return 结果
     */
    public int deleteElectricityPriceDateByIds(String[] ids);

    /**
     * 删除尖峰平谷电价时间段信息
     *
     * @param id 尖峰平谷电价时间段主键
     * @return 结果
     */
    public int deleteElectricityPriceDateById(String id);

    Page<ElectricityPriceDate> selectElectricityPriceDatePage(ElectricityPriceDate electricityPriceDate, Long pageNum, Long pageSize);
}
