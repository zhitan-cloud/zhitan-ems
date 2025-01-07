package com.zhitan.web.controller.costmanagement;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.costmanagement.domain.CostElectricityInput;
import com.zhitan.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.costmanagement.domain.CostPriceRelevancy;
import com.zhitan.costmanagement.service.ICostPriceRelevancyService;
import com.zhitan.common.utils.poi.ExcelUtil;

/**
 * 单价关联Controller
 * 
 * @author ZhiTan
 * @date 2024-11-09
 */
@RestController
@RequestMapping("/cost/relevancy")
public class CostPriceRelevancyController extends BaseController
{
    @Resource
    private ICostPriceRelevancyService costPriceRelevancyService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询单价关联列表
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:relevancy:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostPriceRelevancy costPriceRelevancy,Long pageNum, Long pageSize)
    {
        startPage();
        Page<CostPriceRelevancy> page =  costPriceRelevancyService.selectCostPriceRelevancyList(costPriceRelevancy, pageNum, pageSize);
        return getDataTable(page);
    }


//    /**
//     * 导出单价关联列表
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:relevancy:export')")
//    @Log(title = "单价关联", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, CostPriceRelevancy costPriceRelevancy)
//    {
//        List<CostPriceRelevancy> list = costPriceRelevancyService.selectCostPriceRelevancyList(costPriceRelevancy);
//        ExcelUtil<CostPriceRelevancy> util = new ExcelUtil<CostPriceRelevancy>(CostPriceRelevancy.class);
//        util.exportExcel(response, list, "单价关联数据");
//    }

    /**
     * 获取单价关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:relevancy:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(costPriceRelevancyService.selectCostPriceRelevancyById(id));
    }

    /**
     * 新增单价关联
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:relevancy:add')")
    @Log(title = "单价关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostPriceRelevancy costPriceRelevancy)
    {
        return toAjax(costPriceRelevancyService.insertCostPriceRelevancy(costPriceRelevancy));
    }

    /**
     * 修改单价关联
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:relevancy:edit')")
    @Log(title = "单价关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostPriceRelevancy costPriceRelevancy)
    {
        return toAjax(costPriceRelevancyService.updateCostPriceRelevancy(costPriceRelevancy));
    }

    /**
     * 删除单价关联
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:relevancy:remove')")
    @Log(title = "单价关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(costPriceRelevancyService.deleteCostPriceRelevancyByIds(ids));
    }
}
