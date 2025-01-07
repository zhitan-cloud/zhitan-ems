package com.zhitan.spikesandvalleys.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysScheme;


/**
 * 尖峰平谷时间段明细Mapper接口
 *
 * @author ZhiTan
 * @date 2024-10-29
 */
public interface SpikesAndValleysSchemeMapper extends BaseMapper<SpikesAndValleysScheme> {
    /**
     * 查询尖峰平谷时间段明细
     *
     * @param id 尖峰平谷时间段明细主键
     * @return 尖峰平谷时间段明细
     */
    public SpikesAndValleysScheme selectSpikesAndValleysSchemeById(String id);

    /**
     * 查询尖峰平谷时间段明细列表
     *
     * @param spikesAndValleysScheme 尖峰平谷时间段明细
     * @return 尖峰平谷时间段明细集合
     */
    public List<SpikesAndValleysScheme> selectSpikesAndValleysSchemeList(SpikesAndValleysScheme spikesAndValleysScheme);

    /**
     * 新增尖峰平谷时间段明细
     *
     * @param spikesAndValleysScheme 尖峰平谷时间段明细
     * @return 结果
     */
    public int insertSpikesAndValleysScheme(SpikesAndValleysScheme spikesAndValleysScheme);

    /**
     * 修改尖峰平谷时间段明细
     *
     * @param spikesAndValleysScheme 尖峰平谷时间段明细
     * @return 结果
     */
    public int updateSpikesAndValleysScheme(SpikesAndValleysScheme spikesAndValleysScheme);

    /**
     * 删除尖峰平谷时间段明细
     *
     * @param id 尖峰平谷时间段明细主键
     * @return 结果
     */
    public int deleteSpikesAndValleysSchemeById(String id);

    /**
     * 批量删除尖峰平谷时间段明细
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSpikesAndValleysSchemeByIds(String[] ids);
}
