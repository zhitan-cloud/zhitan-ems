package com.zhitan.costmanagement.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import java.util.UUID;

import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysItem;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysScheme;
import com.zhitan.spikesandvalleys.domain.vo.SpikesAndValleysSchemeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.zhitan.costmanagement.mapper.CostElectricityInputMapper;
import com.zhitan.costmanagement.domain.CostElectricityInput;
import com.zhitan.costmanagement.service.ICostElectricityInputService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@Service
public class CostElectricityInputServiceImpl extends ServiceImpl<CostElectricityInputMapper, CostElectricityInput> implements ICostElectricityInputService {
    @Autowired
    private CostElectricityInputMapper costElectricityInputMapper;

    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public CostElectricityInput selectCostElectricityInputById(String id) {
        return costElectricityInputMapper.selectCostElectricityInputById(id);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public Page<CostElectricityInput> selectCostElectricityInputList(CostElectricityInput costElectricityInput,Long pageNum, Long pageSize) {
        LambdaQueryWrapper<CostElectricityInput> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(costElectricityInput.getTime()),CostElectricityInput::getTime,costElectricityInput.getTime());
        queryWrapper.eq(StringUtils.isNotEmpty(costElectricityInput.getType()),CostElectricityInput::getType,costElectricityInput.getType());
        queryWrapper.orderByDesc(CostElectricityInput::getCreateTime);
        Page<CostElectricityInput> page = costElectricityInputMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);


        return page;
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 结果
     */
    @Override
    @Transactional
    public int insertCostElectricityInput(CostElectricityInput costElectricityInput) throws Exception {
        costElectricityInput.setCreateTime(DateUtils.getNowDate());
        costElectricityInput.setId(UUID.randomUUID().toString());

        CostElectricityInput search = costElectricityInputMapper.selectOne(new QueryWrapper<CostElectricityInput>().
                eq("type",costElectricityInput.getType()).eq("time",costElectricityInput.getTime()));
        if (search!=null){
            throw new Exception("该时间段已维护电量信息！");
        }
        return costElectricityInputMapper.insertCostElectricityInput(costElectricityInput);
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCostElectricityInput(CostElectricityInput costElectricityInput) throws Exception {
        costElectricityInput.setUpdateTime(DateUtils.getNowDate());
        CostElectricityInput search = costElectricityInputMapper.selectOne(new QueryWrapper<CostElectricityInput>().
                eq("type",costElectricityInput.getType()).eq("time",costElectricityInput.getTime()));
        if (search!=null){
            throw new Exception("该时间段已维护电量信息！");
        }
        return costElectricityInputMapper.updateCostElectricityInput(costElectricityInput);
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCostElectricityInputByIds(String[] ids) {
        return costElectricityInputMapper.deleteCostElectricityInputByIds(ids);
    }

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteCostElectricityInputById(String id) {
        return costElectricityInputMapper.deleteCostElectricityInputById(id);
    }
}
