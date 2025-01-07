package com.zhitan.web.controller.benchmarkmanage;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhitan.benchmarkmanage.domain.EnergyBenchmarkManage;
import com.zhitan.benchmarkmanage.service.IEnergyBenchmarkManageService;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.core.domain.model.LoginUser;
import com.zhitan.common.core.page.TableDataInfo;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.ServletUtils;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.common.utils.poi.ExcelUtil;
import com.zhitan.framework.web.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

/**
 * 标杆值管理Controller
 *
 * @author ZhiTan
 * @date 2024-11-12
 */
@RestController
@RequestMapping("/benchmarkManage")
public class EnergyBenchmarkManageController extends BaseController
{
    @Resource
    private IEnergyBenchmarkManageService energyBenchmarkManageService;

    @Autowired
    private TokenService tokenService;

    /**
     * 查询标杆值管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:benchmarkManage:list')")
    @GetMapping("/list")
    public TableDataInfo list(EnergyBenchmarkManage energyBenchmarkManage, @RequestParam Long pageNum, @RequestParam Long pageSize)
    {
        //        startPage();
        Page<EnergyBenchmarkManage> list = energyBenchmarkManageService.selectBenchmarkManagePage(energyBenchmarkManage,pageNum,pageSize);
        return getDataTable(list);
    }


    /**
     * 导出标杆值管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:benchmarkManage:export')")
    @Log(title = "标杆值管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, EnergyBenchmarkManage energyBenchmarkManage)
    {
        List<EnergyBenchmarkManage> list = energyBenchmarkManageService.selectEnergyBenchmarkManageList(energyBenchmarkManage);
        ExcelUtil<EnergyBenchmarkManage> util = new ExcelUtil<EnergyBenchmarkManage>(EnergyBenchmarkManage.class);
        util.exportExcel(response, list, "标杆值管理数据");
    }

    /**
     * 获取标杆值管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:benchmarkManage:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(energyBenchmarkManageService.selectEnergyBenchmarkManageById(id));
    }

    /**
     * 新增标杆值管理
     */
    @PreAuthorize("@ss.hasPermi('system:benchmarkManage:add')")
    @Log(title = "标杆值管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EnergyBenchmarkManage energyBenchmarkManage)
    {
        if (StringUtils.isBlank(energyBenchmarkManage.getCode())) {
            return error("请输入标杆编号！");
        }

        //校验唯一
        EnergyBenchmarkManage queryParams=new EnergyBenchmarkManage();
        queryParams.setCode(energyBenchmarkManage.getCode());
        List<EnergyBenchmarkManage> codeReptList = energyBenchmarkManageService.getList(queryParams);
        if (null!=codeReptList && !codeReptList.isEmpty()) {
            return error("标杆编号重复！");
        }
        queryParams=new EnergyBenchmarkManage();
        queryParams.setType(energyBenchmarkManage.getType());
        queryParams.setGrade(energyBenchmarkManage.getGrade());
        List<EnergyBenchmarkManage> dataReptList = energyBenchmarkManageService.getList(queryParams);
        if (null!=dataReptList && !dataReptList.isEmpty()) {
            return error("标杆信息已维护！");
        }
        energyBenchmarkManage.setId(UUID.randomUUID().toString());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        energyBenchmarkManage.setCreateBy(loginUser.getUsername());
        return toAjax(energyBenchmarkManageService.insertEnergyBenchmarkManage(energyBenchmarkManage));
    }

    /**
     * 修改标杆值管理
     */
    @PreAuthorize("@ss.hasPermi('system:benchmarkManage:edit')")
    @Log(title = "标杆值管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EnergyBenchmarkManage energyBenchmarkManage)
    {
        if (StringUtils.isBlank(energyBenchmarkManage.getCode())) {
            return error("请输入标杆编号！");
        }
        EnergyBenchmarkManage queryParams=new EnergyBenchmarkManage();
        queryParams.setCode(energyBenchmarkManage.getCode());
        List<EnergyBenchmarkManage> codeReptList = energyBenchmarkManageService.getList(queryParams);
        if (null!=codeReptList && !codeReptList.isEmpty()) {
            return error("标杆编号重复！");
        }
        queryParams=new EnergyBenchmarkManage();
        queryParams.setType(energyBenchmarkManage.getType());
        queryParams.setGrade(energyBenchmarkManage.getGrade());
        List<EnergyBenchmarkManage> dataReptList = energyBenchmarkManageService.getList(queryParams);
        if (null!=dataReptList && !dataReptList.isEmpty()) {
            return error("标杆信息已维护！");
        }
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        energyBenchmarkManage.setUpdateBy(loginUser.getUsername());
        return toAjax(energyBenchmarkManageService.updateEnergyBenchmarkManage(energyBenchmarkManage));
    }

    /**
     * 删除标杆值管理
     */
    @PreAuthorize("@ss.hasPermi('system:benchmarkManage:remove')")
    @Log(title = "标杆值管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(energyBenchmarkManageService.deleteEnergyBenchmarkManageByIds(ids));
    }
}
