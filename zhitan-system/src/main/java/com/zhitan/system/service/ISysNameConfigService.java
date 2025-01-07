package com.zhitan.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhitan.system.domain.SysNameConfig;

/**
 * 系统名称配置Service接口
 *
 * @author ZhiTan
 * @date 2024-11-05
 */
public interface ISysNameConfigService extends IService<SysNameConfig> {

    SysNameConfig getSysNameConfig();
}
