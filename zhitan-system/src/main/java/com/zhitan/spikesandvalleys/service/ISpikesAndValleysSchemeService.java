package com.zhitan.spikesandvalleys.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.alarm.domain.LimitType;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysScheme;
import com.zhitan.spikesandvalleys.domain.vo.SpikesAndValleysSchemeVo;

/**
 * 尖峰平谷时间段明细Service接口
 *
 * @author ZhiTan
 * @date 2024-10-29
 */
public interface ISpikesAndValleysSchemeService extends IService<SpikesAndValleysScheme> {
    /**
     * 查询尖峰平谷时间段明细
     *
     * @param id 尖峰平谷时间段明细主键
     * @return 尖峰平谷时间段明细
     */
    public SpikesAndValleysSchemeVo selectSpikesAndValleysSchemeById(String id);

    /**
     * 查询尖峰平谷时间段明细列表
     *
     * @param spikesAndValleysScheme 尖峰平谷时间段明细
     * @return 尖峰平谷时间段明细集合
     */
    public Page<SpikesAndValleysSchemeVo> selectSpikesAndValleysSchemeList(SpikesAndValleysScheme spikesAndValleysScheme, Long pageNum, Long pageSize);

    /**
     * 新增尖峰平谷时间段明细
     *
     * @param spikesAndValleysScheme 尖峰平谷时间段明细
     * @return 结果
     */
    public int insertSpikesAndValleysScheme(SpikesAndValleysSchemeVo spikesAndValleysScheme);

    /**
     * 修改尖峰平谷时间段明细
     *
     * @param spikesAndValleysScheme 尖峰平谷时间段明细
     * @return 结果
     */
    public int updateSpikesAndValleysScheme(SpikesAndValleysSchemeVo spikesAndValleysScheme);

    /**
     * 批量删除尖峰平谷时间段明细
     *
     * @param ids 需要删除的尖峰平谷时间段明细主键集合
     * @return 结果
     */
    public int deleteSpikesAndValleysSchemeByIds(String[] ids);

    /**
     * 删除尖峰平谷时间段明细信息
     *
     * @param id 尖峰平谷时间段明细主键
     * @return 结果
     */
    public int deleteSpikesAndValleysSchemeById(String id);
}
