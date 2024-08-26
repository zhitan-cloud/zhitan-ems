package com.dingzhuo.energy.framework.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zhaow
 * 庞巴迪系统配置
 */
@Component
@ConfigurationProperties(prefix = "sysconfig")
public class SystemConfig {
  /**  首页IFrame加载地址 **/
  public static String indexUrl;

  public static String getIndexUrl() {
    return indexUrl;
  }

  public void setIndexUrl(String indexUrl) {
    SystemConfig.indexUrl = indexUrl;
  }
}
