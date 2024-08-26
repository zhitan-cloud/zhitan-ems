package com.dingzhuo.energy.basic.data.facility.controller;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.common.utils.IdUtils;
import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.security.service.TokenService;
//import com.dingzhuo.energy.basic.data.meter.domain.MeterImplement;
//import com.dingzhuo.energy.basic.data.meter.domain.MeterImplementExcel;
//import com.sun.org.glassfish.external.statistics.annotations.Reset;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import com.dingzhuo.energy.basic.data.facility.domain.FacilityArchives;
import com.dingzhuo.energy.basic.data.facility.service.IFacilityArchivesService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 设备档案Controller
 *
 * @author zhaowei
 * @date 2020-02-24
 */
@RestController
@RequestMapping("/facility/archives")
@Api(value = "设备档案controller",tags = {"设备档案管理"})
public class FacilityArchivesController extends BaseController
{
    @Autowired
    private IFacilityArchivesService facilityArchivesService;
    @Autowired
    private TokenService tokenService;
    /**
     * 查询设备档案列表
     */
    @ApiOperation(value = "设备档案列表")
    @PreAuthorize("@ss.hasPermi('facility:archives:list')")
    @GetMapping("/list")
    public TableDataInfo list(FacilityArchives facilityArchives)
    {
        startPage();
        List<FacilityArchives> list = facilityArchivesService.selectFacilityArchivesList(facilityArchives);
        return getDataTable(list);
    }

    /**
     * 导出设备档案列表
     */
    @PreAuthorize("@ss.hasPermi('facility:archives:export')")
    @Log(title = "设备档案", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    @ApiOperation("设备档案列表导出")
    public AjaxResult export(FacilityArchives facilityArchives)
    {
        List<FacilityArchives> list = facilityArchivesService.excelFacilityArchivesList(facilityArchives);
        ExcelUtil<FacilityArchives> util = new ExcelUtil<FacilityArchives>(FacilityArchives.class);
        return util.exportExcel(list, "archives");
    }

    /**
     * 获取设备档案详细信息
     */
    @PreAuthorize("@ss.hasPermi('facility:archives:query')")
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "根据id获取设备档案详细信息")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(facilityArchivesService.selectFacilityArchivesById(id));
    }

    /**
     * 新增设备档案
     */
    @PreAuthorize("@ss.hasPermi('facility:archives:add')")
    @Log(title = "设备档案", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增设备档案")
    public AjaxResult add(@RequestBody FacilityArchives facilityArchives)
    {
        FacilityArchives check = facilityArchivesService.selectFacilityArchivesByCode(facilityArchives);
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        //编号唯一性检测
        if(check!=null && check.getCode()!=null && check.getCode().length()>0)
        {
            return  AjaxResult.error(check.getCode()+"编码已存在!");
        }else
        {
            facilityArchives.setId(UUID.randomUUID().toString());
            facilityArchives.setCreateBy(loginUser.getUsername());
            return toAjax(facilityArchivesService.insertFacilityArchives(facilityArchives));
        }
    }

    /**
     * 修改设备档案
     */
    @PreAuthorize("@ss.hasPermi('facility:archives:edit')")
    @Log(title = "设备档案", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "编辑设备档案")
    public AjaxResult edit(@RequestBody FacilityArchives facilityArchives)
    {
        //更新时的编码 唯一约束 要判断 id不等于自己，且 code存在重复的，要先去掉自己，否则 自己的修改也报错
        FacilityArchives check = facilityArchivesService.selectFacilityArchivesByCode(facilityArchives);
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        //编号唯一性检测
        if(check!=null && check.getCode()!=null && check.getCode().length()>0)
        {
            return  AjaxResult.error(check.getCode()+"编码已存在!");
        }else
        {
            facilityArchives.setUpdateBy(loginUser.getUsername());
            return toAjax(facilityArchivesService.updateFacilityArchives(facilityArchives));
        }

    }

    /**
     * 删除设备档案
     */
    @PreAuthorize("@ss.hasPermi('facility:archives:remove')")/**/
    @Log(title = "设备档案", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    @ApiOperation(value = "删除设备档案")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(facilityArchivesService.deleteFacilityArchivesByIds(ids));
    }
  /**
     * 检定恢复
     */
    @PreAuthorize("@ss.hasPermi('facility:archives:reset')")
    @Log(title = "设备档案", businessType = BusinessType.UPDATE)
    @PostMapping ("/{ids}")
    @ApiOperation(value = "设备档案检定恢复")
    public AjaxResult reset(@PathVariable String[] ids)
    {
        return toAjax(facilityArchivesService.resetFacilityArchivesByIds(ids));
    }


    @Log(title = "设备档案维护导入", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('facility:archives:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<FacilityArchives> util = new ExcelUtil<FacilityArchives>(FacilityArchives.class);
        List<FacilityArchives> facilityList = util.importExcel(file.getInputStream());
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        String message = facilityArchivesService.excelImpSave(facilityList,loginUser);
        return AjaxResult.success(message);
    }

    @GetMapping("/importTemplate")
    public AjaxResult importTemplate()
    {
        ExcelUtil<FacilityArchives> util = new ExcelUtil<FacilityArchives>(FacilityArchives.class);
        return util.importTemplateExcel("设备档案数据");
    }
}
