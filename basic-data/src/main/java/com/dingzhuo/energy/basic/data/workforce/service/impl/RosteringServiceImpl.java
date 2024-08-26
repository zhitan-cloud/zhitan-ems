package com.dingzhuo.energy.basic.data.workforce.service.impl;

import com.dingzhuo.energy.basic.data.workforce.domain.Rostering;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringCopy;
import com.dingzhuo.energy.basic.data.workforce.domain.RosteringScheme;
import com.dingzhuo.energy.basic.data.workforce.mapper.RosteringMapper;
import com.dingzhuo.energy.basic.data.workforce.service.IRosteringService;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 排班表查询Service业务层处理
 * 
 * @author liuli
 * @date 2020-05-13
 */
@Service
public class RosteringServiceImpl implements IRosteringService 
{
    @Autowired
    private RosteringMapper rosteringMapper;

    /**
     * 查询轮值方案名称
     * @return 轮值方案名称集合
     */
    @Override
    public List<RosteringScheme> selectSchemeNameList() {
        return rosteringMapper.selectSchemeNameList();
    }


    /**
     * 查询排班表查询
     * 
     * @param id 排班表查询ID
     * @return 排班表查询
     */
    @Override
    public Rostering selectRosteringById(String id)
    {
        return rosteringMapper.selectRosteringById(id);
    }

    /**
     * 查询排班表查询列表
     * 
     * @param rostering 排班表查询
     * @return 排班表查询
     */
    @Override
    public List<Rostering> selectRosteringList(Rostering rostering)
    {
        return rosteringMapper.selectRosteringList(rostering);
    }
    @Override
    public List<RosteringCopy> selectList(Rostering rostering)
    {
        return rosteringMapper.selectList(rostering);
    }
    /**
     * 新增排班表查询
     * 
     * @param rostering 排班表查询
     * @return 结果
     */
    @Override
    public int insertRostering(Rostering rostering)
    {
        return rosteringMapper.insertRostering(rostering);
    }
    @Override
    public void saveRostering(List<Rostering> dataItems){
        rosteringMapper.saveRostering(dataItems);
    }
    /**
     * 修改排班表查询
     * 
     * @param rostering 排班表查询
     * @return 结果
     */
    @Override
    public int updateRostering(Rostering rostering)
    {
        rostering.setUpdateTime(DateUtils.getNowDate());
        return rosteringMapper.updateRostering(rostering);
    }

    /**
     * 批量删除排班表查询
     * 
     * @param ids 需要删除的排班表查询ID
     * @return 结果
     */
    @Override
    public int deleteRosteringByIds(String[] ids)
    {
        return rosteringMapper.deleteRosteringByIds(ids);
    }

    /**
     * 删除排班表查询信息
     * 
     * @param id 排班表查询ID
     * @return 结果
     */
    @Override
    public int deleteRosteringById(String id)
    {
        return rosteringMapper.deleteRosteringById(id);
    }
}
