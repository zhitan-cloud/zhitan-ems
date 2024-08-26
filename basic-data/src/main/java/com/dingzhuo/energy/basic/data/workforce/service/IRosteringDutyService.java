package com.dingzhuo.energy.basic.data.workforce.service;

import com.dingzhuo.energy.basic.data.workforce.domain.RosteringDuty;
import java.util.List;

/**
 * 排班管理Service接口
 * 
 * @author liuli
 * @date 2020-05-12
 */
public interface IRosteringDutyService 
{
    /**
     * 查询排班管理
     * 
     * @param id 排班管理ID
     * @return 排班管理
     */
    public RosteringDuty selectRosteringDutyById(String id);

    /**
     * 查询排班管理列表
     * 
     * @param rosteringDuty 排班管理
     * @return 排班管理集合
     */
    public List<RosteringDuty> selectRosteringDutyList(RosteringDuty rosteringDuty);

    /**
     * 新增排班管理
     * 
     * @param rosteringDuty 排班管理
     * @return 结果
     */
    public int insertRosteringDuty(RosteringDuty rosteringDuty);

    /**
     * 修改排班管理
     * 
     * @param rosteringDuty 排班管理
     * @return 结果
     */
    public int updateRosteringDuty(RosteringDuty rosteringDuty);

    /**
     * 批量删除排班管理
     * 
     * @param ids 需要删除的排班管理ID
     * @return 结果
     */
    public int deleteRosteringDutyByIds(String[] ids);

    /**
     * 删除排班管理信息
     * 
     * @param id 排班管理ID
     * @return 结果
     */
    public int deleteRosteringDutyById(String id);
}
