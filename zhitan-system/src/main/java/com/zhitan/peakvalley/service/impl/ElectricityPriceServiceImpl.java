package com.zhitan.peakvalley.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhitan.common.utils.DateUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.common.utils.time.Time24HourUtil;
import com.zhitan.peakvalley.domain.ElectricityPrice;
import com.zhitan.peakvalley.mapper.ElectricityPriceMapper;
import com.zhitan.peakvalley.service.IElectricityPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 【尖峰平谷电价明细】Service业务层处理
 *
 * @author ZhiTan
 * @date 2024-10-10
 */
@Service
public class ElectricityPriceServiceImpl extends ServiceImpl<ElectricityPriceMapper, ElectricityPrice> implements IElectricityPriceService {
    @Autowired
    private ElectricityPriceMapper electricityPriceMapper;

    /**
     * 查询【尖峰平谷电价明细】
     *
     * @param id 【尖峰平谷电价明细】主键
     * @return 【尖峰平谷电价明细】
     */
    @Override
    public ElectricityPrice selectElectricityPriceById(String id) {
        return electricityPriceMapper.selectElectricityPriceById(id);
    }

    /**
     * 查询【尖峰平谷电价明细】列表
     *
     * @param electricityPrice 【尖峰平谷电价明细】
     * @return 【尖峰平谷电价明细】
     */
    @Override
    public List<ElectricityPrice> selectElectricityPriceList(ElectricityPrice electricityPrice) {
        if(StringUtils.isEmpty(electricityPrice.getParentId())){
            throw new RuntimeException("父级id不能为空");
        }
        return electricityPriceMapper.selectElectricityPriceList(electricityPrice);
    }

    /**
     * 新增【尖峰平谷电价明细】
     *
     * @param electricityPrice 【尖峰平谷电价明细】
     * @return 结果
     */
    @Override
    public int insertElectricityPrice(ElectricityPrice electricityPrice) {
        electricityPrice.setCreateTime(DateUtils.getNowDate());
        return electricityPriceMapper.insertElectricityPrice(electricityPrice);
    }

    /**
     * 修改【尖峰平谷电价明细】
     *
     * @param electricityPrice 【尖峰平谷电价明细】
     * @return 结果
     */
    @Override
    public int updateElectricityPrice(ElectricityPrice electricityPrice) {
        electricityPrice.setUpdateTime(DateUtils.getNowDate());
        return electricityPriceMapper.updateElectricityPrice(electricityPrice);
    }

    /**
     * 批量删除【尖峰平谷电价明细】
     *
     * @param ids 需要删除的【尖峰平谷电价明细】主键
     * @return 结果
     */
    @Override
    public int deleteElectricityPriceByIds(String[] ids) {
        return electricityPriceMapper.deleteElectricityPriceByIds(ids);
    }

    /**
     * 删除【尖峰平谷电价明细】信息
     *
     * @param id 【尖峰平谷电价明细】主键
     * @return 结果
     */
    @Override
    public int deleteElectricityPriceById(String id) {
        return electricityPriceMapper.deleteElectricityPriceById(id);
    }

    @Override
    public void saveList(List<ElectricityPrice> electricityPriceList) {
        if(CollectionUtil.isEmpty(electricityPriceList)){
            throw new RuntimeException("价格明细不能为空！");
        }
        
        //校验时间是否重叠
        boolean isOverlap = checkIntersection(electricityPriceList);
        if(isOverlap){
            throw new RuntimeException("时间段存在重叠，请检查！");
        }

        HashMap<String,String> collect = new HashMap<>();
        electricityPriceList.forEach(electricityPrice -> {
            collect.put(DateUtils.getHhMmSs(electricityPrice.getStartTime()) + "-" + DateUtils.getHhMmSs(electricityPrice.getStopTime()),electricityPrice.getType());
        });
        boolean is24Hour = Time24HourUtil.checkTimeSpan(collect);
        if(!is24Hour){
            throw new RuntimeException("时间没有覆盖24小时");
        }

        String parentId = electricityPriceList.get(0).getParentId();
        LambdaQueryWrapper<ElectricityPrice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ElectricityPrice::getParentId,parentId);
        if(electricityPriceMapper.selectCount(queryWrapper)>0) {
            electricityPriceMapper.delete(queryWrapper);
        }
        Collection<ElectricityPrice> collection = new ArrayList<>(electricityPriceList);
        saveBatch(collection);
    }

    public static boolean checkIntersection(List<ElectricityPrice> dtoList) {
        int length = dtoList.size();
        for (int i = 0; i < length; i++) {
            long startTimeI = dtoList.get(i).getStartTime().getTime();
            long endTimeI = dtoList.get(i).getStopTime().getTime();

            for (int j = i + 1; j < length; j++) {
                long startTimeJ = dtoList.get(j).getStartTime().getTime();
                long endTimeJ = dtoList.get(j).getStopTime().getTime();

                if (startTimeI < endTimeJ && startTimeJ < endTimeI) {
                    return true;  // 存在相交，返回true
                }
            }
        }
        return false;  // 不存在相交，返回false
    }
}
