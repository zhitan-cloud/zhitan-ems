package com.dingzhuo.compute.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author fanxinfu
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class},
    scanBasePackages = "com.dingzhuo.*")
public class ComputeEngineApplication {

  public static void main(String[] args) {
    SpringApplication.run(ComputeEngineApplication.class, args);
  }

}
