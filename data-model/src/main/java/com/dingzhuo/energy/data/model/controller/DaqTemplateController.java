package com.dingzhuo.energy.data.model.controller;

import java.util.List;
import java.util.UUID;

import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.data.model.domain.DaqTemplate;
import com.dingzhuo.energy.data.model.service.IDaqTemplateService;
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

/**
 * 采集参数模板Controller
 *
 * @author ruoyi
 * @date 2020-02-08
 */
@RestController
@RequestMapping("/system/template")
public class DaqTemplateController extends BaseController {
  @Autowired
  private IDaqTemplateService daqTemplateService;

  /**
   * 查询采集参数模板列表
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:template:query')")
  @GetMapping("/list")
  public TableDataInfo list(DaqTemplate daqTemplate) {
    startPage();
    List<DaqTemplate> list = daqTemplateService.selectDaqTemplateList(daqTemplate);
    return getDataTable(list);
  }

  /**
   * 导出采集参数模板列表
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:template:query')")
  @Log(title = "采集参数模板" , businessType = BusinessType.EXPORT)
  @GetMapping("/export")
  public AjaxResult export(DaqTemplate daqTemplate) {
    List<DaqTemplate> list = daqTemplateService.selectDaqTemplateList(daqTemplate);
    ExcelUtil<DaqTemplate> util = new ExcelUtil<DaqTemplate>(DaqTemplate.class);
    return util.exportExcel(list, "template");
  }

  /**
   * 获取采集参数模板详细信息
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:template:query')")
  @GetMapping(value = "/{id}")
  public AjaxResult getInfo(@PathVariable("id") String id) {
    return AjaxResult.success(daqTemplateService.selectDaqTemplateById(id));
  }

  /**
   * 新增采集参数模板
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:template:add')")
  @Log(title = "采集参数模板" , businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@RequestBody DaqTemplate daqTemplate) {
    daqTemplate.setId(UUID.randomUUID().toString());
    boolean isExist = daqTemplateService.dapHasExist(daqTemplate.getCode(), daqTemplate.getDeviceType());
    if (isExist) {
      return AjaxResult.error("相同设备类型下的参数编码不能重复！");
    } else {
      return toAjax(daqTemplateService.insertDaqTemplate(daqTemplate));
    }
  }

  /**
   * 修改采集参数模板
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:template:edit')")
  @Log(title = "采集参数模板" , businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@RequestBody DaqTemplate daqTemplate) {
    boolean isExist = daqTemplateService.dapHasExist(daqTemplate);
    if (isExist) {
      return AjaxResult.error("相同设备类型下的参数编码不能重复！");
    } else {
      return toAjax(daqTemplateService.updateDaqTemplate(daqTemplate));
    }
  }

  /**
   * 删除采集参数模板
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:template:remove')")
  @Log(title = "采集参数模板" , businessType = BusinessType.DELETE)
  @DeleteMapping("/{ids}")
  public AjaxResult remove(@PathVariable String[] ids) {
    return toAjax(daqTemplateService.deleteDaqTemplateByIds(ids));
  }
}
