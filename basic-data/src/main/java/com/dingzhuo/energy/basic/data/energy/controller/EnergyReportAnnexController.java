package com.dingzhuo.energy.basic.data.energy.controller;

import java.util.List;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyProjectAnnex;
import com.dingzhuo.energy.common.utils.IdUtils;
import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.common.utils.file.FileUploadUtils;
import com.dingzhuo.energy.framework.config.RuoYiConfig;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.security.service.TokenService;
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
import com.dingzhuo.energy.basic.data.energy.domain.EnergyReportAnnex;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyReportAnnexService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 节能分析报告管理附件Controller
 * 
 * @author zhaow
 * @date 2020-12-21
 */
@RestController
@RequestMapping("/energy/reportAnnex")
public class EnergyReportAnnexController extends BaseController
{
    @Autowired
    private IEnergyReportAnnexService energyReportAnnexService;
    @Autowired
    private TokenService tokenService;
    /**
     * 查询节能分析报告管理附件列表
     */
    @PreAuthorize("@ss.hasPermi('energy:reportAnnex:list')")
    @GetMapping("/list")
    public TableDataInfo list(EnergyReportAnnex energyReportAnnex)
    {
        startPage();
        List<EnergyReportAnnex> list = energyReportAnnexService.selectEnergyReportAnnexList(energyReportAnnex);
        return getDataTable(list);
    }

    /**
     * 导出节能分析报告管理附件列表
     */
    @PreAuthorize("@ss.hasPermi('energy:reportAnnex:export')")
    @Log(title = "节能分析报告管理附件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EnergyReportAnnex energyReportAnnex)
    {
        List<EnergyReportAnnex> list = energyReportAnnexService.selectEnergyReportAnnexList(energyReportAnnex);
        ExcelUtil<EnergyReportAnnex> util = new ExcelUtil<EnergyReportAnnex>(EnergyReportAnnex.class);
        return util.exportExcel(list, "reportAnnex");
    }

    /**
     * 获取节能分析报告管理附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:reportAnnex:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(energyReportAnnexService.selectEnergyReportAnnexById(id));
    }

    /**
     * 新增节能分析报告管理附件
     */
    @PreAuthorize("@ss.hasPermi('energy:reportAnnex:add')")
    @Log(title = "节能分析报告管理附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EnergyReportAnnex energyReportAnnex)
    {
        return toAjax(energyReportAnnexService.insertEnergyReportAnnex(energyReportAnnex));
    }

    /**
     * 修改节能分析报告管理附件
     */
    @PreAuthorize("@ss.hasPermi('energy:reportAnnex:edit')")
    @Log(title = "节能分析报告管理附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EnergyReportAnnex energyReportAnnex)
    {
        return toAjax(energyReportAnnexService.updateEnergyReportAnnex(energyReportAnnex));
    }

    /**
     * 删除节能分析报告管理附件
     */
    @PreAuthorize("@ss.hasPermi('energy:reportAnnex:remove')")
    @Log(title = "节能分析报告管理附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(energyReportAnnexService.deleteEnergyReportAnnexByIds(ids));
    }

    /**
     * 节能计划附件上传
     */
    @Log(title = "节能分析报告管理附件上传", businessType = BusinessType.IMPORT)
    @PostMapping("/fileImport")
    public AjaxResult importData(MultipartFile file, String reportId) throws Exception
    {
        if (!file.isEmpty())
        {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            //获取配置的文件上传路径
            String uploadPathDir = RuoYiConfig.getUploadPath();
            //获取文件原始名称
            String fileName = file.getOriginalFilename();
            //获取文件扩展名
            String fileSuffix = FileUploadUtils.getExtension(file);
            //文件最终保存的绝对路径
            String filePath = FileUploadUtils.uploadAll(uploadPathDir, file);
//            System.out.println("上传文件地址:===>"+filePath);
            EnergyReportAnnex energyReportAnnex=new EnergyReportAnnex();
            //设置主键UUID
            energyReportAnnex.setId(IdUtils.simpleUUID());
            energyReportAnnex.setFileName(fileName);
            energyReportAnnex.setFilePath(filePath);
            energyReportAnnex.setFileSuffix(fileSuffix);
            energyReportAnnex.setReportId(reportId);
            energyReportAnnex.setCreateBy(loginUser.getUsername());
            if (this.energyReportAnnexService.insertEnergyReportAnnex(energyReportAnnex)>0)
            {
                return AjaxResult.success("附件上传完成");
            }
        }
        return AjaxResult.success("附件上传失败");
    }
}
