package com.dingzhuo.energy.basic.data.workforce.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.workforce.mapper.RosteringSchemeMapper;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringScheme;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringSchemeService;

/**
 * 轮值方案Service业务层处理
 * 
 * @author sys
 * @date 2020-05-12
 */
@Service
public class RosteringSchemeServiceImpl implements IRosteringSchemeService 
{
    @Autowired
    private RosteringSchemeMapper rosteringSchemeMapper;

    /**
     * 查询轮值方案
     * 
     * @param id 轮值方案ID
     * @return 轮值方案
     */
    @Override
    public RosteringScheme selectRosteringSchemeById(String id)
    {
        return rosteringSchemeMapper.selectRosteringSchemeById(id);
    }

    /**
     * 查询轮值方案列表
     * 
     * @param rosteringScheme 轮值方案
     * @return 轮值方案
     */
    @Override
    public List<RosteringScheme> selectRosteringSchemeList(RosteringScheme rosteringScheme)
    {
        return rosteringSchemeMapper.selectRosteringSchemeList(rosteringScheme);
    }

    /**
     * 新增轮值方案
     * 
     * @param rosteringScheme 轮值方案
     * @return 结果
     */
    @Override
    public int insertRosteringScheme(RosteringScheme rosteringScheme)
    {
        return rosteringSchemeMapper.insertRosteringScheme(rosteringScheme);
    }

    /**
     * 修改轮值方案
     * 
     * @param rosteringScheme 轮值方案
     * @return 结果
     */
    @Override
    public int updateRosteringScheme(RosteringScheme rosteringScheme)
    {
        rosteringScheme.setUpdateTime(DateUtils.getNowDate());
        return rosteringSchemeMapper.updateRosteringScheme(rosteringScheme);
    }

    /**
     * 批量删除轮值方案
     * 
     * @param ids 需要删除的轮值方案ID
     * @return 结果
     */
    @Override
    public int deleteRosteringSchemeByIds(String[] ids)
    {
        return rosteringSchemeMapper.deleteRosteringSchemeByIds(ids);
    }

    /**
     * 删除轮值方案信息
     * 
     * @param id 轮值方案ID
     * @return 结果
     */
    @Override
    public int deleteRosteringSchemeById(String id)
    {
        return rosteringSchemeMapper.deleteRosteringSchemeById(id);
    }
}
