package com.dingzhuo.energy.project.benchmarking.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.common.utils.time.TimeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.benchmarking.mapper.BenchmarkingManagementMapper;
import com.dingzhuo.energy.project.benchmarking.domain.BenchmarkingManagement;
import com.dingzhuo.energy.project.benchmarking.service.IBenchmarkingManagementService;

/**
 * 标杆管理Service业务层处理
 * 
 * @author sys
 * @date 2020-12-21
 */
@Service
public class BenchmarkingManagementServiceImpl implements IBenchmarkingManagementService 
{
    private Logger logger = LogManager.getLogger(BenchmarkingManagementServiceImpl.class);
    @Autowired
    private BenchmarkingManagementMapper benchmarkingManagementMapper;

    /**
     * 查询标杆管理
     * 
     * @param indexId 标杆管理ID
     * @return 标杆管理
     */
    @Override
    public BenchmarkingManagement selectBenchmarkingManagementById(String indexId)
    {
        return benchmarkingManagementMapper.selectBenchmarkingManagementById(indexId);
    }
    @Override
    public List<BenchmarkingManagement> selectBenchmarkingList(List<String> indexIds,BenchmarkingManagement benchmarkingManagement)
    {
        if (indexIds.size() !=0) {
            return benchmarkingManagementMapper.selectBenchmarkingList(indexIds,benchmarkingManagement);
        }
        return Collections.emptyList();
    }
    /**
     * 查询标杆管理列表
     * 
     * @param
     * @return 标杆管理
     */
    @Override
    public List<BenchmarkingManagement> selectBenchmarkingManagementList(List<String> indexIds, Date dateTime, TimeType timeType)
    {
        if (indexIds.size() !=0) {
            String timeCode = TimeManager.getTimeCode(dateTime, timeType);
            return  benchmarkingManagementMapper.selectBenchmarkingManagementList(indexIds,timeCode);
        }
        return Collections.emptyList();
    }

    /**
     * 新增标杆管理
     * 
     * @param benchmarkingManagement 标杆管理
     * @return 结果
     */
    @Override
    public int insertBenchmarkingManagement(BenchmarkingManagement benchmarkingManagement)
    {
        benchmarkingManagement.setCreateTime(DateUtils.getNowDate());
        return benchmarkingManagementMapper.insertBenchmarkingManagement(benchmarkingManagement);
    }

    @Override
    public void save(List<BenchmarkingManagement> datas) {
        List<List<BenchmarkingManagement>> splitDatas = splitList(datas, 100);
        if (splitDatas != null) {
            splitDatas.parallelStream().forEach(dataPart -> {
                dataPart.parallelStream().forEach(dataItem -> {
                    try {
                        benchmarkingManagementMapper.save(dataItem);
                    } catch (Exception singleEx) {
                        logger.error("单个指标数据保存失败！【" + dataItem + "】", singleEx);
                    }
                });
            });
        }
    }
    /**
     * 修改标杆管理
     * 
     * @param benchmarkingManagement 标杆管理
     * @return 结果
     */
    @Override
    public int updateBenchmarkingManagement(BenchmarkingManagement benchmarkingManagement)
    {
        benchmarkingManagement.setUpdateTime(DateUtils.getNowDate());
        return benchmarkingManagementMapper.updateBenchmarkingManagement(benchmarkingManagement);
    }

    /**
     * 批量删除标杆管理
     * 
     * @param indexIds 需要删除的标杆管理ID
     * @return 结果
     */
    @Override
    public int deleteBenchmarkingManagementByIds(String[] indexIds)
    {
        return benchmarkingManagementMapper.deleteBenchmarkingManagementByIds(indexIds);
    }

    /**
     * 删除标杆管理信息
     * 
     * @param indexId 标杆管理ID
     * @return 结果
     */
    @Override
    public int deleteBenchmarkingManagementById(String indexId)
    {
        return benchmarkingManagementMapper.deleteBenchmarkingManagementById(indexId);
    }
    public static <E> List<List<E>> splitList(List<E> targetList, Integer splitSize) {
        if (targetList == null) {
            return Collections.emptyList();
        }

        int size = targetList.size();
        List<List<E>> resultList = new ArrayList<>();
        if (size <= splitSize) {
            resultList.add(targetList);
        } else {
            for (int i = 0; i < size; i += splitSize) {
                //用于限制最后一部分size小于splitSize的list
                int limit = i + splitSize;
                if (limit > size) {
                    limit = size;
                }
                resultList.add(targetList.subList(i, limit));
            }
        }
        return resultList;
    }
}
