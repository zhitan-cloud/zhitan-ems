package com.zhitan.costmanagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.UUID;

import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.costmanagement.domain.CostElectricityInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhitan.costmanagement.mapper.CostPriceRelevancyMapper;
import com.zhitan.costmanagement.domain.CostPriceRelevancy;
import com.zhitan.costmanagement.service.ICostPriceRelevancyService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 单价关联Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-11-09
 */
@Service
public class CostPriceRelevancyServiceImpl extends ServiceImpl<CostPriceRelevancyMapper, CostPriceRelevancy> implements ICostPriceRelevancyService {
    @Autowired
    private CostPriceRelevancyMapper costPriceRelevancyMapper;

    /**
     * 查询单价关联
     *
     * @param id 单价关联主键
     * @return 单价关联
     */
    @Override
    public CostPriceRelevancy selectCostPriceRelevancyById(String id) {
        return costPriceRelevancyMapper.selectCostPriceRelevancyById(id);
    }

    /**
     * 查询单价关联列表
     *
     * @param costPriceRelevancy 单价关联
     * @return 单价关联
     */
    @Override
    public Page<CostPriceRelevancy> selectCostPriceRelevancyList(CostPriceRelevancy costPriceRelevancy,Long pageNum, Long pageSize) {
        LambdaQueryWrapper<CostPriceRelevancy> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(costPriceRelevancy.getNodeId()),CostPriceRelevancy::getNodeId,costPriceRelevancy.getNodeId());
      if(costPriceRelevancy.getEnergyType()!=null){
          queryWrapper.eq(StringUtils.isNotEmpty(costPriceRelevancy.getEnergyType().toString()),CostPriceRelevancy::getEnergyType,costPriceRelevancy.getEnergyType());

      }
         queryWrapper.orderByDesc(CostPriceRelevancy::getCreateTime);
        Page<CostPriceRelevancy> page = costPriceRelevancyMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
        return page;
    }

    /**
     * 新增单价关联
     *
     * @param costPriceRelevancy 单价关联
     * @return 结果
     */
    @Override
    public int insertCostPriceRelevancy(CostPriceRelevancy costPriceRelevancy) {
        costPriceRelevancy.setCreateTime(DateUtils.getNowDate());
        costPriceRelevancy.setId(UUID.randomUUID().toString());
        return costPriceRelevancyMapper.insertCostPriceRelevancy(costPriceRelevancy);
    }

    /**
     * 修改单价关联
     *
     * @param costPriceRelevancy 单价关联
     * @return 结果
     */
    @Override
    public int updateCostPriceRelevancy(CostPriceRelevancy costPriceRelevancy) {
        costPriceRelevancy.setUpdateTime(DateUtils.getNowDate());
        return costPriceRelevancyMapper.updateCostPriceRelevancy(costPriceRelevancy);
    }

    /**
     * 批量删除单价关联
     *
     * @param ids 需要删除的单价关联主键
     * @return 结果
     */
    @Override
    public int deleteCostPriceRelevancyByIds(String[] ids) {
        return costPriceRelevancyMapper.deleteCostPriceRelevancyByIds(ids);
    }

    /**
     * 删除单价关联信息
     *
     * @param id 单价关联主键
     * @return 结果
     */
    @Override
    public int deleteCostPriceRelevancyById(String id) {
        return costPriceRelevancyMapper.deleteCostPriceRelevancyById(id);
    }
}
