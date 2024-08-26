package com.dingzhuo.energy.project.basicSetup.controller;

import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.framework.config.RuoYiConfig;
import java.io.*;
import java.net.MalformedURLException;
import java.util.List;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.file.FileUploadUtils;
import com.dingzhuo.energy.common.utils.file.FileUtils;
import com.dingzhuo.energy.dataservice.domain.TagValue;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import com.dingzhuo.energy.dataservice.service.impl.RealtimeDatabaseServiceImpl;

import com.dingzhuo.energy.project.basicSetup.domain.SysSvgInfo;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.*;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.project.basicSetup.domain.SysEquipmentfile;
import com.dingzhuo.energy.project.basicSetup.service.ISysEquipmentfileService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;


/**
 * 组态图Controller
 *
 * @author sys
 * @date 2020-02-24
 */
@RestController
@RequestMapping("/basicSetup/equipmentfile")
public class SysEquipmentfileController extends BaseController {

  private final ISysEquipmentfileService sysEquipmentfileService;

  public SysEquipmentfileController(
      ISysEquipmentfileService sysEquipmentfileService) {
    this.sysEquipmentfileService = sysEquipmentfileService;
  }

  @PostMapping(value = "/upload")
  @Log(title = "系统图", businessType = BusinessType.IMPORT)
  public AjaxResult upload(MultipartFile file) throws IOException {
    if (!file.isEmpty()) {
      String fileSuffix = FileUploadUtils.getExtension(file);
      if (StringUtils.containsIgnoreCase(".svg,.jpg,.png,.gif", fileSuffix)) {
        //文件最终保存的绝对路径
        String filePath = FileUploadUtils.upload(RuoYiConfig.getConfigurePath(), file);
        return AjaxResult.success(filePath);
      }
      return AjaxResult.error("文件格式错误");
    }
    return AjaxResult.error("系统图上传失败");
  }

  /**
   * 修改组态图
   */
  @Log(title = "系统图", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@RequestBody SysEquipmentfile sysEquipmentfile) {
    try {
      sysEquipmentfileService.saveEquipmentFile(sysEquipmentfile);
      return AjaxResult.success();
    } catch (Exception ex) {
      return AjaxResult.error();
    }
  }

  @PutMapping("/setting/{nodeId}")
  public AjaxResult saveSetting(@PathVariable("nodeId") String nodeId,
      @RequestBody List<SysSvgInfo> svgInfo) {
    try {
      svgInfo.forEach(info -> info.setId(UUID.fastUUID().toString()));
      sysEquipmentfileService.saveSettingInfo(nodeId, svgInfo);
      return AjaxResult.success("保存成功！");
    } catch (Exception ex) {
      return AjaxResult.error("保存失败！");
    }
  }

  @GetMapping("/configure/{nodeId}")
  public AjaxResult getConfigure(@PathVariable("nodeId") String nodeId) {
    try {
      SysEquipmentfile sysEquipmentfile = sysEquipmentfileService.getConfigure(nodeId);
      return AjaxResult.success(sysEquipmentfile);
    } catch (Exception ex) {
      return AjaxResult.error("保存失败！");
    }
  }
}
