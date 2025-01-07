package com.zhitan.energyIndicators.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.energyIndicators.domain.EnergyIndicators;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 能源指标Service接口
 *
 * @author ZhiTan
 * @date 2024-10-25
 */
public interface IEnergyIndicatorsService extends IService<EnergyIndicators> {
    /**
     * 查询能源指标
     *
     * @param nodeId 能源指标主键
     * @return 能源指标
     */
    public EnergyIndicators selectEnergyIndicatorsByNodeId(String nodeId);

    /**
     * 查询能源指标列表
     *
     * @param energyIndicators 能源指标
     * @return 能源指标集合
     */
    public Page<EnergyIndicators> selectEnergyIndicatorsPage(EnergyIndicators energyIndicators, Long pageNum, Long pageSize);

    public List<EnergyIndicators> selectEnergyIndicatorsList(EnergyIndicators energyIndicators);

    /**
     * 新增能源指标
     *
     * @param energyIndicators 能源指标
     * @return 结果
     */
    public int insertEnergyIndicators(EnergyIndicators energyIndicators);

    /**
     * 修改能源指标
     *
     * @param energyIndicators 能源指标
     * @return 结果
     */
    public int updateEnergyIndicators(EnergyIndicators energyIndicators);

    /**
     * 批量删除能源指标
     *
     * @param energyIndicatorsIds 需要删除的能源指标主键集合
     * @return 结果
     */
    public int deleteEnergyIndicatorsByEnergyIndicatorsIds(String[] energyIndicatorsIds);

    /**
     * 删除能源指标信息
     *
     * @param energyIndicatorsId 能源指标主键
     * @return 结果
     */
    public int deleteEnergyIndicatorsByEnergyIndicatorsId(String energyIndicatorsId);
}
