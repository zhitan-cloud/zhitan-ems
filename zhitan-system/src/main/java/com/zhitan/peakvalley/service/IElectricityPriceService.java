package com.zhitan.peakvalley.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.peakvalley.domain.ElectricityPrice;

import java.util.List;

/**
 * 【尖峰平谷电价明细】Service接口
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
public interface IElectricityPriceService extends IService<ElectricityPrice> {
    /**
     * 查询【尖峰平谷电价明细】
     *
     * @param id 【尖峰平谷电价明细】主键
     * @return 【尖峰平谷电价明细】
     */
    public ElectricityPrice selectElectricityPriceById(String id);

    /**
     * 查询【尖峰平谷电价明细】列表
     *
     * @param electricityPrice 【尖峰平谷电价明细】
     * @return 【尖峰平谷电价明细】集合
     */
    public List<ElectricityPrice> selectElectricityPriceList(ElectricityPrice electricityPrice);

    /**
     * 新增【尖峰平谷电价明细】
     *
     * @param electricityPrice 【尖峰平谷电价明细】
     * @return 结果
     */
    public int insertElectricityPrice(ElectricityPrice electricityPrice);

    /**
     * 修改【尖峰平谷电价明细】
     *
     * @param electricityPrice 【尖峰平谷电价明细】
     * @return 结果
     */
    public int updateElectricityPrice(ElectricityPrice electricityPrice);

    /**
     * 批量删除【尖峰平谷电价明细】
     *
     * @param ids 需要删除的【尖峰平谷电价明细】主键集合
     * @return 结果
     */
    public int deleteElectricityPriceByIds(String[] ids);

    /**
     * 删除【尖峰平谷电价明细】信息
     *
     * @param id 【尖峰平谷电价明细】主键
     * @return 结果
     */
    public int deleteElectricityPriceById(String id);

    void saveList(List<ElectricityPrice> electricityPriceList);
}
