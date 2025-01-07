package com.zhitan.costmanagement.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.costmanagement.domain.CostPriceTacticsItem;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
public interface CostPriceTacticsItemMapper extends BaseMapper<CostPriceTacticsItem> {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public CostPriceTacticsItem selectCostPriceTacticsItemById(String id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param costPriceTacticsItem 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostPriceTacticsItem> selectCostPriceTacticsItemList(CostPriceTacticsItem costPriceTacticsItem);

    /**
     * 新增【请填写功能名称】
     *
     * @param costPriceTacticsItem 【请填写功能名称】
     * @return 结果
     */
    public int insertCostPriceTacticsItem(CostPriceTacticsItem costPriceTacticsItem);

    /**
     * 修改【请填写功能名称】
     *
     * @param costPriceTacticsItem 【请填写功能名称】
     * @return 结果
     */
    public int updateCostPriceTacticsItem(CostPriceTacticsItem costPriceTacticsItem);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteCostPriceTacticsItemById(String id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCostPriceTacticsItemByIds(String[] ids);
}
