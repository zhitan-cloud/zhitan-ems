package com.zhitan.peakvalley.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.zhitan.costmanagement.domain.vo.CostPriceTacticsVo;
import com.zhitan.costmanagement.domain.vo.ElectricityPriceDateVo;
import com.zhitan.peakvalley.domain.ElectricityPriceDate;

import java.util.List;

/**
 * 尖峰平谷电价时间段Service接口
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
public interface IElectricityPriceDateService extends IService<ElectricityPriceDate> {
    /**
     * 查询尖峰平谷电价时间段
     *
     * @param id 尖峰平谷电价时间段主键
     * @return 尖峰平谷电价时间段
     */
    public ElectricityPriceDate selectElectricityPriceDateById(String id);

    /**
     * 查询尖峰平谷电价时间段列表
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 尖峰平谷电价时间段集合
     */
    public List<ElectricityPriceDate> selectElectricityPriceDateList(ElectricityPriceDate electricityPriceDate);

    /**
     * 新增尖峰平谷电价时间段
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 结果
     */
    public int insertElectricityPriceDate(ElectricityPriceDate electricityPriceDate);

    /**
     * 修改尖峰平谷电价时间段
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 结果
     */
    public int updateElectricityPriceDate(ElectricityPriceDate electricityPriceDate);

    /**
     * 批量删除尖峰平谷电价时间段
     *
     * @param ids 需要删除的尖峰平谷电价时间段主键集合
     * @return 结果
     */
    public int deleteElectricityPriceDateByIds(String[] ids);

    /**
     * 删除尖峰平谷电价时间段信息
     *
     * @param id 尖峰平谷电价时间段主键
     * @return 结果
     */
    public int deleteElectricityPriceDateById(String id);

    Page<ElectricityPriceDate> selectElectricityPriceDatePage(ElectricityPriceDate electricityPriceDate, Long pageNum, Long pageSize);



//    /**
//     * 单价策略列表查询
//     *
//     */
//    Page<ElectricityPriceDateVo> selectElectricityPriceDatePageTactics(ElectricityPriceDate electricityPriceDate, Long pageNum, Long pageSize);

//    /**
//     * 单价策略列表查询（不分页）
//     *
//     */
//    List<ElectricityPriceDate> selectElectricityPriceDatePageTacticsAll();
//
//    /**
//     * 查询成本策略
//     *
//     * @param id 成本策略主键
//     * @return 成本策略
//     */
//    public ElectricityPriceDateVo selectCostPriceTacticsById(String id);
//    /**
//     * 新增成本策略
//     *
//     * @param electricityPriceDateVo 成本策略
//     * @return 结果
//     */
//    public int insertCostPriceTactics(ElectricityPriceDateVo electricityPriceDateVo) throws Exception;
//
//    /**
//     * 修改成本策略
//     *
//     * @param electricityPriceDateVo 成本策略
//     * @return 结果
//     */
//    public int updateCostPriceTactics(ElectricityPriceDateVo electricityPriceDateVo);
//
//    /**
//     * 批量删除成本策略
//     *
//     * @param ids 需要删除的成本策略主键集合
//     * @return 结果
//     */
//    public int deleteCostPriceTacticsByIds(String[] ids);
//
//    /**
//     * 删除成本策略信息
//     *
//     * @param id 成本策略主键
//     * @return 结果
//     */
//    public int deleteCostPriceTacticsById(String id);

}
