package com.zhitan.costmanagement.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.zhitan.costmanagement.domain.vo.CostPriceTacticsVo;

/**
 * 成本策略Mapper接口
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
public interface CostPriceTacticsMapper extends BaseMapper<CostPriceTactics> {
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
    public List<CostPriceTactics> selectCostPriceTacticsList(CostPriceTactics costPriceTactics);

    /**
     * 新增成本策略
     *
     * @param costPriceTactics 成本策略
     * @return 结果
     */
    public int insertCostPriceTactics(CostPriceTactics costPriceTactics);

    /**
     * 修改成本策略
     *
     * @param costPriceTactics 成本策略
     * @return 结果
     */
    public int updateCostPriceTactics(CostPriceTactics costPriceTactics);

    /**
     * 删除成本策略
     *
     * @param id 成本策略主键
     * @return 结果
     */
    public int deleteCostPriceTacticsById(String id);

    /**
     * 批量删除成本策略
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCostPriceTacticsByIds(String[] ids);

    /**
     * 查询所有单价策略列表
     *

     * @return 成本策略集合
     */
    public List<CostPriceTacticsVo> selectCostPriceTacticsListAll();
}
