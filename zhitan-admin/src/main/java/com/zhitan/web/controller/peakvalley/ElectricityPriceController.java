package com.zhitan.web.controller.peakvalley;

import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.peakvalley.domain.ElectricityPrice;
import com.zhitan.peakvalley.service.IElectricityPriceService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 【尖峰平谷电价明细】Controller
 * 
 * @author ZhiTan
 * @date 2024-10-10
 */
@RestController
@RequestMapping("/electricityprice")
public class ElectricityPriceController extends BaseController
{
    @Resource
    private IElectricityPriceService electricityPriceService;

    /**
     * 查询【尖峰平谷电价明细】列表
     */
    @PreAuthorize("@ss.hasPermi('system:price:list')")
    @GetMapping("/list")
    public TableDataInfo list(ElectricityPrice electricityPrice)
    {
        List<ElectricityPrice> list = electricityPriceService.selectElectricityPriceList(electricityPrice);
        return getDataTable(list);
    }


    /**
     * 导出【尖峰平谷电价明细】列表
     */
    @PreAuthorize("@ss.hasPermi('system:price:export')")
    @Log(title = "【尖峰平谷电价明细】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ElectricityPrice electricityPrice)
    {
        List<ElectricityPrice> list = electricityPriceService.selectElectricityPriceList(electricityPrice);
        ExcelUtil<ElectricityPrice> util = new ExcelUtil<ElectricityPrice>(ElectricityPrice.class);
        util.exportExcel(response, list, "【尖峰平谷电价明细】数据");
    }

    /**
     * 获取【尖峰平谷电价明细】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:price:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(electricityPriceService.selectElectricityPriceById(id));
    }

    /**
     * 新增【尖峰平谷电价明细】
     */
    @PreAuthorize("@ss.hasPermi('system:price:add')")
    @Log(title = "【尖峰平谷电价明细】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ElectricityPrice electricityPrice)
    {
        return toAjax(electricityPriceService.insertElectricityPrice(electricityPrice));
    }

    /**
     * 修改【尖峰平谷电价明细】
     */
    @PreAuthorize("@ss.hasPermi('system:price:edit')")
    @Log(title = "【尖峰平谷电价明细】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ElectricityPrice electricityPrice)
    {
        return toAjax(electricityPriceService.updateElectricityPrice(electricityPrice));
    }

    /**
     * 删除【尖峰平谷电价明细】
     */
    @PreAuthorize("@ss.hasPermi('system:price:remove')")
    @Log(title = "【尖峰平谷电价明细】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(electricityPriceService.deleteElectricityPriceByIds(ids));
    }

    /**
     * 修改【尖峰平谷电价明细】
     */
    @PreAuthorize("@ss.hasPermi('system:price:edit')")
    @Log(title = "【尖峰平谷电价明细】", businessType = BusinessType.UPDATE)
    @PutMapping(value = "/save")
    public AjaxResult saveList(@RequestBody List<ElectricityPrice> electricityPriceList)
    {
        electricityPriceService.saveList(electricityPriceList);
        return AjaxResult.success();
    }
}
