package com.zhitan.web.controller.costmanagement;

import java.math.BigDecimal;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.framework.web.service.TokenService;
import com.zhitan.spikesandvalleys.domain.vo.SpikesAndValleysSchemeVo;
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
import com.zhitan.costmanagement.domain.CostElectricityInput;
import com.zhitan.costmanagement.service.ICostElectricityInputService;
import com.zhitan.common.utils.poi.ExcelUtil;

/**
 * 【请填写功能名称】Controller
 * 
 * @author ZhiTan
 * @date 2024-11-08
 */
@RestController
@RequestMapping("/cost/input")
public class CostElectricityInputController extends BaseController
{
    @Resource
    private ICostElectricityInputService costElectricityInputService;
    @Autowired
    private TokenService tokenService;
    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:Input:list')")
    @GetMapping("/list")
    public TableDataInfo list(CostElectricityInput costElectricityInput,Long pageNum, Long pageSize)
    {
//        startPage();
//        List<CostElectricityInput> list = costElectricityInputService.selectCostElectricityInputList(costElectricityInput);
        Page<CostElectricityInput> page =  costElectricityInputService.selectCostElectricityInputList(costElectricityInput, pageNum, pageSize);
        return getDataTable(page);
    }


    /**
     * 导出【请填写功能名称】列表
     */
//    @PreAuthorize("@ss.hasPermi('system:Input:export')")
//    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, CostElectricityInput costElectricityInput)
//    {
//        List<CostElectricityInput> list = costElectricityInputService.selectCostElectricityInputList(costElectricityInput);
//        ExcelUtil<CostElectricityInput> util = new ExcelUtil<CostElectricityInput>(CostElectricityInput.class);
//        util.exportExcel(response, list, "【请填写功能名称】数据");
//    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:Input:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(costElectricityInputService.selectCostElectricityInputById(id));
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:Input:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostElectricityInput costElectricityInput) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        costElectricityInput.setCreateBy(loginUser.getUsername());
        //校验尖峰平谷电量相加是否=总用电
        BigDecimal total = costElectricityInput.getFlatElectricity().add(costElectricityInput.getSharpElectricity()).
                add(costElectricityInput.getPeakElectricity()).add(costElectricityInput.getValleyElectricity());
        if(total.compareTo(costElectricityInput.getElectricityNum())!=0){
                return AjaxResult.error("尖峰平谷电量相加不等于总用电量");
        }
        return toAjax(costElectricityInputService.insertCostElectricityInput(costElectricityInput));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:Input:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostElectricityInput costElectricityInput) throws Exception {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        costElectricityInput.setUpdateBy(loginUser.getUsername());
        return toAjax(costElectricityInputService.updateCostElectricityInput(costElectricityInput));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:Input:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(costElectricityInputService.deleteCostElectricityInputByIds(ids));
    }
}
