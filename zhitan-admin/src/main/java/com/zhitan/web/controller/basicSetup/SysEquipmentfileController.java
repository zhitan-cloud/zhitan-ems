package com.zhitan.web.controller.basicSetup;

import com.zhitan.basicSetup.service.ISysEquipmentfileService;
import com.zhitan.common.annotation.Log;
import com.zhitan.common.config.BaseConfig;
import com.zhitan.common.core.controller.BaseController;
import com.zhitan.common.core.domain.AjaxResult;
import com.zhitan.common.enums.BusinessType;
import com.zhitan.common.utils.file.FileUploadUtils;
import com.zhitan.common.utils.uuid.UUID;
import com.zhitan.realtimedata.domain.SysEquipmentFile;
import com.zhitan.realtimedata.domain.SysSvgInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


/**
 * 组态图Controller
 *
 * @author sys
 * @date 2020-02-24
 */
@Slf4j
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
        String filePath = FileUploadUtils.upload(BaseConfig.getConfigurePath(), file);
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
  public AjaxResult edit(@RequestBody SysEquipmentFile sysEquipmentfile) {
    try {
      sysEquipmentfileService.saveEquipmentFile(sysEquipmentfile);
      return AjaxResult.success();
    } catch (Exception ex) {
      log.error("组态图更新失败", ex);
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
      SysEquipmentFile sysEquipmentfile = sysEquipmentfileService.getConfigure(nodeId);
      return AjaxResult.success(sysEquipmentfile);
    } catch (Exception ex) {
      return AjaxResult.error("查询失败！");
    }
  }
}
