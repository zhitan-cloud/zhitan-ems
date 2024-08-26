package com.dingzhuo.energy.data.model.mapper;

import com.dingzhuo.energy.data.model.domain.CalcFunction;

import java.util.List;

/**
 * 计算函数Mapper接口
 * 
 * @author fanxinfu
 * @date 2020-03-10
 */
public interface CalcFunctionMapper 
{
    /**
     * 查询计算函数
     * 
     * @param id 计算函数ID
     * @return 计算函数
     */
    public CalcFunction selectCalcFunctionById(String id);

    /**
     * 查询计算函数列表
     * 
     * @param calcFunction 计算函数
     * @return 计算函数集合
     */
    public List<CalcFunction> selectCalcFunctionList(CalcFunction calcFunction);

    /**
     * 新增计算函数
     * 
     * @param calcFunction 计算函数
     * @return 结果
     */
    public int insertCalcFunction(CalcFunction calcFunction);

    /**
     * 修改计算函数
     * 
     * @param calcFunction 计算函数
     * @return 结果
     */
    public int updateCalcFunction(CalcFunction calcFunction);

    /**
     * 删除计算函数
     * 
     * @param id 计算函数ID
     * @return 结果
     */
    public int deleteCalcFunctionById(String id);

    /**
     * 批量删除计算函数
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCalcFunctionByIds(String[] ids);
}
