package com.dingzhuo.energy.basic.data.energy.controller;

import com.dingzhuo.energy.basic.data.energy.domain.EnergyProjectAnnex;
import com.dingzhuo.energy.basic.data.energy.service.IEnergyProjectAnnexService;
import com.dingzhuo.energy.common.utils.IdUtils;
import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.common.utils.file.FileUploadUtils;
import com.dingzhuo.energy.common.utils.file.FileUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.config.RuoYiConfig;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.security.service.TokenService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 节能项目管理附件Controller
 * 
 * @author sys
 * @date 2020-12-08
 */
@RestController
@RequestMapping("/energy/energyAnnex")
public class EnergyProjectAnnexController extends BaseController
{
    @Autowired
    private IEnergyProjectAnnexService energyProjectAnnexService;
    @Autowired
    private TokenService tokenService;
    private static final Logger log = LoggerFactory.getLogger(EnergyProjectAnnexController.class);
    /**
     * 查询节能项目管理附件列表
     */
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:list')")
    @GetMapping("/list")
    public TableDataInfo list(EnergyProjectAnnex energyProjectAnnex)
    {
        startPage();
        List<EnergyProjectAnnex> list = energyProjectAnnexService.selectEnergyProjectAnnexList(energyProjectAnnex);
        return getDataTable(list);
    }

    /**
     * 导出节能项目管理附件列表
     */
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:export')")
    @Log(title = "节能项目管理附件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(EnergyProjectAnnex energyProjectAnnex)
    {
        List<EnergyProjectAnnex> list = energyProjectAnnexService.selectEnergyProjectAnnexList(energyProjectAnnex);
        ExcelUtil<EnergyProjectAnnex> util = new ExcelUtil<EnergyProjectAnnex>(EnergyProjectAnnex.class);
        return util.exportExcel(list, "energyAnnex");
    }

    /**
     * 获取节能项目管理附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(energyProjectAnnexService.selectEnergyProjectAnnexById(id));
    }

    /**
     * 新增节能项目管理附件
     */
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:add')")
    @Log(title = "节能项目管理附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody EnergyProjectAnnex energyProjectAnnex)
    {
        return toAjax(energyProjectAnnexService.insertEnergyProjectAnnex(energyProjectAnnex));
    }

    /**
     * 修改节能项目管理附件
     */
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:edit')")
    @Log(title = "节能项目管理附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody EnergyProjectAnnex energyProjectAnnex)
    {
        return toAjax(energyProjectAnnexService.updateEnergyProjectAnnex(energyProjectAnnex));
    }

    /**
     * 删除节能项目管理附件
     */
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:remove')")
    @Log(title = "节能项目管理附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(energyProjectAnnexService.deleteEnergyProjectAnnexByIds(ids));
    }
    /**
     * 节能计划附件上传
     */
    @Log(title = "节能计划附件上传", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:fileImport')")
    @PostMapping("/fileImport")
    public AjaxResult importData(MultipartFile file, String projectId) throws Exception
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
//            System.out.println("implementId==="+implementId);
//            System.out.println("filedir==="+uploadPathDir);
//            System.out.println("fileName==="+fileName);
//            System.out.println("fileSuffix==="+fileSuffix);
            //文件最终保存的绝对路径
            String filePath = FileUploadUtils.uploadAll(uploadPathDir, file);
//            System.out.println("上传文件地址:===>"+filePath);
            EnergyProjectAnnex energyProjectAnnex=new EnergyProjectAnnex();
            //设置主键UUID
            energyProjectAnnex.setId(IdUtils.simpleUUID());
            energyProjectAnnex.setFileName(fileName);
            energyProjectAnnex.setFilePath(filePath);
            energyProjectAnnex.setFileSuffix(fileSuffix);
            energyProjectAnnex.setProjectId(projectId);
            energyProjectAnnex.setCreateBy(loginUser.getUsername());
            if (this.energyProjectAnnexService.insertEnergyProjectAnnex(energyProjectAnnex)>0)
            {
                return AjaxResult.success("附件上传完成");
            }
        }
        return AjaxResult.success("附件上传失败");
    }
    /**
     * 通用指定文件下载请求
     *
     * @param showFileName 下载展示文件名称
    //     * @param filePath 下载文件绝对路径 带 文件真实名字及路径
     * @param delete 是否删除/
     */
    @PreAuthorize("@ss.hasPermi('energy:energyAnnex:assignDownload')")
    @PostMapping("/assignDownload")
    public void fileAssignDownload(String showFileName, String filePath, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {

        try
        {
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + FileUtils.setFileDownloadHeader(request, showFileName));
            FileUtils.writeBytes(filePath, response.getOutputStream());
//            if (delete)
//            {
//                FileUtils.deleteFile(filePath);
//            }
            log.error("下载成功", "aa");
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
}
