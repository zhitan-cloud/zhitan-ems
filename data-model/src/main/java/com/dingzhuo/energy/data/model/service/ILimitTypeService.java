package com.dingzhuo.energy.data.model.service;

import com.dingzhuo.energy.data.model.domain.LimitType;
import java.util.List;

/**
 * 报警限值类型维护Service接口
 * 
 * @author zw
 * @date 2020-03-07
 */
public interface ILimitTypeService 
{
    /**
     * 查询报警限值类型维护
     * 
     * @param id 报警限值类型维护ID
     * @return 报警限值类型维护
     */
    public LimitType selectLimitTypeById(String id);

    /**
     * 查询报警限值类型维护列表
     * 
     * @param limitType 报警限值类型维护
     * @return 报警限值类型维护集合
     */
    public List<LimitType> selectLimitTypeList(LimitType limitType);

    /**
     * 新增报警限值类型维护
     * 
     * @param limitType 报警限值类型维护
     * @return 结果
     */
    public int insertLimitType(LimitType limitType);

    /**
     * 修改报警限值类型维护
     * 
     * @param limitType 报警限值类型维护
     * @return 结果
     */
    public int updateLimitType(LimitType limitType);

    /**
     * 批量删除报警限值类型维护
     * 
     * @param ids 需要删除的报警限值类型维护ID
     * @return 结果
     */
    public int deleteLimitTypeByIds(String[] ids);

    /**
     * 删除报警限值类型维护信息
     * 
     * @param id 报警限值类型维护ID
     * @return 结果
     */
    public int deleteLimitTypeById(String id);
}
