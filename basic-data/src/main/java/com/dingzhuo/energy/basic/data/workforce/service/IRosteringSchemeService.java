package com.dingzhuo.energy.basic.data.workforce.service;

import com.dingzhuo.energy.basic.data.workforce.domain.RosteringScheme;
import java.util.List;

/**
 * 轮值方案Service接口
 * 
 * @author sys
 * @date 2020-05-12
 */
public interface IRosteringSchemeService 
{
    /**
     * 查询轮值方案
     * 
     * @param id 轮值方案ID
     * @return 轮值方案
     */
    public RosteringScheme selectRosteringSchemeById(String id);

    /**
     * 查询轮值方案列表
     * 
     * @param rosteringScheme 轮值方案
     * @return 轮值方案集合
     */
    public List<RosteringScheme> selectRosteringSchemeList(RosteringScheme rosteringScheme);

    /**
     * 新增轮值方案
     * 
     * @param rosteringScheme 轮值方案
     * @return 结果
     */
    public int insertRosteringScheme(RosteringScheme rosteringScheme);

    /**
     * 修改轮值方案
     * 
     * @param rosteringScheme 轮值方案
     * @return 结果
     */
    public int updateRosteringScheme(RosteringScheme rosteringScheme);

    /**
     * 批量删除轮值方案
     * 
     * @param ids 需要删除的轮值方案ID
     * @return 结果
     */
    public int deleteRosteringSchemeByIds(String[] ids);

    /**
     * 删除轮值方案信息
     * 
     * @param id 轮值方案ID
     * @return 结果
     */
    public int deleteRosteringSchemeById(String id);
}
