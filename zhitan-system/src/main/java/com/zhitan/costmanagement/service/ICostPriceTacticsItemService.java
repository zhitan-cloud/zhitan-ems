package com.zhitan.costmanagement.service;

import java.util.List;
import com.zhitan.costmanagement.domain.CostPriceTacticsItem;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
public interface ICostPriceTacticsItemService extends IService<CostPriceTacticsItem> {
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
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteCostPriceTacticsItemByIds(String[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteCostPriceTacticsItemById(String id);
}
