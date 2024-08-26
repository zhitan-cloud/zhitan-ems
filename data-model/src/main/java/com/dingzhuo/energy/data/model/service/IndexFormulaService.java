package com.dingzhuo.energy.data.model.service;

import com.dingzhuo.energy.data.model.domain.IndexFormula;

public interface IndexFormulaService {

  void saveIndexFormula(IndexFormula indexFormula);

  IndexFormula getIndexFormula(String indexId);
}
