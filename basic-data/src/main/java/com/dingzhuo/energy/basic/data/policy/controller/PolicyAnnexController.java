package com.dingzhuo.energy.basic.data.policy.controller;

import com.dingzhuo.energy.basic.data.policy.domain.PolicyAnnex;
import com.dingzhuo.energy.basic.data.policy.service.IPolicyAnnexService;
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
 * 政策法规附件Controller
 * 
 * @author liuli
 * @date 2020-04-24
 */
@RestController
@RequestMapping("/policy/annex")
public class PolicyAnnexController extends BaseController
{
    @Autowired
    private IPolicyAnnexService policyAnnexService;
    @Autowired
    private TokenService tokenService;
    private static final Logger log = LoggerFactory.getLogger(PolicyAnnexController.class);
    /**
     * 查询政策法规附件列表
     */
    @PreAuthorize("@ss.hasPermi('policy:annex:list')")
    @GetMapping("/list")
    public TableDataInfo list(PolicyAnnex policyAnnex)
    {
        startPage();
        List<PolicyAnnex> list = policyAnnexService.selectPolicyAnnexList(policyAnnex);
        return getDataTable(list);
    }

    /**
     * 导出政策法规附件列表
     */
    @PreAuthorize("@ss.hasPermi('policy:annex:export')")
    @Log(title = "政策法规附件", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(PolicyAnnex policyAnnex)
    {
        List<PolicyAnnex> list = policyAnnexService.selectPolicyAnnexList(policyAnnex);
        ExcelUtil<PolicyAnnex> util = new ExcelUtil<PolicyAnnex>(PolicyAnnex.class);
        return util.exportExcel(list, "annex");
    }

    /**
     * 获取政策法规附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('policy:annex:query')")
    @GetMapping(value = "/{filePath}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(policyAnnexService.selectPolicyAnnexById(id));
    }

    /**
     * 新增政策法规附件
     */
    @PreAuthorize("@ss.hasPermi('policy:annex:add')")
    @Log(title = "政策法规附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PolicyAnnex policyAnnex)
    {
        return toAjax(policyAnnexService.insertPolicyAnnex(policyAnnex));
    }

    /**
     * 修改政策法规附件
     */
    @PreAuthorize("@ss.hasPermi('policy:annex:edit')")
    @Log(title = "政策法规附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PolicyAnnex policyAnnex)
    {
        return toAjax(policyAnnexService.updatePolicyAnnex(policyAnnex));
    }

    /**
     * 删除政策法规附件
     */
    @PreAuthorize("@ss.hasPermi('policy:annex:remove')")
    @Log(title = "政策法规附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(policyAnnexService.deletePolicyAnnexByIds(ids));
    }

    @Log(title = "政策法规附件上传", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('policy:annex:fileImport')")
    @PostMapping("/fileImport")
    public AjaxResult importData(MultipartFile file, String regulations_id) throws Exception
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
            PolicyAnnex policyAnnex=new PolicyAnnex();
            //设置主键UUID
            policyAnnex.setId(IdUtils.simpleUUID());
            policyAnnex.setFileName(fileName);
            policyAnnex.setFilePath(filePath);
            policyAnnex.setFileSuffix(fileSuffix);
            policyAnnex.setRegulationsId(regulations_id);
            policyAnnex.setCreateBy(loginUser.getUsername());
            if (this.policyAnnexService.insertPolicyAnnex(policyAnnex)>0)
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
    @PreAuthorize("@ss.hasPermi('policy:annex:assignDownload')")
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
