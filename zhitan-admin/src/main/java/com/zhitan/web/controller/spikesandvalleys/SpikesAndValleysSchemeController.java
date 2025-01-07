package com.zhitan.web.controller.spikesandvalleys;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.alarm.domain.LimitType;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.framework.web.service.TokenService;
import com.zhitan.spikesandvalleys.domain.SpikesAndValleysScheme;
import com.zhitan.spikesandvalleys.domain.vo.SpikesAndValleysSchemeVo;
import com.zhitan.spikesandvalleys.service.ISpikesAndValleysSchemeService;
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

import com.zhitan.common.utils.poi.ExcelUtil;

/**
 * 尖峰平谷时间段明细Controller
 * 
 * @author ZhiTan
 * @date 2024-10-29
 */
@RestController
@RequestMapping("/system/scheme")
public class SpikesAndValleysSchemeController extends BaseController
{
    @Resource
    private ISpikesAndValleysSchemeService spikesAndValleysSchemeService;

    @Autowired
    private TokenService tokenService;
    /**
     * 查询尖峰平谷时间段明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:scheme:list')")
    @GetMapping("/list")
    public TableDataInfo list(SpikesAndValleysScheme spikesAndValleysScheme, Long pageNum, Long pageSize)
    {
//        startPage();

        Page<SpikesAndValleysSchemeVo> page =  spikesAndValleysSchemeService.selectSpikesAndValleysSchemeList(spikesAndValleysScheme, pageNum, pageSize);

        return getDataTable(page);
    }


//    /**
//     * 导出尖峰平谷时间段明细列表
//     */
//    @PreAuthorize("@ss.hasPermi('system:scheme:export')")
//    @Log(title = "尖峰平谷时间段明细", businessType = BusinessType.EXPORT)
//    @PostMapping("/export")
//    public void export(HttpServletResponse response, SpikesAndValleysScheme spikesAndValleysScheme)
//    {
//        List<SpikesAndValleysScheme> list = spikesAndValleysSchemeService.selectSpikesAndValleysSchemeList(spikesAndValleysScheme);
//        ExcelUtil<SpikesAndValleysScheme> util = new ExcelUtil<SpikesAndValleysScheme>(SpikesAndValleysScheme.class);
//        util.exportExcel(response, list, "尖峰平谷时间段明细数据");
//    }

    /**
     * 获取尖峰平谷时间段明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:scheme:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(spikesAndValleysSchemeService.selectSpikesAndValleysSchemeById(id));
    }

    /**
     * 新增尖峰平谷时间段明细
     */
    @PreAuthorize("@ss.hasPermi('system:scheme:add')")
    @Log(title = "尖峰平谷时间段明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SpikesAndValleysSchemeVo spikesAndValleysScheme)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        spikesAndValleysScheme.setCreateBy(loginUser.getUsername());
        return toAjax(spikesAndValleysSchemeService.insertSpikesAndValleysScheme(spikesAndValleysScheme));
    }

    /**
     * 修改尖峰平谷时间段明细
     */
    @PreAuthorize("@ss.hasPermi('system:scheme:edit')")
    @Log(title = "尖峰平谷时间段明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SpikesAndValleysSchemeVo spikesAndValleysScheme)
    {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        spikesAndValleysScheme.setUpdateBy(loginUser.getUsername());
        return toAjax(spikesAndValleysSchemeService.updateSpikesAndValleysScheme(spikesAndValleysScheme));
    }

    /**
     * 删除尖峰平谷时间段明细
     */
    @PreAuthorize("@ss.hasPermi('system:scheme:remove')")
    @Log(title = "尖峰平谷时间段明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(spikesAndValleysSchemeService.deleteSpikesAndValleysSchemeByIds(ids));
    }
}
