package com.dingzhuo.energy.data.model.service;

import java.util.List;

public interface DataAuthService {

  List<String> getUserDataAuth(String modelCode, String id);

  List<String> getRoleDataAuth(String modelCode, String id);

  void setUserDataAuth(String userOrRoleId, String modelCode, List<String> ids);

  void setRoleDataAuth(String userOrRoleId, String modelCode, List<String> ids);
}
