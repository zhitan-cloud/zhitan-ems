package com.dingzhuo.compute.engine.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author fanxinfu
 */
@Component
@ConfigurationProperties(prefix = "calc")
public class CalculationConfig {

  private int interval;

  public int getInterval() {
    return interval;
  }

  public void setInterval(int interval) {
    this.interval = interval;
  }
}