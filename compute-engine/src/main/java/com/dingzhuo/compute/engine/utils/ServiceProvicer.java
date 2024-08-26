package com.dingzhuo.compute.engine.utils;

import com.dingzhuo.compute.engine.config.CalculationConfig;
import com.dingzhuo.energy.dataservice.service.PeriodDataService;
import com.dingzhuo.energy.dataservice.service.RealtimeDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceProvicer {

  private static RealtimeDatabaseService realtimeDatabaseService;
  private static PeriodDataService periodDataService;
  private static CacheService cacheService;
  private static CalculationConfig calculationConfig;

  public static RealtimeDatabaseService getRealtimeDatabaseService() {
    return realtimeDatabaseService;
  }

  public static PeriodDataService getPeriodDataService() {
    return periodDataService;
  }

  public static CacheService getCacheService() {
    return cacheService;
  }

  public static CalculationConfig getCalculationConfig() {
    return calculationConfig;
  }

  @Autowired
  public void setCalculationConfig(
      CalculationConfig calculationConfig) {
    ServiceProvicer.calculationConfig = calculationConfig;
  }

  @Autowired
  public void setCacheService(CacheService cacheService) {
    ServiceProvicer.cacheService = cacheService;
  }

  @Autowired
  public void setPeriodDataService(
      PeriodDataService periodDataService) {
    ServiceProvicer.periodDataService = periodDataService;
  }

  @Autowired
  public void setRealtimeDatabaseService(
      RealtimeDatabaseService realtimeDatabaseService) {
    ServiceProvicer.realtimeDatabaseService = realtimeDatabaseService;
  }
}
