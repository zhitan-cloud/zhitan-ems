package com.dingzhuo.energy.basic.data.enerInfoManage.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.basic.data.enerInfoManage.domain.SysProduct;
import com.dingzhuo.energy.basic.data.enerInfoManage.service.ISysProductService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * 产品Controller
 *
 * @author sys
 * @date 2020-02-19
 */
@RestController
@RequestMapping("/enerInfoManage/product")
public class SysProductController extends BaseController
{
    @Autowired
    private ISysProductService sysProductService;

    /**
     * 查询产品列表
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysProduct sysProduct)
    {
        startPage();
        List<SysProduct> list = sysProductService.selectSysProductList(sysProduct);
        return getDataTable(list);
    }

    /**
     * 导出产品列表
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:product:export')")
    @Log(title = "产品", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysProduct sysProduct)
    {
        List<SysProduct> list = sysProductService.selectSysProductList(sysProduct);
        ExcelUtil<SysProduct> util = new ExcelUtil<SysProduct>(SysProduct.class);
        return util.exportExcel(list, "product");
    }

    /**
     * 获取产品详细信息
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:product:query')")
    @GetMapping(value = "/{productid}")
    public AjaxResult getInfo(@PathVariable("productid") Integer productid)
    {
        return AjaxResult.success(sysProductService.selectSysProductById(productid));
    }

    /**
     * 新增产品
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:product:add')")
    @Log(title = "产品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysProduct sysProduct) {
        Integer sameNameNum = sysProductService.selectCountByName(sysProduct);
        Integer sameNoNum = sysProductService.selectCountByNo(sysProduct);
        if(sameNameNum==sameNoNum){
            if(sameNameNum==0){
                return toAjax(sysProductService.insertSysProduct(sysProduct));
            }
        }
        return AjaxResult.error("新增失败，产品名称或产品编号重复！");
    }

    /**
     * 修改产品
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:product:edit')")
    @Log(title = "产品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysProduct sysProduct)
    {
        Integer id = sysProduct.getProductid();
        Integer sameNameNum = sysProductService.selectCountByName(sysProduct);
        Integer sameNoNum = sysProductService.selectCountByNo(sysProduct);
        if(sameNameNum==sameNoNum){
            if(sameNameNum==0){
                return toAjax(sysProductService.updateSysProduct(sysProduct));
            }
        }
        boolean s = id==sysProductService.selectIdByName(sysProduct);
        boolean a = id==sysProductService.selectIdByNo(sysProduct);
        if(sameNameNum==1&&sameNoNum==1){
            if(a&&s){
                return toAjax(sysProductService.updateSysProduct(sysProduct));
            }
        }else if(sameNameNum==1&&sameNoNum==0&&s){
            return toAjax(sysProductService.updateSysProduct(sysProduct));
        }else if(sameNoNum==1&&sameNameNum==0&&a){
            return toAjax(sysProductService.updateSysProduct(sysProduct));
        }
        return AjaxResult.error("修改失败，产品名称或产品编号重复！");
    }

    /**
     * 删除产品
     */
    @PreAuthorize("@ss.hasPermi('enerInfoManage:product:remove')")
    @Log(title = "产品", businessType = BusinessType.DELETE)
	@DeleteMapping("/{productids}")
    public AjaxResult remove(@PathVariable Integer[] productids)
    {
        return toAjax(sysProductService.deleteSysProductByIds(productids));
    }
}
