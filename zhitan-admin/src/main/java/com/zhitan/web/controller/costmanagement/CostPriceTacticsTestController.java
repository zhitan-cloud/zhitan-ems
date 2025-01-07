//package com.zhitan.web.controller.costmanagement;
//
//import java.util.List;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.zhitan.common.core.domain.model.LoginUser;
//import com.zhitan.common.core.page.TableDataInfo;
//import com.zhitan.common.utils.ServletUtils;
//import com.zhitan.costmanagement.domain.vo.ElectricityPriceDateVo;
//import com.zhitan.framework.web.service.TokenService;
//import com.zhitan.peakvalley.domain.ElectricityPriceDate;
//import com.zhitan.peakvalley.service.IElectricityPriceDateService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import javax.annotation.Resource;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import com.zhitan.common.annotation.Log;
//import com.zhitan.common.core.controller.BaseController;
//import com.zhitan.common.core.domain.AjaxResult;
//import com.zhitan.common.enums.BusinessType;
//import com.zhitan.costmanagement.service.ICostPriceTacticsService;
//
///**
// * 成本策略Controller
// *
// * @author ZhiTan
// * @date 2024-11-08
// */
//@RestController
//@RequestMapping("/cost/tactics")
//public class CostPriceTacticsTestController extends BaseController
//{
//    @Resource
//    private ICostPriceTacticsService costPriceTacticsService;
//    @Autowired
//    private TokenService tokenService;
//    @Resource
//    private IElectricityPriceDateService electricityPriceDateService;
//
//
//    /**
//     * 查询成本策略列表
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:list')")
//    @GetMapping("/list")
//    public TableDataInfo list(ElectricityPriceDate electricityPriceDate, Long pageNum, Long pageSize)
//    {
//        Page<ElectricityPriceDateVo> page =  electricityPriceDateService.selectElectricityPriceDatePageTactics(electricityPriceDate, pageNum, pageSize);
//
//        return getDataTable(page);
//    }
//    /**
//     * 查询成本策略列表
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:list')")
//    @GetMapping("/allList")
//    public AjaxResult allList()
//    {
//        List<ElectricityPriceDate> list =  electricityPriceDateService.selectElectricityPriceDatePageTacticsAll();
//        return AjaxResult.success(list);
//    }
//
//
//
//    /**
//     * 获取成本策略详细信息
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:query')")
//    @GetMapping(value = "/{id}")
//    public AjaxResult getInfo(@PathVariable("id") String id)
//    {
//        return success(electricityPriceDateService.selectCostPriceTacticsById(id));
//    }
//
//    /**
//     * 新增成本策略
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:add')")
//    @Log(title = "成本策略", businessType = BusinessType.INSERT)
//    @PostMapping
//    public AjaxResult add(@RequestBody ElectricityPriceDateVo electricityPriceDateVo) throws Exception {
//        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//        electricityPriceDateVo.setCreateBy(loginUser.getUsername());
//        return toAjax(electricityPriceDateService.insertCostPriceTactics(electricityPriceDateVo));
//    }
//
//    /**
//     * 修改成本策略
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:edit')")
//    @Log(title = "成本策略", businessType = BusinessType.UPDATE)
//    @PutMapping
//    public AjaxResult edit(@RequestBody ElectricityPriceDateVo electricityPriceDateVo)
//    {
//        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
//        electricityPriceDateVo.setUpdateBy(loginUser.getUsername());
//        return toAjax(electricityPriceDateService.updateCostPriceTactics(electricityPriceDateVo));
//    }
//
//    /**
//     * 删除成本策略
//     */
//    @PreAuthorize("@ss.hasPermi('costmanagement:tactics:remove')")
//    @Log(title = "成本策略", businessType = BusinessType.DELETE)
//	@DeleteMapping("/{ids}")
//    public AjaxResult remove(@PathVariable String[] ids)
//    {
//        return toAjax(electricityPriceDateService.deleteCostPriceTacticsByIds(ids));
//    }
//}
