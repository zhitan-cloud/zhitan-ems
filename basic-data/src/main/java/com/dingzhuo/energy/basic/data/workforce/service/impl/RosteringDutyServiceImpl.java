package com.dingzhuo.energy.basic.data.workforce.service.impl;

import com.dingzhuo.energy.basic.data.workforce.domain.RosteringDuty;
import com.dingzhuo.energy.basic.data.workforce.mapper.RosteringDutyMapper;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringDutyService;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排班管理Service业务层处理
 * 
 * @author liuli
 * @date 2020-05-12
 */
@Service
public class RosteringDutyServiceImpl implements IRosteringDutyService 
{
    @Autowired
    private RosteringDutyMapper rosteringDutyMapper;

    /**
     * 查询排班管理
     * 
     * @param id 排班管理ID
     * @return 排班管理
     */
    @Override
    public RosteringDuty selectRosteringDutyById(String id)
    {
        return rosteringDutyMapper.selectRosteringDutyById(id);
    }

    /**
     * 查询排班管理列表
     * 
     * @param rosteringDuty 排班管理
     * @return 排班管理
     */
    @Override
    public List<RosteringDuty> selectRosteringDutyList(RosteringDuty rosteringDuty)
    {
        return rosteringDutyMapper.selectRosteringDutyList(rosteringDuty);
    }

    /**
     * 新增排班管理
     * 
     * @param rosteringDuty 排班管理
     * @return 结果
     */
    @Override
    public int insertRosteringDuty(RosteringDuty rosteringDuty)
    {
        return rosteringDutyMapper.insertRosteringDuty(rosteringDuty);
    }

    /**
     * 修改排班管理
     * 
     * @param rosteringDuty 排班管理
     * @return 结果
     */
    @Override
    public int updateRosteringDuty(RosteringDuty rosteringDuty)
    {
        rosteringDuty.setUpdateTime(DateUtils.getNowDate());
        return rosteringDutyMapper.updateRosteringDuty(rosteringDuty);
    }

    /**
     * 批量删除排班管理
     * 
     * @param ids 需要删除的排班管理ID
     * @return 结果
     */
    @Override
    public int deleteRosteringDutyByIds(String[] ids)
    {
        return rosteringDutyMapper.deleteRosteringDutyByIds(ids);
    }

    /**
     * 删除排班管理信息
     * 
     * @param id 排班管理ID
     * @return 结果
     */
    @Override
    public int deleteRosteringDutyById(String id)
    {
        return rosteringDutyMapper.deleteRosteringDutyById(id);
    }
}
