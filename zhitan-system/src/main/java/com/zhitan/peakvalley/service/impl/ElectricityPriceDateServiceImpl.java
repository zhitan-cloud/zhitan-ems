package com.zhitan.peakvalley.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.common.utils.uuid.UUID;
import com.zhitan.peakvalley.domain.ElectricityPriceDate;
import com.zhitan.peakvalley.mapper.ElectricityPriceDateMapper;
import com.zhitan.peakvalley.service.IElectricityPriceDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 尖峰平谷电价时间段Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
@Service
public class ElectricityPriceDateServiceImpl extends ServiceImpl<ElectricityPriceDateMapper, ElectricityPriceDate> implements IElectricityPriceDateService {
    @Autowired
    private ElectricityPriceDateMapper electricityPriceDateMapper;

    /**
     * 查询尖峰平谷电价时间段
     *
     * @param id 尖峰平谷电价时间段主键
     * @return 尖峰平谷电价时间段
     */
    @Override
    public ElectricityPriceDate selectElectricityPriceDateById(String id) {
        return electricityPriceDateMapper.selectElectricityPriceDateById(id);
    }

    /**
     * 查询尖峰平谷电价时间段列表
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 尖峰平谷电价时间段
     */
    @Override
    public List<ElectricityPriceDate> selectElectricityPriceDateList(ElectricityPriceDate electricityPriceDate) {
        return electricityPriceDateMapper.selectElectricityPriceDateList(electricityPriceDate);
    }

    /**
     * 新增尖峰平谷电价时间段
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 结果
     */
    @Override
    public int insertElectricityPriceDate(ElectricityPriceDate electricityPriceDate) {
        electricityPriceDate.setCreateTime(DateUtils.getNowDate());
        electricityPriceDate.setId(UUID.fastUUID().toString());
        return electricityPriceDateMapper.insertElectricityPriceDate(electricityPriceDate);
    }

    /**
     * 修改尖峰平谷电价时间段
     *
     * @param electricityPriceDate 尖峰平谷电价时间段
     * @return 结果
     */
    @Override
    public int updateElectricityPriceDate(ElectricityPriceDate electricityPriceDate) {
        electricityPriceDate.setUpdateTime(DateUtils.getNowDate());
        return electricityPriceDateMapper.updateElectricityPriceDate(electricityPriceDate);
    }

    /**
     * 批量删除尖峰平谷电价时间段
     *
     * @param ids 需要删除的尖峰平谷电价时间段主键
     * @return 结果
     */
    @Override
    public int deleteElectricityPriceDateByIds(String[] ids) {
        return electricityPriceDateMapper.deleteElectricityPriceDateByIds(ids);
    }

    /**
     * 删除尖峰平谷电价时间段信息
     *
     * @param id 尖峰平谷电价时间段主键
     * @return 结果
     */
    @Override
    public int deleteElectricityPriceDateById(String id) {
        return electricityPriceDateMapper.deleteElectricityPriceDateById(id);
    }

    @Override
    public Page<ElectricityPriceDate> selectElectricityPriceDatePage(ElectricityPriceDate electricityPriceDate, Long pageNum, Long pageSize) {

        LambdaQueryWrapper<ElectricityPriceDate> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(electricityPriceDate.getRemark()),ElectricityPriceDate::getRemark,electricityPriceDate.getRemark());
        wrapper.orderByDesc(ElectricityPriceDate::getCreateTime);
        Page<ElectricityPriceDate> page = electricityPriceDateMapper.selectPage(new Page<ElectricityPriceDate>(pageNum, pageSize), wrapper);
        return page;
    }
}
