package com.zhitan.benchmarkmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.benchmarkmanage.domain.EnergyBenchmarkManage;
import com.zhitan.benchmarkmanage.mapper.EnergyBenchmarkManageMapper;
import com.zhitan.benchmarkmanage.service.IEnergyBenchmarkManageService;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 标杆值管理Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-11-12
 */
@Service
public class EnergyBenchmarkManageServiceImpl extends ServiceImpl<EnergyBenchmarkManageMapper, EnergyBenchmarkManage> implements IEnergyBenchmarkManageService {
    @Autowired
    private EnergyBenchmarkManageMapper energyBenchmarkManageMapper;

    /**
     * 查询标杆值管理
     *
     * @param id 标杆值管理主键
     * @return 标杆值管理
     */
    @Override
    public EnergyBenchmarkManage selectEnergyBenchmarkManageById(String id) {
        return energyBenchmarkManageMapper.selectEnergyBenchmarkManageById(id);
    }

    /**
     * 查询标杆值管理列表
     *
     * @param energyBenchmarkManage 标杆值管理
     * @return 标杆值管理
     */
    @Override
    public List<EnergyBenchmarkManage> selectEnergyBenchmarkManageList(EnergyBenchmarkManage energyBenchmarkManage) {
        return energyBenchmarkManageMapper.selectEnergyBenchmarkManageList(energyBenchmarkManage);
    }

    /**
     * 新增标杆值管理
     *
     * @param energyBenchmarkManage 标杆值管理
     * @return 结果
     */
    @Override
    public int insertEnergyBenchmarkManage(EnergyBenchmarkManage energyBenchmarkManage) {
        energyBenchmarkManage.setCreateTime(DateUtils.getNowDate());
        return energyBenchmarkManageMapper.insertEnergyBenchmarkManage(energyBenchmarkManage);
    }

    /**
     * 修改标杆值管理
     *
     * @param energyBenchmarkManage 标杆值管理
     * @return 结果
     */
    @Override
    public int updateEnergyBenchmarkManage(EnergyBenchmarkManage energyBenchmarkManage) {
        energyBenchmarkManage.setUpdateTime(DateUtils.getNowDate());
        return energyBenchmarkManageMapper.updateEnergyBenchmarkManage(energyBenchmarkManage);
    }

    /**
     * 批量删除标杆值管理
     *
     * @param ids 需要删除的标杆值管理主键
     * @return 结果
     */
    @Override
    public int deleteEnergyBenchmarkManageByIds(String[] ids) {
        return energyBenchmarkManageMapper.deleteEnergyBenchmarkManageByIds(ids);
    }

    /**
     * 删除标杆值管理信息
     *
     * @param id 标杆值管理主键
     * @return 结果
     */
    @Override
    public int deleteEnergyBenchmarkManageById(String id) {
        return energyBenchmarkManageMapper.deleteEnergyBenchmarkManageById(id);
    }

    @Override
    public Page<EnergyBenchmarkManage> selectBenchmarkManagePage(EnergyBenchmarkManage energyBenchmarkManage, Long pageNum, Long pageSize) {
        LambdaQueryWrapper<EnergyBenchmarkManage> queryWrapper=new LambdaQueryWrapper<EnergyBenchmarkManage>();
        queryWrapper.like(StringUtils.isNotEmpty(energyBenchmarkManage.getCode()),EnergyBenchmarkManage::getCode,energyBenchmarkManage.getCode());
        queryWrapper.eq(StringUtils.isNotEmpty(energyBenchmarkManage.getType()),EnergyBenchmarkManage::getType,energyBenchmarkManage.getType());
        queryWrapper.eq(StringUtils.isNotEmpty(energyBenchmarkManage.getGrade()),EnergyBenchmarkManage::getGrade,energyBenchmarkManage.getGrade());
        queryWrapper.orderByDesc(EnergyBenchmarkManage::getCreateTime);

        return energyBenchmarkManageMapper.selectPage(new Page<EnergyBenchmarkManage>(pageNum, pageSize), queryWrapper);
    }

    @Override
    public List<EnergyBenchmarkManage> getList(EnergyBenchmarkManage queryParams) {
        return energyBenchmarkManageMapper.getList(queryParams);
    }
}
