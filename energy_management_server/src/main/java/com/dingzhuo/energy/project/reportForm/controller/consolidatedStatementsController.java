package com.dingzhuo.energy.project.reportForm.controller;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.time.TimeManager;
import com.dingzhuo.energy.framework.config.RuoYiConfig;
import com.dingzhuo.energy.project.reportForm.domain.JtZhnhExcel;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
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
import com.dingzhuo.energy.project.reportForm.domain.consolidatedStatements;
import com.dingzhuo.energy.project.reportForm.service.IconsolidatedStatementsService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;

import javax.servlet.ServletContext;

/**
 * 焦化工序综合报表Controller
 * 
 * @author sys
 * @date 2021-01-15
 */
@RestController
@RequestMapping("/reportForm/consolidatedStatements")
public class consolidatedStatementsController extends BaseController
{
    @Autowired
    private IconsolidatedStatementsService consolidatedStatementsService;

    /**
     * 查询焦化工序综合报表列表
     */
    @PreAuthorize("@ss.hasPermi('reportForm:consolidatedStatements:list')")
    @GetMapping("/list")
    public AjaxResult list(consolidatedStatements consolidatedStatements)
    {
        List<consolidatedStatements> list = consolidatedStatementsService.selectconsolidatedStatementsList(consolidatedStatements);
        return AjaxResult.success(list);
    }

    /**
     * 获取焦化工序综合报表详细信息
     */
    @PreAuthorize("@ss.hasPermi('reportForm:consolidatedStatements:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return AjaxResult.success(consolidatedStatementsService.selectconsolidatedStatementsById(id));
    }

    /**
     * 新增焦化工序综合报表
     */
    @PreAuthorize("@ss.hasPermi('reportForm:consolidatedStatements:add')")
    @Log(title = "焦化工序综合报表", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody consolidatedStatements consolidatedStatements) throws ParseException {
        consolidatedStatements.setId(UUID.randomUUID().toString());
        consolidatedStatements.setCreateBy(SecurityUtils.getUsername());
        return toAjax(consolidatedStatementsService.insertconsolidatedStatements(consolidatedStatements));
    }

    /**
     * 修改焦化工序综合报表
     */
    @PreAuthorize("@ss.hasPermi('reportForm:consolidatedStatements:edit')")
    @Log(title = "焦化工序综合报表", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody consolidatedStatements consolidatedStatements)
    {
        return toAjax(consolidatedStatementsService.updateconsolidatedStatements(consolidatedStatements));
    }

    /**
     * 删除焦化工序综合报表
     */
    @PreAuthorize("@ss.hasPermi('reportForm:consolidatedStatements:remove')")
    @Log(title = "焦化工序综合报表", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids)
    {
        return toAjax(consolidatedStatementsService.deleteconsolidatedStatementsByIds(ids));
    }
    /**
     * 导出焦化工序综合报表列表
     */
    @PreAuthorize("@ss.hasPermi('reportForm:consolidatedStatements:export')")
    @Log(title = "焦化工序综合报表", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(consolidatedStatements consolidatedStatements)
    {
        List<consolidatedStatements> list = consolidatedStatementsService.selectconsolidatedStatementsList(consolidatedStatements);
        /*ExcelUtil<consolidatedStatements> util = new ExcelUtil<consolidatedStatements>(consolidatedStatements.class);
        return util.exportExcel(list, "consolidatedStatements");*/
        String downLoadFilePaht = null;
        if(list.size()>0){
            JtZhnhExcel excelout=new JtZhnhExcel();
            downLoadFilePaht=excelout.writeExcel(RuoYiConfig.getDownloadPath() + "jhnhrep.xlsx",list);
        }
        return AjaxResult.success(downLoadFilePaht);
    }
}
