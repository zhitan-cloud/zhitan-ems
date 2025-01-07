package com.zhitan.model.mapper;

import com.zhitan.model.domain.IndexFormula;
import com.zhitan.model.domain.IndexFormulaParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IndexFormulaMapper {

  void insertIndexFormula(IndexFormula indexFormula);

  void updateIndexFormula(IndexFormula indexFormula);

  void saveIndexFormulaParam(@Param("indexId") String indexId, @Param("indexFormulaParams") List<IndexFormulaParam> indexFormulaParams);

  IndexFormula getFormula(String indexId);

  List<IndexFormulaParam> getFormulaParam(String indexId);
}
