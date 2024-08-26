package com.dingzhuo.energy.basic.data.enerInfoManage.service.impl;

import java.util.List;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.enerInfoManage.mapper.SysPriceMapper;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysPriceService;

/**
 * 单价设置Service业务层处理
 *
 * @author ruoyi
 * @date 2020-02-15
 */
@Service
public class SysPriceServiceImpl implements ISysPriceService
{
    @Autowired
    private SysPriceMapper sysPriceMapper;

    /**
     * 查询单价设置
     *
     * @param priceid 单价设置ID
     * @return 单价设置
     */
    @Override
    public Object selectSysPriceById(Integer priceid)
    {
        return sysPriceMapper.selectSysPriceById(priceid);
    }

    /**
     * 查询单价设置列表
     *
     * @param sysPrice 单价设置
     * @return 单价设置
     */
    @Override
    public List<SysPrice> selectSysPriceList(SysPrice sysPrice)
    {
        return sysPriceMapper.selectSysPriceList(sysPrice);
    }

    /**
     * 新增单价设置
     *
     * @param sysPrice 单价设置
     * @return 结果
     */
    @Override
    public int insertSysPrice(SysPrice sysPrice)
    {
        return sysPriceMapper.insertSysPrice(sysPrice);
    }

    /**
     * 修改单价设置
     *
     * @param sysPrice 单价设置
     * @return 结果
     */
    @Override
    public int updateSysPrice(SysPrice sysPrice)
    {
        return sysPriceMapper.updateSysPrice(sysPrice);
    }

    /**
     * 批量删除单价设置
     *
     * @param priceids 需要删除的单价设置ID
     * @return 结果
     */
    @Override
    public int deleteSysPriceByIds(Integer[] priceids)
    {
        return sysPriceMapper.deleteSysPriceByIds(priceids);
    }

    /**
     * 删除单价设置信息
     *
     * @param priceid 单价设置ID
     * @return 结果
     */
    @Override
    public int deleteSysPriceById(Integer priceid)
    {
        return sysPriceMapper.deleteSysPriceById(priceid);
    }
}
