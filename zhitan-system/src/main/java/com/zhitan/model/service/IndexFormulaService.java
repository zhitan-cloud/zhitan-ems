package com.zhitan.model.service;


import com.zhitan.model.domain.IndexFormula;

public interface IndexFormulaService {

  void saveIndexFormula(IndexFormula indexFormula);

  IndexFormula getIndexFormula(String indexId);
}
