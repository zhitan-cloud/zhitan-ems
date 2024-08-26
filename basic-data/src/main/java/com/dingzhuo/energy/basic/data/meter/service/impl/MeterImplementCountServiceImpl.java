package com.dingzhuo.energy.basic.data.meter.service.impl;

import java.util.List;
import com.dingzhuo.energy.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.meter.mapper.MeterImplementCountMapper;
import com.dingzhuo.energy.basic.data.meter.domain.MeterImplementCount;
import com.dingzhuo.energy.basic.data.meter.service.IMeterImplementCountService;

/**
 * 计量器具统计查询Service业务层处理
 * 
 * @author zhaowei
 * @date 2020-02-21
 */
@Service
public class MeterImplementCountServiceImpl implements IMeterImplementCountService 
{
    @Autowired
    private MeterImplementCountMapper meterImplementCountMapper;

    /**
     * 查询计量器具统计查询
     * 
     * @param code 计量器具统计查询ID
     * @return 计量器具统计查询
     */
    @Override
    public MeterImplementCount selectMeterImplementCountById(String code)
    {
        return meterImplementCountMapper.selectMeterImplementCountById(code);
    }

    /**
     * 查询计量器具统计查询列表
     * 
     * @param meterImplementCount 计量器具统计查询
     * @return 计量器具统计查询
     */
    @Override
    public List<MeterImplementCount> selectMeterImplementCountList(MeterImplementCount meterImplementCount)
    {
        return meterImplementCountMapper.selectMeterImplementCountList(meterImplementCount);
    }

    /**
     * 新增计量器具统计查询
     * 
     * @param meterImplementCount 计量器具统计查询
     * @return 结果
     */
    @Override
    public int insertMeterImplementCount(MeterImplementCount meterImplementCount)
    {
        meterImplementCount.setCreateTime(DateUtils.getNowDate());
        return meterImplementCountMapper.insertMeterImplementCount(meterImplementCount);
    }

    /**
     * 修改计量器具统计查询
     * 
     * @param meterImplementCount 计量器具统计查询
     * @return 结果
     */
    @Override
    public int updateMeterImplementCount(MeterImplementCount meterImplementCount)
    {
        meterImplementCount.setUpdateTime(DateUtils.getNowDate());
        return meterImplementCountMapper.updateMeterImplementCount(meterImplementCount);
    }

    /**
     * 批量删除计量器具统计查询
     * 
     * @param codes 需要删除的计量器具统计查询ID
     * @return 结果
     */
    @Override
    public int deleteMeterImplementCountByIds(String[] codes)
    {
        return meterImplementCountMapper.deleteMeterImplementCountByIds(codes);
    }

    /**
     * 删除计量器具统计查询信息
     * 
     * @param code 计量器具统计查询ID
     * @return 结果
     */
    @Override
    public int deleteMeterImplementCountById(String code)
    {
        return meterImplementCountMapper.deleteMeterImplementCountById(code);
    }
}
