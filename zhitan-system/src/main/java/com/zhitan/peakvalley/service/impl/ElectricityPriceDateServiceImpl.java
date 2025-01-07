package com.zhitan.peakvalley.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.common.utils.uuid.UUID;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.zhitan.costmanagement.domain.CostPriceTacticsItem;
import com.zhitan.costmanagement.domain.vo.CostPriceTacticsVo;
import com.zhitan.costmanagement.domain.vo.ElectricityPriceDateVo;
import com.zhitan.peakvalley.domain.ElectricityPrice;
import com.zhitan.peakvalley.domain.ElectricityPriceDate;
import com.zhitan.peakvalley.mapper.ElectricityPriceDateMapper;
import com.zhitan.peakvalley.mapper.ElectricityPriceMapper;
import com.zhitan.peakvalley.service.IElectricityPriceDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ElectricityPriceMapper electricityPriceMapper;



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

//    @Override
//    public Page<ElectricityPriceDateVo> selectElectricityPriceDatePageTactics(ElectricityPriceDate electricityPriceDate, Long pageNum, Long pageSize) {
//        LambdaQueryWrapper<ElectricityPriceDate> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.like(StringUtils.isNotEmpty(electricityPriceDate.getTacticsName()),ElectricityPriceDate::getTacticsName,electricityPriceDate.getTacticsName());
//        queryWrapper.like(StringUtils.isNotEmpty(electricityPriceDate.getTacticsNumber()),ElectricityPriceDate::getTacticsNumber,electricityPriceDate.getTacticsNumber());
//
//        queryWrapper.eq(StringUtils.isNotEmpty(electricityPriceDate.getType()),ElectricityPriceDate::getType,electricityPriceDate.getType());
//        queryWrapper.orderByDesc(ElectricityPriceDate::getCreateTime);
//        Page<ElectricityPriceDate> page = electricityPriceDateMapper.selectPage(new Page<>(pageNum,pageSize),queryWrapper);
//        IPage aa =  page.convert(s->{
//            ElectricityPriceDateVo v = new ElectricityPriceDateVo();
//            BeanUtil.copyProperties(s, v);
//            LambdaQueryWrapper<ElectricityPrice> queryWrapperItem = new LambdaQueryWrapper<>();
//            queryWrapperItem.eq(ElectricityPrice::getParentId,s.getId());
//            List<ElectricityPrice> itemListOld = electricityPriceMapper.selectList(queryWrapperItem);
//            v.setItemList(itemListOld);
//            return v;
//        });
//
//        return (Page<ElectricityPriceDateVo>) aa;
//    }
//
//    @Override
//    public List<ElectricityPriceDate> selectElectricityPriceDatePageTacticsAll() {
//        return electricityPriceDateMapper.selectElectricityPriceDatePageTacticsAll();
//    }
//
//    @Override
//    public ElectricityPriceDateVo selectCostPriceTacticsById(String id) {
//        ElectricityPriceDateVo result = new ElectricityPriceDateVo();
//        ElectricityPriceDate info = electricityPriceDateMapper.selectElectricityPriceDateById(id);
//        BeanUtil.copyProperties(info,result);
//        LambdaQueryWrapper<ElectricityPrice> queryWrapperItem = new LambdaQueryWrapper<>();
//        queryWrapperItem.eq(ElectricityPrice::getParentId,id);
//        List<ElectricityPrice> itemListOld = electricityPriceMapper.selectList(queryWrapperItem);
//        result.setItemList(itemListOld);
//        return result ;
//    }
//
//    @Override
//    public int insertCostPriceTactics(ElectricityPriceDateVo electricityPriceDateVo) throws Exception {
//        ElectricityPriceDate search = electricityPriceDateMapper.selectOne(new QueryWrapper<ElectricityPriceDate>().eq("tactics_number",electricityPriceDateVo.getTacticsNumber()));
//        if (search!=null){
//            throw new Exception("该策略编码已存在！");
//        }
//        electricityPriceDateVo.setCreateTime(DateUtils.getNowDate());
//        electricityPriceDateVo.setId(java.util.UUID.randomUUID().toString());
//        ElectricityPriceDate electricityPriceDate = new ElectricityPriceDate();
//        BeanUtil.copyProperties(electricityPriceDateVo,electricityPriceDate);
//        int result =  electricityPriceDateMapper.insert(electricityPriceDate);
//        //插入子表
//        List<ElectricityPrice> itemList =  electricityPriceDateVo.getItemList();
//        itemList.stream().forEach(s->{
//            s.setId(java.util.UUID.randomUUID().toString());
//            s.setCreateTime(DateUtils.getNowDate());
//            s.setCreateBy(electricityPriceDateVo.getCreateBy());
//            s.setParentId(electricityPriceDateVo.getId());
//            electricityPriceMapper.insert(s);
//        });
//
//        return result;
//    }
//
//    @Override
//    public int updateCostPriceTactics(ElectricityPriceDateVo electricityPriceDateVo) {
//        electricityPriceDateVo.setUpdateTime(DateUtils.getNowDate());
//        ElectricityPriceDate electricityPriceDate = new ElectricityPriceDate();
//        BeanUtil.copyProperties(electricityPriceDateVo,electricityPriceDate);
//        int result = electricityPriceDateMapper.updateElectricityPriceDate(electricityPriceDate);
//        //删除子表
//        LambdaQueryWrapper<ElectricityPrice> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ElectricityPrice::getParentId,electricityPriceDate.getId());
//        List<ElectricityPrice> itemListOld = electricityPriceMapper.selectList(queryWrapper);
//        List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
//        String[] ids = idList.toArray(new String[idList.size()]);
//        electricityPriceMapper.deleteElectricityPriceByIds(ids);
//        //插入子表
//        List<ElectricityPrice> itemList =  electricityPriceDateVo.getItemList();
//        itemList.stream().forEach(s->{
//            s.setId(java.util.UUID.randomUUID().toString());
//            s.setCreateTime(DateUtils.getNowDate());
//            s.setCreateBy(electricityPriceDateVo.getCreateBy());
//            s.setParentId(electricityPriceDateVo.getId());
//            electricityPriceMapper.insert(s);
//        });
//        return result;
//    }
//
//    @Override
//    public int deleteCostPriceTacticsByIds(String[] ids) {
//        //删除子表
//        List<String> list = Arrays.asList(ids);
//        list.stream().forEach(item->{
//            LambdaQueryWrapper<ElectricityPrice> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(ElectricityPrice::getParentId,item);
//            List<ElectricityPrice> itemListOld = electricityPriceMapper.selectList(queryWrapper);
//            List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
//            String[] itemIds = idList.toArray(new String[idList.size()]);
//            electricityPriceMapper.deleteElectricityPriceByIds(itemIds);
//        });
//
//        return electricityPriceDateMapper.deleteElectricityPriceDateByIds(ids);
//    }
//
//    @Override
//    public int deleteCostPriceTacticsById(String id) {
//        LambdaQueryWrapper<ElectricityPrice> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ElectricityPrice::getParentId,id);
//        List<ElectricityPrice> itemListOld = electricityPriceMapper.selectList(queryWrapper);
//        List<String>idList = itemListOld.stream().map(s->s.getId()).collect(Collectors.toList());
//        String[] itemIds = idList.toArray(new String[idList.size()]);
//        electricityPriceMapper.deleteElectricityPriceByIds(itemIds);
//        return electricityPriceDateMapper.deleteElectricityPriceDateById(id);
//    }
}
