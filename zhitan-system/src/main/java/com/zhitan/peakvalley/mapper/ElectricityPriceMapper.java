package com.zhitan.peakvalley.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.peakvalley.domain.ElectricityPrice;

import java.util.List;

/**
 * 【尖峰平谷电价明细】Mapper接口
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
public interface ElectricityPriceMapper extends BaseMapper<ElectricityPrice> {
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
     * 删除【尖峰平谷电价明细】
     *
     * @param id 【尖峰平谷电价明细】主键
     * @return 结果
     */
    public int deleteElectricityPriceById(String id);

    /**
     * 批量删除【尖峰平谷电价明细】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteElectricityPriceByIds(String[] ids);
}
