package com.dingzhuo.energy.project.statisticalData.mapper;

import com.dingzhuo.energy.project.statisticalData.domain.GxdhCount;
import com.dingzhuo.energy.project.statisticalData.domain.GxdhCountIndex;

import java.util.List;

/**
 * 工序单耗统计功能Mapper接口
 * 
 * @author zhaow
 * @date 2020-12-26
 */
public interface GxdhCountMapper 
{
    /**
     * 查询工序单耗统计功能
     * 
     * @param id 工序单耗统计功能ID
     * @return 工序单耗统计功能
     */
    public GxdhCount selectGxdhCountById(String id);

    /**
     * 查询工序单耗统计功能列表
     * 
     * @param gxdhCount 工序单耗统计功能
     * @return 工序单耗统计功能集合
     */
    public List<GxdhCount> selectGxdhCountList(GxdhCount gxdhCount);

    /**
     * 新增工序单耗统计功能
     * 
     * @param gxdhCount 工序单耗统计功能
     * @return 结果
     */
    public int insertGxdhCount(GxdhCount gxdhCount);

    /**
     * 修改工序单耗统计功能
     * 
     * @param gxdhCount 工序单耗统计功能
     * @return 结果
     */
    public int updateGxdhCount(GxdhCount gxdhCount);

    /**
     * 删除工序单耗统计功能
     * 
     * @param id 工序单耗统计功能ID
     * @return 结果
     */
    public int deleteGxdhCountById(String id);

    /**
     * 批量删除工序单耗统计功能
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteGxdhCountByIds(String[] ids);

    /**
     * 查询工序单耗统计功能报表的指标数据列表
     *
     * @param gxdhCountIndex 工序单耗统计功能报表的指标数据
     * @return 工序单耗统计功能集合
     */
    public List<GxdhCountIndex> selectGxdhCountIndexList(GxdhCountIndex gxdhCountIndex);
}
