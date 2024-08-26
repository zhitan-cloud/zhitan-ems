package com.dingzhuo.energy.data.model.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.data.model.mapper.LimitTypeMapper;
import com.dingzhuo.energy.data.model.domain.LimitType;
import com.dingzhuo.energy.data.model.service.ILimitTypeService;

/**
 * 报警限值类型维护Service业务层处理
 * 
 * @author zw
 * @date 2020-03-07
 */
@Service
public class LimitTypeServiceImpl implements ILimitTypeService 
{
    @Autowired
    private LimitTypeMapper limitTypeMapper;

    /**
     * 查询报警限值类型维护
     * 
     * @param id 报警限值类型维护ID
     * @return 报警限值类型维护
     */
    @Override
    public LimitType selectLimitTypeById(String id)
    {
        return limitTypeMapper.selectLimitTypeById(id);
    }

    /**
     * 查询报警限值类型维护列表
     * 
     * @param limitType 报警限值类型维护
     * @return 报警限值类型维护
     */
    @Override
    public List<LimitType> selectLimitTypeList(LimitType limitType)
    {
        return limitTypeMapper.selectLimitTypeList(limitType);
    }

    /**
     * 新增报警限值类型维护
     * 
     * @param limitType 报警限值类型维护
     * @return 结果
     */
    @Override
    public int insertLimitType(LimitType limitType)
    {
        limitType.setCreateTime(DateUtils.getNowDate());
        return limitTypeMapper.insertLimitType(limitType);
    }

    /**
     * 修改报警限值类型维护
     * 
     * @param limitType 报警限值类型维护
     * @return 结果
     */
    @Override
    public int updateLimitType(LimitType limitType)
    {
        limitType.setUpdateTime(DateUtils.getNowDate());
        return limitTypeMapper.updateLimitType(limitType);
    }

    /**
     * 批量删除报警限值类型维护
     * 
     * @param ids 需要删除的报警限值类型维护ID
     * @return 结果
     */
    @Override
    public int deleteLimitTypeByIds(String[] ids)
    {
        return limitTypeMapper.deleteLimitTypeByIds(ids);
    }

    /**
     * 删除报警限值类型维护信息
     * 
     * @param id 报警限值类型维护ID
     * @return 结果
     */
    @Override
    public int deleteLimitTypeById(String id)
    {
        return limitTypeMapper.deleteLimitTypeById(id);
    }
}
