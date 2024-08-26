package com.dingzhuo.energy.data.model.controller;

import com.alibaba.fastjson.JSONObject;
import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import com.dingzhuo.energy.data.model.domain.CalcType;
import com.dingzhuo.energy.data.model.domain.IndexFormula;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
import com.dingzhuo.energy.data.model.service.IIndexStorageService;
import com.dingzhuo.energy.data.model.service.IndexFormulaService;
import com.greenpineyu.fel.FelEngine;
import com.greenpineyu.fel.FelEngineImpl;
import com.greenpineyu.fel.parser.FelNode;
import com.greenpineyu.fel.parser.VarAstNode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fanxinfu
 */
@RestController
@RequestMapping("/basicsetting/indexStorage")
public class IndexStorageController extends BaseController {

  private final IIndexStorageService indexStorageService;

  private final IndexFormulaService indexFormulaService;

  @Autowired
  public IndexStorageController(
      IIndexStorageService indexStorageService, IndexFormulaService indexFormulaService) {
    this.indexStorageService = indexStorageService;
    this.indexFormulaService = indexFormulaService;
  }

  @GetMapping("/{indexId}")
  public AjaxResult getIndexStorage(@PathVariable String indexId) {
    JSONObject result = new JSONObject();
    IndexFormula indexFormula = indexFormulaService.getIndexFormula(indexId);
    List<IndexStorage> indexStorageList = indexStorageService.getIndexStorage(indexId);
    List<IndexStorage> defaultStorageList = getDefaultIndexStorage();
    Map<TimeType, IndexStorage> map = indexStorageList.stream()
        .collect(Collectors.toMap(IndexStorage::getTimeType, storage -> storage));
    List<IndexStorage> storageList;
    if (!indexStorageList.isEmpty()) {
      storageList = new ArrayList<>();
      defaultStorageList.forEach(storage -> {
        storageList.add(map.get(storage.getTimeType()));
      });
    } else {
      storageList = defaultStorageList;
    }

    result.put("indexFormula", indexFormula);
    result.put("indexStorage", storageList);
    return AjaxResult.success(result);
  }

  @PostMapping("/{indexId}")
  public AjaxResult saveIndexStorage(@RequestBody JSONObject param, @PathVariable String indexId) {
    try {
      IndexFormula indexFormula = param.getObject("indexFormula", IndexFormula.class);
      List<IndexStorage> indexStorage =
          param.getJSONArray("indexStorage").toJavaList(IndexStorage.class);
      indexFormula.setIndexId(indexId);
      indexStorageService.saveFormulaAndStorage(indexFormula, indexStorage, indexId);
    } catch (Exception ex) {
      logger.error("", ex);
      return AjaxResult.error();
    }

    return AjaxResult.success();
  }


  @PostMapping("/parseFormula")
  public AjaxResult parseFormula(@RequestBody JSONObject formulaText) {
    String calcText = formulaText.getString("calcText");
    FelEngine e = new FelEngineImpl();
    Set<String> params = new HashSet<>();
    try {
      FelNode felNode = e.parse(calcText);
      buildParam(felNode, params);
    } catch (Exception ex) {
      logger.error("公式解析出错！");
    }
    return AjaxResult.success(params);
  }

  @GetMapping("/calcPeriod")
  public AjaxResult getIndexStorage() {
    List<IndexStorage> calcPeriods = getDefaultIndexStorage();
    return AjaxResult.success(calcPeriods);
  }

  @NotNull
  private List<IndexStorage> getDefaultIndexStorage() {
    List<IndexStorage> calcPeriods = new ArrayList<>();
    getPeriod(calcPeriods, TimeType.HOUR);
    getPeriod(calcPeriods, TimeType.SCHEDULING);
    getPeriod(calcPeriods, TimeType.DAY);
    getPeriod(calcPeriods, TimeType.MONTH);
    getPeriod(calcPeriods, TimeType.YEAR);
    return calcPeriods;
  }

  private void getPeriod(List<IndexStorage> calcPeriods, TimeType timeType) {
    IndexStorage hour = new IndexStorage();
    hour.setTimeType(timeType);
    hour.setCalcType(CalcType.CALC);
    calcPeriods.add(hour);
  }

  private void buildParam(FelNode felNode, Set<String> params) {
    if (felNode.getChildren() == null) {
      if (isConfigParam(felNode.getText())) {
        params.add(felNode.getText());
      }
    } else {
      for (FelNode node : felNode.getChildren()) {
        if (node.getChildren() == null) {
          if (node instanceof VarAstNode && isConfigParam(node.getText())) {
            params.add(node.getText());
          }
        } else {
          buildParam(node, params);
        }
      }
    }
  }

  private boolean isConfigParam(String param) {
    if (param.startsWith("'") && param.endsWith("'")) {
      return false;
    }

    if (param.startsWith("$")) {
      return true;
    }

    return true;
  }
}
