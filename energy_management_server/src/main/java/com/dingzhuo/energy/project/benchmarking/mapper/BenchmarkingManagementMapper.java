package com.dingzhuo.energy.project.benchmarking.mapper;

import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标杆管理Mapper接口
 * 
 * @author sys
 * @date 2020-12-21
 */
public interface BenchmarkingManagementMapper 
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
    public List<BenchmarkingManagement> selectBenchmarkingManagementList(@Param("indexIds") List<String> indexIds,@Param("timeCode") String timeCode);
    public List<BenchmarkingManagement> selectBenchmarkingList(@Param("indexIds") List<String> indexIds, BenchmarkingManagement benchmarkingManagement);
    /**
     * 新增标杆管理
     * 
     * @param benchmarkingManagement 标杆管理
     * @return 结果
     */
    public int insertBenchmarkingManagement(BenchmarkingManagement benchmarkingManagement);

    /**
     * 修改标杆管理
     * 
     * @param benchmarkingManagement 标杆管理
     * @return 结果
     */
    public int updateBenchmarkingManagement(BenchmarkingManagement benchmarkingManagement);

    /**
     * 删除标杆管理
     * 
     * @param indexId 标杆管理ID
     * @return 结果
     */
    public int deleteBenchmarkingManagementById(String indexId);

    /**
     * 批量删除标杆管理
     * 
     * @param indexIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteBenchmarkingManagementByIds(String[] indexIds);

    void save(@Param("benchmarkingManagement") BenchmarkingManagement benchmarkingManagement);
}
