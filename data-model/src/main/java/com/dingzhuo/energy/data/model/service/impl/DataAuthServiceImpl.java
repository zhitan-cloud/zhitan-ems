package com.dingzhuo.energy.data.model.service.impl;

import com.dingzhuo.energy.data.model.mapper.DataAuthMapper;
import com.dingzhuo.energy.data.model.service.DataAuthService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataAuthServiceImpl implements DataAuthService {

  @Autowired
  private DataAuthMapper dataAuthMapper;

  @Override
  public List<String> getUserDataAuth(String modelCode, String id) {
    return dataAuthMapper.getUserDataAuth(modelCode, id);
  }

  @Override
  public List<String> getRoleDataAuth(String modelCode, String id) {
    return dataAuthMapper.getRoleDataAuth(modelCode, id);
  }

  @Override
  public void setUserDataAuth(String userOrRoleId, String modelCode, List<String> ids) {
    dataAuthMapper.setUserDataAuth(userOrRoleId, modelCode, ids);
  }

  @Override
  public void setRoleDataAuth(String userOrRoleId, String modelCode, List<String> ids) {
    dataAuthMapper.setRoleDataAuth(userOrRoleId, modelCode, ids);
  }
}
