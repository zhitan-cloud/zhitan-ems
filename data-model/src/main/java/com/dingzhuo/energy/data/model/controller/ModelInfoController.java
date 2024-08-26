package com.dingzhuo.energy.data.model.controller;

import com.dingzhuo.energy.common.utils.StringUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.ModelInfo;
import com.dingzhuo.energy.data.model.domain.vo.PointDataVO;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelInfoService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模型Controller
 *
 * @author fanxinfu
 * @date 2020-02-17
 */
@RestController
@RequestMapping("/basicsetting/model")
public class ModelInfoController extends BaseController {
  @Autowired
  private IModelInfoService modelInfoService;

  @Autowired
  private IEnergyIndexService energyIndexService;

  /**
   * 查询模型列表
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:model:list')")
  @GetMapping("/list")
  public AjaxResult list(ModelInfo modelInfo) {
    List<ModelInfo> list = modelInfoService.selectModelInfoList(modelInfo);
    return AjaxResult.success(list);
  }

  /**
   * 导出模型列表
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:model:export')")
  @Log(title = "模型", businessType = BusinessType.EXPORT)
  @GetMapping("/export")
  public AjaxResult export(ModelInfo modelInfo) {
    List<ModelInfo> list = modelInfoService.selectModelInfoList(modelInfo);
    ExcelUtil<ModelInfo> util = new ExcelUtil<ModelInfo>(ModelInfo.class);
    return util.exportExcel(list, "model");
  }

  /**
   * 获取模型详细信息
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:model:query')")
  @GetMapping(value = "/{modelCode}")
  public AjaxResult getInfo(@PathVariable("modelCode") String modelCode) {
    return AjaxResult.success(modelInfoService.selectModelInfoById(modelCode));
  }

  /**
   * 新增模型
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:model:add')")
  @Log(title = "模型", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@RequestBody ModelInfo modelInfo) {
    return toAjax(modelInfoService.insertModelInfo(modelInfo));
  }

  /**
   * 修改模型
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:model:edit')")
  @Log(title = "模型", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@RequestBody ModelInfo modelInfo) {
    return toAjax(modelInfoService.updateModelInfo(modelInfo));
  }

  /**
   * 删除模型
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:model:remove')")
  @Log(title = "模型", businessType = BusinessType.DELETE)
  @DeleteMapping("/{modelCode}")
  public AjaxResult remove(@PathVariable String modelCode) {
    boolean hasConfig = energyIndexService.modelHasConfig(modelCode);
    if (hasConfig) {
      return AjaxResult.error("该模型已经配置节点或指标，不能删除！");
    }

    return toAjax(modelInfoService.deleteModelInfoByCode(modelCode));
  }

  /**
   * 根据模型id查询对应点位信息
   *
   * @param modelId 查询模型id
   * @return
   */
  @ApiOperation("根据模型id查询对应点位信息")
  @GetMapping("/getEnergyIndexByModelId")
  public AjaxResult listEnergyIndexByModelId(String modelId) {
    if (StringUtils.isEmpty(modelId)) {
      return AjaxResult.error("未找到查询模型信息");
    }
    List<PointDataVO> voList = modelInfoService.listEnergyIndexByModelId(modelId);
    return AjaxResult.success(voList);
  }
}
