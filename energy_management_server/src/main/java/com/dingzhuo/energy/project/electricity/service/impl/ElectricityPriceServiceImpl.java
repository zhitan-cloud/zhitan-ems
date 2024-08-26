package com.dingzhuo.energy.project.electricity.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.project.electricity.domain.TimePeriodPrice;
import com.dingzhuo.energy.project.electricity.mapper.TimePeriodPriceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.project.electricity.mapper.ElectricityPriceMapper;
import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
import com.dingzhuo.energy.project.electricity.service.IElectricityPriceService;

/**
 * electricityPriceService业务层处理
 *
 * @author sys
 * @date 2020-02-17
 */
@Service
public class ElectricityPriceServiceImpl implements IElectricityPriceService
{
    @Autowired
    private ElectricityPriceMapper electricityPriceMapper;

    @Autowired
    private TimePeriodPriceMapper timePeriodPriceMapper;
    /**
     * 查询electricityPrice
     *
     * @param priceId electricityPriceID
     * @return electricityPrice
     */
    @Override
    public ElectricityPrice selectElectricityPriceById(String priceId)
    {
        return electricityPriceMapper.selectElectricityPriceById(priceId);
    }

    /**
     * 查询electricityPrice列表
     *
     * @param electricityPrice electricityPrice
     * @return electricityPrice
     */
    @Override
    public List<ElectricityPrice> selectElectricityPriceList(ElectricityPrice electricityPrice){
        List<ElectricityPrice> listdate=electricityPriceMapper.selectList(electricityPrice);
        if(listdate.size()>0){
            electricityPrice.setEffectiveDate(listdate.get(0).getEffectiveDate());
        }
        List<ElectricityPrice> list=electricityPriceMapper.selectElectricityPriceList(electricityPrice);
        return list;
    }
    /**
     * 查询生效日期列表
     *
     * @param electricityPrice electricityPrice
     * @return electricityPrice
     */
    @Override
    public List<ElectricityPrice> selectList(ElectricityPrice electricityPrice)
    {
        return electricityPriceMapper.selectList(electricityPrice);
    }
    /**
     * 新增electricityPrice
     *
     * @param electricityPrice electricityPrice
     * @return 结果
     */
    @Override
    public int insertElectricityPrice(ElectricityPrice electricityPrice){
        electricityPrice.setId(UUID.randomUUID().toString());
        TimePeriodPrice timePeriodPrice =new TimePeriodPrice();
        timePeriodPrice.setEffectiveDate(electricityPrice.getEffectiveDate());
        timePeriodPrice.setTimePeriod(electricityPrice.getEffectiveName());
        List<TimePeriodPrice> priceList = timePeriodPriceMapper.selectTimePeriodPriceList(timePeriodPrice);
        electricityPrice.setPriceId(priceList.get(0).getId());
        return electricityPriceMapper.insertElectricityPrice(electricityPrice);
    }

    /**
     * 修改electricityPrice
     *
     * @param electricityPrice electricityPrice
     * @return 结果
     */
    @Override
    public int updateElectricityPrice(ElectricityPrice electricityPrice)
    {
        return electricityPriceMapper.updateElectricityPrice(electricityPrice);
    }

    /**
     * 批量删除electricityPrice
     *
     * @param priceIds 需要删除的electricityPriceID
     * @return 结果
     */
    @Override
    public int deleteElectricityPriceByIds(String[] priceIds)
    {
        return electricityPriceMapper.deleteElectricityPriceByIds(priceIds);
    }

    /**
     * 删除electricityPrice信息
     *
     * @param priceId electricityPriceID
     * @return 结果
     */
    @Override
    public int deleteElectricityPriceById(String priceId)
    {
        return electricityPriceMapper.deleteElectricityPriceById(priceId);
    }

    /**
     * 查询历史列表
     *
     * @param electricityPrice electricityPrice
     * @return electricityPrice集合
     */
    public List<ElectricityPrice> listHistory(ElectricityPrice electricityPrice){
        return electricityPriceMapper.selectElectricityPriceList(electricityPrice);
    }
}
