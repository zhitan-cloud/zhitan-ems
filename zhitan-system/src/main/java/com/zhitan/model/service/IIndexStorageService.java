package com.zhitan.model.service;


import com.zhitan.common.enums.TimeType;
import com.zhitan.model.domain.IndexFormula;
import com.zhitan.model.domain.IndexStorage;

import java.util.List;

/**
 * @author fanxinfu
 */
public interface IIndexStorageService {

  void saveIndexStorage(String indexId, List<IndexStorage> indexStorage);

    void saveFormulaAndStorage(IndexFormula indexFormula, List<IndexStorage> indexStorage,
                               String indexId);

  List<IndexStorage> getIndexStorage(String indexId);

  List<IndexStorage> getAllCalcIndexStorage();

  IndexStorage getIndexStorage(String indexId, TimeType timeType);
}
