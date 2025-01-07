package com.zhitan.costmanagement.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhitan.costmanagement.domain.CostElectricityInput;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
public interface CostElectricityInputMapper extends BaseMapper<CostElectricityInput> {
    /**
     * 查询【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    public CostElectricityInput selectCostElectricityInputById(String id);

    /**
     * 查询【请填写功能名称】列表
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 【请填写功能名称】集合
     */
    public List<CostElectricityInput> selectCostElectricityInputList(CostElectricityInput costElectricityInput);

    /**
     * 新增【请填写功能名称】
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 结果
     */
    public int insertCostElectricityInput(CostElectricityInput costElectricityInput);

    /**
     * 修改【请填写功能名称】
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 结果
     */
    public int updateCostElectricityInput(CostElectricityInput costElectricityInput);

    /**
     * 删除【请填写功能名称】
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteCostElectricityInputById(String id);

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCostElectricityInputByIds(String[] ids);
}
