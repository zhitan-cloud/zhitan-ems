package com.zhitan.costmanagement.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
        import com.zhitan.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhitan.costmanagement.mapper.CostPriceTacticsItemMapper;
import com.zhitan.costmanagement.domain.CostPriceTacticsItem;
import com.zhitan.costmanagement.service.ICostPriceTacticsItemService;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@Service
public class CostPriceTacticsItemServiceImpl extends ServiceImpl<CostPriceTacticsItemMapper, CostPriceTacticsItem> implements ICostPriceTacticsItemService {
    @Autowired
    private CostPriceTacticsItemMapper costPriceTacticsItemMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public CostPriceTacticsItem selectCostPriceTacticsItemById(String id) {
        return costPriceTacticsItemMapper.selectCostPriceTacticsItemById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param costPriceTacticsItem 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<CostPriceTacticsItem> selectCostPriceTacticsItemList(CostPriceTacticsItem costPriceTacticsItem) {
        return costPriceTacticsItemMapper.selectCostPriceTacticsItemList(costPriceTacticsItem);
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param costPriceTacticsItem 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int insertCostPriceTacticsItem(CostPriceTacticsItem costPriceTacticsItem) {
        costPriceTacticsItem.setCreateTime(DateUtils.getNowDate());
        return costPriceTacticsItemMapper.insertCostPriceTacticsItem(costPriceTacticsItem);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param costPriceTacticsItem 【请填写功能名称】
     * @return 结果
     */
    @Override
    public int updateCostPriceTacticsItem(CostPriceTacticsItem costPriceTacticsItem) {
        costPriceTacticsItem.setUpdateTime(DateUtils.getNowDate());
        return costPriceTacticsItemMapper.updateCostPriceTacticsItem(costPriceTacticsItem);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCostPriceTacticsItemByIds(String[] ids) {
        return costPriceTacticsItemMapper.deleteCostPriceTacticsItemByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    public int deleteCostPriceTacticsItemById(String id) {
        return costPriceTacticsItemMapper.deleteCostPriceTacticsItemById(id);
    }
}
