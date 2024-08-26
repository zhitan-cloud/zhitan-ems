package com.dingzhuo.energy.basic.data.enerInfoManage.service.impl;

import java.util.List;

import com.dingzhuo.energy.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dingzhuo.energy.basic.data.enerInfoManage.mapper.SysProductMapper;
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysProductService;

/**
 * 产品Service业务层处理
 *
 * @author sys
 * @date 2020-02-19
 */
@Service
public class SysProductServiceImpl implements ISysProductService
{
    @Autowired
    private SysProductMapper sysProductMapper;

    /**
     * 查询产品
     *
     * @param productid 产品ID
     * @return 产品
     */
    @Override
    public SysProduct selectSysProductById(Integer productid)
    {
        return sysProductMapper.selectSysProductById(productid);
    }

    /**
     * 查询产品列表
     *
     * @param sysProduct 产品
     * @return 产品
     */
    @Override
    public List<SysProduct> selectSysProductList(SysProduct sysProduct)
    {
        return sysProductMapper.selectSysProductList(sysProduct);
    }

    /**
     * 新增产品
     *
     * @param sysProduct 产品
     * @return 结果
     */
    @Override
    public int insertSysProduct(SysProduct sysProduct)
    {
        //获取当前登录人
        String nowMan = SecurityUtils.getUsername();
        sysProduct.setModMan(nowMan);
        sysProduct.setOprMan(nowMan);
        return sysProductMapper.insertSysProduct(sysProduct);
    }

    /**
     * 修改产品
     *
     * @param sysProduct 产品
     * @return 结果
     */
    @Override
    public int updateSysProduct(SysProduct sysProduct)
    {
        String nowMan = SecurityUtils.getUsername();
        sysProduct.setModMan(nowMan);
        return sysProductMapper.updateSysProduct(sysProduct);
    }

    /**
     * 批量删除产品
     *
     * @param productids 需要删除的产品ID
     * @return 结果
     */
    @Override
    public int deleteSysProductByIds(Integer[] productids)
    {
        return sysProductMapper.deleteSysProductByIds(productids);
    }

    /**
     * 删除产品信息
     *
     * @param productid 产品ID
     * @return 结果
     */
    @Override
    public int deleteSysProductById(Integer productid)
    {
        return sysProductMapper.deleteSysProductById(productid);
    }


    //校验no和name 唯一
    @Override
    public Integer selectCountByName(SysProduct sysProduct) {
        return sysProductMapper.selectCountByName(sysProduct);
    }

    @Override
    public Integer selectCountByNo(SysProduct sysProduct) {
        return sysProductMapper.selectCountByNo(sysProduct);
    }

    @Override
    public Integer selectIdByName(SysProduct sysProduct) {
        return sysProductMapper.selectIdByName(sysProduct);
    }

    @Override
    public Integer selectIdByNo(SysProduct sysProduct) {
        return sysProductMapper.selectIdByNo(sysProduct);
    }
}
