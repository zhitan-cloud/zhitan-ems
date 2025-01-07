package com.zhitan.benchmarkmanage.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.benchmarkmanage.domain.EnergyBenchmarkManage;

import java.util.List;

/**
 * 标杆值管理Service接口
 *
 * @author ZhiTan
 * @date 2024-11-12
 */
public interface IEnergyBenchmarkManageService extends IService<EnergyBenchmarkManage> {
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
     * 批量删除标杆值管理
     *
     * @param ids 需要删除的标杆值管理主键集合
     * @return 结果
     */
    public int deleteEnergyBenchmarkManageByIds(String[] ids);

    /**
     * 删除标杆值管理信息
     *
     * @param id 标杆值管理主键
     * @return 结果
     */
    public int deleteEnergyBenchmarkManageById(String id);

    /**
     * 分页查询数据
     * @param energyBenchmarkManage
     * @param pageNum
     * @param pageSize
     * @return
     */
    Page<EnergyBenchmarkManage> selectBenchmarkManagePage(EnergyBenchmarkManage energyBenchmarkManage, Long pageNum, Long pageSize);

    List<EnergyBenchmarkManage> getList(EnergyBenchmarkManage queryParams);
}
