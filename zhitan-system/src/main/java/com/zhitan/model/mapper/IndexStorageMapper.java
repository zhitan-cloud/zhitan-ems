package com.zhitan.model.mapper;

import com.zhitan.common.enums.CalcType;
import com.zhitan.common.enums.TimeType;
import com.zhitan.model.domain.IndexStorage;
import com.zhitan.model.domain.IndexStorageParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IndexStorageMapper {

  void insertIndexStorage(IndexStorage storage);

  void updateIndexStorage(IndexStorage storage);

  List<IndexStorage> getIndexStorage(String indexId);

  void saveParams(@Param("storageId")String storageId, @Param("parameterIds")List<String> parameterIds);

  List<IndexStorageParam> getAllParameter();

  List<IndexStorage> getAllCalcIndexStorage(CalcType calc);

  IndexStorage getWithTimetype(@Param("indexId") String indexId, @Param("timeType") TimeType timeType);
}
