package com.zhitan.costmanagement.service;

import java.util.List;
import com.zhitan.costmanagement.domain.CostElectricityInput;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * 【请填写功能名称】Service接口
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
public interface ICostElectricityInputService extends IService<CostElectricityInput> {
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
    public Page<CostElectricityInput> selectCostElectricityInputList(CostElectricityInput costElectricityInput,Long pageNum, Long pageSize);

    /**
     * 新增【请填写功能名称】
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 结果
     */
    public int insertCostElectricityInput(CostElectricityInput costElectricityInput) throws Exception;

    /**
     * 修改【请填写功能名称】
     *
     * @param costElectricityInput 【请填写功能名称】
     * @return 结果
     */
    public int updateCostElectricityInput(CostElectricityInput costElectricityInput) throws Exception;

    /**
     * 批量删除【请填写功能名称】
     *
     * @param ids 需要删除的【请填写功能名称】主键集合
     * @return 结果
     */
    public int deleteCostElectricityInputByIds(String[] ids);

    /**
     * 删除【请填写功能名称】信息
     *
     * @param id 【请填写功能名称】主键
     * @return 结果
     */
    public int deleteCostElectricityInputById(String id);
}
