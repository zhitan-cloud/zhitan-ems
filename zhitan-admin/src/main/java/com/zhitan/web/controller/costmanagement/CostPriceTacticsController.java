package com.zhitan.web.controller.costmanagement;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.costmanagement.domain.CostPriceTactics;
import com.zhitan.costmanagement.domain.vo.CostPriceTacticsVo;
import com.zhitan.costmanagement.service.ICostPriceTacticsService;
import com.zhitan.framework.web.service.TokenService;
import com.zhitan.peakvalley.service.IElectricityPriceDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 成本策略Controller
 *
 * @author ZhiTan
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/cost/tactics")
public class CostPriceTacticsController extends BaseController
{
    @Resource
    private ICostPriceTacticsService costPriceTacticsService;
    @Autowired
    private TokenService tokenService;
    @Resource
    private IElectricityPriceDateService electricityPriceDateService;

    /**
     * 查询成本策略列表
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostPriceTactics costPriceTactics,Long pageNum, Long pageSize)
    {
        Page<CostPriceTacticsVo> page =  costPriceTacticsService.selectCostPriceTacticsList(costPriceTactics, pageNum, pageSize);

        return getDataTable(page);
    }
    /**
     * 查询成本策略列表
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:list')")
    @GetMapping("/allList")
    public AjaxResult allList()
    {
        List<CostPriceTacticsVo> list =  costPriceTacticsService.selectCostPriceTacticsListAll();

        return AjaxResult.success(list);
    }

//    /**
//     * 导出成本策略列表
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:export')")
//    @Log(title = "成本策略", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, CostPriceTactics costPriceTactics)
//    {
//        List<CostPriceTactics> list = costPriceTacticsService.selectCostPriceTacticsList(costPriceTactics);
//        ExcelUtil<CostPriceTactics> util = new ExcelUtil<CostPriceTactics>(CostPriceTactics.class);
//        util.exportExcel(response, list, "成本策略数据");
//    }

    /**
     * 获取成本策略详细信息
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(costPriceTacticsService.selectCostPriceTacticsById(id));
    }

    /**
     * 新增成本策略
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:add')")
    @Log(title = "成本策略", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostPriceTacticsVo costPriceTacticsVo) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        costPriceTacticsVo.setCreateBy(loginUser.getUsername());
        return toAjax(costPriceTacticsService.insertCostPriceTactics(costPriceTacticsVo));
    }

    /**
     * 修改成本策略
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:edit')")
    @Log(title = "成本策略", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostPriceTacticsVo costPriceTacticsVo)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        costPriceTacticsVo.setUpdateBy(loginUser.getUsername());
        return toAjax(costPriceTacticsService.updateCostPriceTactics(costPriceTacticsVo));
    }

    /**
     * 删除成本策略
     */
    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:remove')")
    @Log(title = "成本策略", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(costPriceTacticsService.deleteCostPriceTacticsByIds(ids));
    }
}
