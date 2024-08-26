package com.dingzhuo.energy.data.model.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.common.core.lang.UUID;
import com.dingzhuo.energy.common.utils.SecurityUtils;
import com.dingzhuo.energy.common.utils.poi.ExcelUtil;
import com.dingzhuo.energy.data.model.domain.EnergyIndex;
import com.dingzhuo.energy.data.model.domain.ModelNode;
import com.dingzhuo.energy.data.model.service.IEnergyIndexService;
import com.dingzhuo.energy.data.model.service.IModelNodeService;
import com.dingzhuo.energy.framework.aspectj.lang.annotation.Log;
import com.dingzhuo.energy.framework.aspectj.lang.enums.BusinessType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.framework.web.page.TableDataInfo;
import com.dingzhuo.energy.project.system.service.ISysRoleService;
import com.dingzhuo.energy.project.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模型节点Controller
 *
 * @author fanxinfu
 * @date 2020-02-10
 */
@RestController
@RequestMapping("/basicsetting/modelnode")
@Api(value = "模型节点",tags = {"模型节点"})
public class ModelNodeController extends BaseController {

  private final IModelNodeService modelNodeService;


  @Autowired
  public ModelNodeController(
      IModelNodeService modelNodeService, IEnergyIndexService energyIndexService,
      ISysUserService sysUserService,
      ISysRoleService roleService) {
    this.modelNodeService = modelNodeService;
  }

  /**
   * 查询模型节点列表
   */
  @ApiOperation(value = "查询模型节点列表")
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:query')")
  @GetMapping("/list")
  public TableDataInfo list(ModelNode modelNode) {
    startPage();
    List<ModelNode> list = modelNodeService.selectModelNodeList(modelNode);
    return getDataTable(list);
  }

  /**
   * 查询模型节点列表
   */
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:list')")
  @GetMapping("/treelist")
  @ApiOperation(value = "查询模型节点树状列表")
  public AjaxResult treeList(String modelCode) {
    List<ModelNode> list = modelNodeService.getModelNodeByModelCode(modelCode);
    return AjaxResult.success(modelNodeService.buildModelNodeTree(list));
  }

  /**
   * 查询模型节点列表
   */
  @GetMapping("/treelist/withAuth")
  @ApiOperation(value = "查询模型节点列表")
  public AjaxResult treeListWithAuth(String modelCode) {
    Long userId = SecurityUtils.getUserId();
    List<ModelNode> list = modelNodeService.getModelNodeByModelCodeWithAuth(modelCode, userId);
    return AjaxResult.success(modelNodeService.buildModelNodeTree(list));
  }

  /**
   * 导出模型节点列表
   */
  @ApiOperation(value = "导出模型节点列表")
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:export')")
  @Log(title = "模型节点", businessType = BusinessType.EXPORT)
  @GetMapping("/export")
  public AjaxResult export(ModelNode modelNode) {
    List<ModelNode> list = modelNodeService.selectModelNodeList(modelNode);
    ExcelUtil<ModelNode> util = new ExcelUtil<ModelNode>(ModelNode.class);
    return util.exportExcel(list, "modelNode");
  }

  /**
   * 获取模型节点详细信息
   */
  @ApiOperation(value = "获取模型节点详细信息")
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:query')")
  @GetMapping(value = "/{nodeId}")
  public AjaxResult getInfo(@PathVariable("nodeId") String nodeId) {
    return AjaxResult.success(modelNodeService.selectModelNodeById(nodeId));
  }

  @GetMapping(value = "/hasEnergyIndex")
  @ApiOperation(value = "获取模型节点详细信息")
  public AjaxResult hasEnergyIndex(String nodeId) {
    return AjaxResult.success(modelNodeService.hasEnergyIndex(nodeId));
  }

  /**
   * 新增模型节点
   */
  @ApiOperation(value = "新增模型节点")
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:add')")
  @Log(title = "模型节点", businessType = BusinessType.INSERT)
  @PostMapping
  public AjaxResult add(@RequestBody ModelNode modelNode) {
    boolean isExist = modelNodeService
        .modelNodeHasExist(modelNode.getCode(), modelNode.getModelCode());
    if (isExist) {
      return AjaxResult.error("模型节点编码不能重复！");
    } else {
      modelNode.setNodeId(UUID.randomUUID().toString());
      modelNodeService.insertModelNode(modelNode);
      return AjaxResult.success(modelNode);
    }
  }

  /**
   * 修改模型节点
   */
  @ApiOperation(value = "修改模型节点")
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "模型节点", businessType = BusinessType.UPDATE)
  @PutMapping
  public AjaxResult edit(@RequestBody ModelNode modelNode) {
    boolean isExist = modelNodeService.modelNodeHasExist(modelNode);
    if (isExist) {
      return AjaxResult.error("模型节点编码不能重复！");
    } else {
      return toAjax(modelNodeService.updateModelNode(modelNode));
    }
  }

  /**
   * 修改模型节点
   */
  @ApiOperation(value = "模型节点排序")
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "模型节点", businessType = BusinessType.UPDATE)
  @PutMapping("/order")
  public AjaxResult order(@RequestBody JSONObject param) {
    String nodeId = param.getString("nodeId");
    String parentId = param.getString("parentId");
    String[] children = param.getJSONArray("children").toArray(new String[0]);
    modelNodeService.updateOrder(nodeId, parentId, children);
    return AjaxResult.success();
  }

  /**
   * 删除模型节点
   */
  @ApiOperation(value = "删除模型节点")
  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:remove')")
  @Log(title = "模型节点", businessType = BusinessType.DELETE)
  @DeleteMapping("/{nodeIds}")
  public AjaxResult remove(@PathVariable String[] nodeIds) {
    return toAjax(modelNodeService.deleteModelNodeByIds(nodeIds));
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联设备", businessType = BusinessType.UPDATE)
  @GetMapping("/device/{nodeId}")
  @ApiOperation(value = "设置模型节点关联设备")
  public AjaxResult getSettingDevice(@PathVariable("nodeId") String nodeId) {
    try {
      return AjaxResult.success(modelNodeService.getSettingDevice(nodeId));
    } catch (Exception ex) {
      logger.error("获取关联设备出错！", ex);
      return AjaxResult.error("获取关联设备出错!");
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联设备", businessType = BusinessType.UPDATE)
  @PostMapping("/device/{nodeId}")
  @ApiOperation(value = "设置模型节点关联设备")
  public AjaxResult setDevice(@PathVariable("nodeId") String nodeId,
      @RequestBody String[] deviceIds) {
    try {
      modelNodeService.setDevice(nodeId, deviceIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("设置关联设备出错！", ex);
      return AjaxResult.error();
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联设备", businessType = BusinessType.UPDATE)
  @DeleteMapping("/device/{nodeId}")
  @ApiOperation(value = "删除模型节点关联设备")
  public AjaxResult delDevice(@PathVariable("nodeId") String nodeId,
      @RequestBody String[] deviceIds) {
    try {
      modelNodeService.delDevice(nodeId, deviceIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("删除关联设备出错！", ex);
      return AjaxResult.error();
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联能源", businessType = BusinessType.UPDATE)
  @GetMapping("/energy/{nodeId}")
  @ApiOperation(value = "获取模型节点关联能源")
  public AjaxResult getSettingEnergy(@PathVariable("nodeId") String nodeId) {
    try {
      return AjaxResult.success(modelNodeService.getSettingEnergy(nodeId));
    } catch (Exception ex) {
      logger.error("获取关联能源出错！", ex);
      return AjaxResult.error("获取关联能源出错!");
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联能源", businessType = BusinessType.UPDATE)
  @PostMapping("/energy/{nodeId}")
  @ApiOperation(value = "设置模型节点关联能源")
  public AjaxResult setEnergy(@PathVariable("nodeId") String nodeId,
      @RequestBody Integer[] energyIds) {
    try {
      modelNodeService.setEnergy(nodeId, energyIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("设置关联能源出错！", ex);
      return AjaxResult.error();
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联能源", businessType = BusinessType.UPDATE)
  @DeleteMapping("/energy/{nodeId}")
  @ApiOperation(value = "删除模型节点关联能源")
  public AjaxResult delEnergy(@PathVariable("nodeId") String nodeId,
      @RequestBody Integer[] energyIds) {
    try {
      modelNodeService.delEnergy(nodeId, energyIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("删除关联能源出错！", ex);
      return AjaxResult.error();
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联产品", businessType = BusinessType.UPDATE)
  @GetMapping("/product/{nodeId}")
  @ApiOperation(value = "获取模型节点关联产品")
  public AjaxResult getSettingProduct(@PathVariable("nodeId") String nodeId) {
    try {
      return AjaxResult.success(modelNodeService.getSettingProduct(nodeId));
    } catch (Exception ex) {
      logger.error("获取关联产品出错！", ex);
      return AjaxResult.error("获取关联产品出错!");
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联产品", businessType = BusinessType.UPDATE)
  @PostMapping("/product/{nodeId}")
  @ApiOperation(value = "设置模型节点关联产品")
  public AjaxResult setProduct(@PathVariable("nodeId") String nodeId,
      @RequestBody Integer[] productIds) {
    try {
      modelNodeService.setProduct(nodeId, productIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("设置关联产品出错！", ex);
      return AjaxResult.error();
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联产品", businessType = BusinessType.UPDATE)
  @DeleteMapping("/product/{nodeId}")
  @ApiOperation(value = "删除模型节点关联产品")
  public AjaxResult delProduct(@PathVariable("nodeId") String nodeId,
      @RequestBody Integer[] productIds) {
    try {
      modelNodeService.delProduct(nodeId, productIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("删除关联产品出错！", ex);
      return AjaxResult.error();
    }
  }

  @GetMapping("/energyIndex/{nodeId}")
  @ApiOperation(value = "获取模型节点关联指标")
  public AjaxResult getSettingIndex(@PathVariable("nodeId") String nodeId) {
    try {
      return AjaxResult.success(modelNodeService.getSettingIndex(nodeId));
    } catch (Exception ex) {
      logger.error("获取关联采集指标出错！", ex);
      return AjaxResult.error("获取关联指标出错!");
    }
  }

  @GetMapping("/energyIndex/{indexType}/{nodeId}")
  @ApiOperation(value = "获取模型节点关联指标")
  public AjaxResult getSettingIndex(@PathVariable("indexType") String indexType,
      @PathVariable("nodeId") String nodeId) {
    try {
      return AjaxResult.success(modelNodeService.getSettingIndex(indexType, nodeId));
    } catch (Exception ex) {
      logger.error("获取关联采集指标出错！", ex);
      return AjaxResult.error("获取关联指标出错!");
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "设置模型节点关联采集指标", businessType = BusinessType.UPDATE)
  @PostMapping("/energyIndex/{nodeId}/{indexType}")
  @ApiOperation(value = "设置模型节点关联指标")
  public AjaxResult setSettingIndex(@PathVariable("nodeId") String nodeId,
      @PathVariable("indexType") String indexType,
      @RequestBody String[] indexIds) {
    try {
      modelNodeService.setIndex(nodeId, indexType, indexIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("设置关联采集指标出错！", ex);
      return AjaxResult.error();
    }
  }

  @PreAuthorize("@ss.hasPermi('basicsetting:modelNode:edit')")
  @Log(title = "删除模型节点关联采集指标", businessType = BusinessType.UPDATE)
  @DeleteMapping("/energyIndex/{nodeId}")
  @ApiOperation(value = "删除模型节点关联指标")
  public AjaxResult delSettingIndex(@PathVariable("nodeId") String nodeId,
                                    @RequestBody String[] indexIds) {
    try {
      modelNodeService.delIndex(nodeId, indexIds);
      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("删除关联采集指标出错！", ex);
      return AjaxResult.error();
    }
  }

  @Log(title = "根据nodeId查询点位信息", businessType = BusinessType.UPDATE)
  @GetMapping("/getIndexInforByNodeId")
  @ApiOperation(value = "根据nodeId查询点位信息")
  public AjaxResult getIndexInforByNodeId(String nodeId) {
    try {
      List<EnergyIndex> settingIndex = modelNodeService.getSettingIndex(nodeId);


      return AjaxResult.success();
    } catch (Exception ex) {
      logger.error("删除关联采集指标出错！", ex);
      return AjaxResult.error();
    }
  }

}
