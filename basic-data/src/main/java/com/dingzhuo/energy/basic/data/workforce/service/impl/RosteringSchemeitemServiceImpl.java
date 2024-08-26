package com.dingzhuo.energy.basic.data.workforce.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.workforce.mapper.RosteringSchemeitemMapper;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringSchemeitem;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringSchemeitemService;

/**
 * 轮值方案Service业务层处理
 * 
 * @author sys
 * @date 2020-05-13
 */
@Service
public class RosteringSchemeitemServiceImpl implements IRosteringSchemeitemService 
{
    @Autowired
    private RosteringSchemeitemMapper rosteringSchemeitemMapper;

    /**
     * 查询轮值方案
     * 
     * @param description 轮值方案ID
     * @return 轮值方案
     */
    @Override
    public RosteringSchemeitem selectRosteringSchemeitemById(String description)
    {
        return rosteringSchemeitemMapper.selectRosteringSchemeitemById(description);
    }

    /**
     * 查询轮值方案列表
     * 
     * @param rosteringSchemeitem 轮值方案
     * @return 轮值方案
     */
    @Override
    public List<RosteringSchemeitem> selectRosteringSchemeitemList(RosteringSchemeitem rosteringSchemeitem)
    {
        return rosteringSchemeitemMapper.selectRosteringSchemeitemList(rosteringSchemeitem);
    }

    /**
     * 新增轮值方案
     * 
     * @param rosteringSchemeitem 轮值方案
     * @return 结果
     */
    @Override
    public int insertRosteringSchemeitem(RosteringSchemeitem rosteringSchemeitem)
    {
        return rosteringSchemeitemMapper.insertRosteringSchemeitem(rosteringSchemeitem);
    }

    /**
     * 修改轮值方案
     * 
     * @param rosteringSchemeitem 轮值方案
     * @return 结果
     */
    @Override
    public int updateRosteringSchemeitem(RosteringSchemeitem rosteringSchemeitem)
    {
        rosteringSchemeitem.setUpdateTime(DateUtils.getNowDate());
        return rosteringSchemeitemMapper.updateRosteringSchemeitem(rosteringSchemeitem);
    }

    /**
     * 批量删除轮值方案
     * 
     * @param descriptions 需要删除的轮值方案ID
     * @return 结果
     */
    @Override
    public int deleteRosteringSchemeitemByIds(String[] descriptions)
    {
        return rosteringSchemeitemMapper.deleteRosteringSchemeitemByIds(descriptions);
    }

    /**
     * 删除轮值方案信息
     * 
     * @param description 轮值方案ID
     * @return 结果
     */
    @Override
    public int deleteRosteringSchemeitemById(String description)
    {
        return rosteringSchemeitemMapper.deleteRosteringSchemeitemById(description);
    }
}
