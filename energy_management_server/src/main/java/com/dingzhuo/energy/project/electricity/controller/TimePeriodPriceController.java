package com.dingzhuo.energy.project.electricity.controller;

import java.util.List;

import com.dingzhuo.energy.project.electricity.domain.ElectricityPrice;
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
import com.dingzhuo.energy.project.electricity.domain.TimePeriodPrice;
import com.dingzhuo.energy.project.electricity.service.ITimePeriodPriceService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

/**
 * electricityPriceController
 * 
 * @author sys
 * @date 2020-02-19
 */
@RestController
@RequestMapping("/electricityPrice/price")
public class TimePeriodPriceController extends BaseController
{
    @Autowired
    private ITimePeriodPriceService timePeriodPriceService;

    /**
     * 查询electricityPrice列表
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:list')")
    @GetMapping("/list")
    public TableDataInfo list(TimePeriodPrice timePeriodPrice)
    {
        startPage();
        List<TimePeriodPrice> list= timePeriodPriceService.selectTimePeriodPriceList(timePeriodPrice);
        return getDataTable(list);
    }

    /**
     * 导出electricityPrice列表
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:export')")
    @Log(title = "electricityPrice", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(TimePeriodPrice timePeriodPrice)
    {
        List<TimePeriodPrice> list = timePeriodPriceService.selectTimePeriodPriceList(timePeriodPrice);
        ExcelUtil<TimePeriodPrice> util = new ExcelUtil<TimePeriodPrice>(TimePeriodPrice.class);
        return util.exportExcel(list, "price");
    }

    /**
     * 获取electricityPrice详细信息
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(timePeriodPriceService.selectTimePeriodPriceById(id));
    }

    /**
     * 新增electricityPrice
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:add')")
    @Log(title = "electricityPrice", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TimePeriodPrice timePeriodPrice) {
        int msg=timePeriodPriceService.insertTimePeriodPrice(timePeriodPrice);
        /*if(msg>0){
            timePeriodPriceService.editPeriodPrice(timePeriodPrice);
        }*/
        return toAjax(msg);
    }

    /**
     * 修改electricityPrice
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:edit')")
    @Log(title = "electricityPrice", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TimePeriodPrice timePeriodPrice)
    {
        return toAjax(timePeriodPriceService.updateTimePeriodPrice(timePeriodPrice));
    }

    /**
     * 删除electricityPrice
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:remove')")
    @Log(title = "electricityPrice", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(timePeriodPriceService.deleteTimePeriodPriceByIds(ids));
    }
    /**
     * 根据字典类型查询字典数据信息
     */
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:dictType')")
    @GetMapping("/dictType")
    public AjaxResult EdictType(TimePeriodPrice timePeriodPrice){
        List<TimePeriodPrice> list=timePeriodPriceService.selectDictType(timePeriodPrice);
        return AjaxResult.success(list);
    }
    @PreAuthorize("@ss.hasPermi('electricityPrice:price:dictTypeList')")
    @GetMapping("/dictTypeList")
    public AjaxResult dictTypeList(TimePeriodPrice timePeriodPrice){
        List<TimePeriodPrice> list=timePeriodPriceService.selectDictType(timePeriodPrice);
        if (!list.isEmpty()) {
            return AjaxResult.success(list);
        }else{
            List<TimePeriodPrice> TimePeriodPriceList=timePeriodPriceService.dictTypeList(timePeriodPrice);
            return AjaxResult.success(TimePeriodPriceList);
        }
    }
}
