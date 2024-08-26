package com.dingzhuo.energy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author ruoyi
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class EnergyManagementApplication {
  public static void main(String[] args) {
    SpringApplication.run(EnergyManagementApplication.class, args);
    System.out.println("平台启动成功 O(∩_∩)O~");
  }
}
