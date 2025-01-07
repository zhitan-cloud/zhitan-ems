package com.zhitan.costmanagement.service;

import java.util.List;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.costmanagement.domain.vo.CostPriceTacticsVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 成本策略Service接口
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
public interface ICostPriceTacticsService extends IService<CostPriceTactics> {
    /**
     * 查询成本策略
     *
     * @param id 成本策略主键
     * @return 成本策略
     */
    public CostPriceTactics selectCostPriceTacticsById(String id);

    /**
     * 查询成本策略列表
     *
     * @param costPriceTactics 成本策略
     * @return 成本策略集合
     */
    public Page<CostPriceTacticsVo> selectCostPriceTacticsList(CostPriceTactics costPriceTactics, Long pageNum, Long pageSize);

    /**
     * 查询所有单价策略列表
     *

     * @return 成本策略集合
     */
    public List<CostPriceTacticsVo> selectCostPriceTacticsListAll();

    /**
     * 新增成本策略
     *
     * @param costPriceTacticsVo 成本策略
     * @return 结果
     */
    public int insertCostPriceTactics(CostPriceTacticsVo costPriceTacticsVo) throws Exception;

    /**
     * 修改成本策略
     *
     * @param costPriceTacticsVo 成本策略
     * @return 结果
     */
    public int updateCostPriceTactics(CostPriceTacticsVo costPriceTacticsVo);

    /**
     * 批量删除成本策略
     *
     * @param ids 需要删除的成本策略主键集合
     * @return 结果
     */
    public int deleteCostPriceTacticsByIds(String[] ids);

    /**
     * 删除成本策略信息
     *
     * @param id 成本策略主键
     * @return 结果
     */
    public int deleteCostPriceTacticsById(String id);
}
