package com.dingzhuo.energy.basic.data.workforce.service;

import com.dingzhuo.energy.basic.data.workforce.domain.RosteringSchemeitem;
import java.util.List;

/**
 * 轮值方案Service接口
 * 
 * @author sys
 * @date 2020-05-13
 */
public interface IRosteringSchemeitemService 
{
    /**
     * 查询轮值方案
     * 
     * @param description 轮值方案ID
     * @return 轮值方案
     */
    public RosteringSchemeitem selectRosteringSchemeitemById(String description);

    /**
     * 查询轮值方案列表
     * 
     * @param rosteringSchemeitem 轮值方案
     * @return 轮值方案集合
     */
    public List<RosteringSchemeitem> selectRosteringSchemeitemList(RosteringSchemeitem rosteringSchemeitem);

    /**
     * 新增轮值方案
     * 
     * @param rosteringSchemeitem 轮值方案
     * @return 结果
     */
    public int insertRosteringSchemeitem(RosteringSchemeitem rosteringSchemeitem);

    /**
     * 修改轮值方案
     * 
     * @param rosteringSchemeitem 轮值方案
     * @return 结果
     */
    public int updateRosteringSchemeitem(RosteringSchemeitem rosteringSchemeitem);

    /**
     * 批量删除轮值方案
     * 
     * @param descriptions 需要删除的轮值方案ID
     * @return 结果
     */
    public int deleteRosteringSchemeitemByIds(String[] descriptions);

    /**
     * 删除轮值方案信息
     * 
     * @param description 轮值方案ID
     * @return 结果
     */
    public int deleteRosteringSchemeitemById(String description);
}
