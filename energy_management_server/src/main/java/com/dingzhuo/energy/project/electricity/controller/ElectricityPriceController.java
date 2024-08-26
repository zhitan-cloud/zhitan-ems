package com.dingzhuo.energy.project.electricity.controller;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
import com.dingzhuo.energy.project.electricity.service.IElectricityPriceService;
import com.dingzhuo.energy.project.system.domain.SysDictData;
import com.dingzhuo.energy.project.system.service.ISysDictDataService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * electricityPriceController
 *
 * @author sys
 * @date 2020-02-18
 */
@RestController
@RequestMapping("/electricityPrice/electricity")
public class ElectricityPriceController extends BaseController
{
    @Autowired
    private IElectricityPriceService electricityPriceService;

    @Autowired
    private ISysDictDataService dictDataService;

    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:dictList')")
    @GetMapping("/dictList")
    public TableDataInfo dictList(SysDictData dictData)
    {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }
    /**
     * 查询electricityPrice列表
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectricityPrice electricityPrice){
        startPage();
        List<ElectricityPrice> Datelist = electricityPriceService.selectList(electricityPrice);
        if(Datelist.size()>0){
            electricityPrice.setEffectiveDate(Datelist.get(0).getEffectiveDate());
        }
        List<ElectricityPrice> list = electricityPriceService.selectElectricityPriceList(electricityPrice);
        return getDataTable(list);
    }
    /**
     * 查询历史列表
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:listHistory')")
    @GetMapping("/listHistory")
    public TableDataInfo listHistory(ElectricityPrice electricityPrice){
        startPage();
        List<ElectricityPrice> list = electricityPriceService.listHistory(electricityPrice);
        return getDataTable(list);
    }
    /**
     * 查询生效日期列表
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:listDate')")
    @GetMapping("/listDate")
    public TableDataInfo listDate(ElectricityPrice electricityPrice) {
        List<ElectricityPrice> list = electricityPriceService.selectList(electricityPrice);
        return getDataTable(list);
    }

    /**
     * 导出electricityPrice列表
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:export')")
    @Log(title = "electricityPrice", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(ElectricityPrice electricityPrice)
    {
        List<ElectricityPrice> list = electricityPriceService.selectElectricityPriceList(electricityPrice);
        ExcelUtil<ElectricityPrice> util = new ExcelUtil<ElectricityPrice>(ElectricityPrice.class);
        return util.exportExcel(list, "electricity");
    }

    /**
     * 获取electricityPrice详细信息
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(electricityPriceService.selectElectricityPriceById(id));
    }

    /**
     * 新增electricityPrice
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:add')")
    @Log(title = "electricityPrice", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectricityPrice electricityPrice){
        return toAjax(electricityPriceService.insertElectricityPrice(electricityPrice));
    }

    /**
     * 修改electricityPrice
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:edit')")
    @Log(title = "electricityPrice", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectricityPrice electricityPrice)
    {
        return toAjax(electricityPriceService.updateElectricityPrice(electricityPrice));
    }

    /**
     * 删除electricityPrice
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:electricity:remove')")
    @Log(title = "electricityPrice", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(electricityPriceService.deleteElectricityPriceByIds(ids));
    }

}
