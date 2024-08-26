package com.dingzhuo.energy.data.model.mapper;

import com.dingzhuo.energy.data.model.domain.IndexFormula;
import com.dingzhuo.energy.data.model.domain.IndexFormulaParam;
import java.util.List;

public interface IndexFormulaMapper {

  void insertIndexFormula(IndexFormula indexFormula);

  void updateIndexFormula(IndexFormula indexFormula);

  void saveIndexFormulaParam(String indexId, List<IndexFormulaParam> indexFormulaParams);

  IndexFormula getFormula(String indexId);

  List<IndexFormulaParam> getFormulaParam(String indexId);
}
