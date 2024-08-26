package com.dingzhuo.energy.data.model.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DataAuthMapper {

  List<String> getUserDataAuth(@Param("modelCode") String modelCode, @Param("id") String id);

  List<String> getRoleDataAuth(@Param("modelCode") String modelCode, @Param("id") String id);

  void setUserDataAuth(@Param("id") String id, @Param("modelCode") String modelCode,
      @Param("ids") List<String> ids);

  void setRoleDataAuth(@Param("id") String id, @Param("modelCode") String modelCode,
      @Param("ids") List<String> ids);
}
