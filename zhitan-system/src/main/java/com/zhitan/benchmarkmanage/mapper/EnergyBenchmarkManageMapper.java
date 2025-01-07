package com.zhitan.benchmarkmanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.benchmarkmanage.domain.EnergyBenchmarkManage;

import java.util.List;

/**
 * 标杆值管理Mapper接口
 *
 * @author ZhiTan
 * @date 2024-11-12
 */
public interface EnergyBenchmarkManageMapper extends BaseMapper<EnergyBenchmarkManage> {
    /**
     * 查询标杆值管理
     *
     * @param id 标杆值管理主键
     * @return 标杆值管理
     */
    public EnergyBenchmarkManage selectEnergyBenchmarkManageById(String id);

    /**
     * 查询标杆值管理列表
     *
     * @param energyBenchmarkManage 标杆值管理
     * @return 标杆值管理集合
     */
    public List<EnergyBenchmarkManage> selectEnergyBenchmarkManageList(EnergyBenchmarkManage energyBenchmarkManage);

    /**
     * 新增标杆值管理
     *
     * @param energyBenchmarkManage 标杆值管理
     * @return 结果
     */
    public int insertEnergyBenchmarkManage(EnergyBenchmarkManage energyBenchmarkManage);

    /**
     * 修改标杆值管理
     *
     * @param energyBenchmarkManage 标杆值管理
     * @return 结果
     */
    public int updateEnergyBenchmarkManage(EnergyBenchmarkManage energyBenchmarkManage);

    /**
     * 删除标杆值管理
     *
     * @param id 标杆值管理主键
     * @return 结果
     */
    public int deleteEnergyBenchmarkManageById(String id);

    /**
     * 批量删除标杆值管理
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEnergyBenchmarkManageByIds(String[] ids);

    List<EnergyBenchmarkManage> getList(EnergyBenchmarkManage queryParams);
}
