package com.dingzhuo.energy.project.conglomeratepush.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 项目启动 监听主题 主动订阅用 不订阅可 不启用 注释掉Component
 *
 * @author zhw
 * @since 2022/04/05
 */
@Slf4j
@Component //不要跟随系统启动订阅就先屏蔽，需要再打开
public class MQTTListener implements ApplicationListener<ContextRefreshedEvent> {

  @Value("${mqtt.username}")
  private String username;
  @Value("${mqtt.password}")
  private String password;
  @Value("${mqtt.subhbt}")
  private String subhbt;
  @Value("${mqtt.subhbtqos}")
  private int subhbtqos;
  private final MQTTConnect server;
  private final InitCallback initCallback;

  @Autowired
  public MQTTListener(MQTTConnect server, InitCallback initCallback) {
//    System.out.println("MQTT server 跟随启动监听初始化");
    this.server = server;
    this.initCallback = initCallback;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//    System.out.println("MQTT server 启动订阅");
//    try {
//      server.setMqttClient(username, password, initCallback);
//      server.sub(subhbt,subhbtqos);
//    } catch (MqttException e) {
//      log.error(e.getMessage(), e);
//    }
  }
}


