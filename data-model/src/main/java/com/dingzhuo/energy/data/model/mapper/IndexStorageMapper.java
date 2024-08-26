package com.dingzhuo.energy.data.model.mapper;

import com.dingzhuo.energy.common.utils.time.TimeType;
import com.dingzhuo.energy.data.model.domain.CalcType;
import com.dingzhuo.energy.data.model.domain.IndexStorage;
import com.dingzhuo.energy.data.model.domain.IndexStorageParam;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IndexStorageMapper {

  void insertIndexStorage(IndexStorage storage);

  void updateIndexStorage(IndexStorage storage);

  List<IndexStorage> getIndexStorage(String indexId);

  void saveParams(String storageId, List<String> parameterIds);

  List<IndexStorageParam> getAllParameter();

  List<IndexStorage> getAllCalcIndexStorage(CalcType calc);

  IndexStorage getWithTimetype(@Param("indexId") String indexId, @Param("timeType") TimeType timeType);
}
