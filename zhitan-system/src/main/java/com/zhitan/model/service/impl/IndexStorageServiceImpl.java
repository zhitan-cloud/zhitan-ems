package com.zhitan.model.service.impl;

import com.zhitan.common.enums.CalcType;
import com.zhitan.common.enums.TimeType;
import com.zhitan.common.utils.StringUtils;
import com.zhitan.model.domain.*;
import com.zhitan.model.mapper.IndexStorageMapper;
import com.zhitan.model.service.IEnergyIndexService;
import com.zhitan.model.service.IIndexStorageService;
import com.zhitan.model.service.IndexFormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@Service
public class IndexStorageServiceImpl implements IIndexStorageService {

  @Autowired
  private IndexStorageMapper indexStorageMapper;
  @Autowired
  private IndexFormulaService indexFormulaService;
  @Autowired
  private IEnergyIndexService energyIndexService;

  @Override
  public void saveIndexStorage(String indexId, List<IndexStorage> indexStorage) {
    indexStorage.forEach(storage -> {
      storage.setIndexId(indexId);
      if (StringUtils.isEmpty(storage.getId())) {
        storage.setId(UUID.randomUUID().toString());
        indexStorageMapper.insertIndexStorage(storage);
      } else {
        indexStorageMapper.updateIndexStorage(storage);
      }

      indexStorageMapper.saveParams(storage.getId(), storage.getParamIndex());
    });
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void saveFormulaAndStorage(IndexFormula indexFormula, List<IndexStorage> indexStorage,
                                    String indexId) {
    indexFormulaService.saveIndexFormula(indexFormula);
    String calcText = indexFormula.getFormulaText();
    List<String> paramNames = new ArrayList<>();
    for (IndexFormulaParam param : indexFormula.getIndexFormulaParams()) {
        calcText = calcText
                .replace("(" + param.getParamName() + ")", "(" + String.format("'%s'", param.getParamValue()) + ")");
        paramNames.add(param.getParamValue());
    }

    List<EnergyIndex> energyIndexList = energyIndexService.getEnergyIndexByCodes(paramNames);
    List<String> paramIndex = energyIndexList.stream()
        .map(EnergyIndex::getIndexId)
        .collect(Collectors.toList());
    for (IndexStorage storage : indexStorage) {
      storage.setCalcText(calcText);
      storage.setIsPvCalc(indexFormula.getIsPvCalc());
      storage.getParamIndex().addAll(paramIndex);
    }

    saveIndexStorage(indexId, indexStorage);
  }

  @Override
  public List<IndexStorage> getIndexStorage(String indexId) {
    return indexStorageMapper.getIndexStorage(indexId);
  }

  @Override
  public List<IndexStorage> getAllCalcIndexStorage() {
    List<IndexStorage> indexStorageList = indexStorageMapper.getAllCalcIndexStorage(CalcType.CALC);
    List<IndexStorageParam> params = indexStorageMapper.getAllParameter();
    indexStorageList.forEach(storage -> {
      List<String> indexIds = params.stream()
          .filter(f -> equalsIgnoreCase(f.getStorageId(), storage.getId()))
          .map(IndexStorageParam::getIndexId)
          .collect(Collectors.toList());
      if (!indexIds.isEmpty()) {
        storage.getParamIndex().addAll(indexIds);
      }
    });

    return indexStorageList;
  }

  @Override
  public IndexStorage getIndexStorage(String indexId, TimeType timeType) {
    return indexStorageMapper.getWithTimetype(indexId, timeType);
  }

}
