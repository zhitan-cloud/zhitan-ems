package com.dingzhuo.energy.basic.data.enerInfoManage.mapper;

import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import java.util.List;

/**
 * 产品Mapper接口
 *
 * @author sys
 * @date 2020-02-19
 */
public interface SysProductMapper
{
    /**
     * 查询产品
     *
     * @param productid 产品ID
     * @return 产品
     */
    public SysProduct selectSysProductById(Integer productid);

    /**
     * 查询产品列表
     *
     * @param sysProduct 产品
     * @return 产品集合
     */
    public List<SysProduct> selectSysProductList(SysProduct sysProduct);

    /**
     * 新增产品
     *
     * @param sysProduct 产品
     * @return 结果
     */
    public int insertSysProduct(SysProduct sysProduct);

    /**
     * 修改产品
     *
     * @param sysProduct 产品
     * @return 结果
     */
    public int updateSysProduct(SysProduct sysProduct);

    /**
     * 删除产品
     *
     * @param productid 产品ID
     * @return 结果
     */
    public int deleteSysProductById(Integer productid);

    /**
     * 批量删除产品
     *
     * @param productids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysProductByIds(Integer[] productids);

    //校验no和name 唯一
    Integer selectCountByName(SysProduct sysProduct);
    Integer selectCountByNo(SysProduct sysProduct);
    Integer selectIdByName(SysProduct sysProduct);
    Integer selectIdByNo(SysProduct sysProduct);
}
