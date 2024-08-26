package com.dingzhuo.energy.data.model.controller;

import com.dingzhuo.energy.data.model.domain.AuthType;
import com.dingzhuo.energy.data.model.service.DataAuthService;
import com.dingzhuo.energy.framework.web.controller.BaseController;
import com.dingzhuo.energy.framework.web.domain.AjaxResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data/auth")
public class DataAuthController extends BaseController {

  @Autowired
  private DataAuthService dataAuthService;

  @GetMapping("/{authType}/{modelCode}/{userOrRoleId}")
  public AjaxResult selectDataAuth(
      @PathVariable("authType") String authType,
      @PathVariable("modelCode") String modelCode,
      @PathVariable("userOrRoleId") String userOrRoleId) {
    List<String> authIds;
    if (AuthType.valueOf(authType.toUpperCase()) == AuthType.USER) {
      authIds = dataAuthService.getUserDataAuth(modelCode, userOrRoleId);
    } else {
      authIds = dataAuthService.getRoleDataAuth(modelCode, userOrRoleId);
    }

    return AjaxResult.success(authIds);
  }

  @PostMapping("/{authType}/{modelCode}/{userOrRoleId}")
  public AjaxResult setDataAuth(
      @PathVariable("authType") String authType,
      @PathVariable("modelCode") String modelCode,
      @PathVariable("userOrRoleId") String userOrRoleId,
      @RequestBody List<String> ids) {
    if (AuthType.valueOf(authType.toUpperCase()) == AuthType.USER) {
      dataAuthService.setUserDataAuth(userOrRoleId, modelCode, ids);
    } else {
      dataAuthService.setRoleDataAuth(userOrRoleId, modelCode, ids);
    }

    return AjaxResult.success();
  }
}
