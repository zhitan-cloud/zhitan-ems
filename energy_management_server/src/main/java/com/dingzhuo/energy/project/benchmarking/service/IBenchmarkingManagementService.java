package com.dingzhuo.energy.project.benchmarking.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;

import java.util.Date;
import java.util.List;

/**
 * 标杆管理Service接口
 * 
 * @author sys
 * @date 2020-12-21
 */
public interface IBenchmarkingManagementService 
{
    /**
     * 查询标杆管理
     * 
     * @param indexId 标杆管理ID
     * @return 标杆管理
     */
    public BenchmarkingManagement selectBenchmarkingManagementById(String indexId);

    /**
     * 查询标杆管理列表
     * 
     * @param
     * @return 标杆管理集合
     */
    public List<BenchmarkingManagement> selectBenchmarkingManagementList(List<String> indexIds, Date dateTime,TimeType timeType);
    public List<BenchmarkingManagement> selectBenchmarkingList(List<String> indexIds,BenchmarkingManagement benchmarkingManagement);
    /**
     * 新增标杆管理
     * 
     * @param benchmarkingManagement 标杆管理
     * @return 结果
     */
    public int insertBenchmarkingManagement(BenchmarkingManagement benchmarkingManagement);
    public void save(List<BenchmarkingManagement> datas);
    /**
     * 修改标杆管理
     * 
     * @param benchmarkingManagement 标杆管理
     * @return 结果
     */
    public int updateBenchmarkingManagement(BenchmarkingManagement benchmarkingManagement);

    /**
     * 批量删除标杆管理
     * 
     * @param indexIds 需要删除的标杆管理ID
     * @return 结果
     */
    public int deleteBenchmarkingManagementByIds(String[] indexIds);

    /**
     * 删除标杆管理信息
     * 
     * @param indexId 标杆管理ID
     * @return 结果
     */
    public int deleteBenchmarkingManagementById(String indexId);
}
