package com.zhitan.realtimedata.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Data
@Component
@ConfigurationProperties(prefix = "rtdb")
public class RtdbConfig {

  public String org;

  public String host;

  public String token;

  public String bucket;

  public String measurement;
}
