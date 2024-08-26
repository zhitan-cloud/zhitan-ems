package com.dingzhuo.energy.project.electricity.service.impl;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
import com.dingzhuo.energy.project.electricity.mapper.ElectricityPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.electricity.mapper.TimePeriodPriceMapper;
import com.dingzhuo.energy.project.electricity.domain.TimePeriodPrice;
import com.dingzhuo.energy.project.electricity.service.ITimePeriodPriceService;

/**
 * electricityService业务层处理
 * 
 * @author sys
 * @date 2020-02-19
 */
@Service
public class TimePeriodPriceServiceImpl implements ITimePeriodPriceService 
{
    @Autowired
    private TimePeriodPriceMapper timePeriodPriceMapper;

    /**
     * 查询electricity
     * 
     * @param id electricityID
     * @return electricity
     */
    @Override
    public TimePeriodPrice selectTimePeriodPriceById(String id)
    {
        return timePeriodPriceMapper.selectTimePeriodPriceById(id);
    }

    /**
     * 查询electricity列表
     * 
     * @param timePeriodPrice electricity
     * @return electricity
     */
    @Override
    public List<TimePeriodPrice> selectTimePeriodPriceList(TimePeriodPrice timePeriodPrice)
    {
        return timePeriodPriceMapper.selectTimePeriodPriceList(timePeriodPrice);
    }

    /**
     * 新增electricity
     * 
     * @param timePeriodPrice electricity
     * @return 结果
     */
    @Override
    public int insertTimePeriodPrice(TimePeriodPrice timePeriodPrice)
    {
        String[] s_array=timePeriodPrice.getPrice().split(",");
        String[] TimePeriod=timePeriodPrice.getDictValue().split(",");
        int msg=0;
        for (int i=0;i<TimePeriod.length;i++) {
            timePeriodPrice.setId(UUID.randomUUID().toString());
            timePeriodPrice.setTimePeriod(TimePeriod[i]);
            timePeriodPrice.setPrice(s_array[i]);
           msg= timePeriodPriceMapper.insertTimePeriodPrice(timePeriodPrice);
        }
        return msg;
    }
    /**
     * 修改electricity
     * 
     * @param timePeriodPrice electricity
     * @return 结果
     */
    @Override
    public int updateTimePeriodPrice(TimePeriodPrice timePeriodPrice){
        String[] s_array=timePeriodPrice.getPrice().split(",");
        String[] TimePeriod=timePeriodPrice.getDictValue().split(",");
        String[] ids=timePeriodPrice.getId().split(",");
        int msg=0;
        for (int i=0;i<ids.length;i++) {
            timePeriodPrice.setId(ids[i]);
            timePeriodPrice.setTimePeriod(TimePeriod[i]);
            timePeriodPrice.setPrice(s_array[i]);
            msg= timePeriodPriceMapper.updateTimePeriodPrice(timePeriodPrice);
        }
        return msg;
    }

    /**
     * 批量删除electricity
     * 
     * @param ids 需要删除的electricityID
     * @return 结果
     */
    @Override
    public int deleteTimePeriodPriceByIds(String[] ids)
    {
        return timePeriodPriceMapper.deleteTimePeriodPriceByIds(ids);
    }

    /**
     * 删除electricity信息
     * 
     * @param id electricityID
     * @return 结果
     */
    @Override
    public int deleteTimePeriodPriceById(String id)
    {
        return timePeriodPriceMapper.deleteTimePeriodPriceById(id);
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param timePeriodPrice timePeriodPrice
     * @return electricityPrice
     */
    @Override
    public List<TimePeriodPrice> selectDictType(TimePeriodPrice timePeriodPrice)
    {
        return timePeriodPriceMapper.selectDictType(timePeriodPrice);
    }
    public List<TimePeriodPrice> dictTypeList(TimePeriodPrice timePeriodPrice){
        return timePeriodPriceMapper.dictTypeList(timePeriodPrice);
    }
}
