package com.dingzhuo.energy.basic.data.enerInfoManage.mapper;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysPrice;

import java.util.List;

/**
 * 单价设置Mapper接口
 *
 * @author ruoyi
 * @date 2020-02-15
 */
public interface SysPriceMapper
{
    /**
     * 查询单价设置
     *
     * @param priceid 单价设置ID
     * @return 单价设置
     */
    public SysPrice selectSysPriceById(Integer priceid);

    /**
     * 查询单价设置列表
     *
     * @param sysPrice 单价设置
     * @return 单价设置集合
     */
    public List<SysPrice> selectSysPriceList(SysPrice sysPrice);

    /**
     * 新增单价设置
     *
     * @param sysPrice 单价设置
     * @return 结果
     */
    public int insertSysPrice(SysPrice sysPrice);

    /**
     * 修改单价设置
     *
     * @param sysPrice 单价设置
     * @return 结果
     */
    public int updateSysPrice(SysPrice sysPrice);

    /**
     * 删除单价设置
     *
     * @param priceid 单价设置ID
     * @return 结果
     */
    public int deleteSysPriceById(Integer priceid);

    /**
     * 批量删除单价设置
     *
     * @param priceids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysPriceByIds(Integer[] priceids);
}
