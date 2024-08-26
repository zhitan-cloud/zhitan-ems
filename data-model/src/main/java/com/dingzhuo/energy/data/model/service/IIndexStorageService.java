package com.dingzhuo.energy.data.model.service;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.IndexFormula;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
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
