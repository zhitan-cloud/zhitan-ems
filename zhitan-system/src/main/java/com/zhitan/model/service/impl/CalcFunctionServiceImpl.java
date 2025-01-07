package com.zhitan.model.service.impl;

import com.zhitan.model.domain.CalcFunction;
import com.zhitan.model.mapper.CalcFunctionMapper;
import com.zhitan.model.service.ICalcFunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 计算函数Service业务层处理
 * 
 * @author fanxinfu
 * @date 2020-03-10
 */
@Service
public class CalcFunctionServiceImpl implements ICalcFunctionService 
{
    @Autowired
    private CalcFunctionMapper calcFunctionMapper;

    /**
     * 查询计算函数
     * 
     * @param id 计算函数ID
     * @return 计算函数
     */
    @Override
    public CalcFunction selectCalcFunctionById(String id)
    {
        return calcFunctionMapper.selectCalcFunctionById(id);
    }

    /**
     * 查询计算函数列表
     * 
     * @param calcFunction 计算函数
     * @return 计算函数
     */
    @Override
    public List<CalcFunction> selectCalcFunctionList(CalcFunction calcFunction)
    {
        return calcFunctionMapper.selectCalcFunctionList(calcFunction);
    }

    /**
     * 新增计算函数
     * 
     * @param calcFunction 计算函数
     * @return 结果
     */
    @Override
    public int insertCalcFunction(CalcFunction calcFunction)
    {
        return calcFunctionMapper.insertCalcFunction(calcFunction);
    }

    /**
     * 修改计算函数
     * 
     * @param calcFunction 计算函数
     * @return 结果
     */
    @Override
    public int updateCalcFunction(CalcFunction calcFunction)
    {
        return calcFunctionMapper.updateCalcFunction(calcFunction);
    }

    /**
     * 批量删除计算函数
     * 
     * @param ids 需要删除的计算函数ID
     * @return 结果
     */
    @Override
    public int deleteCalcFunctionByIds(String[] ids)
    {
        return calcFunctionMapper.deleteCalcFunctionByIds(ids);
    }

    /**
     * 删除计算函数信息
     * 
     * @param id 计算函数ID
     * @return 结果
     */
    @Override
    public int deleteCalcFunctionById(String id)
    {
        return calcFunctionMapper.deleteCalcFunctionById(id);
    }
}
