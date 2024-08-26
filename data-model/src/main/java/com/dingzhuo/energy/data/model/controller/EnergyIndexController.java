package com.dingzhuo.energy.data.model.controller;

import com.dingzhuo.energy.common.utils.ServletUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.security.LoginUser;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.IndexType;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.domain.EnergyIndexQuery;
import com.dingzhuo.energy.project.system.domain.SysUser;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

/**
 * 指标信息Controller
 *
 * @author fanxinfu
 * @date 2020-02-14
 */
@RestController
@RequestMapping("/basicsetting/energyindex")
public class EnergyIndexController extends BaseController {

  @Autowired
  private IEnergyIndexService energyIndexService;

  /**
   * 查询指标信息列表
   */
  @PreAuthorize("@ss.hasPermi('energyindex:energyindex:query')")
  @GetMapping("/list")
  public TableDataInfo list(EnergyIndex energyIndex) {
    startPage();
    EnergyIndexQuery query = new EnergyIndexQuery(energyIndex.getNodeId(), energyIndex.getName(),
        energyIndex.getIndexCategory(), energyIndex.getIndexType());
    List<EnergyIndex> list = energyIndexService.selectEnergyIndexList(query);
    return getDataTable(list);
  }

  /**
   * 查询指标信息列表
   */
  @GetMapping("/filter")
  public AjaxResult filter(EnergyIndexQuery query) {
    List<EnergyIndex> list = energyIndexService.selectEnergyIndexList(query);
    return AjaxResult.success(list);
  }

  /**
   * 查询指标信息列表
   */
  @PreAuthorize("@ss.hasPermi('energyindex:energyindex:query')")
  @GetMapping("/collectIndex")
  public TableDataInfo listCollectIndex(String deviceId) {
    startPage();
    List<EnergyIndex> list = energyIndexService.selectCollectIndex(deviceId);
    return getDataTable(list);
  }

  /**
   * 导出指标信息列表
   */
  @PreAuthorize("@ss.hasPermi('energyindex:energyindex:export')")
  @Log(title = "指标信息", businessType = BusinessType.EXPORT)
  @GetMapping("/export")
  public AjaxResult export(EnergyIndex energyIndex) {
    List<EnergyIndex> list = energyIndexService.selectEnergyIndexList(energyIndex);
    ExcelUtil<EnergyIndex> util = new ExcelUtil<>(EnergyIndex.class);
    return util.exportExcel(list, "energyindex");
  }

  /**
   * 获取指标信息详细信息
   */
  @PreAuthorize("@ss.hasPermi('energyindex:energyindex:query')")
  @GetMapping(value = "/{indexId}")
  public AjaxResult getInfo(@PathVariable("indexId") String indexId) {
    return AjaxResult.success(energyIndexService.selectEnergyIndexById(indexId));
  }

  /**
   * 新增指标信息
   */
  @PreAuthorize("@ss.hasPermi('energyindex:energyindex:add')")
  @Log(title = "指标信息", businessType = BusinessType.INSERT)
  @PostMapping(value = "/{nodeId}")
  public AjaxResult add(@PathVariable("nodeId") String nodeId,
      @RequestBody EnergyIndex energyIndex) {
    boolean isExist = energyIndexService.energyIndexHasExist(energyIndex.getCode());
    if (isExist) {
      return AjaxResult.error("指标编码不能重复！");
    } else {
      energyIndex.setIndexId(UUID.randomUUID().toString());
      return toAjax(energyIndexService.insertEnergyIndex(nodeId, energyIndex));
    }
  }

  /**
   * 修改指标信息
   */
  @PreAuthorize("@ss.hasPermi('energyindex:energyindex:edit')")
  @Log(title = "指标信息", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@RequestBody EnergyIndex energyIndex) {
    boolean isExist = energyIndexService
        .energyIndexHasExist(energyIndex.getIndexId(), energyIndex.getCode());
    if (isExist) {
      return AjaxResult.error("指标编码不能重复！");
    } else {
      return toAjax(energyIndexService.updateEnergyIndex(energyIndex));
    }
  }

  /**
   * 删除指标信息
   */
  @PreAuthorize("@ss.hasPermi('energyindex:energyindex:remove')")
  @Log(title = "指标信息", businessType = BusinessType.DELETE)
  @DeleteMapping("/{nodeId}/{indexIds}")
  public AjaxResult remove(@PathVariable String nodeId, @PathVariable String[] indexIds) {
    List<EnergyIndex> energyIndexList = energyIndexService.getEnergyIndexByIds(
        Arrays.asList(indexIds));
    List<String> deleteIds = energyIndexList.stream()
        .filter(f -> StringUtils.isBlank(f.getMeterId()))
        .map(EnergyIndex::getIndexId)
        .collect(Collectors.toList());
    List<String> removeLink = energyIndexList.stream()
        .filter(f -> StringUtils.isNotBlank(f.getMeterId()))
        .map(EnergyIndex::getIndexId)
        .collect(Collectors.toList());
    if (!removeLink.isEmpty()) {
      energyIndexService.removeNodeIndex(nodeId, removeLink);
    }

    if (!deleteIds.isEmpty()) {
      energyIndexService.deleteEnergyIndexByIds(nodeId, deleteIds.toArray(new String[0]));
    }
    return AjaxResult.success();
  }

  @Log(title = "增加计量器具采集点", businessType = BusinessType.INSERT)
  @PostMapping("/meterIndex/{meterId}")
  public AjaxResult addCollectIndex(@PathVariable("meterId") String meterId) {
    try {
      return energyIndexService.addMeterIndex(meterId);
    } catch (Exception ex) {
      logger.error("创建计量器具采集点失败！", ex);
      return AjaxResult.error();
    }
  }

  @GetMapping("/meterIndex/{meterId}")
  public AjaxResult getDeviceCollectIndex(@PathVariable("meterId") String meterId) {
    return AjaxResult.success(energyIndexService.getMeterIndex(meterId));
  }

  @GetMapping("/includeChildrenNode/{nodeId}")
  public AjaxResult getIndexByNodeAndChildrenNode(@PathVariable("nodeId") String nodeId) {
    try {
      return AjaxResult.success(energyIndexService.getIndexByNodeAndChildrenNode(nodeId));
    } catch (Exception ex) {
      logger.error("获取关联采集指标出错！", ex);
      return AjaxResult.error("获取关联指标出错!");
    }
  }

  @GetMapping("/includeChildrenNode/search")
  public AjaxResult searchIndexByNodeAndChildrenNode(String nodeId, String filter) {
    try {
      return AjaxResult
          .success(energyIndexService.searchIndexByNodeAndChildrenNode(nodeId, filter));
    } catch (Exception ex) {
      logger.error("获取关联采集指标出错！", ex);
      return AjaxResult.error("获取关联指标出错!");
    }
  }

  @GetMapping("/importTemplate")
  public AjaxResult importTemplate() {
    ExcelUtil<EnergyIndex> util = new ExcelUtil<>(EnergyIndex.class);
    return util.importTemplateExcel("指标数据");
  }

  @PostMapping("/importData")
  public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
    ExcelUtil<EnergyIndex> util = new ExcelUtil<>(EnergyIndex.class);
    List<EnergyIndex> energyIndexList = util.importExcel(file.getInputStream());
    return energyIndexService.importEnergyIndex(energyIndexList, updateSupport);
  }
}
