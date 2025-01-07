package com.zhitan.costmanagement.service;

import java.util.List;
import com.zhitan.costmanagement.domain.CostPriceRelevancy;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 单价关联Service接口
 *
 * @author ZhiTan
 * @date 2024-11-09
 */
public interface ICostPriceRelevancyService extends IService<CostPriceRelevancy> {
    /**
     * 查询单价关联
     *
     * @param id 单价关联主键
     * @return 单价关联
     */
    public CostPriceRelevancy selectCostPriceRelevancyById(String id);

    /**
     * 查询单价关联列表
     *
     * @param costPriceRelevancy 单价关联
     * @return 单价关联集合
     */
    public Page<CostPriceRelevancy> selectCostPriceRelevancyList(CostPriceRelevancy costPriceRelevancy,Long pageNum, Long pageSize);

    /**
     * 新增单价关联
     *
     * @param costPriceRelevancy 单价关联
     * @return 结果
     */
    public int insertCostPriceRelevancy(CostPriceRelevancy costPriceRelevancy);

    /**
     * 修改单价关联
     *
     * @param costPriceRelevancy 单价关联
     * @return 结果
     */
    public int updateCostPriceRelevancy(CostPriceRelevancy costPriceRelevancy);

    /**
     * 批量删除单价关联
     *
     * @param ids 需要删除的单价关联主键集合
     * @return 结果
     */
    public int deleteCostPriceRelevancyByIds(String[] ids);

    /**
     * 删除单价关联信息
     *
     * @param id 单价关联主键
     * @return 结果
     */
    public int deleteCostPriceRelevancyById(String id);
}
