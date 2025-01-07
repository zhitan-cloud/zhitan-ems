package com.zhitan.energyIndicators.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
        import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.energyIndicators.domain.EnergyIndicators;
import com.zhitan.energyIndicators.mapper.EnergyIndicatorsMapper;
import com.zhitan.energyIndicators.service.IEnergyIndicatorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 能源指标Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-10-25
 */
@Service
public class EnergyIndicatorsServiceImpl extends ServiceImpl<EnergyIndicatorsMapper, EnergyIndicators> implements IEnergyIndicatorsService {
    @Autowired
    private EnergyIndicatorsMapper energyIndicatorsMapper;

    /**
     * 查询能源指标
     *
     * @param nodeId 能源指标主键
     * @return 能源指标
     */
    @Override
    public EnergyIndicators selectEnergyIndicatorsByNodeId(String nodeId) {
        return energyIndicatorsMapper.selectEnergyIndicatorsByNodeId(nodeId);
    }

    @Override
    public Page<EnergyIndicators> selectEnergyIndicatorsPage(EnergyIndicators energyIndicators, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<EnergyIndicators> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.isNotEmpty(energyIndicators.getTimeType()),EnergyIndicators::getTimeType,energyIndicators.getTimeType());
        wrapper.eq(StringUtils.isNotEmpty(energyIndicators.getDataTime()),EnergyIndicators::getDataTime,energyIndicators.getDataTime());
        wrapper.like(StringUtils.isNotEmpty(energyIndicators.getName()),EnergyIndicators::getName,energyIndicators.getName());
        wrapper.between(StringUtils.isNotEmpty(energyIndicators.getBeginTime()),EnergyIndicators::getDataTime,energyIndicators.getBeginTime(),energyIndicators.getEndTime());
        wrapper.eq(StringUtils.isNotEmpty(energyIndicators.getEnergyType()),EnergyIndicators::getEnergyType,energyIndicators.getEnergyType());
        wrapper.eq(StringUtils.isNotEmpty(energyIndicators.getIndicatorsType()),EnergyIndicators::getIndicatorsType,energyIndicators.getIndicatorsType());
        wrapper.eq(StringUtils.isNotEmpty(energyIndicators.getNodeId()),EnergyIndicators::getNodeId,energyIndicators.getNodeId());
        wrapper.orderByDesc(EnergyIndicators::getCreateTime);
        Page<EnergyIndicators> page = energyIndicatorsMapper.selectPage(new Page<EnergyIndicators>(pageNum, pageSize), wrapper);
        return page;
//        return energyIndicatorsMapper.selectEnergyIndicatorsList(energyIndicators);
    }
    /**
     * 查询能源指标列表
     *
     * @param energyIndicators 能源指标
     * @return 能源指标
     */
    @Override
    public List<EnergyIndicators> selectEnergyIndicatorsList(EnergyIndicators energyIndicators) {
        return energyIndicatorsMapper.selectEnergyIndicatorsList(energyIndicators);
    }

    /**
     * 新增能源指标
     *
     * @param energyIndicators 能源指标
     * @return 结果
     */
    @Override
    public int insertEnergyIndicators(EnergyIndicators energyIndicators) {
        energyIndicators.setCreateTime(DateUtils.getNowDate());
        return energyIndicatorsMapper.insertEnergyIndicators(energyIndicators);
    }

    /**
     * 修改能源指标
     *
     * @param energyIndicators 能源指标
     * @return 结果
     */
    @Override
    public int updateEnergyIndicators(EnergyIndicators energyIndicators) {
        energyIndicators.setUpdateTime(DateUtils.getNowDate());
        return energyIndicatorsMapper.updateEnergyIndicators(energyIndicators);
    }

    /**
     * 批量删除能源指标
     *
     * @param energyIndicatorsId 需要删除的能源指标主键
     * @return 结果
     */
    @Override
    public int deleteEnergyIndicatorsByEnergyIndicatorsIds(String[] energyIndicatorsId) {
        return energyIndicatorsMapper.deleteEnergyIndicatorsByEnergyIndicatorsIds(energyIndicatorsId);
    }

    /**
     * 删除能源指标信息
     *
     * @param energyIndicatorsId 能源指标主键
     * @return 结果
     */
    @Override
    public int deleteEnergyIndicatorsByEnergyIndicatorsId(String energyIndicatorsId) {
        return energyIndicatorsMapper.deleteEnergyIndicatorsByEnergyIndicatorsId(energyIndicatorsId);
    }
}
