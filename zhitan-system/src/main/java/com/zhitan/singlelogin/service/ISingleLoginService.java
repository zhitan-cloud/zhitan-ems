package com.zhitan.singlelogin.service;

import com.zhitan.common.core.domain.entity.SysUser;

/**
 * description todu
 *
 * @author hmj
 * @date 2024-11-15 17:08
 */
public interface ISingleLoginService {
    SysUser singleLogin(String token);
}
