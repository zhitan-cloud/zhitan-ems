package com.dingzhuo.energy.data.model.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import com.dingzhuo.energy.data.model.domain.StateType;
import com.dingzhuo.energy.data.model.mapper.StateTypeMapper;
import com.dingzhuo.energy.data.model.service.IStateTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统状态维护Service业务层处理
 * 
 * @author sys
 * @date 2020-03-18
 */
@Service
public class StateTypeServiceImpl implements IStateTypeService
{
    @Autowired
    private StateTypeMapper stateTypeMapper;

    /**
     * 查询系统状态维护
     * 
     * @param stateId 系统状态维护ID
     * @return 系统状态维护
     */
    @Override
    public StateType selectStateTypeById(String stateId)
    {
        return stateTypeMapper.selectStateTypeById(stateId);
    }

    /**
     * 查询系统状态维护列表
     * 
     * @param stateType 系统状态维护
     * @return 系统状态维护
     */
    @Override
    public List<StateType> selectStateTypeList(StateType stateType)
    {
        return stateTypeMapper.selectStateTypeList(stateType);
    }

    /**
     * 新增系统状态维护
     * 
     * @param stateType 系统状态维护
     * @return 结果
     */
    @Override
    public int insertStateType(StateType stateType)
    {
        stateType.setCreateTime(DateUtils.getNowDate());
        return stateTypeMapper.insertStateType(stateType);
    }

    /**
     * 修改系统状态维护
     * 
     * @param stateType 系统状态维护
     * @return 结果
     */
    @Override
    public int updateStateType(StateType stateType)
    {
        stateType.setUpdateTime(DateUtils.getNowDate());
        return stateTypeMapper.updateStateType(stateType);
    }

    /**
     * 批量删除系统状态维护
     * 
     * @param stateIds 需要删除的系统状态维护ID
     * @return 结果
     */
    @Override
    public int deleteStateTypeByIds(String[] stateIds)
    {
        return stateTypeMapper.deleteStateTypeByIds(stateIds);
    }

    /**
     * 删除系统状态维护信息
     * 
     * @param stateId 系统状态维护ID
     * @return 结果
     */
    @Override
    public int deleteStateTypeById(String stateId)
    {
        return stateTypeMapper.deleteStateTypeById(stateId);
    }
}
